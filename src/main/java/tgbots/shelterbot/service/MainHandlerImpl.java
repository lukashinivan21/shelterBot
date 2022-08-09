package tgbots.shelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tgbots.shelterbot.constants.Keyboards;
import tgbots.shelterbot.models.BotState;
import tgbots.shelterbot.models.Candidate;
import tgbots.shelterbot.models.CatCandidate;
import tgbots.shelterbot.models.DogCandidate;
import tgbots.shelterbot.repository.CatCandidateRepository;
import tgbots.shelterbot.repository.DogCandidateRepository;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static tgbots.shelterbot.constants.FilePath.*;
import static tgbots.shelterbot.constants.StringConstants.*;

@Service
public class MainHandlerImpl implements MainHandler {

    private final Logger logger = LoggerFactory.getLogger(MainHandlerImpl.class);

    private final TelegramBot bot;

    private final DogCandidateRepository dogCandidateRepository;
    private final CatCandidateRepository catCandidateRepository;

    private final Pattern pattern = Pattern.compile("([+0-9]{10,})(\\s*)([\\W+]+)");

    public MainHandlerImpl(TelegramBot bot, DogCandidateRepository dogCandidateRepository, CatCandidateRepository catCandidateRepository) {
        this.bot = bot;
        this.dogCandidateRepository = dogCandidateRepository;
        this.catCandidateRepository = catCandidateRepository;
    }


    @Override
    public SendMessage handleMessage(Message message) {
        Long chatId = message.chat().id();
        String text = message.text();
        String userName = message.from().username();

        SendMessage sendMessage = null;


        List<Long> dogIds = dogCandidateRepository.findAll().stream().map(DogCandidate::getId).toList();
        List<Long> catIds = catCandidateRepository.findAll().stream().map(CatCandidate::getId).toList();


        if (text != null) {

            if (dogIds.contains(chatId) || catIds.contains(chatId)) {
                Candidate candidate = personFromDogOrCatRepository(chatId);
                if (candidate.getBotState().equals(BotState.GET_REPORT.toString())) {
                    sendMessage = collectSendMessage(chatId, REPORT_WITHOUT_PHOTO);
                }
            }

            if (text.equals(START) && (dogIds.contains(chatId) || catIds.contains(chatId))) {
                sendMessage = collectSendMessage(chatId, NO_FIRST_VISIT, Keyboards.mainKeyboard());
                if (dogIds.contains(chatId)) {
                    changeDogCandidateBotState(chatId, BotState.DIALOG.toString());
                } else if (catIds.contains(chatId)) {
                    changeCatCandidateBotState(chatId, BotState.DIALOG.toString());
                }
            } else if (text.equals(START)) {
                sendMessage = collectSendMessage(chatId, MAIN_GREETING, Keyboards.chooseShelter());
            }

            if (!text.equals(START) && (dogIds.contains(chatId) || catIds.contains(chatId))) {

                Candidate candidate = personFromDogOrCatRepository(chatId);

                if (candidate.getBotState().equals(BotState.DIALOG.toString())) {
                    switch (text) {
                        case TEXT_BUTTON1 -> sendMessage = collectSendMessage(chatId, GREETING_STEP1, Keyboards.keyboard1());
                        case TEXT_BUTTON2 -> {
                            if (dogIds.contains(chatId)) {
                                sendMessage = collectSendMessage(chatId, GREETING_STEP2, Keyboards.keyboard2Dog());
                            } else if (catIds.contains(chatId)) {
                                sendMessage = collectSendMessage(chatId, GREETING_STEP2, Keyboards.keyboard2Cat());
                            }
                        }
                        case TEXT_BUTTON3 -> {
                            sendMessage = collectSendMessage(chatId, MESS_FOR_BUTTON3, Keyboards.keyboard3());
                            candidate.setBotState(BotState.GET_REPORT.toString());
                            if (dogIds.contains(chatId)) {
                                dogCandidateRepository.save((DogCandidate) candidate);
                            } else if (catIds.contains(chatId)) {
                                catCandidateRepository.save((CatCandidate) candidate);
                            }
                        }
                        case TEXT_BUTTON4 -> sendMessage = collectSendMessage(chatId, MESS_FOR_BUTTON4);
                        default -> sendMessage = collectSendMessage(chatId, MESS_DEFAULT, Keyboards.mainKeyboard());
                    }
                }
            }

            if (dogIds.contains(chatId) || catIds.contains(chatId)) {
                Candidate candidate = personFromDogOrCatRepository(chatId);

                Matcher matcher = pattern.matcher(text);
                if (candidate.getBotState().equals(BotState.GET_INFO.toString()) && matcher.matches()) {

                    String phoneNumber = matcher.group(1);
                    String name = matcher.group(3);
                    candidate.setName(name);
                    candidate.setPhoneNumber(phoneNumber);
                    candidate.setUserName(userName);
                    candidate.setBotState(BotState.DIALOG.toString());

                    if (dogIds.contains(chatId)) {
                        dogCandidateRepository.save((DogCandidate) candidate);
                    } else if (catIds.contains(chatId)) {
                        catCandidateRepository.save((CatCandidate) candidate);
                    }
                    sendMessage = collectSendMessage(chatId, SUCCESS_ADD);
                } else if (candidate.getBotState().equals(BotState.GET_INFO.toString()) && !matcher.matches()) {
                    sendMessage = collectSendMessage(chatId, CHECK_MESS);
                }
            }
        }
        return sendMessage;
    }

