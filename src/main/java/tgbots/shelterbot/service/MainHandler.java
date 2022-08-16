package tgbots.shelterbot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;


public interface MainHandler {

    SendMessage handleMessage(Message message);

    SendMessage handleMessageWithPhoto(Message message);


}
