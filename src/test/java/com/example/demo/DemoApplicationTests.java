package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestRestTemplate
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private RestTestClient restTestClient;

	@Test
	void homeResponse() {
	}
}

