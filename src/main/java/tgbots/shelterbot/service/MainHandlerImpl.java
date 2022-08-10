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
import tgbots.shelterbot.models.*;
import tgbots.shelterbot.repository.CatCandidateRepository;
import tgbots.shelterbot.repository.DogCandidateRepository;
import tgbots.shelterbot.repository.ReportCatRepository;
import tgbots.shelterbot.repository.ReportDogRepository;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final ReportDogRepository reportDogRepository;
    private final ReportCatRepository reportCatRepository;

    private final Pattern pattern = Pattern.compile("([+0-9]{10,})(\\s*)([\\W+]+)");

    public MainHandlerImpl(TelegramBot bot, DogCandidateRepository dogCandidateRepository,
                           CatCandidateRepository catCandidateRepository,
                           ReportDogRepository reportDogRepository,
                           ReportCatRepository reportCatRepository) {
        this.bot = bot;
        this.dogCandidateRepository = dogCandidateRepository;
        this.catCandidateRepository = catCandidateRepository;
        this.reportDogRepository = reportDogRepository;
        this.reportCatRepository = reportCatRepository;
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
            LocalDate dateToday = LocalDate.now();
            LocalTime timeNow = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
            String today = dateToday.toString();
            String time = timeNow.toString();
            PhotoSize[] photo = message.photo();
            String fileId = photo[0].fileId();
            GetFile request = new GetFile(fileId);
            GetFileResponse response = bot.execute(request);
            File file = response.file();
            String path = file.filePath();
            try {
                byte[] data = bot.getFileContent(file);
                uploadReport(chatId, data, file, userName, today, time, caption, path, dateToday);
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

    private void uploadReport(Long id, byte[] data, File file, String userName, String today, String time, String caption, String path, LocalDate dateToday) throws IOException {
        logger.info("Upload report from user with id: {}, username: {}", id, userName);
        List<Long> dogIds = dogCandidateRepository.findAll().stream().map(DogCandidate::getId).toList();
        List<Long> catIds = catCandidateRepository.findAll().stream().map(CatCandidate::getId).toList();
        String secondFolder = null;
        if (dogIds.contains(id)) {
            secondFolder = "/dog_reports/";
        } else if (catIds.contains(id)) {
            secondFolder = "/cat_reports/";
        }
        Path filePath = Path.of(reportsDir + secondFolder + id + " " + userName + "/" + today, userName + " " + today + " " + time + "." + getExtension(path));
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

        if (dogIds.contains(id)) {
            DogCandidate dogCandidate = dogCandidateRepository.findDogCandidateById(id);
            DogReport newDogReport = new DogReport();
            newDogReport.setData(data);
            newDogReport.setDateReport(dateToday);
            newDogReport.setCaption(caption);
            newDogReport.setFileSize(file.fileSize());
            newDogReport.setFilePath(filePath.toString());
            newDogReport.setDogCandidate(dogCandidate);

            reportDogRepository.save(newDogReport);

        } else if (catIds.contains(id)) {
            CatCandidate catCandidate = catCandidateRepository.findCatCandidateById(id);
            CatReport newCatReport = new CatReport();
            newCatReport.setCatCandidate(catCandidate);
            newCatReport.setDateReport(dateToday);
            newCatReport.setCaption(caption);
            newCatReport.setData(data);
            newCatReport.setFilePath(filePath.toString());
            newCatReport.setFileSize(file.fileSize());

            reportCatRepository.save(newCatReport);
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public List<Long> idsForMentionToSendReport() {
        LocalDate rightNow = LocalDate.now();
        List<Long> resultIds = new ArrayList<>();

        List<Long> dogIds = dogCandidateRepository.findAll()
                .stream()
                .map(DogCandidate::getId)
                .filter(id -> !reportDogRepository.findDogReportByDogCandidate_Id(id).isEmpty()).toList();

        List<Long> catIds = catCandidateRepository.findAll()
                .stream()
                .map(CatCandidate::getId)
                .filter(id -> !reportCatRepository.findCatReportByCatCandidate_Id(id).isEmpty()).toList();

        if (!dogIds.isEmpty()) {
            for (Long id : dogIds) {
                List<DogReport> dogReports = reportDogRepository.findDogReportByDogCandidate_Id(id).stream().sorted(Comparator.comparing(DogReport::getDateReport)).toList();
                LocalDate date = dogReports.get(dogReports.size() - 1).getDateReport();
                Period period = Period.between(rightNow, date);
                if (Math.abs(period.getDays()) > 1) {
                    resultIds.add(id);
                }
            }
        }

        if (!catIds.isEmpty()) {
            for (Long id : catIds) {
                List<CatReport> catReports = reportCatRepository.findCatReportByCatCandidate_Id(id).stream().sorted(Comparator.comparing(CatReport::getDateReport)).toList();
                LocalDate date = catReports.get(catReports.size() - 1).getDateReport();
                Period period = Period.between(rightNow, date);
                if (Math.abs(period.getDays()) > 1) {
                    resultIds.add(id);
                }
            }
        }

        return resultIds;
    }


}
