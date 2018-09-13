package selenium;

import data.PrivateData;
import data.Code;
import excel.ExcelManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.util.Scanner;

public class SeleniumRunner {

    private static boolean successLogin = false;
    private static int loginAttempts = 0;
    private static int AmountOfCodes = ExcelManager.getAmountOfCodes();

    public void start() {
        System.setProperty("webdriver.gecko.driver", PrivateData.geckoDriverPathName);
        WebDriver driver = new FirefoxDriver();

        login(driver);
        quit(driver);
    }

    private void login(WebDriver driver){
        driver.get(PrivateData.testSiteUrl);
        Code[] codeList = null;
        try {
             codeList = ExcelManager.getUniqueCodeList(3);
        } catch (IOException e){
            System.err.println("Error loading excel file "+ e.getMessage());
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

    private void connectionCheck(WebDriver driver){
        try {
            ((FirefoxDriver) driver).findElementById("prepaid_code").click();
        } catch (Exception e) {
            System.err.println("ERROR: No internet connection!");
            loginAttempts++;
            quit(driver);
        }
    }

    private static void webdriver(WebDriver driver, Code code) throws InterruptedException {
        ((FirefoxDriver) driver).findElementById("prepaid_code").click();
        ((FirefoxDriver) driver).findElementById("prepaid_code").sendKeys(code.getCode());
        ((FirefoxDriver) driver).findElementById("sign_in").click();
        loginAttempts++;
        Thread.sleep(1000);
        ((FirefoxDriver) driver).findElementByXPath("// *[contains(text(), 'success')]");
        successLogin = true;
    }

    private void error(Code code) {
        System.out.println("Invalid card number: " + code.getCode());
        if (loginAttempts >= AmountOfCodes) {
            System.err.println("ERROR: FAILED TO LOGIN");
        }
    }

    private void quit(WebDriver driver){
        closeDriver(driver);

        if (successLogin) {
            System.out.println("Succesfull login, goodbye! :)");
            //deleteWorking code toevoegen!
            System.exit(0);
        } else {
            System.out.println("There seems to be a problem logging in..." + "\n" + "This can be caused by internal errors or connection problems.");
            System.out.println("Would you like to retry logging in? Type " + "retry" + "! " + "Would you like to quit this programm? Type " + "quit" + "!");

            input(driver);
        }
    }

    private void input(WebDriver driver) {
        Scanner input = new Scanner(System.in);
        String command = input.next();

        if (!command.matches("[a-zA-Z]+")) {
            System.out.println("ERROR: Input should be alphabetical! Please type either \"retry\" or \"quit\" !");
            input(driver);
        }

        inputLoop(command, driver);
    }

    private void inputLoop(String command, WebDriver driver) {
        switch (command.toLowerCase().trim()) {
            case "retry":
                this.start();
                break;
            case "quit":
                System.exit(0);
            case "help":
                quit(driver);
                break;
            default:
                System.out.println("ERROR: Input does not match options! Please type either \"retry\" or \"quit\" !");
                input(driver);
                break;
        }
    }

    private void closeDriver(WebDriver driver) {
        if (loginAttempts > 0) {
            try {
                Thread.sleep(2000);
            } catch(InterruptedException e) {
                System.err.println("Thread is interrupted!");
            }
            driver.quit();
        }
        loginAttempts = 0;
    }

}
