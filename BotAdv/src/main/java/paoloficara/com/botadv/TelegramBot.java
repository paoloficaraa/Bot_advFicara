/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paoloficara.com.botadv;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static org.glassfish.hk2.utilities.reflection.Pretty.array;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
    @SuppressWarnings("empty-statement")
    public void onUpdateReceived(Update update) {
//        String message = update.getMessage().getText();
//        int pos = message.indexOf(" ");
//        System.out.println(message.substring(pos + 1));
        if (update.hasMessage() && update.getMessage().hasText()) {

            String command = update.getMessage().getText();
            String city = command.substring(command.indexOf(" ") + 1);

            SendMessage message = new SendMessage();
            SendLocation location = new SendLocation();
            
            if (command.equals("/username")) {
                message.setText(getBotUsername());
            } else if (command.split(" ")[0].equals("/city")) {
                try {
                    if (getCity(city).equals("")) {
                        message.setText("City not found");
                    } else {
                        //System.out.println(command.split(" - ")[0] + " " + command.split(" - ")[1]);
                        MyFile file = new MyFile("user;chatId;cityName;latitude;longitude.txt");
                        if (!isThere(update.getMessage().getChat().getUserName(), file.leggi())) {
                            if (update.getMessage().getLocation() != null) {
                                //System.out.println(update.getMessage().getLocation().getLatitude() + " " + update.getMessage().getLocation().getLongitude());
                                file.scrivi(update.getMessage().getChat().getUserName() + ";" + update.getMessage().getChatId() + ";" + update.getMessage().getLocation().getLatitude() + ";" + update.getMessage().getLocation().getLongitude());
                            } else {
                                try {
                                    file.scrivi(update.getMessage().getChat().getUserName() + ";" + update.getMessage().getChatId() + ";" + getCity(city));
                                } catch (IOException ex) {
                                    Logger.getLogger(TelegramBot.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ParserConfigurationException ex) {
                                    Logger.getLogger(TelegramBot.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (SAXException ex) {
                                    Logger.getLogger(TelegramBot.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else if (position(update.getMessage().getChat().getUserName(), file.leggi()) != -1) {
                            List<String[]> strings = file.leggi();
                            String lat = "";
                            String lon = "";
                            String name = "";
                            try {
                                name = getCity(city).split(";")[0];
                                lat = getCity(city).split(";")[1];
                                lon = getCity(city).split(";")[2];
                            } catch (IOException ex) {
                                Logger.getLogger(TelegramBot.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ParserConfigurationException ex) {
                                Logger.getLogger(TelegramBot.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SAXException ex) {
                                Logger.getLogger(TelegramBot.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            String[] array = {update.getMessage().getChat().getUserName(), update.getMessage().getChatId().toString(), name, lat, lon};
                            strings.set(position(update.getMessage().getChat().getUserName(), file.leggi()), array);
                            file.riscriviFile(strings);
                        }
                        //message.setText("Datas saved");
                        location.setLatitude(Double.parseDouble(getCity(city).split(";")[1]));
                        location.setLongitude(Double.parseDouble(getCity(city).split(";")[2]));
                    }
                } catch (IOException ex) {
                    Logger.getLogger(TelegramBot.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(TelegramBot.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(TelegramBot.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                message.setText("Invalid command");
            }
            
            try {
                if(message.getText() != null){
                    message.setChatId(update.getMessage().getChatId().toString());
                    execute(message);
                }
                location.setChatId(update.getMessage().getChatId().toString());
                execute(location);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendAd(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException ex) {
            Logger.getLogger(TelegramBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getBotUsername() {
        return "advFicara_bot";
    }

    private String getCity(String city) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document d;
        String latitude = "", longitude = "", name = "";
        try {
            d = builder.parse("https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(city, StandardCharsets.UTF_8) + "&format=xml&addressdetails=1");
            Element root = d.getDocumentElement();
            NodeList places = root.getElementsByTagName("place");
            Element e = (Element) places.item(0);
            if (e == null) {
                return "";
            }
            if (e.getElementsByTagName("city").getLength() > 0 || e.getElementsByTagName("town").getLength() > 0 || e.getElementsByTagName("village").getLength() > 0) {
                name = e.getAttribute("display_name");
                latitude = e.getAttribute("lat");
                longitude = e.getAttribute("lon");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name + ";" + latitude + ";" + longitude;
    }

    private Boolean isThere(String username, List<String[]> strings) {
        for (String[] array : strings) {
            if (array[0].equals(username)) {
                return true;
            }
        }
        return false;
    }

    private int position(String username, List<String[]> strings) {
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i)[0].equals(username)) {
                return i;
            }
        }
        return -1;
    }
}
