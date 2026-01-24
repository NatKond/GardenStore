package de.telran.gardenStore.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.dto.security.LoginRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StepDefinition extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest loginRequest;

    private ResultActions result;

    @Given("I want to sing in with valid credentials: {string} login and {string} password")
    public void iWantToSingInWithValidCredentialsLoginAndPassword(String login, String password) {
        loginRequest = LoginRequest.builder()
                .email(login)
                .password(password)
                .build();
    }

    @When("I submit login and password")
    public void iSubmitLoginAndPassword() throws Exception {
        result = mockMvc.perform(post("/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));
    }

    @Then("I receive response with JWT Token")
    public void iReceiveResponseWithJWTToken() throws Exception {
        result.andExpectAll(
                status().isAccepted(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().string(containsString("token")));
    }
}
