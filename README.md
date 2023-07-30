# Deutsche Bank Coding Challenge

After the simplest solution to be found in branch [1_simple_solution](https://github.com/errfld/db_coding_challenge_crypto_exchange/tree/1_simple_solution) I started with CI/CD and created a simple pipeline based on github actions, this step can be found at [2_ci-cd](https://github.com/errfld/db_coding_challenge_crypto_exchange/tree/2_ci-cd).

The current step is some refactoring to tidy up the code and make it easier to evolve in the future.

## What changed?

### Backend

I started with the backend and splitted the Endpoint files, in two separate endpoints, after that created Services which encapsulate the logic and pull it out of the endpoints. The `CurrencyService` for example gets its data injected, using the `HashMap` as a mock Database.
Of course, this was made while creating new tests beforehand and running the existing tests to go forward with confidence.

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

### Frontend

Within the frontend I also started encapsulation and refactoring out components. The app had been a single Component style app, which is hard to work with with teams or even for a single person, while keeping your sanity.

The single component is now splitted in:

- BaseCurrencyDropdown (the dropdown in the upper right)
- WatchListTable
- ExchangeRateTable
- Add/Remove Buttons

This components can be placed in different files, but an application this small has no need for it, I refactored the Buttons as the "most atomic" style components in their own file, just for demonstration purposes.

Furthermore did I refactor the api and types our in its own file, to not _pollute_ the React Components. Data between Components is past by properties, as I did not see a real benefit of the introduction of even small state libraries like zustand or jotai.
