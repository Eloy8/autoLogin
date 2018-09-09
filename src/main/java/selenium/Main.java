package selenium;

import PrivateData.PrivateData;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Main {

    //Een succes method bouwen, eentje die opnieuw probeert in te loggen en de codes (wanneer geldig) weggooid.
    //Een quit methode bouwen voor wanneer de login succesvol was

    public static void main(String[] args) throws Exception {

        String code = "test";
        int loginAttempts = 0;
        boolean successLogin = false;

        //Initialize Gecko and Firefox Drivers
        System.setProperty("webdriver.gecko.driver", PrivateData.geckoDriverPathName);
//        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        WebDriver driver = new FirefoxDriver();
        driver.get(PrivateData.testSiteUrl);

        while (!successLogin && loginAttempts < 3) {
            try {
                ((FirefoxDriver) driver).findElementById("prepaid_code").click();
                ((FirefoxDriver) driver).findElementById("prepaid_code").sendKeys(code);
                //Hoe ervoor zorgen dat er 3 keer een andere code is? For-each loop?
                Thread.sleep(1000);
                ((FirefoxDriver) driver).findElementById("sign_in").click();
                ((FirefoxDriver) driver).findElementByXPath("//*[contains(text(), 'success')]");
                successLogin = true;
            } catch (Exception e) {
                System.out.println("Invalid card number: " + code);
                loginAttempts++;
                if (loginAttempts == 3) {
                    System.out.println("ERROR: FAILED TO LOGIN");
                }
            }
        }

        Thread.sleep(5000);
        driver.quit();

    }
}