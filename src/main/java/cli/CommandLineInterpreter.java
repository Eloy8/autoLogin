package cli;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Command;
import selenium.SeleniumRunner;

import java.util.Scanner;

public class CommandLineInterpreter {

    public void quit(WebDriver driver, boolean successLogin, int loginAttempts){
        closeDriver(driver, loginAttempts);

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
                SeleniumRunner sr = new SeleniumRunner();
                sr.start();
                break;
            case "quit":
                System.exit(0);
            case "help":
                quit(driver, false, 0);
                break;
            default:
                System.err.println("ERROR: Input does not match options! Please type either \"retry\" or \"quit\" !");
                input(driver);
                break;
        }
    }

    private void closeDriver(WebDriver driver, int loginAttempts) {
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