    private SendMessage collectSendMessage(Long chatId, String textAnswer, ReplyKeyboardMarkup keyboardMarkup) {
        return new SendMessage(chatId, textAnswer).replyMarkup(keyboardMarkup);
    }

    private SendMessage collectSendMessage(Long chatId, String textAnswer, InlineKeyboardMarkup keyboardMarkup) {
        return new SendMessage(chatId, textAnswer).replyMarkup(keyboardMarkup);
    }

    private SendMessage collectSendMessage(Long chatId, String textAnswer) {
        return new SendMessage(chatId, textAnswer);
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

    private Candidate personFromDogOrCatRepository(Long chatId) {
        Candidate person = new Candidate();
        List<Long> dogIds = dogCandidateRepository.findAll().stream().map(DogCandidate::getId).toList();
        List<Long> catIds = catCandidateRepository.findAll().stream().map(CatCandidate::getId).toList();
        if (dogIds.contains(chatId)) {
            person = dogCandidateRepository.findDogCandidateById(chatId);
        } else if (catIds.contains(chatId)) {
            person = catCandidateRepository.findCatCandidateById(chatId);
        }
        return person;
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

    @Override
    public SendMessage handleMessageWithPhoto(Message message) {

        Long chatId = message.chat().id();

        String userName = message.from().username();

        Candidate candidate = personFromDogOrCatRepository(chatId);

        SendMessage sendMessage = null;

        if (candidate.getBotState().equals(BotState.GET_REPORT.toString()) && message.caption() != null) {
            String caption = message.caption();
            String today = LocalDate.now().toString();
            PhotoSize[] photo = message.photo();
            String fileId = photo[0].fileId();
            GetFile request = new GetFile(fileId);
            GetFileResponse response = bot.execute(request);
            com.pengrad.telegrambot.model.File file = response.file();
            String path = file.filePath();
            try {
                byte[] data = bot.getFileContent(file);
                uploadReport(chatId, fileId, data, file, userName, today, caption, path);
                sendMessage = collectSendMessage(chatId, REPORT_OK);
            } catch (IOException e) {
                logger.info("Something happens...");
                e.printStackTrace();
                sendMessage = collectSendMessage(chatId, ERROR);
            }
        } else if (candidate.getBotState().equals(BotState.GET_REPORT.toString())) {
            sendMessage = collectSendMessage(chatId, REPORT_NOT_FULL);
        }

        return sendMessage;
    }

    @Value("${path.to.reports.folder}")
    private String reportsDir;

    @Value("${telegram.bot.token}")
    private String token;

    private void uploadReport(Long id, String fileId, byte[] data, com.pengrad.telegrambot.model.File file, String userName, String today, String caption, String path) throws IOException {
        logger.info("Upload report from user with id: {}, username: {}", id, userName);
        List<Long> dogIds = dogCandidateRepository.findAll().stream().map(DogCandidate::getId).toList();
        List<Long> catIds = catCandidateRepository.findAll().stream().map(CatCandidate::getId).toList();
        String secondFolder = null;
        if (dogIds.contains(id)) {
            secondFolder = "/dog_reports/";
        } else if (catIds.contains(id)) {
            secondFolder = "/cat_reports/";
        }
        Path filePath = Path.of(reportsDir + secondFolder + id + " " + userName + "/" + today, userName + " " + today + "." + getExtension(path));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        URL url = new URL("https://api.telegram.org/file/bot" + token + "/" + path);

        try (InputStream is = url.openStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }


    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


}
