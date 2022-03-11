/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advficara;

import TelegramBots.TelegramBotsAPI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ficara_paolo
 */
public class ThreadListen extends Thread {

    private String apiToken;
    private TelegramBotsAPI api;

    public ThreadListen(String apiToken, TelegramBotsAPI api) {
        this.apiToken = apiToken;
        this.api = api;
    }

    @Override
    public void run() {
        String lastMessage = "";
        while (true) {
            try {
                String chatId = api.getUpdatesFromTelegram(apiToken)[0];
                String text = api.getUpdatesFromTelegram(apiToken)[1];
                if (!lastMessage.equals(text)) {
                    if (text.charAt(0) == '/') {
                        if (text.equals("/città")) {
                            api.sendToTelegram(chatId, "asso city", apiToken);
                        }
                    }
                }
                lastMessage = api.getUpdatesFromTelegram(apiToken)[1];
            } catch (ParseException ex) {
                Logger.getLogger(ThreadListen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
