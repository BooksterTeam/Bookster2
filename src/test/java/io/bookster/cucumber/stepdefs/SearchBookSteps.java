package io.bookster.cucumber.stepdefs;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.bookster.config.BaseDriverIntegration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.id;

/**
 * Created on 03/12/15
 * author: nixoxo
 */
public class SearchBookSteps extends BaseDriverIntegration {

    private WebDriver browser;

    @Given("^user is authenticated and navigated to the market$")
    public void userIsAuthenticatedAndNavigatedToTheMarket() throws Throwable {
        browser = webDriver();
        authenticate(browser);
        Thread.sleep(1000);
        browser.findElement(id("market")).click();

    }


    @Given("^user search for 'gesch'$")
    public void userSearchForGesch() throws Throwable {
        Thread.sleep(1000);
        browser.findElement(id("book-query")).sendKeys("gesch");
        Thread.sleep(1000);

    }

    @Then("^a book is found which has the id 'details(\\d+)'$")
    public void aBookIsFoundWhichHasTheIdDetails(int bookid) throws Throwable {
        WebElement element = browser.findElement(id("details" + bookid));
        assertTrue(element.isDisplayed());
    }

    @After
    public void tearDown() throws Exception {
        closeBrowser();
    }

    @Given("^user search for 'jqwerajksndfnjk'$")
    public void userSearchForJqwerajksndfnjk() throws Throwable {
        Thread.sleep(1000);
        browser.findElement(id("book-query")).sendKeys("jqwerajksndfnjk");
        Thread.sleep(1000);
    }


    @Then("^no book is found$")
    public void noBookIsFound() throws Throwable {
        WebElement element = browser.findElement(id("book-market"));
    }
}
