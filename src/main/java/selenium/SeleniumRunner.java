package selenium;

import constants.PrivateData;
import excel.ExcelManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

class SeleniumRunner {

    private static boolean successLogin = false;
    private static int loginAttempts = 0;
    private static int AmountOfCodes = ExcelManager.getAmountOfCodes();

    static void start() throws InterruptedException {
        String[] codeList = new String[3];
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
        Thread.sleep(5000);
        driver.quit();
        if (successLogin) {
            //deleteWorking code toevoegen!

        } else {
            //TODO: Een message over een internetcheck en de mogelijkheid om opnieuw te proberen!

        }


    }

}
