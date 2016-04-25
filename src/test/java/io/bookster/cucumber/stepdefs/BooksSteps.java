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
public class BooksSteps extends BaseDriverIntegration {

    private WebDriver browser;

    @Given("^user authenticated and clicked on the market$")
    public void userAuthenticatedAndClickedOnTheMarket() throws Throwable {
        browser = webDriver();
        authenticate(browser);
        Thread.sleep(1000);
        WebElement marketButton =browser.findElement(id("market"));
        marketButton.click();
    }

    @Then("^books are shown$")
    public void booksAreShown() throws Throwable {
        Thread.sleep(500);
        assertTrue(browser.findElement(id("book-market")).isDisplayed());
    }


    @Then("^one book has the id 'details(\\d+)'$")
    public void oneBookHasTheIdDetails(int bookid) throws Throwable {
        WebElement detailsButton = browser.findElement(id("details" + bookid));
        assertTrue(detailsButton.isDisplayed());
    }

    @After
    public void tearDown(){
        closeBrowser();
    }
}
