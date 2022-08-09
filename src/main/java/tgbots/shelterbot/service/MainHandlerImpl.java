package tgbots.shelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
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
            File file = response.file();
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

    private void uploadReport(Long id, String fileId, byte[] data, File file, String userName, String today, String caption, String path) throws IOException {
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
