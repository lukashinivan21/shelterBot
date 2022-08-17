package tgbots.shelterbot.service;

/**
 * Интерфейс, содержащий метод для начала общения между волонтером и пользователем
 * при проведении консультации
 */
public interface DialogBetweenUserAndVolunteer {

    void firstMessage(Long id, String userName);
}
