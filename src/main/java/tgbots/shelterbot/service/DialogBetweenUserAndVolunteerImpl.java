package tgbots.shelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import tgbots.shelterbot.constants.StringConstants;
import tgbots.shelterbot.models.*;
import tgbots.shelterbot.repository.CatCandidateRepository;
import tgbots.shelterbot.repository.DogCandidateRepository;
import tgbots.shelterbot.repository.VolunteerRepository;

import java.util.List;

import static tgbots.shelterbot.constants.Emoji.WINK;

@Service
public class DialogBetweenUserAndVolunteerImpl implements DialogBetweenUserAndVolunteer{

    private final TelegramBot telegramBot;
    private final VolunteerRepository volunteerRepository;
    private final DogCandidateRepository dogCandidateRepository;
    private final CatCandidateRepository catCandidateRepository;

    public DialogBetweenUserAndVolunteerImpl(TelegramBot telegramBot, VolunteerRepository volunteerRepository,
                                             DogCandidateRepository dogCandidateRepository,
                                             CatCandidateRepository catCandidateRepository) {
        this.telegramBot = telegramBot;
        this.volunteerRepository = volunteerRepository;
        this.dogCandidateRepository = dogCandidateRepository;
        this.catCandidateRepository = catCandidateRepository;
    }


    @Override
    public void firstMessage(Message message) {

        Long userId = message.chat().id();
        String userName = message.from().username();

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
            if (dogIds.contains(userId)) {
                DogCandidate dogCandidate = dogCandidateRepository.findDogCandidateById(userId);
                dogCandidate.setBotState(BotState.DIALOG_WITH_VOL.toString());
                dogCandidateRepository.save(dogCandidate);
            } else if (catIds.contains(userId)) {
                CatCandidate catCandidate = catCandidateRepository.findCatCandidateById(userId);
                catCandidate.setBotState(BotState.DIALOG_WITH_VOL.toString());
                catCandidateRepository.save(catCandidate);
            }
            telegramBot.execute(new SendMessage(userId, "Волонтер свяжется c вами в ближайшее время..."));
            telegramBot.execute(new SendMessage(volunteerId, "Приветствую, " + nameVol + "!\n" + StringConstants.msgToVol(userName) + " " + WINK));
            first.setFree(false);
            volunteerRepository.save(first);
        } else {
            telegramBot.execute(new SendMessage(userId, "К сожалению  в данный момент все волонтеры заняты. Попробуйте позже..."));
        }


    }
}
