package name.lattuada.trading.tests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import name.lattuada.trading.model.EOrderType;
import name.lattuada.trading.model.dto.OrderDTO;
import name.lattuada.trading.model.dto.SecurityDTO;
import name.lattuada.trading.model.dto.TradeDTO;
import name.lattuada.trading.model.dto.UserDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

public class TradeSteps {

    private static final Logger logger = LoggerFactory.getLogger(CucumberTest.class);
    private final RestUtility restUtility;
    private final Map<String, SecurityDTO> securityMap;
    private final Map<String, UserDTO> userMap;
    private OrderDTO buyOrder;
    private OrderDTO sellOrder;
    private String validUserName;
    private String validSecurity;
    private Double orderPrice;
    private Long orderQuantity;
    private OrderDTO orderReturned;
    private UUID orderID;

    TradeSteps() {
        restUtility = new RestUtility();
        securityMap = new HashMap<>();
        userMap = new HashMap<>();
    }

    // TODO implement: Given for "one security {string} and two users {string} and {string} exist"
    @Given("one security {string} and two users {string} and {string} exist")
    public void oneSecurityAndTwoUsers(String securityName, String userName1, String userName2) {

        logger.info("Creating user1 : {}",userName1);
        createUser(userName1);
        logger.info("Creating user2 : {}", userName2);
        createUser(userName2);
        logger.info("Creating security : {}", securityName);
        createSecurity(securityName);

        UserDTO createdUser1 = userMap.get(userName1);
        UserDTO createdUser2 = userMap.get(userName2);
        SecurityDTO createdSecurity = securityMap.get(securityName);

        assertNotNull(String.format("User \"%s\" does not exist", userName1), createdUser1);
        assertNotNull(String.format("User \"%s\" does not exist", userName2), createdUser2);
        assertNotNull(String.format("User \"%s\" does not exist", userName2),createdSecurity);

        assertEquals("Username1 not expected", userName1, createdUser1.getUsername());
        assertEquals("Username2 not expected", userName2, createdUser2.getUsername());
        assertEquals("Security name not expected", securityName, createdSecurity.getName());

    }

    @Given("one security {string} and three users {string}, {string}, {string} exist")
    public void oneSecurityAndThreeUsers(String securityName, String userName1, String userName2, String userName3) {

        logger.info("Creating user1 : {}",userName1);
        createUser(userName1);
        logger.info("Creating user2 : {}", userName2);
        createUser(userName2);
        logger.info("Creating user2 : {}", userName3);
        createUser(userName3);
        logger.info("Creating security : {}", securityName);
        createSecurity(securityName);

        UserDTO createdUser1 = userMap.get(userName1);
        UserDTO createdUser2 = userMap.get(userName2);
        UserDTO createdUser3 = userMap.get(userName3);
        SecurityDTO createdSecurity = securityMap.get(securityName);

        assertNotNull(String.format("User \"%s\" does not exist", userName1), createdUser1);
        assertNotNull(String.format("User \"%s\" does not exist", userName2), createdUser2);
        assertNotNull(String.format("User \"%s\" does not exist", userName3), createdUser3);
        assertNotNull(String.format("User \"%s\" does not exist", securityName),createdSecurity);

        assertEquals("Username1 not expected", userName1, createdUser1.getUsername());
        assertEquals("Username2 not expected", userName2, createdUser2.getUsername());
        assertEquals("Username3 not expected", userName3, createdUser3.getUsername());
        assertEquals("Security name not expected", securityName, createdSecurity.getName());
    }

