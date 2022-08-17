package tgbots.shelterbot.service;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import tgbots.shelterbot.constants.StringConstants;
import tgbots.shelterbot.models.Volunteer;
import tgbots.shelterbot.repository.VolunteerRepository;
import tgbots.shelterbot.service.storeage.RepositoryIds;
import tgbots.shelterbot.service.storeage.RepositoryIdsUsersOnTestPeriod;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tgbots.shelterbot.constants.Emoji.*;
import static tgbots.shelterbot.constants.StringConstants.*;

@Service
public class HandlerMessageFromVolunteerImpl implements HandlerMessageFromVolunteer {

//  Шаблоны возможных сообщений и команд от волонтеров
    private final Pattern patternForVolunteer = Pattern.compile("([a-zA-Z\\s*]+|[а-яёА-ЯЁ\\s*]+)");
    private final Pattern msgFromVolTestPeriodSuccess = Pattern.compile("([0-9]{6,})(\\s*)(SUCCESS)");
    private final Pattern msgFromVolTestPeriodLose = Pattern.compile("([0-9]{6,})(\\s*)(LOSE)");
    private final Pattern msgFromVolTestPeriodLong = Pattern.compile("([0-9]{6,})(\\s*)(LONG)(\\s*)(14|30)");
    private final Pattern msgFromVolNotice = Pattern.compile("([0-9]{6,})(\\s*)(NOTICE)");

    private final VolunteerRepository volunteerRepository;
    private final RepositoryIds repositoryIds;
    private final RepositoryIdsUsersOnTestPeriod idsTestPeriod;

    public HandlerMessageFromVolunteerImpl(VolunteerRepository volunteerRepository, RepositoryIds repositoryIds, RepositoryIdsUsersOnTestPeriod idsTestPeriod) {
        this.volunteerRepository = volunteerRepository;
        this.repositoryIds = repositoryIds;
        this.idsTestPeriod = idsTestPeriod;
    }

    /**
     * Метод для обработки сообщений и команд от волонтеров и формирования необходимого ответа в виде сообщения
     */
    @Override
    public SendMessage messageToOther(Long chatId, String text, String userName) {

        List<Long> volunteerIds = volunteerRepository.findAll().stream().map(Volunteer::getId).toList();
        Map<Long, Long> second = repositoryIds.secondMap();

        SendMessage sendMessage = null;

        if (text.equals(VOLUNTEER)) {
            Volunteer newVolunteer = new Volunteer();
            newVolunteer.setId(chatId);
            newVolunteer.setUserName(userName);
            newVolunteer.setFree(true);
            volunteerRepository.save(newVolunteer);
            sendMessage = collectSendMessage(chatId, SUCCESS_ADD_VOLUNTEER + " " + SMILE);
        }

        Matcher matcher1 = patternForVolunteer.matcher(text);
        Matcher matcherSuccess = msgFromVolTestPeriodSuccess.matcher(text);
        Matcher matcherLose = msgFromVolTestPeriodLose.matcher(text);
        Matcher matcherLong = msgFromVolTestPeriodLong.matcher(text);
        Matcher matcherNotice = msgFromVolNotice.matcher(text);

        if (volunteerIds.contains(chatId)) {
            Volunteer getVol = volunteerRepository.findVolunteerById(chatId);

            if (getVol.isFree() && matcher1.matches()) {
                String name = matcher1.group(1);
                getVol.setName(name);
                volunteerRepository.save(getVol);
                sendMessage = collectSendMessage(chatId, CONGRATULATION_VOL + " " + WINK);
            }

            if (getVol.isFree() && matcherSuccess.matches()) {
                String id = matcherSuccess.group(1);
                Long idL = Long.parseLong(id);
                if (idsTestPeriod.containsId(idL)) {
                    sendMessage = collectSendMessage(idL, PARTY + " " + CON_USER + " " + PARTY);
                    idsTestPeriod.removeId(idL);
                } else if (!idsTestPeriod.containsId(idL)) {
                    sendMessage = collectSendMessage(chatId, "Проверьте введенный id чата");
                }
            }

            if (getVol.isFree() && matcherLose.matches()) {
                String id = matcherLose.group(1);
                Long idL = Long.parseLong(id);
                if (idsTestPeriod.containsId(idL)) {
                    sendMessage = collectSendMessage(idL, PENSIVE + LOSE + PENSIVE);
                    idsTestPeriod.removeId(idL);
                } else if (!idsTestPeriod.containsId(idL)) {
                    sendMessage = collectSendMessage(chatId, "Проверьте введенный id чата");
                }
            }

            if (getVol.isFree() && matcherLong.matches()) {
                String id = matcherLong.group(1);
                String days = matcherLong.group(5);
                Long idL = Long.parseLong(id);
                if (idsTestPeriod.containsId(idL)) {
                    sendMessage = collectSendMessage(idL, StringConstants.periodProLong(days));
                } else if (!idsTestPeriod.containsId(idL)) {
                    sendMessage = collectSendMessage(chatId, "Проверьте введенный id чата");
                }
            }

            if (getVol.isFree() && matcherNotice.matches()) {
                String id = matcherNotice.group(1);
                Long idL = Long.parseLong(id);
                if (idsTestPeriod.containsId(idL)) {
                    sendMessage = collectSendMessage(idL, NOTICE);
                } else if (!idsTestPeriod.containsId(idL)) {
                    sendMessage = collectSendMessage(chatId, "Проверьте введенный id чата");
                }
            }

            if (getVol.isFree() && !matcher1.matches() && !matcherSuccess.matches() && !matcherLose.matches() && !matcherLong.matches()) {
                sendMessage = collectSendMessage(chatId, """
                        Проверьте правильность введенных данных:
                        Вводимое имя должно содержать только буквы;
                        Будьте внимательнее при вводе команд.""");
            }


            if (!getVol.isFree() && text.equals(FINISH)) {
                getVol.setFree(true);
                volunteerRepository.save(getVol);
                repositoryIds.deleteFromSecondMap(chatId);
                sendMessage = collectSendMessage(chatId, VOL_IS_FREE + " " + COFFEE);
            } else if (!getVol.isFree() && !text.equals(FINISH) && second.containsKey(chatId)) {
                Long userId = second.get(chatId);
                if (userId != null) {
                    sendMessage = collectSendMessage(userId, text);
                }
            }
        }

        return sendMessage;
    }

//  Метод для формирования ответного сообщения
    private SendMessage collectSendMessage(Long chatId, String textAnswer) {
        return new SendMessage(chatId, textAnswer);
    }
}
