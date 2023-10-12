package com.rhythmpoizon.currencyconverterbot.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class CurrencyConverter {
    final static double serviceCommission = 1250;
    public final static double serviceCommissionRate = 1.2;
    public final static double  costStorageInWarehouse = 299;
    private static final String CBR_URL = "https://www.cbr.ru/scripts/XML_daily.asp";

    public static double currencyCNY() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(CBR_URL);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (InputStream inputStream = entity.getContent()) {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document doc = builder.parse(inputStream);

                        NodeList nodeList = doc.getElementsByTagName("Valute");
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            Element element = (Element) nodeList.item(i);
                            String charCode = element.getElementsByTagName("CharCode").item(0).getTextContent();
                            if ("CNY".equals(charCode)) {
                                String value = element.getElementsByTagName("Value").item(0).getTextContent();
                                return Double.parseDouble(value.replace(",", ".")); // Преобразовать курс в число
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        throw new Exception("Курс CNY не найден");
    }

    public static double getTotalAmount(double priceInCNY) throws Exception {
        double amount = (priceInCNY * (currencyCNY()+serviceCommissionRate)) + serviceCommission + costStorageInWarehouse;
        return amount;
    }
}
