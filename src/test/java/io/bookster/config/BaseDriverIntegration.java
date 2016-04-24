package io.bookster.config;

import org.apache.commons.lang.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

import static org.openqa.selenium.By.id;

/**
 * Created on 03/12/15
 * author: nixoxo
 */
public class BaseDriverIntegration {

    protected final String server = "http://localhost:3000/#/";

    private WebDriver browser;

    protected WebDriver webDriver() throws InterruptedException {
        return webDriver("");
    }

    protected WebDriver webDriver(String url) throws InterruptedException {
        browser = chromeDriver();
        browser.get(server + url);
        Thread.sleep(200);
        return browser;
    }

    private ChromeDriver chromeDriver() {
        File file = null;
        if (SystemUtils.IS_OS_MAC) {
            file = new File("src/test/resources/driver/mac/chromedriver");
        }
        if (SystemUtils.IS_OS_LINUX) {
            file = new File("src/test/resources/driver/linux/chromedriver");
        }
        if (SystemUtils.IS_OS_WINDOWS) {
            file = new File("src/test/resources/driver/windows/chromedriver.exe");
        }
        if (file == null) {
            throw new RuntimeException("ChromeDriver is not supported by your operating system");
        }
        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setJavascriptEnabled(true);
        ChromeDriver driver = new ChromeDriver(capabilities);
        driver.manage().window().maximize();
        return driver;
    }

    protected void authenticate(WebDriver browser) throws InterruptedException {
        browser.get(server + "login");
        Thread.sleep(500);
        browser.findElement(id("username")).sendKeys("admin");
        browser.findElement(id("password")).sendKeys("admin");
        WebElement loginForm = browser.findElement(id("login-button"));
        loginForm.submit();
    }

    protected void closeBrowser() {
        if (browser != null) {
            browser.quit();
        }
    }
}
