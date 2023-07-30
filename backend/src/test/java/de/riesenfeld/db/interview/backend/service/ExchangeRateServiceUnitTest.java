package de.riesenfeld.db.interview.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class ExchangeRateServiceUnitTest {

  @Test
  public void testGetExchangeRate() {
    CurrencyService currencyService = new CurrencyService();
    ExchangeRateService exchangeRateService = new ExchangeRateService(currencyService);
    var result = exchangeRateService.getExchangeRate(currencyService.getCurrencyByCode("USD").get(),
        currencyService.getCurrencyByCode("USD").get());
    assertEquals(BigDecimal.ONE, result, "The exchange rate should be 1");
    result = exchangeRateService.getExchangeRate(currencyService.getCurrencyByCode("USD").get(),
        currencyService.getCurrencyByCode("EUR").get());
    assertEquals(BigDecimal.valueOf(1.10).setScale(2), result, "The exchange rate should be 1.10");
  }

  @Test
  public void testGetExchangeRateNull() {
    CurrencyService currencyService = new CurrencyService();
    ExchangeRateService exchangeRateService = new ExchangeRateService(currencyService);
    assertThrows(NullPointerException.class, () -> exchangeRateService.getExchangeRate(null,
        currencyService.getCurrencyByCode("USD").get()), "The baseCurrency should not be null");
    assertThrows(NullPointerException.class, () -> exchangeRateService.getExchangeRate(
        currencyService.getCurrencyByCode("USD").get(), null), "The currency should not be null");
  }

  @Test
  public void testGetExchangeRatesNull() {
    CurrencyService currencyService = new CurrencyService();
    ExchangeRateService exchangeRateService = new ExchangeRateService(currencyService);
    assertThrows(NullPointerException.class, () -> exchangeRateService.getExchangeRates(null),
        "The baseCurrency should not be null");
  }
}
