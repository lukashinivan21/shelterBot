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
import tgbots.shelterbot.repository.VolunteerRepository;
import tgbots.shelterbot.service.HandlerCallbacks;
import tgbots.shelterbot.service.MainHandler;
import tgbots.shelterbot.service.Mention;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static tgbots.shelterbot.constants.StringConstants.LIST_CALLBACKS;
import static tgbots.shelterbot.constants.StringConstants.MENTION_TO_SEND_REPORT;

/**
 * Класс, отвечающий за прием входящих обновление и на основании этого
 * передающий эти обновление на обработку в нужный обработчик.
 * После получения ответа от обработчика происходит отправка ответа.
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final MainHandler mainHandler;
    private final HandlerCallbacks handlerCallbacks;
    private final Mention mention;
    private final VolunteerRepository volunteerRepository;

    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(MainHandler mainHandler, HandlerCallbacks handlerCallbacks, Mention mention, VolunteerRepository volunteerRepository, TelegramBot telegramBot) {
        this.mainHandler = mainHandler;
        this.handlerCallbacks = handlerCallbacks;
        this.mention = mention;
        this.volunteerRepository = volunteerRepository;
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

    /**
     * Метод, выполняющий рассылку напоминаний пользователям, если они
     * не присылают отчеты в соответствии с требованиями, находясь на испытательном сроке.
     */
    @Scheduled(cron = "0 0 12 * * *")
    public void mentionForUserToSendReport() {
        List<Long> ids = mention.idsForMentionToSendReport();
        if (!ids.isEmpty()) {
            ids.forEach(id -> telegramBot.execute(new SendMessage(id, MENTION_TO_SEND_REPORT)));
        }
    }

    /**
     * Метод, отправляющий волонтеру список злостных нарушителей правил отправки отчетов,
     * т.е. не присылающих отчет более двух дней.
     * Волонтер выбтрается случайным образом из списка волонтеров.
     */
    @Scheduled(cron = "0 30 10 * * *")
    public void mentionToVolunteer1() {
        String result = mention.mentionForVolunteer();
        int size = volunteerRepository.findAll().size();
        if (size > 0 && !result.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(0, size);
            Long volunteerId = volunteerRepository.findAll().get(index).getId();
            telegramBot.execute(new SendMessage(volunteerId, result));
        }
    }

    /**
     * Метод, отправляющий волонтеру список тех пользователей, испытательный срок которых подошел к концу,
     * и по которым необходимо принять решение: пройден ли испытатльный срок или нет.
     * Волонтер выбтрается случайным образом из списка волонтеров.
     */
    @Scheduled(cron = "0 15 11 * * * ")
    public void mentionToVolunteerAboutTestPeriod() {
        String result = mention.mentionForVolunteerAboutTestPeriod();
        int size = volunteerRepository.findAll().size();
        if (size > 0 && !result.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(0, size);
            Long volunteerId = volunteerRepository.findAll().get(index).getId();
            telegramBot.execute(new SendMessage(volunteerId, result));
        }
    }



}
