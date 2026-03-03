package stepDefinitions;


import io.cucumber.java.AfterAll;

import java.io.IOException;

public class Hooks {
    static StepDefinition s = new StepDefinition();

    @AfterAll
    public static void cleanup() throws IOException {
        s.deleteAllBoards(); //delete all boards before running the tests to ensure a clean state
    }

}
