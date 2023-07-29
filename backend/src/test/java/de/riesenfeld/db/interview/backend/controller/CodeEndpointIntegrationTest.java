package de.riesenfeld.db.interview.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CodeEndpointIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void CodeEndpointShouldReturnCodes() throws Exception {
    var result = this.restTemplate.getForObject("http://localhost:" + port + "/codes", String.class);
    assertEquals(
        """
            {"currencies":{"BTC":{"code":"BTC","description":"Bitcoin"},"SOL":{"code":"SOL","description":"Solana"},"EUR":{"code":"EUR","description":"Euro"},"USD":{"code":"USD","description":"US Dollar"},"ETH":{"code":"ETH","description":"Ethereum"},"USDT":{"code":"USDT","description":"Tether"},"DOGE":{"code":"DOGE","description":"Dogecoin"}}}
              """
            .strip(),
        result);
  }
}
