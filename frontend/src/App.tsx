import {
  QueryClient,
  QueryClientProvider,
  useQuery,
} from '@tanstack/react-query'
import { useState } from 'react';


const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <CryptoExchangeRateApp />
    </QueryClientProvider>
  )
}

type Currency = {
  code: string,
  description: string,
}

type CodesResponse = {
  currencies: {
    [key: string]: Currency
  }
}

type CurrencyWithRate = {
  rate: number,
} & Currency

type ExchangeRatesResponse = {
  base: string,
  date: string,
  rates: {
    [key: string]: number
  }
}

const initialBaseData: CodesResponse = {
  currencies: {
    "USD": {
      code: "USD",
      description: "United States Dollar",
    }
  }
}

const plusIcon = <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
  <path fillRule="evenodd" d="M12 3.75a.75.75 0 01.75.75v6.75h6.75a.75.75 0 010 1.5h-6.75v6.75a.75.75 0 01-1.5 0v-6.75H4.5a.75.75 0 010-1.5h6.75V4.5a.75.75 0 01.75-.75z" clipRule="evenodd" />
</svg>
const removeIcon = <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
  <path fillRule="evenodd" d="M5.47 5.47a.75.75 0 011.06 0L12 10.94l5.47-5.47a.75.75 0 111.06 1.06L13.06 12l5.47 5.47a.75.75 0 11-1.06 1.06L12 13.06l-5.47 5.47a.75.75 0 01-1.06-1.06L10.94 12 5.47 6.53a.75.75 0 010-1.06z" clipRule="evenodd" />
</svg>



function CryptoExchangeRateApp() {
  const { data: baseData } = useQuery<CodesResponse>({
    queryKey: ['base'],
    queryFn: async () => await (await fetch("http://localhost:8080/codes")).json(),
    initialData: initialBaseData,
  });
  const [base, setBase] = useState("USD");
  const [watchList, setWatchList] = useState<CurrencyWithRate[]>([]);

  const { data: ratesData } = useQuery<CurrencyWithRate[]>({
    queryKey: ['base', 'rates', base],
    queryFn: async () => {
      const latestRates = await (await fetch(`http://localhost:8080/latest?base=${base}`)).json() as ExchangeRatesResponse
      const codes = (await (await fetch("http://localhost:8080/codes")).json() as CodesResponse).currencies
      const res = Object.keys(latestRates.rates).map((key) => ({
        code: key,
        description: codes[key].description,
        rate: latestRates.rates[key]
      } as CurrencyWithRate))
      return res;
    },
    initialData: [],
  })


  return (
    <div className="flex flex-col w-full justify-center">
      <div className="navbar bg-base-200">
        <div className="flex flex-row container justify-end">
          <div className="dropdown">
            <label tabIndex={0} className="btn btn-primary m-1">{base}</label>
            <ul tabIndex={0} className="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52">
              {
                Object.keys(baseData.currencies).map((key) =>
                  <li key={key} onClick={() => setBase(key)}><a>{key}</a></li>
                )
              }
            </ul>
          </div>
        </div>
      </div>
      <div className="flex flex-col container justify-center mx-auto">
        <table className="table w-full">
          <thead>
            <tr>
              <th>Watchlist</th>
            </tr>
          </thead>
          <tbody>
            {watchList.map((row) => (
              <tr key={row.code}>
                <td>{row.code}</td>
                <td>{row.description}</td>
                <td>{row.rate}</td>
                <td>
                  <button className="btn btn-outline btn-primary btn-sm" onClick={() => setWatchList(wl => wl.filter(v => v.code !== row.code))}>{removeIcon}</button>
                </td>
              </tr>
            ))}

          </tbody>
        </table>
        <table className="table w-full">
          <thead>
            <tr>
              <th>Symbol</th>
              <th>Asset</th>
              <th>Exchange rate</th>
            </tr>
          </thead>
          <tbody>
            {ratesData.map((row) => (
              <tr key={row.code}>
                <td>{row.code}</td>
                <td>{row.description}</td>
                <td>{row.rate}</td>
                <td>
                  {watchList.find(v => v.code === row.code)
                    ? <button className="btn btn-outline btn-primary btn-sm" onClick={() => setWatchList(wl => wl.filter(v => v.code !== row.code))}>{removeIcon}</button>
                    : <button className="btn btn-outline btn-primary btn-sm" onClick={() => setWatchList(wl => [row, ...wl])}>{plusIcon}</button>}

                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default App
