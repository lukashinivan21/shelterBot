package tgbots.shelterbot.service.bymodels;

import tgbots.shelterbot.models.Volunteer;

import java.util.List;

public interface VolunteerService {

    Volunteer getVolunteer(Long id);

    Volunteer getVolunteer(String name);

    Volunteer updateVolunteer(Volunteer volunteer);

    String deleteVolunteer(Long id);

    String deleteVolunteer(String name);

    List<Volunteer> allVolunteers();
}