    @When("user {string} puts a {string} order for security {string} with a price of {double} and quantity of {long}")
    @And("user {string} puts a {string} order for security {string} with a price of {double} and a quantity of {long}")
    public void userPutAnOrder(String userName, String orderType, String securityName, Double price, Long quantity) {
        logger.trace("Got username = \"{}\"; orderType = \"{}\"; securityName = \"{}\"; price = \"{}\"; quantity = \"{}\"",
                userName, EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)), securityName, price, quantity);
        assertTrue(String.format("Unknown user \"%s\"", userName),
                userMap.containsKey(userName));
        assertTrue(String.format("Unknown security \"%s\"", securityName),
                securityMap.containsKey(securityName));
        createOrder(userName,
                EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)),
                securityName,
                price,
                quantity);
    }

    @Then("a trade occurs with the price of {double} and quantity of {long}")
    public void aTradeOccursWithThePriceOfAndQuantityOf(Double price, Long quantity) {
        logger.trace("Got price = \"{}\"; quantity = \"{}\"",
                price, quantity);
        TradeDTO trade = restUtility.get("api/trades/orderBuyId/" + buyOrder.getId().toString()
                        + "/orderSellId/" + sellOrder.getId().toString(),
                TradeDTO.class);
        assertEquals("Price not expected", trade.getPrice(), price);
        assertEquals("Quantity not expected", trade.getQuantity(), quantity);
    }

    @Then("no trades occur")
    public void noTradesOccur() {
        assertThatThrownBy(() -> restUtility.get("api/trades/orderBuyId/" + buyOrder.getId().toString()
                        + "/orderSellId/" + sellOrder.getId().toString(),
                TradeDTO.class)).isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    private void createUser(String userName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userName);
        userDTO.setPassword(RandomStringUtils.randomAlphanumeric(64));
        UserDTO userReturned = restUtility.post("api/users",
                userDTO,
                UserDTO.class);
        userMap.put(userName, userReturned);
        logger.info("User created: {}", userReturned);
    }

    private void createSecurity(String securityName) {
        SecurityDTO securityDTO = new SecurityDTO();
        securityDTO.setName(securityName);
        SecurityDTO securityReturned = restUtility.post("api/securities",
                securityDTO,
                SecurityDTO.class);
        securityMap.put(securityName, securityReturned);
        logger.info("Security created: {}", securityReturned);
    }

    private void createOrder(String userName,
                             EOrderType orderType,
                             String securityName,
                             Double price,
                             Long quantity) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(userMap.get(userName).getId());
        orderDTO.setType(orderType);
        orderDTO.setSecurityId(securityMap.get(securityName).getId());
        orderDTO.setPrice(price);
        orderDTO.setQuantity(quantity);
        orderReturned = restUtility.post("api/orders", orderDTO, OrderDTO.class);
        assertNotNull("Order creation failed", orderReturned);
        assertEquals("UserId not expected", userMap.get(userName).getId(), orderReturned.getUserId());
        assertEquals("OrderType not expected",orderType, orderReturned.getType());
        assertEquals("Security Id not expected",securityMap.get(securityName).getId(), orderReturned.getSecurityId());
        assertEquals("Price not expected",price,orderReturned.getPrice());
        assertEquals("Quantity not expected", quantity,orderReturned.getQuantity());
        logger.info("Order created: {}", orderReturned);
        if (orderType == EOrderType.BUY){
            buyOrder = orderReturned;
        }else{
            sellOrder = orderReturned;
        }
    }

    @Given("a valid user name provided {string}")
    public void a_valid_user_name_provided(String userName) {
        validUserName = userName;
        logger.info("The user name set: {}",validUserName );
    }
    @When("a post request to create user send to API")
    public void a_post_request_to_create_user_send_to_api() {
        createUser(validUserName);
    }
    @When("receive the response and validate the user data")
    public void receive_the_response_and_validate_the_user_data() {
        UserDTO createdUser = userMap.get(validUserName);
        assertNotNull(String.format("User \"%s\" does not exist", validUserName), createdUser);
        assertEquals("Username not expected", validUserName, createdUser.getUsername());
        logger.info("Validation completed successfully");
    }

    @Then("send a get request to list all users and validate the user exist")
    public void send_a_get_request_to_list_all_users_and_validate_the_user_exist() {
        UserDTO[] userArray = restUtility.get("api/users", UserDTO[].class);
        List<String> usernames = Arrays.stream(userArray)
                .map(UserDTO::getUsername)
                .collect(Collectors.toList());
        assertTrue(usernames.contains(validUserName));
        logger.info("Validation completed successfully");
    }
    @Given("a valid security name provided {string}")
    public void a_valid_security_name_provided(String securityName) {
        validSecurity = securityName;
        logger.info("Security name set: {}", validSecurity);
    }
    @When("a post request to create security send to API")
    public void a_post_request_to_create_security_send_to_api() {
        createSecurity(validSecurity);
    }
    @When("receive the response and validate the security data")
    public void receive_the_response_and_validate_the_security_data() {
        SecurityDTO createdSecurity = securityMap.get(validSecurity);
        assertNotNull(String.format("Security \"%s\" does not exist", validSecurity), createdSecurity);
        assertEquals("Security not expected", validSecurity, createdSecurity.getName());
        logger.info("Validation completed successfully");
    }
    @Then("send a get request to list all users and validate the security exist")
    public void send_a_get_request_to_list_all_users_and_validate_the_security_exist() {
        SecurityDTO[] securityArray = restUtility.get("api/securities", SecurityDTO[].class);
        List<String> securityNames = Arrays.stream(securityArray)
                .map(SecurityDTO::getName)
                .collect(Collectors.toList());
        assertTrue(securityNames.contains(validSecurity));
        logger.info("Validation completed successfully");
    }
    @Given("an exist security {string}, an exist user {string}, quantity {long}, price {double}")
    public void an_exist_security_an_exist_user_quantity_price(String security, String user, Long quantity, Double price) {
        createUser(user);
        validUserName= userMap.get(user).getUsername();
        createSecurity(security);
        validSecurity = securityMap.get(security).getName();
        orderQuantity = quantity;
        orderPrice = price;
    }
    @When("a post request to create {string} order send to API")
    public void a_post_request_to_create_order_send_to_api(String orderType) {
        createOrder(validUserName,EOrderType.valueOf(orderType.toUpperCase(Locale.ROOT)),validSecurity,orderPrice,orderQuantity);
    }
    @When("receive the response and validate the order data")
    public void receive_the_response_and_validate_the_order_data() {
        //Validations implemented inside the createOrder() method, no need to repeat
        logger.info("Validation done successfully");
        orderID = orderReturned.getId();
    }
    @Then("send a get request to list all orders and validate the {string} order exist")
    public void send_a_get_request_to_list_all_orders_and_validate_the_order_exist(String orderType) {
        OrderDTO[] orderArray = restUtility.get("api/orders",OrderDTO[].class);
        List<UUID> orderIds = Arrays.stream(orderArray)
                .map(OrderDTO::getId)
                .collect(Collectors.toList());
        assertTrue(orderIds.contains(orderID));
        logger.info("Validation completed successfully");
    }
}
