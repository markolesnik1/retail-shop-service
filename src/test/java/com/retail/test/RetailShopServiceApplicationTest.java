package com.retail.test;

import com.retail.RetailShopServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest(classes = { RetailShopServiceApplication.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("devmock")
@Test
public class RetailShopServiceApplicationTest extends AbstractTestNGSpringContextTests {
	@Test
	public void contextLoads() {}
}
