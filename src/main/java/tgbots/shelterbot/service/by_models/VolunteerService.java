package tgbots.shelterbot.service.by_models;

import tgbots.shelterbot.models.Volunteer;

import java.util.List;

/**
 * Интерфейс, содержащий методы для взаимодействия с базой волонтеров
 */
public interface VolunteerService {

    Volunteer getVolunteer(Long id);

    Volunteer getVolunteer(String name);

    Volunteer updateVolunteer(Volunteer volunteer);

    String deleteVolunteer(Long id);

    String deleteVolunteer(String name);

    List<Volunteer> allVolunteers();
}
