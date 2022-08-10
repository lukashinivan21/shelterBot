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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tgbots.shelterbot.service.HandlerCallbacks;
import tgbots.shelterbot.service.MainHandler;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static tgbots.shelterbot.constants.StringConstants.LIST_CALLBACKS;
import static tgbots.shelterbot.constants.StringConstants.MENTION_TO_SEND_REPORT;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final MainHandler mainHandler;
    private final HandlerCallbacks handlerCallbacks;

    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(MainHandler mainHandler, HandlerCallbacks handlerCallbacks, TelegramBot telegramBot) {
        this.mainHandler = mainHandler;
        this.handlerCallbacks = handlerCallbacks;
        this.telegramBot = telegramBot;
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
                    SendDocument doc = handlerCallbacks.handleCallbackAndSendDocument(callbackQuery);
                    if (doc != null) {
                        telegramBot.execute(doc);
                    }
                } else {
                    logger.info("Answer on this callback is message");
                    SendMessage msg = handlerCallbacks.handleCallbackQueryAndSendMessage(callbackQuery);
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


    @Scheduled(cron = "0 0 12 * * *")
    public void mentionForUserToSendReport() {
        List<Long> ids = mainHandler.idsForMentionToSendReport();
        if (!ids.isEmpty()) {
            ids.forEach(id -> telegramBot.execute(new SendMessage(id, MENTION_TO_SEND_REPORT)));
        }
    }



}
