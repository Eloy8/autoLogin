package selenium;

import cli.CommandLineInterpreter;
import data.Code;
import data.PrivateData;
import excel.ExcelManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;

public class SeleniumRunner {

    private CommandLineInterpreter cli = new CommandLineInterpreter();
    private static boolean successLogin = false;
    private static int loginAttempts = 0;
    private static int AmountOfCodes = ExcelManager.getAmountOfCodes();

    public void start() {
        System.setProperty("webdriver.gecko.driver", PrivateData.getGeckoDriverPathName());
        WebDriver driver = new FirefoxDriver();

        login(driver);
        cli.quit(driver, successLogin, loginAttempts);
    }

    private void login(WebDriver driver) {
        driver.get(PrivateData.getTestSiteUrl());
        Code[] codeList = null;
        try {
            codeList = ExcelManager.getUniqueCodeList(3);
        } catch (IOException e) {
            System.err.println("Error loading excel file " + e.getMessage());
            System.exit(1);
        }

        while (!successLogin && loginAttempts < AmountOfCodes) {
            Code code = codeList[loginAttempts];

            connectionCheck(driver);
            try {
                webdriver(driver, code);
            } catch (Exception e) {
                error(code);
            }
        }
    }

    private void connectionCheck(WebDriver driver) {
        try {
            ((FirefoxDriver) driver).findElementById("prepaid_code").click();
        } catch (Exception e) {
            System.err.println("ERROR: No internet connection!");
            loginAttempts++;
            cli.quit(driver, successLogin, loginAttempts);
        }
    }

    private void webdriver(WebDriver driver, Code code) throws InterruptedException {
        ((FirefoxDriver) driver).findElementById("prepaid_code").click();
        ((FirefoxDriver) driver).findElementById("prepaid_code").sendKeys(code.getCode());
        ((FirefoxDriver) driver).findElementById("sign_in").click();
        loginAttempts++;
        sleep(1000);
        ((FirefoxDriver) driver).findElementByXPath("// *[contains(text(), 'success')]");
        successLogin = true;
    }

    private void error(Code code) {
        System.out.println("Invalid card number: " + code.getCode());
        if (loginAttempts >= AmountOfCodes) {
            System.err.println("ERROR: FAILED TO LOGIN");
        }
    }

    void sleep(int time){
        try{
            Thread.sleep(time);
        } catch(Exception e){
            System.err.println("Error loading excel file " + e.getMessage());
        }
    }
}
