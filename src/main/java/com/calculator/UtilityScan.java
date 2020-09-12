package com.calculator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Iterator;

public class UtilityScan {

    public static JSONObject getStats() throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.utility-scan.com/odl/stats?key={API Key goes here}")
                .method("GET", null)
                .addHeader("Requester-ID", "Meow")
                .build();
        Response response = client.newCall(request).execute();
        JSONParser parser = new JSONParser();

        return (JSONObject) parser.parse(response.body().string());
    }

    public static void printPrettyJson(JSONObject obj) {
        //JsonObject myjson = response.body().string();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(String.valueOf((JSONObject) obj));
        String prettyJsonString = gson.toJson(je);
        System.out.println(prettyJsonString);
    }

    public static JSONObject getDay(JSONObject obj, String date) {

        JSONObject volumePerCorridor = (JSONObject) obj.get("volumePerCorridor");
        JSONObject day = (JSONObject) volumePerCorridor.get(date);

        return day;
    }

    public static Money sumCorridors(JSONObject obj) {

        Money money = Money.parse("USD 0.00");
        CurrencyUnit usd = CurrencyUnit.of("USD");

        Iterator<String> keys = obj.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            Double value = (Double) obj.get(key);
            value = Math.round(value * 100.00) / 100.00;
            money = money.plus(Money.of(usd, value));
        }

        return money;
    }

    public static void printFormattedCorridors(JSONObject obj) {

        CurrencyUnit usd = CurrencyUnit.of("USD");
        System.out.println("");

        Iterator<String> keys = obj.keySet().iterator();
        while(keys.hasNext()) {
            Money money = Money.parse("USD 0.00");
            String key = keys.next();
            Double value = (Double) obj.get(key);
            value = Math.round(value * 100.00) / 100.00;
            money = money.plus(Money.of(usd, value));

            MoneyFormatterBuilder moneyFormatBuilder = new MoneyFormatterBuilder();
            MoneyFormatter formatter = moneyFormatBuilder.appendCurrencySymbolLocalized().appendAmount().toFormatter(java.util.Locale.US);

            System.out.println("Corridor: " + key + " was " + formatter.print(money));
        }
    }
}
