package tgbots.shelterbot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import tgbots.shelterbot.constants.Keyboards;
import tgbots.shelterbot.models.BotState;
import tgbots.shelterbot.models.CatCandidate;
import tgbots.shelterbot.models.DogCandidate;
import tgbots.shelterbot.repository.CatCandidateRepository;
import tgbots.shelterbot.repository.DogCandidateRepository;

import java.io.File;
import java.util.List;

import static tgbots.shelterbot.constants.FilePath.*;
import static tgbots.shelterbot.constants.StringConstants.*;

@Service
public class HandlerCallbacksImpl implements HandlerCallbacks{

    private final DogCandidateRepository dogCandidateRepository;
    private final CatCandidateRepository catCandidateRepository;

    public HandlerCallbacksImpl(DogCandidateRepository dogCandidateRepository, CatCandidateRepository catCandidateRepository) {
        this.dogCandidateRepository = dogCandidateRepository;
        this.catCandidateRepository = catCandidateRepository;
    }


    @Override
    public SendMessage handleCallbackQueryAndSendMessage(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.from().id();
        String data = callbackQuery.data();

        String text = null;
        ReplyKeyboardMarkup keyboardMarkup = null;

        List<Long> dogsId = dogCandidateRepository.findAll().stream().map(DogCandidate::getId).toList();
        List<Long> catsId = catCandidateRepository.findAll().stream().map(CatCandidate::getId).toList();

        switch (data) {
            case CALLBACK_DOG -> {
                if (catsId.contains(chatId)) {
                    text = MESS_FOR_CAT_OWNER;
                } else {
                    text = MESS_DOG + SECOND_GREETING;
                    keyboardMarkup = Keyboards.mainKeyboard();
                    DogCandidate dogCandidate = new DogCandidate();
                    dogCandidate.setId(chatId);
                    dogCandidate.setBotState(BotState.DIALOG.toString());
                    dogCandidateRepository.save(dogCandidate);
                }
            }
            case CALLBACK_CAT -> {
                if (dogsId.contains(chatId)) {
                    text = MESS_FOR_DOG_OWNER;
                } else {
                    text = MESS_CAT + SECOND_GREETING;
                    keyboardMarkup = Keyboards.mainKeyboard();
                    CatCandidate catCandidate = new CatCandidate();
                    catCandidate.setId(chatId);
                    catCandidate.setBotState(BotState.DIALOG.toString());
                    catCandidateRepository.save(catCandidate);
                }
            }
            case CALLBACK_BUTTON8 -> {
                text = MESS_FOR_BUTTON8;
                if (dogsId.contains(chatId)) {
                    changeDogCandidateBotState(chatId, BotState.GET_INFO.toString());
                } else if (catsId.contains(chatId)) {
                    changeCatCandidateBotState(chatId, BotState.GET_INFO.toString());
                }
            }
            case CALLBACK_BUTTON4 -> text = MESS_FOR_BUTTON4;
        }

        SendMessage message = new SendMessage(chatId, text);
        if (keyboardMarkup != null) {
            message.replyMarkup(keyboardMarkup);
        }
        return message;
    }


    @Override
    public SendDocument handleCallbackAndSendDocument(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.from().id();
        String data = callbackQuery.data();
        String filePath = DEFAULT_PATH;
        String caption = DEFAULT_CAPTION;
        switch (data) {
            case CALLBACK_BUTTON5 -> {
                filePath = FILE_PATH5;
                caption = MESS_FOR_BUTTON5;
            }
            case CALLBACK_BUTTON6 -> {
                filePath = FILE_PATH6;
                caption = MESS_FOR_BUTTON6;
            }
            case CALLBACK_BUTTON7 -> {
                filePath = FILE_PATH7;
                caption = MESS_FOR_BUTTON7;
            }
            case CALLBACK_BUTTON9 -> {
                filePath = FILE_PATH9;
                caption = MESS_FOR_BUTTON9;
            }
            case CALLBACK_BUTTON10 -> {
                filePath = FILE_PATH10;
                caption = MESS_FOR_BUTTON10;
            }
            case CALLBACK_BUTTON11 -> {
                filePath = FILE_PATH11;
                caption = MESS_FOR_BUTTON11;
            }
            case CALLBACK_BUTTON12 -> {
                filePath = FILE_PATH12;
                caption = MESS_FOR_BUTTON12;
            }
            case CALLBACK_BUTTON13 -> {
                filePath = FILE_PATH13;
                caption = MESS_FOR_BUTTON13;
            }
            case CALLBACK_BUTTON14 -> {
                filePath = FILE_PATH14;
                caption = MESS_FOR_BUTTON14;
            }
            case CALLBACK_BUTTON15 -> {
                filePath = FILE_PATH15;
                caption = MESS_FOR_BUTTON15;
            }
            case CALLBACK_BUTTON16 -> {
                filePath = FILE_PATH16;
                caption = MESS_FOR_BUTTON16;
            }
            case CALLBACK_BUTTON17 -> {
                filePath = FILE_PATH17;
                caption = MESS_FOR_BUTTON17;
            }
            case CALLBACK_BUTTON18 -> {
                filePath = FILE_PATH18;
                caption = MESS_FOR_BUTTON18;
            }
            case CALLBACK_BUTTON19 -> {
                filePath = FILE_PATH19;
                caption = MESS_FOR_BUTTON19;
            }
        }

        File file = new File(filePath);

        return new SendDocument(chatId, file).caption(caption);
    }


    private void changeDogCandidateBotState(Long chatId, String botState) {
        DogCandidate dogCandidate = dogCandidateRepository.findDogCandidateById(chatId);
        dogCandidate.setBotState(botState);
        dogCandidateRepository.save(dogCandidate);
    }

    private void changeCatCandidateBotState(Long chatId, String botState) {
        CatCandidate catCandidate = catCandidateRepository.findCatCandidateById(chatId);
        catCandidate.setBotState(botState);
        catCandidateRepository.save(catCandidate);
    }
}
