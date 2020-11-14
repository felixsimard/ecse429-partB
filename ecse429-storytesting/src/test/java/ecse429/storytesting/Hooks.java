package ecse429.storytesting;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {

    @Before
    public void beforeScenario() {
        System.out.println("Before scenario");
        HelperFunctions.startApplication();
    }

    @After
    public void afterScenario() {
        System.out.println("After scenario");
        HelperFunctions.stopApplication();
    }
}
