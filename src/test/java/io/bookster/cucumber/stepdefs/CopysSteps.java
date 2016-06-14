package io.bookster.cucumber.stepdefs;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.bookster.config.BaseDriverIntegration;
import org.openqa.selenium.WebDriver;

import static org.openqa.selenium.By.id;

/**
 * Created on 03/12/15
 * author: nixoxo
 */
public class CopysSteps extends BaseDriverIntegration {

    private WebDriver browser;

    @Given("^user authenticated and navigated to the copys$")
    public void userAuthenticatedAndNavigatedToTheCopys() throws Throwable {
        browser = webDriver();
        authenticate(browser);
        Thread.sleep(500);
        browser.findElement(id("entity-menu")).click();
        browser.findElement(id("entity-copy")).click();
    }

    @Given("^a modal does pop up$")
    public void aModalDoesPopUp() throws Throwable {
        Thread.sleep(500);
        browser.findElement(id("copy-add-button")).click();
    }

    @Given("^add a copy for the book with the id '(\\d+)'$")
    public void addACopyForTheBookWithTheId(int bookid) throws Throwable {
        Thread.sleep(500);
        browser.findElement(id("field_book")).sendKeys("" + bookid);
    }

    @Then("^a copy has been added$")
    public void aCopyHasBeenAdded() throws Throwable {
        browser.findElement(id("copy-save-button")).click();
        Thread.sleep(500);
    }

    @After
    public void tearDown() throws Exception {
        closeBrowser();
    }

    @Then("^no copy has been added$")
    public void noCopyHasBeenAdded() throws Throwable {
        Thread.sleep(500);
    }
}