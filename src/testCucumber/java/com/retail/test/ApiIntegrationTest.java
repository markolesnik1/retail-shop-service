package com.retail.test;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.springframework.test.annotation.DirtiesContext;

@CucumberOptions(
        features = "classpath:features",
        tags = {"@restApiIntegration", "~@ignore"},
        glue = {"com.retail.test.steps"},
        strict = true,
        format = {
            "html:build/reports/cucumber/restApiIntegration",
            "json:build/reports/cucumber/restApiIntegration.json"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApiIntegrationTest extends AbstractTestNGCucumberTests {
}
