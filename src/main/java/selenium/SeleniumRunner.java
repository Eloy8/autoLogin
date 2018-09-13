package selenium;

import constants.PrivateData;
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

    public static void start() throws InterruptedException, IOException {
        System.setProperty("webdriver.gecko.driver", PrivateData.geckoDriverPathName);
        WebDriver driver = new FirefoxDriver();

        login(driver);
        quit(driver);
    }

    private static void login(WebDriver driver) throws IOException, InterruptedException {
        driver.get(PrivateData.testSiteUrl);
        while (!successLogin && loginAttempts < AmountOfCodes) {
            Code[] codeList = ExcelManager.getUniqueCodeList();
            Code code = codeList[loginAttempts];
            connectioncheck(driver);
            try {
                webdriver(driver, code);
            } catch (Exception e) {
                error(code);
            }
        }
    }

    private static void connectioncheck(WebDriver driver) throws IOException, InterruptedException {
        try {
            ((FirefoxDriver) driver).findElementById("prepaid_code").click();
        } catch (Exception e) {
            System.out.println("ERROR: No internet connection");
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

    private static void error(Code code) {
        System.out.println("Invalid card number: " + code.getCode());
        if (loginAttempts >= AmountOfCodes) {
            System.out.println("ERROR: FAILED TO LOGIN");
        }
    }

    private static void quit(WebDriver driver) throws InterruptedException, IOException {
        closeDriver(driver);

        if (successLogin) {
            System.out.println("Succesfull login, goodbye! ;)");
            //deleteWorking code toevoegen!
            System.exit(0);
        } else {
            System.out.println("There seems to be a problem logging in..." + "\n" + "This can be caused by internal errors or connection problems.");
            System.out.println("Would you like to retry logging in? Type " + "retry" + "! " + "Would you like to quit this programm? Type " + "quit" + "!");

            input(driver);
        }
    }

    private static void input(WebDriver driver) throws InterruptedException, IOException {
        Scanner input = new Scanner(System.in);
        String seats = input.next();

        if (!seats.matches("[a-zA-Z]+")) {
            System.out.println("ERROR: Input should be alphabetical! Please type either \"retry\" or \"quit\" !");
            input(driver);
        }

        inputLoop(seats, driver);
    }

    private static void inputLoop(String command, WebDriver driver) throws InterruptedException, IOException {
        switch (command.toLowerCase().trim()) {
            case "retry":
                SeleniumRunner.start();
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

    private static void closeDriver(WebDriver driver) throws InterruptedException {
        if (loginAttempts > 0) {
            Thread.sleep(2000);
            driver.quit();
        }
        loginAttempts = 0;
    }

}
