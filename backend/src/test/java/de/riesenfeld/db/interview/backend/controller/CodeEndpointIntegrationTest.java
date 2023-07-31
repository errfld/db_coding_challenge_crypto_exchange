package de.riesenfeld.db.interview.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CodeEndpointIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void CodeEndpointShouldReturnCodes() throws Exception {
    var result = this.restTemplate.getForObject("http://localhost:" + port + "/codes", JsonNode.class);
    assertTrue(result.has("currencies"));
    JsonNode currencies = result.get("currencies");
    assertTrue(currencies.isObject());
    assertTrue(currencies.has("EUR"));
    JsonNode eur = currencies.get("EUR");
    assertTrue(eur.isObject());
    assertTrue(eur.has("code"));
    assertTrue(eur.has("description"));
    var currencyFieldsIterator = currencies.fields();
    while (currencyFieldsIterator.hasNext()) {
      var currency = currencyFieldsIterator.next();
      assertEquals(currency.getKey(), currency.getValue().get("code").asText());
      assertTrue(currency.getValue().has("code"));
      assertTrue(currency.getValue().has("description"));
    }
  }
}
