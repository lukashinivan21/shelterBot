package tgbots.shelterbot.service;

import com.pengrad.telegrambot.request.SendMessage;

/**
 * Интерфейс, содержащий метод для обработки сообщений и команд от волонтеров
 */
public interface HandlerMessageFromVolunteer {

    SendMessage messageToOther(Long chatId, String text, String userName);
}
