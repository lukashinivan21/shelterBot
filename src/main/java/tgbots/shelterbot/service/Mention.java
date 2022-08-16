package tgbots.shelterbot.service;

import java.util.List;

public interface Mention {

    List<Long> idsForMentionToSendReport();

    String mentionForVolunteer();

    String mentionForVolunteerAboutTestPeriod();
}
