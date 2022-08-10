package tgbots.shelterbot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;

public interface MainHandler {

    SendMessage handleMessage(Message message);

    SendMessage handleMessageWithPhoto(Message message);

    List<Long> idsForMentionToSendReport();
}
