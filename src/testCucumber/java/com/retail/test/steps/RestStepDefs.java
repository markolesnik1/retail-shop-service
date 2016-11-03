package com.retail.test.steps;

import com.retail.RetailShopServiceApplication;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.JsonExpectationsHelper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ContextConfiguration(
        classes = {RetailShopServiceApplication.class},
        loader = SpringBootContextLoader.class)
@WebAppConfiguration
@ActiveProfiles("devmock")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestStepDefs {
    @Autowired
    private volatile WebApplicationContext webApplicationContext;

    private volatile MockMvc mockMvc;

    private ResultActions resultActions;

    private MockHttpSession mockHttpSession;

    @Before
    public void beforeScenario() {
        this.mockHttpSession = new MockHttpSession();
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    private ResultActions perform(MockHttpServletRequestBuilder request) throws Exception {
        return this.mockMvc.perform(request.session(mockHttpSession));
    }

    @When("^client requests GET ([\\S]*)$")
    public void performGetOnResourceUri(String resourceUri) throws Exception {
        resultActions = perform(get(resourceUri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @When("^client requests POST (.*) with json data:$")
    public void performPostOnResourceUriWithJsonData(String resourceUri, String jsonData) throws Exception {
        resultActions = perform(post(resourceUri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonData.getBytes(UTF_8)))
                .andDo(print());
    }

    @Then("^response code should be (\\d*)$")
    public void checkResponse(int statusCode) throws Exception {
        resultActions.andExpect(status().is(statusCode));
    }

    @Then("^result json should be:$")
    public void checkResponseJsonMatch(String jsonString) throws Exception {
        resultActions.andExpect(jsonResultMatcher(jsonString));
    }

    @Then("^header \"(.*)\" should be present with value \"(.*)\"$")
    public void checkHeaderPresenceWithValue(String headerName, String headerValue) throws Exception {
        assertEquals("Header not present with value: " + headerName + "=" + headerValue, headerValue,
                resultActions.andReturn().getResponse().getHeaderValue(headerName));
    }

    private ResultMatcher jsonResultMatcher(final String expectedJsonContent) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                String jsonContent = result.getResponse().getContentAsString();

                JsonExpectationsHelper jsonHelper = new JsonExpectationsHelper();
                jsonHelper.assertJsonEqual(expectedJsonContent, jsonContent);
            }
        };
    }
}