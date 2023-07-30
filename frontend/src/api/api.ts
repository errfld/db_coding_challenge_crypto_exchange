export type Currency = {
  code: string;
  description: string;
};

export type CodesResponse = {
  currencies: {
    [key: string]: Currency;
  };
};

export type CurrencyWithRate = {
  rate: number;
} & Currency;

export type ExchangeRatesResponse = {
  base: string;
  date: string;
  rates: {
    [key: string]: number;
  };
};

export async function fetchLatestRates(
  base: string
): Promise<ExchangeRatesResponse> {
  const fetchResult = await fetch(`/api/latest?base=${base}`);
  return (await fetchResult.json()) as ExchangeRatesResponse;
}

export async function fetchAllCodes(): Promise<CodesResponse> {
  return (await (await fetch("/api/codes")).json()) as CodesResponse;
}
