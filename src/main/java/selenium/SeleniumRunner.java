package selenium;

import constants.PrivateData;
import data.Code;
import excel.ExcelManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Scanner;

public class SeleniumRunner {

    private static boolean successLogin = false;
    private static int loginAttempts = 0;
    private static int AmountOfCodes = ExcelManager.getAmountOfCodes();

    public static void start() throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", PrivateData.geckoDriverPathName);
        WebDriver driver = new FirefoxDriver();

        login(driver);
        quit(driver);
    }

    private static void login(WebDriver driver) {
        driver.get(PrivateData.testSiteUrl);
        while (!successLogin && loginAttempts < AmountOfCodes) {
            //TODO: Link maken met CodeManager! De try-catch geen wifi proof maken!
            Code[] codeList = new Code[]{new Code(1, "test1", 2), new Code(2, "test2", 1), new Code(3, "test3", 0)};
            Code code = codeList[loginAttempts];
            try {
                webdriver(driver, code);
            } catch (Exception e) {
                error(code);
            }
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

    private static void quit(WebDriver driver) throws InterruptedException {
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

    private static void input(WebDriver driver) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        String seats = input.next();

        if (!seats.matches("[a-zA-Z]+")) {
            System.out.println("ERROR: Input should be alphabetical! Please type either \"retry\" or \"quit\" !");
            input(driver);
        }

        inputLoop(seats, driver);
    }

    private static void inputLoop(String seats, WebDriver driver) throws InterruptedException {
        switch (seats.toLowerCase().trim()) {
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
