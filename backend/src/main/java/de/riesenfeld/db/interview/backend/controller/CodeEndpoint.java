package de.riesenfeld.db.interview.backend.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.riesenfeld.db.interview.backend.service.Currency;
import de.riesenfeld.db.interview.backend.service.CurrencyService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class CodeEndpoint {

  private final CurrencyService currencyService;

  public CodeEndpoint(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  @RequestMapping("/codes")
  public CodesResponse getCodes() {
    return new CodesResponse(
        this.currencyService.getAllCurrencies().stream().collect(Collectors.toMap(Currency::code, c -> c)));
  }

}

record CodesResponse(
    Map<String, Currency> currencies) {
}
