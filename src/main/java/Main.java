import selenium.LocalSeleniumRunner;
import selenium.SeleniumRunner;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        LocalSeleniumRunner lsr = new LocalSeleniumRunner();
        lsr.start();
//        SeleniumRunner sr = new SeleniumRunner();
//        sr.start();
    }
}