package de.riesenfeld.db.interview.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {
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
  private final CurrencyService currencyService;

  public ExchangeRateService(CurrencyService currencyService) {
    this.currencyService = Optional.of(currencyService)
        .orElseThrow(() -> new NullPointerException("currencyService must not be null"));
  }

  public Map<Currency, BigDecimal> getExchangeRates(Currency baseCurrency) {
    if (baseCurrency == null)
      throw new NullPointerException("baseCurrency must not be null");
    return currencyService.getAllCurrencies().stream()
        .collect(Collectors.toMap(c -> c, c -> getExchangeRate(baseCurrency, c)));
  }

  public BigDecimal getExchangeRate(Currency baseCurrency, Currency currency) {
    if (baseCurrency == null)
      throw new NullPointerException("baseCurrency must not be null");
    if (currency == null)
      throw new NullPointerException("currency must not be null");

    if (baseCurrency.equals(currency)) {
      return BigDecimal.ONE;
    }
    BigDecimal exchangeRateToUSD = exchangeRatesToUSD.get(currency.code());
    BigDecimal exchangeRateToBaseCurrency = exchangeRatesToUSD.get(baseCurrency.code());
    return exchangeRateToUSD.divide(exchangeRateToBaseCurrency, 2, RoundingMode.HALF_UP);
  }

}
