import {
  QueryClient,
  QueryClientProvider,
  useQuery,
} from '@tanstack/react-query'
import { useState } from 'react';
import { CodesResponse, CurrencyWithRate, fetchAllCodes, fetchLatestRates } from './api/api';
import { AddButton, RemoveButton } from './components/Button';


const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <CryptoExchangeRateApp />
    </QueryClientProvider>
  )
}

const initialBaseData: CodesResponse = {
  currencies: {
    "USD": {
      code: "USD",
      description: "United States Dollar",
    }
  }
}


function CryptoExchangeRateApp() {
  const [base, setBase] = useState("USD");
  const [watchList, setWatchList] = useState<CurrencyWithRate[]>([]);

  return (
    <div className="flex flex-col w-full justify-center">
      <div className="navbar bg-base-200">
        <div className="flex flex-row container justify-end">
          <BaseCurrencyDropdown baseCode={base} setBaseCode={setBase} />
        </div>
      </div>
      <div className="flex flex-col container justify-center mx-auto">
        <WatchListTable
          data={watchList}
          remove={(code: string) => setWatchList(wl => wl.filter(v => v.code !== code))}
        />
        <ExchangeRateTable
          baseCode={base}
          watchList={watchList}
          remove={(code: string) => setWatchList(wl => wl.filter(v => v.code !== code))}
          add={(row: CurrencyWithRate) => setWatchList(wl => [row, ...wl])}
        />
      </div>
    </div>
  )
}

function BaseCurrencyDropdown({ baseCode, setBaseCode }: { baseCode: string, setBaseCode: (code: string) => void }) {
  const { data: baseData } = useQuery<CodesResponse>({
    queryKey: ['base'],
    queryFn: () => fetchAllCodes(),
    initialData: initialBaseData,
  });
  return (
    <div className="dropdown">
      <label tabIndex={0} className="btn btn-primary m-1">{baseCode}</label>
      <ul tabIndex={0} className="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52">
        {
          Object.keys(baseData.currencies).map((key) =>
            <li key={key} onClick={() => setBaseCode(key)}><a>{key}</a></li>
          )
        }
      </ul>
    </div>
  )
}

function WatchListTable({ data, remove }: { data: CurrencyWithRate[], remove: (code: string) => void }) {
  return (
    <table className="table w-full">
      <thead>
        <tr>
          <th>Watchlist</th>
        </tr>
      </thead>
      <tbody>
        {data.map((row) => (
          <tr key={row.code}>
            <td>{row.code}</td>
            <td>{row.description}</td>
            <td>{row.rate}</td>
            <td>
              <RemoveButton onClick={() => remove(row.code)} />
            </td>
          </tr>
        ))}

      </tbody>
    </table>
  )
}

function ExchangeRateTable({
  baseCode,
  watchList,
  remove,
  add
}: {
  baseCode: string,
  watchList: CurrencyWithRate[],
  remove: (code: string) => void,
  add: (row: CurrencyWithRate) => void
}) {

  const { data: ratesData } = useQuery<CurrencyWithRate[]>({
    queryKey: ['base', 'rates', baseCode],
    queryFn: async () => {
      const latestRates = await fetchLatestRates(baseCode)
      const codes = (await fetchAllCodes()).currencies
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
                ? <RemoveButton onClick={() => remove(row.code)} />
                : <AddButton onClick={() => add(row)} />
              }
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}

export default App
