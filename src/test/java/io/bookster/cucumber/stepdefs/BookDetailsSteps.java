package io.bookster.cucumber.stepdefs;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.bookster.config.BaseDriverIntegration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.lang.Integer.valueOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.id;

/**
 * Created on 03/12/15
 * author: nixoxo
 */
public class BookDetailsSteps extends BaseDriverIntegration {

    private WebDriver browser;


    @Given("^user authenticated and navigated to the market$")
    public void userIsAdminAndNavigatedToTheMarket() throws Throwable {
        browser = webDriver();
        authenticate(browser);
        Thread.sleep(500);
    }

    @When("^user lookup a book with id '(\\d+)'$")
    public void userLookupABookWithId(int bookid
    ) throws Throwable {
        browser.get(server + "market");
        Thread.sleep(500);
        WebElement detailsButton = browser.findElement(id("details" + bookid));
        detailsButton.click();

    }

    @Then("^the title of the book is 'Master Software Engineering'$")
    public void theTitleOfTheBookIsMasterSoftwareEngineering() throws Throwable {
        Thread.sleep(500);
        WebElement title = browser.findElement(id("book-title"));
        assertThat(title.getText(), is("Master Software Engineering"));
    }

    @Then("^the isbn of the book is '(\\d+)'$")
    public void theIsbnOfTheBookIs(int isbn) throws Throwable {
        WebElement title = browser.findElement(id("book-isbn"));
        assertThat(valueOf(title.getText()), is(isbn));
    }

    @When("^user lookup a book manually$")
    public void userLookupABookManually() throws Throwable {
        browser.get(server + "book/1001717234");
        Thread.sleep(500);
    }

    @Then("^the book is not found$")
    public void theBookIsNotFound() throws Throwable {
        WebElement bookNotFound = browser.findElement(id("book-not-found"));
        assertTrue(bookNotFound.getText().contains("not exist"));
    }

    @After
    public void tearDown() throws Exception {
        closeBrowser();
    }
}
