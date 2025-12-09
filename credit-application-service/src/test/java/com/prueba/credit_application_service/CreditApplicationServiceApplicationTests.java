package com.prueba.credit_application_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CreditApplicationServiceApplicationTests {

	@Test
	void contextLoads() {
		// Verifies that Spring context loads successfully with H2 database
	}

}
