package tgbots.shelterbot.service;

import java.util.List;

/**
 * Интерфейс, содержащий методы для формирования напоминаний
 */
public interface Mention {

    List<Long> idsForMentionToSendReport();

    String mentionForVolunteer();

    String mentionForVolunteerAboutTestPeriod();
}
