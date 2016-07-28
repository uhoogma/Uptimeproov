package com.example.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.conf.PropertyLoader;

public class CurrencyService {

	PropertyLoader propertyLoader = new PropertyLoader();
	String apiKey = propertyLoader.getCurrencylayerApiKey();
	String JSON = "{\"success\":true,\"terms\":\"https://currencylayer.com/terms\",\"privacy\":\"https://currencylayer.com/privacy\",\"timestamp\":1469581986,\"source\":\"USD\",\"quotes\":{\"USDAED\":3.672898,\"USDAFN\":68.900002,\"USDALL\":123.800003,\"USDAMD\":476.23999,\"USDANG\":1.77028,\"USDAOA\":165.061996,\"USDARS\":14.93398,\"USDAUD\":1.331306,\"USDAWG\":1.79,\"USDAZN\":1.575027,\"USDBAM\":1.782202,\"USDBBD\":2,\"USDBDT\":78.330002,\"USDBGN\":1.778498,\"USDBHD\":0.3771,\"USDBIF\":1646.050049,\"USDBMD\":1.00005,\"USDBND\":1.357501,\"USDBOB\":6.860103,\"USDBRL\":3.276305,\"USDBSD\":1,\"USDBTC\":0.001538,\"USDBTN\":67.249928,\"USDBWP\":10.747703,\"USDBYR\":20020,\"USDBZD\":1.979532,\"USDCAD\":1.317261,\"USDCDF\":944.000269,\"USDCHF\":0.99201,\"USDCLF\":0.02503,\"USDCLP\":659.700012,\"USDCNY\":6.66966,\"USDCOP\":3061,\"USDCRC\":541.349976,\"USDCUC\":0.995,\"USDCUP\":0.999671,\"USDCVE\":100.199997,\"USDCZK\":24.5867,\"USDDJF\":176.789993,\"USDDKK\":6.76892,\"USDDOP\":45.769493,\"USDDZD\":110.543999,\"USDEEK\":14.252981,\"USDEGP\":8.8798,\"USDERN\":15.420174,\"USDETB\":21.799999,\"USDEUR\":0.909702,\"USDFJD\":2.080499,\"USDFKP\":0.761199,\"USDGBP\":0.76141,\"USDGEL\":2.344501,\"USDGGP\":0.761488,\"USDGHS\":3.938596,\"USDGIP\":0.761398,\"USDGMD\":42.249572,\"USDGNF\":8921.999484,\"USDGTQ\":7.576501,\"USDGYD\":204.899994,\"USDHKD\":7.756504,\"USDHNL\":22.659788,\"USDHRK\":6.773703,\"USDHTG\":62.990002,\"USDHUF\":284.649994,\"USDIDR\":13135,\"USDILS\":3.838035,\"USDIMP\":0.761488,\"USDINR\":67.360001,\"USDIQD\":1169,\"USDIRR\":30065.000134,\"USDISK\":121.179788,\"USDJEP\":0.761488,\"USDJMD\":126.220001,\"USDJOD\":0.707802,\"USDJPY\":105.082001,\"USDKES\":101.199997,\"USDKGS\":67.126999,\"USDKHR\":4075.000293,\"USDKMF\":446.799988,\"USDKPW\":899.999741,\"USDKRW\":1134.199951,\"USDKWD\":0.301504,\"USDKYD\":0.820055,\"USDKZT\":354.049988,\"USDLAK\":8069.999733,\"USDLBP\":1504.400024,\"USDLKR\":145.949997,\"USDLRD\":90.000224,\"USDLSL\":14.380248,\"USDLTL\":3.0487,\"USDLVL\":0.62055,\"USDLYD\":1.389501,\"USDMAD\":9.8448,\"USDMDL\":19.73045,\"USDMGA\":3016.00025,\"USDMKD\":55.75005,\"USDMMK\":1188.999935,\"USDMNT\":2040.999996,\"USDMOP\":7.989022,\"USDMRO\":352.999894,\"USDMUR\":35.290001,\"USDMVR\":15.11184,\"USDMWK\":709.400024,\"USDMXN\":18.7535,\"USDMYR\":4.054896,\"USDMZN\":66.25046,\"USDNAD\":14.431005,\"USDNGN\":311.999992,\"USDNIO\":28.299999,\"USDNOK\":8.56867,\"USDNPR\":107.300003,\"USDNZD\":1.4187,\"USDOMR\":0.384901,\"USDPAB\":1.003645,\"USDPEN\":3.365498,\"USDPGK\":3.1755,\"USDPHP\":47.119999,\"USDPKR\":104.650002,\"USDPLN\":3.966991,\"USDPYG\":5592.000177,\"USDQAR\":3.641199,\"USDRON\":4.057202,\"USDRSD\":111.870003,\"USDRUB\":65.901604,\"USDRWF\":783.599976,\"USDSAR\":3.7503,\"USDSBD\":7.902594,\"USDSCR\":13.261007,\"USDSDG\":6.069796,\"USDSEK\":8.65417,\"USDSGD\":1.35748,\"USDSHP\":0.761404,\"USDSLL\":5500.000249,\"USDSOS\":557.000065,\"USDSRD\":7.079855,\"USDSTD\":22282,\"USDSVC\":8.722202,\"USDSYP\":215.550003,\"USDSZL\":14.380178,\"USDTHB\":34.959999,\"USDTJS\":7.86798,\"USDTMT\":3.41,\"USDTND\":2.235504,\"USDTOP\":2.291604,\"USDTRY\":3.0404,\"USDTTD\":6.679497,\"USDTWD\":32.054001,\"USDTZS\":2182.000092,\"USDUAH\":24.780001,\"USDUGX\":3377.999911,\"USDUSD\":1,\"USDUYU\":29.879999,\"USDUZS\":2960.000224,\"USDVEF\":9.980315,\"USDVND\":22300,\"USDVUV\":106.550003,\"USDWST\":2.5815,\"USDXAF\":596.380005,\"USDXAG\":0.051138,\"USDXAU\":0.000757,\"USDXCD\":2.7029,\"USDXDR\":0.721041,\"USDXOF\":596.799988,\"USDXPF\":108.050003,\"USDYER\":249.949997,\"USDZAR\":14.347603,\"USDZMK\":5156.083085,\"USDZMW\":9.849988,\"USDZWL\":322.355011}}";

