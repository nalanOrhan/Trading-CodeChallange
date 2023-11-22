package name.lattuada.trading.tests;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.models.Response;
import name.lattuada.trading.model.dto.OrderDTO;
import name.lattuada.trading.model.dto.SecurityDTO;
import name.lattuada.trading.model.dto.UserDTO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApiTestStepDefs {
    private final RestUtility restUtility;
    private final Map<String, SecurityDTO> securityMap;
    private final Map<String, UserDTO> userMap;
    private OrderDTO buyOrder;
    private OrderDTO sellOrder;

    private Response response;

    TradeSteps tradeSteps = new TradeSteps();

    public ApiTestStepDefs() {
        this.restUtility = new RestUtility();
        this.securityMap = new HashMap<>();
        this.userMap = new HashMap<>();
    }


}
