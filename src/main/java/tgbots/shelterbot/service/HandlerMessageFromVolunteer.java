package tgbots.shelterbot.service;

import com.pengrad.telegrambot.request.SendMessage;

public interface HandlerMessageFromVolunteer {

    SendMessage messageToOther(Long chatId, String text, String userName);
}