	public Map<String, Double> getCurrencies(String target) {
		try {
			JSONObject obj = new JSONObject(getCurrency());
			JSONObject quotes = obj.getJSONObject("quotes");
			String defaultCurrency = obj.getString("source");
			Map<String, Double> map = processJSON(quotes);
			Double rate = map.get(defaultCurrency + target);
			Map<String, Double> mappedToTargetCurrency = new HashMap<>();
			mappedToTargetCurrency = mapToTargetCurrency(quotes, rate);
			return mappedToTargetCurrency;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new HashMap<String, Double>();
	}

	private String getCurrency() {
		// String json = callURL("http://www.apilayer.net/api/live?access_key=" + apiKey);
		return JSON;
	}

	// http://crunchify.com/java-url-example-getting-text-from-url/
	public static String callURL(String myURL) {
		System.out.println("Requested URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			if (in != null) {
				in.close();	
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:" + myURL, e);
		}

		return sb.toString();
	}

	private Map<String, Double> processJSON(JSONObject quotes) {
		Map<String, Double> map = new HashMap<String, Double>();
		Iterator<String> iter = quotes.keys();
		while (iter.hasNext()) {
			String key = ((String) iter.next()).toString();
			Object moneyAsObject = (quotes.get(key));
			Double value = moneyAsObject instanceof Double ? value = (Double) (quotes.get(key))
					: new Double((Integer) (quotes.get(key)));
			map.put(key, value);
		}
		return map;
	}

	private Map<String, Double> mapToTargetCurrency(JSONObject quotes, Double rate) {
		Map<String, Double> mappedToTargetCurrency = new HashMap<>();
		Iterator<String> iter = quotes.keys();
		while (iter.hasNext()) {
			String key = ((String) iter.next()).toString();
			String targetCurrency = key.substring(3, key.length());
			Object moneyAsObject = quotes.get(key);
			Double value = moneyAsObject instanceof Double ? value = (Double) (quotes.get(key))
					: new Double((Integer) (quotes.get(key)));
			double finalRate = value / rate;
			mappedToTargetCurrency.put(targetCurrency, finalRate);
		}
		return new TreeMap<String, Double>(mappedToTargetCurrency);
	}
}
