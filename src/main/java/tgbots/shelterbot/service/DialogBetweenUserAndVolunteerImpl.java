package tgbots.shelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import tgbots.shelterbot.constants.StringConstants;
import tgbots.shelterbot.models.*;
import tgbots.shelterbot.repository.CatCandidateRepository;
import tgbots.shelterbot.repository.DogCandidateRepository;
import tgbots.shelterbot.repository.VolunteerRepository;
import tgbots.shelterbot.service.storeage.RepositoryIds;

import java.util.List;

import static tgbots.shelterbot.constants.Emoji.WINK;

@Service
public class DialogBetweenUserAndVolunteerImpl implements DialogBetweenUserAndVolunteer {

    private final TelegramBot telegramBot;
    private final VolunteerRepository volunteerRepository;
    private final DogCandidateRepository dogCandidateRepository;
    private final CatCandidateRepository catCandidateRepository;
    private final RepositoryIds repositoryIds;

    public DialogBetweenUserAndVolunteerImpl(TelegramBot telegramBot, VolunteerRepository volunteerRepository,
                                             DogCandidateRepository dogCandidateRepository,
                                             CatCandidateRepository catCandidateRepository,
                                             RepositoryIds repositoryIds) {
        this.telegramBot = telegramBot;
        this.volunteerRepository = volunteerRepository;
        this.dogCandidateRepository = dogCandidateRepository;
        this.catCandidateRepository = catCandidateRepository;
        this.repositoryIds = repositoryIds;
    }


    @Override
    public void firstMessage(Long id, String userName) {

        List<Volunteer> volunteers = volunteerRepository.findVolunteersByFree(true);
        List<Long> dogIds = dogCandidateRepository.findAll().stream().map(DogCandidate::getId).toList();
        List<Long> catIds = catCandidateRepository.findAll().stream().map(Candidate::getId).toList();

        if (!volunteers.isEmpty()) {
            Volunteer first = volunteers.get(0);
            Long volunteerId = first.getId();
            String nameVol = "Волонтер";
            if (first.getName() != null) {
                nameVol = first.getName();
            }
            changeBotState(id, dogIds, catIds);
            telegramBot.execute(new SendMessage(id, "Волонтер свяжется c вами в ближайшее время... \nПосле завершения общения с волонтером" +
                    " прошу вас отправить мне команду /start"));
            telegramBot.execute(new SendMessage(volunteerId, "Приветствую, " + nameVol + "!\n" + StringConstants.msgToVol(userName) + " " + WINK));
            first.setFree(false);
            volunteerRepository.save(first);
            repositoryIds.addIdsInFirstMap(id, volunteerId);
            repositoryIds.addIdsInSecondMap(volunteerId, id);
        } else {
            telegramBot.execute(new SendMessage(id, "К сожалению  в данный момент все волонтеры заняты. Попробуйте позже..."));
        }

    }

    private void changeBotState(Long userId, List<Long> dogIds, List<Long> catIds) {
        if (dogIds.contains(userId)) {
            DogCandidate dogCandidate = dogCandidateRepository.findDogCandidateById(userId);
            dogCandidate.setBotState(BotState.DIALOG_WITH_VOL.toString());
            dogCandidateRepository.save(dogCandidate);
        } else if (catIds.contains(userId)) {
            CatCandidate catCandidate = catCandidateRepository.findCatCandidateById(userId);
            catCandidate.setBotState(BotState.DIALOG_WITH_VOL.toString());
            catCandidateRepository.save(catCandidate);
        }
    }


}
