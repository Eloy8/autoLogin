package selenium;

import constants.PrivateData;
import excel.ExcelManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Scanner;

class SeleniumRunner {

    private static boolean successLogin = false;
    private static int loginAttempts = 0;
    private static int AmountOfCodes = ExcelManager.getAmountOfCodes();

    static void start() throws InterruptedException {
        String[] codeList = null;
        codeList = new String[AmountOfCodes];
        String code = codeList[loginAttempts];

        System.setProperty("webdriver.gecko.driver", PrivateData.geckoDriverPathName);
        WebDriver driver = new FirefoxDriver();

        login(driver, code);
        quit(driver);
    }

    private static void login(WebDriver driver, String code) {
        driver.get(PrivateData.testSiteUrl);
        while (!successLogin && loginAttempts < AmountOfCodes) {
            try {
                webdriver(driver, code);
            } catch (Exception e) {
                error(code);
            }
        }
    }

    private static void webdriver(WebDriver driver, String code) throws InterruptedException {
        ((FirefoxDriver) driver).findElementById("prepaid_code").click();
        ((FirefoxDriver) driver).findElementById("prepaid_code").sendKeys(code);
        ((FirefoxDriver) driver).findElementById("sign_in").click();
        Thread.sleep(1000);
        ((FirefoxDriver) driver).findElementByXPath("//*[contains(text(), 'success')]");
        successLogin = true;
    }

    private static void error(String code) {
        System.out.println("Invalid card number: " + code);
        loginAttempts++;
        if (loginAttempts >= AmountOfCodes) {
            System.out.println("ERROR: FAILED TO LOGIN");
        }
    }

    private static void quit(WebDriver driver) throws InterruptedException {
        closeDriver(driver);

        if (successLogin) {
            //deleteWorking code toevoegen en venster sluiten!

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
            System.out.println("ERROR: Input should be alphabetical! Please type either " + "retry" + " or " + "quit" + " !");
            input(driver);
        }

        inputLoop(input, seats, driver);
    }

    private static void inputLoop(Scanner input, String seats, WebDriver driver) throws InterruptedException {
        if (seats.toLowerCase().trim().equals("retry")) {
            SeleniumRunner.start();
        } else if (seats.toLowerCase().trim().equals("quit")) {
            System.exit(0);
        } else if (seats.toLowerCase().trim().equals("help")) {
            quit(driver);
        } else {
            System.out.println("ERROR: Input does not match options! Please type either " + "retry" + " or " + "quit" + " !");
            input(driver);
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
