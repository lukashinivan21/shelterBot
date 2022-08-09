package tgbots.shelterbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgbots.shelterbot.service.MainHandler;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static tgbots.shelterbot.constants.StringConstants.LIST_CALLBACKS;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final MainHandler mainHandler;


    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(MainHandler mainHandler) {
        this.mainHandler = mainHandler;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        list.forEach(update -> {
            Message message = update.message();
            CallbackQuery callbackQuery = update.callbackQuery();

            if (message != null && message.photo() == null) {
                logger.info("New message from user: {}, userId: {}, chatId: {}, with text: {}", message.from().username(), message.from().id(),
                        message.chat().id(), message.text());
                SendMessage sendMessage = mainHandler.handleMessage(message);
                if (sendMessage != null) {
                    telegramBot.execute(sendMessage);
                }
            }

            if (callbackQuery != null) {
                logger.info("New callbackQuery from user: {}, userId: {}, with data: {}", callbackQuery.from().username(),
                        callbackQuery.from().id(), callbackQuery.data());
                String data = callbackQuery.data();

                if (LIST_CALLBACKS.contains(data)) {
                    logger.info("Answer on this callback is document with caption");
                    SendDocument doc = mainHandler.handleCallbackAndSendDocument(callbackQuery);
                    if (doc != null) {
                        telegramBot.execute(doc);
                    }
                } else {
                    logger.info("Answer on this callback is message");
                    SendMessage msg = mainHandler.handleCallbackQueryAndSendMessage(callbackQuery);
                    if (msg != null) {
                        telegramBot.execute(msg);
                    }
                }
            }


            if (message != null && message.photo() != null) {
                logger.info("New message with photo from user: {}, userId: {}, chatId: {}, with photo: {} with caption: {}",
                        message.from().username(), message.from().id(), message.chat().id(), Arrays.toString(message.photo()), message.caption());
                SendMessage mess = mainHandler.handleMessageWithPhoto(message);
                if (mess != null) {
                    telegramBot.execute(mess);
                }
            }
        });




        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
