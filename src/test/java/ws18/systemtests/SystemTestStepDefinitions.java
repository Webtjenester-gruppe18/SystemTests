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

    private final String URL = "http://fastmoney-18.compute.dtu.dk:8082/";
    private Customer customer;
    private Merchant merchant;
    private BankService bank;

    @Before
    public void setUp() {
        customer = new Customer("test"+randomValue(),
                "test"+randomValue(),
                "Test"+randomValue(),
                "test"+randomValue());
        merchant = new Merchant("test"+randomValue(),
                "test"+randomValue(),
                "Test"+randomValue(),
                "test"+randomValue());
        bank = new BankServiceService().getBankServicePort();
    }

    private String randomValue() {
        int firstDigit = (int) (Math.random() * 999999 + 1);
        int secondDigit = (int) (Math.random() * 999999 + 1);
        int thirdDigit = (int) (Math.random() * 999999 + 1);
        return String.valueOf(firstDigit) + String.valueOf(secondDigit) + String.valueOf(thirdDigit);
    }

    @Test
    public void testHttp() {
        String url = this.URL + "swagger-ui.html";
        HttpResponse<JsonNode> response = Unirest.get(url)
                .asJson();
        Assert.assertEquals(200, (long) response.getStatus());

    }

    @Given("The customer is not already registerered")
    public void theCustomerIsNotAlreadyRegistrered() throws IOException {
        String url = this.URL + "customers/" + this.customer.getCprNumber();
        HttpResponse<String> response = Unirest.get(url)
                .asString();
        Assert.assertEquals(404, response.getStatus());
//        System.out.println(response.getStatus());
    }

    @When("The customer registers himself")
    public void theCustomerRegistersHimself() throws BankServiceException_Exception {
        String url = this.URL + "customers";
        HttpResponse<String> response = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body(this.customer).asString();
        Assert.assertEquals(201, response.getStatus());
    }

    @Then("The customer will be registered")
    public void theCustomerWillBeRegistered() {
        String url = this.URL + "customers/" + this.customer.getCprNumber();
        HttpResponse<String> response = Unirest.get(url)
                .asString();
        Assert.assertEquals(200, response.getStatus());
    }

    @Then("The customer cannot register himself again")
    public void theCustomerCannotRegisterHimselfAgain() {
        String url = this.URL + "customers";
        HttpResponse<String> response = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body(this.customer).asString();
        Assert.assertEquals(400, response.getStatus());
    }

    @When("The customer requests tokens")
    public void theCustomerRequestsTokens() {
        String url = this.URL + "tokens/" + this.customer.getCprNumber();
        HttpResponse<Token[]> response = Unirest.post(url)
                .header("Content-Type", "application/json")
                .asObject(Token[].class);
        Assert.assertEquals(200, response.getStatus());
    }

    @Then("The customer has {int} tokens")
    public void theCustomerHasTokens(Integer int1) {
        String url = this.URL + "tokens/" + this.customer.getCprNumber();
        HttpResponse<Token[]> response = Unirest.get(url).asObject(Token[].class);
        int length = response.getBody().length;
        Assert.assertEquals((long)int1, length);
    }

    @Given("The customer has at least {int} token")
    public void theCustomerHasAtLeastToken(Integer int1) {
        theCustomerRequestsTokens();
        String url = this.URL + "tokens/" + this.customer.getCprNumber();
        HttpResponse<Token[]> response = Unirest.get(url).asObject(Token[].class);
        int length = response.getBody().length;
        boolean isMore = length >= int1;
        Assert.assertTrue(isMore);
    }

    @Given("The customer has a bank account")
    public void theCustomerHasABankAccount() throws BankServiceException_Exception {
        User user = new User();
        user.setCprNumber(this.customer.getCprNumber());
        user.setFirstName(this.customer.getFirstName());
        user.setLastName(this.customer.getLastName());
        String accountId = bank.createAccountWithBalance(user, BigDecimal.valueOf(100));
        this.customer.setAccountId(accountId);
    }

    @Given("The merchant has a bank account")
    public void theMerchantHasABankAccount() throws BankServiceException_Exception {
        User user = new User();
        user.setCprNumber(this.merchant.getCprNumber());
        user.setFirstName(this.merchant.getFirstName());
        user.setLastName(this.merchant.getLastName());
        String accountId = bank.createAccountWithBalance(user, BigDecimal.valueOf(100));
        this.merchant.setAccountId(accountId);
    }


    @When("The customer transfers {int} currency to the merchant")
    public void theCustomerTransfersCurrencyToTheMerchant(Integer int1) {
        String url = this.URL + "payments";
        String urlToken = this.URL + "tokens/" + this.customer.getCprNumber();
        HttpResponse<Token[]> token = Unirest.get(urlToken).asObject(Token[].class);
        Token tokenToUse = token.getBody()[0];
        Payment payment = new Payment();
        payment.setAmount(int1);
        payment.setCpr(customer.getCprNumber());
        payment.setDescription("test");
        payment.setFromAccountNumber(customer.getAccountId());
        payment.setToAccountNumber(merchant.getAccountId());
        payment.setToken(tokenToUse);

        HttpResponse<String> response = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body(payment).asString();
        Assert.assertEquals(200, response.getStatus());
    }

    @When("The customer deletes himself")
    public void theCustomerDeletesHimself() {
        String url = this.URL + "customers/" + this.customer.getCprNumber();
        HttpResponse<String> response = Unirest.delete(url)
                .body(this.customer).asString();
        Assert.assertEquals(400, response.getStatus());
    }

    @Then("The customer does not exist")
    public void theCustomerDoesNotExist() {
        String url = this.URL + "customers/" + this.customer.getCprNumber();
        HttpResponse<String> response = Unirest.get(url)
                .asString();
        Assert.assertEquals(404, response.getStatus());    }

    @And("The customer has {string} currency in the bank")
    public void theCustomerHasCurrencyInTheBank(String amount) throws BankServiceException_Exception {
        String balance = bank.getAccountByCprNumber(this.customer.getCprNumber()).getBalance().toString();

        Assert.assertEquals(amount, balance);
    }

    @And("The merchant has {string} currency in the bank")
    public void theMerchantHasCurrencyInTheBank(String amount) throws BankServiceException_Exception {
        String balance = bank.getAccountByCprNumber(this.merchant.getCprNumber()).getBalance().toString();
        Assert.assertEquals(amount.toString(), balance);
    }
}
