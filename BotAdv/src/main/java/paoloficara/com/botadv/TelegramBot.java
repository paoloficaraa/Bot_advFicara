/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paoloficara.com.botadv;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author paolo
 */
public class TelegramBot extends TelegramLongPollingBot {
    
    @Override
    public String getBotToken() {
        return "5130234394:AAGZZykFoVSf94g4qyRpsk6gQ0CvDN-Wr8I";
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        //System.out.println(update.getMessage().getText());
        String command = update.getMessage().getText();
        
        SendMessage message = new SendMessage();
        
        if (command.equals("/username")) {
            message.setText(getBotUsername());
        } else if (command.split(" ")[0].equals("/city")) {
            message.setText("si");
        } else {
            message.setText("Invalid command");
        }
        
        message.setChatId(update.getMessage().getChatId().toString());
        
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String getBotUsername() {
        return "advFicara_bot";
    }
    
}
