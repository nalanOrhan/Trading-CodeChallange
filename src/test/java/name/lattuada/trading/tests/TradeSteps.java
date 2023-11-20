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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

public class TradeSteps {

    private static final Logger logger = LoggerFactory.getLogger(CucumberTest.class);
    private final RestUtility restUtility;
    private final Map<String, SecurityDTO> securityMap;
    private final Map<String, UserDTO> userMap;
    private OrderDTO buyOrder;
    private OrderDTO sellOrder;

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
        OrderDTO orderReturned = restUtility.post("api/orders", orderDTO, OrderDTO.class);
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

}
