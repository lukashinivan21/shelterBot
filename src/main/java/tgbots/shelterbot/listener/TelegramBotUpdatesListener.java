package tgbots.shelterbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);


    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        list.forEach(update -> {
            Message message = update.message();
            CallbackQuery callbackQuery = update.callbackQuery();

            if (message != null) {
                logger.info("New message from user: {}, userId: {}, chatId: {}, with text: {}", message.from().username(), message.from().id(),
                        message.chat().id(), message.text());
                Long chatId = message.chat().id();
                telegramBot.execute(new SendMessage(chatId, "Есть контакт!!!"));
            }
        });




        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
