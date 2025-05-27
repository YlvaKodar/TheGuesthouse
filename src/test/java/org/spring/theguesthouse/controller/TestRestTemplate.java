package org.spring.theguesthouse.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


// Simple TestRestTemplate test with minimal setup

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerSimpleRestTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${local.server.port}")
    private int port;

    @Test
    void testGetAllCustomers() {
        // Simple GET request test
        String url = "http://localhost:" + port + "/customers/all";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Basic assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).contains("Customers");
        assertNotNull(response.getBody());
    }
}
