package de.riesenfeld.db.interview.backend.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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

import de.riesenfeld.db.interview.backend.service.Currency;
import de.riesenfeld.db.interview.backend.service.CurrencyService;
import de.riesenfeld.db.interview.backend.service.ExchangeRateService;

@RestController
@RequestMapping("/latest")
@CrossOrigin(origins = "*")
public class ExchangeRateEndpoint {

  private final CurrencyService currencyService;

  private final ExchangeRateService exchangeRateService;

  public ExchangeRateEndpoint(ExchangeRateService exchangeRateService, CurrencyService currencyService) {
    this.exchangeRateService = Optional.of(exchangeRateService).orElseThrow(() -> new NullPointerException(
        "exchangeRateService must not be null"));
    this.currencyService = Optional.of(currencyService)
        .orElseThrow(() -> new NullPointerException("currencyService must not be null"));
  }

  @RequestMapping(path = { "", "/" })
  public ExchangeRatesResponse getLatest(@RequestParam Optional<String> base) {
    Currency baseCurrency = currencyService.getCurrencyByCode(
        base.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing base currency")))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid base currency: " + base.get()));

    return new ExchangeRatesResponse(baseCurrency.code(), LocalDate.now(),
        exchangeRateService.getExchangeRates(baseCurrency).entrySet().stream()
            .collect(Collectors.toMap(e -> e.getKey().code(), e -> e.getValue().setScale(2, RoundingMode.HALF_UP))));
  }
}

record ExchangeRatesResponse(
    String base,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate date,
    Map<String, BigDecimal> rates) {
}