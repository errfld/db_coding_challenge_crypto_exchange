# Deutsche Bank Coding Challenge

After the simplest solution to be found in branch [1_simple_solution](https://github.com/errfld/db_coding_challenge_crypto_exchange/tree/1_simple_solution) I started with CI/CD and created a simple pipeline based on github actions, this step can be found at [2_ci-cd](https://github.com/errfld/db_coding_challenge_crypto_exchange/tree/2_ci-cd).

The next step is some refactoring to tidy up the code and make it easier to evolve in the future.

## What changed?

### Backend

I started with the backend and splitted the Endpoint files, in two separate endpoints, after that created Services which encapsulate the logic and pull it out of the endpoints. The `CurrencyService` for example gets its data injected, using the `HashMap` as a mock Database.
Of course, this was made while creating new test and running the existing test to go forward with confidence.

I added checks for `null` on most places and tests to ensure null checking.

On the matter of code style, right now I kept most of the code imperative in favor over a more fluent code style, to keep it simple.

A lot of engineers prefer fluent code, but I sometime wonder why, as shown in this example:

```java
if (base.isEmpty())
  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing base currency");

Optional<Currency> requestedBaseCurrency = currencyService.getCurrencyByCode(base.get());

if (requestedBaseCurrency.isEmpty())
  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid base currency: " + base.get());

Currency baseCurrency = requestedBaseCurrency.get();
```

In comparison to the following:

```java
Currency baseCurrency = currencyService
  .getCurrencyByCode(base
    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing base currency"))
  ).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid base currency: " + base.get()));
```

Both snippets do the exact same thing, and I personally think the first one is easier to read, but the second example more "elegant".
