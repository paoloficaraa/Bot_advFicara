/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelegramBots;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author paolo
 */
public class TelegramBotsAPI {

    

    public void sendToTelegram(String chatId, String text, String apiToken) {
        //metodo send message per inviare messaggi tramite Telegram API
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

        urlString = String.format(urlString, apiToken, chatId, text);

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getUpdatesFromTelegram(String apiToken) throws ParseException {
        String urlString = "https://api.telegram.org/bot%s/getUpdates";

        urlString = String.format(urlString, apiToken);

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(in);
            JSONObject json = (JSONObject) obj;
            //System.out.println(json);
            JSONArray result = (JSONArray) json.get("result");
            //System.out.println(result);
            //System.out.println(result.get(result.size() - 1));
            JSONObject obj2 = (JSONObject) result.get(result.size() - 1);
            JSONObject obj3 = (JSONObject) obj2.get("message");
            JSONObject obj4 = (JSONObject) obj3.get("chat");
            String chatId = obj4.get("id").toString();
            //System.out.println(chatId);
            in.close();
            String[] array = {chatId, obj3.get("text").toString()};
            return array;
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] wrongArray = {"CHAT ERROR", "TEXT ERROR"};
        return wrongArray;
    }

//    public void encodeJSON() {
//        JSONObject obj = new JSONObject();
//        obj.put("name", "sonoo");
//        obj.put("age", 27);
//        obj.put("salary", 600000);
//        System.out.print(obj);
//    }
//
//    public void encodeJSONArray() {
//        JSONArray arr = new JSONArray();
//        arr.add("sonoo");
//        arr.add(27);
//        arr.add(600000);
//        System.out.print(arr);
//    }
//    public void decodeJSON(String s) {
//        Object obj = JSONValue.parse(s);
//        JSONObject jsonObject = (JSONObject) obj;
//        System.out.println(jsonObject.toJSONString());
//        return (String) jsonObject.get("ok");
//          JSONParser parser = new JSONParser();
//          
//    }
}
