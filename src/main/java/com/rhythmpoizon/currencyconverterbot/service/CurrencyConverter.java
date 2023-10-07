package com.rhythmpoizon.currencyconverterbot.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class CurrencyConverter {
    final static double serviceСommission = 1000;
    public static double correncyCNY() throws Exception {
        try {
            // Создание HTTP-клиента
            HttpClient httpClient = HttpClients.createDefault();

            String url = "https://api.currencyapi.com/v3/latest?apikey=cur_live_koRvKwmzPFS7ZSoWkghuvew6x4zLNguQqm5nJrrh&currencies=RUB&base_currency=CNY";

            HttpGet httpGet = new HttpGet(url);

            HttpResponse response = httpClient.execute(httpGet);

            // Проверка кода ответа HTTP
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new Exception("Ошибка при выполнении HTTP-запроса. Код ответа: " + statusCode);
            }

            // Извлечение JSON-ответа
            String jsonResponse = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Извлечение курса CNY к RUB
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject rubData = data.getJSONObject("RUB");
            double rate = rubData.getDouble("value");

            return rate;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static double getTotalAmount(double priceInCNY) throws Exception {
        double amount = (priceInCNY * correncyCNY()) + serviceСommission ;
        return amount;
    }
}
