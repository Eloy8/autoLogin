package selenium;

import constants.PrivateData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

class SeleniumRunner {

    //Een succes method bouwen, eentje die opnieuw probeert in te loggen en de codes (wanneer geldig) weggooid.
    //Een quit methode bouwen voor wanneer de login succesvol was

    static void start() throws Exception {
        String code = null;
        int loginAttempts = 0;
        boolean successLogin = false;

        //Initialize Gecko and Firefox Drivers
        System.setProperty("webdriver.gecko.driver", PrivateData.geckoDriverPathName);
        WebDriver driver = new FirefoxDriver();
        driver.get(PrivateData.testSiteUrl);

        while (!successLogin && loginAttempts < 3)

        {
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
        return;
    }
}
