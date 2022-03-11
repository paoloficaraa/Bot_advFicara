/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advficara;
import TelegramBots.TelegramBotsAPI;
import org.json.simple.parser.ParseException;
/**
 *
 * @author paolo
 */
public class AdvFicara {
    
    private static String apiToken = "5130234394:AAGZZykFoVSf94g4qyRpsk6gQ0CvDN-Wr8I";

    public static void main(String[] args) throws ParseException {
        TelegramBotsAPI api = new TelegramBotsAPI();
        ThreadListen thread1 = new ThreadListen(apiToken, api);
        thread1.start();
    }
    
}
