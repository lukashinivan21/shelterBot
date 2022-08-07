package tgbots.shelterbot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;

public interface MainHandler {

    SendMessage handleMessage(Message message);

    SendMessage handleCallbackQuery(CallbackQuery callbackQuery);

    SendDocument handleCallbackAndSendDocument(CallbackQuery callbackQuery);

    SendMessage handleMessageWithPhoto(Message message);
}
