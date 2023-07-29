package de.riesenfeld.db.interview.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExchangeRateLatestEndpointIntegrationTest {
  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Test
  public void LatestEndpointShouldReturnExchangeRatesForUSD() throws Exception {
    var result = this.restTemplate.getForObject("http://localhost:" + port + "/latest?base=USD", String.class);
    var today = dtf.format(LocalDate.now());
    assertEquals(
        """
              {"base":"USD","date":"%s","rates":{"BTC":22951.50,"SOL":23.72,"EUR":1.10,"USD":1.00,"ETH":1577.00,"USDT":1.00,"DOGE":0.09}}
            """
            .formatted(today)
            .strip(),
        result);
  }

  @Test
  public void LatestEndpointShouldThrowExceptionForUnknownBaseCode() throws Exception {
    var result = this.restTemplate.getForObject("http://localhost:" + port + "/latest?base=XYZ", String.class);
    assertTrue(
        result
            .contains(
                "\"status\":400,\"error\":\"Bad Request\",\"message\":\"Invalid base currency: XYZ\",\"path\":\"/latest\""));
  }

  @Test
  public void LatestEndpointShouldReturnExchangeRatesForBTC() throws Exception {
    var result = this.restTemplate.getForObject("http://localhost:" + port + "/latest?base=BTC", String.class);
    var today = dtf.format(LocalDate.now());
    assertEquals(
        """
            {"base":"BTC","date":"%s","rates":{"BTC":1.00,"SOL":0.00,"EUR":0.00,"USD":0.00,"ETH":0.07,"USDT":0.00,"DOGE":0.00}}
            """
            .formatted(today)
            .strip(),
        result);
  }
}
