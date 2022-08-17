package tgbots.shelterbot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;

/**
 * Интерфейс, содержащий методы для обработки сигналов от кнопок клавиатур
 */
public interface HandlerCallbacks {

    SendMessage handleCallbackQueryAndSendMessage(CallbackQuery callbackQuery);

    SendDocument handleCallbackAndSendDocument(CallbackQuery callbackQuery);
}
