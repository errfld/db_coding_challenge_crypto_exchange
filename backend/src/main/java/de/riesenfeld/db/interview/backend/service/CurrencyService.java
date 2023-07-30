package de.riesenfeld.db.interview.backend.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

  private final Set<Currency> currencyData;

  public CurrencyService() {
    this(Set.of(
        new Currency("EUR", "Euro"),
        new Currency("USD", "US Dollar"),
        new Currency("BTC", "Bitcoin"),
        new Currency("ETH", "Ethereum"),
        new Currency("USDT", "Tether"),
        new Currency("DOGE", "Dogecoin"),
        new Currency("SOL", "Solana")));
  }

  public CurrencyService(Set<Currency> currencyData) {
    if (currencyData == null) {
      throw new NullPointerException("currencyData must not be null");
    }
    this.currencyData = currencyData;
  }

  /**
   * 
   * @return Read-only set of currencies
   */
  public Set<Currency> getAllCurrencies() {
    return currencyData;
  }

  public Optional<Currency> getCurrencyByCode(String code) {
    return currencyData.stream().filter(c -> c.code().equals(code)).findFirst();
  }

}