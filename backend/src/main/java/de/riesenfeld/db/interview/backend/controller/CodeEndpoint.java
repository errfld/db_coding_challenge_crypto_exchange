package de.riesenfeld.db.interview.backend.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonFormat;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/")
public class CodeEndpoint {

  private Map<String, Currency> allCurrencies = new HashMap<>() {
    {
      put("EUR", new Currency("EUR", "Euro"));
      put("USD", new Currency("USD", "US Dollar"));
      put("BTC", new Currency("BTC", "Bitcoin"));
      put("ETH", new Currency("ETH", "Ethereum"));
      put("USDT", new Currency("USDT", "Tether"));
      put("DOGE", new Currency("DOGE", "Dogecoin"));
      put("SOL", new Currency("SOL", "Solana"));
    }
  };

  private Map<String, BigDecimal> exchangeRatesToUSD = new HashMap<>() {
    {
      put("USD", BigDecimal.ONE);
      put("EUR", BigDecimal.valueOf(1.10));
      put("BTC", BigDecimal.valueOf(22951.50));
      put("ETH", BigDecimal.valueOf(1577));
      put("USDT", BigDecimal.valueOf(1.00));
      put("DOGE", BigDecimal.valueOf(0.09));
      put("SOL", BigDecimal.valueOf(23.72));
    }
  };

  @RequestMapping("/codes")
  public CodesResponse getCodes() {
    return new CodesResponse(allCurrencies);
  }

  @RequestMapping("/latest")
  public ExchangeRatesResponse getLatest(@RequestParam Optional<String> base) {
    if (base.isEmpty() || !allCurrencies.containsKey(base.get()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid base currency: " + base.orElse("MISSING"));
    var res = exchangeRatesToUSD
        .entrySet().stream()
        .collect(
            Collectors.toMap(
                e -> e.getKey(),
                e -> e.getValue().divide(exchangeRatesToUSD.get(base.get()), 2, RoundingMode.HALF_UP)));
    return new ExchangeRatesResponse(base.get(), LocalDate.now(), res);
  }

}

record Currency(
    String code,
    String description) {

}

record CodesResponse(
    Map<String, Currency> currencies) {
}

record ExchangeRatesResponse(
    String base,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate date,
    Map<String, BigDecimal> rates) {
}