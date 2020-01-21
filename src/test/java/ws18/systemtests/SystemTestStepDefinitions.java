package ws18.systemtests;

import dtu.ws.fastmoney.*;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import models.ws18.Customer;
import models.ws18.Merchant;
import models.ws18.Payment;
import models.ws18.Token;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author oliver
 */
@SpringBootTest
public class SystemTestStepDefinitions {
}
