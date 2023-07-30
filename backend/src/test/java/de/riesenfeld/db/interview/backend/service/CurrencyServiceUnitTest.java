package de.riesenfeld.db.interview.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class CurrencyServiceUnitTest {

  @Test
  public void testGetAllCurrenciesNoArgsConstructor() {
    CurrencyService currencyService = new CurrencyService();
    var result = currencyService.getAllCurrencies();
    assertNotNull(currencyService, "getAllCurrencies() should not return null");
    assertTrue(result.size() == 7, "There should be 7 Elements in the Set, when using no args constructor");
  }

  @Test
  public void testGetAllCurrenciesWithArgsConstructor() {
    CurrencyService currencyService = new CurrencyService(Set.of(new Currency("EUR", "Euro")));
    var result = currencyService.getAllCurrencies();
    assertNotNull(currencyService, "getAllCurrencies() should not return null");
    assertTrue(result.size() == 1, "There should be 7 Elements in the Set, when using no args constructor");
    assertEquals("EUR", result.iterator().next().code(), "The code of the currency should be EUR");
  }

  @Test
  public void testForbidCreateServiceWithNullArgs() {
    try {
      new CurrencyService(null);
      fail("should throw Exception");
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException, "The Exception should be a NullPointerException");
    }
  }
}
