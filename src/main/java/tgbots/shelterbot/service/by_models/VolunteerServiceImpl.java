package tgbots.shelterbot.service.by_models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tgbots.shelterbot.models.Volunteer;
import tgbots.shelterbot.repository.VolunteerRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис, отвечающий за взаимодействие с базой волонтеров
 */
@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final Logger logger = LoggerFactory.getLogger(VolunteerServiceImpl.class);

    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public Volunteer getVolunteer(Long id) {
        Volunteer volunteer = volunteerRepository.findById(id).orElse(null);
        if (volunteer == null) {
            logger.info("Volunteer with id {} doesn't exist", id);
        }
        return volunteer;
    }

    @Override
    public Volunteer getVolunteer(String name) {
        Volunteer volunteer = volunteerRepository.findVolunteerByName(name);
        if (volunteer == null) {
            logger.info("Volunteer with name {} doesn't exist", name);
        }
        return volunteer;
    }

    @Override
    public Volunteer updateVolunteer(Volunteer volunteer) {
        logger.info("Was requested method for update volunteer");
        Optional<Volunteer> volunteerOptional = volunteerRepository.findById(volunteer.getId());
        if (volunteerOptional.isPresent()) {
            Volunteer result = volunteerOptional.get();
            result.setName(volunteer.getName());
            result.setUserName(volunteer.getUserName());
            return volunteerRepository.save(result);
        } else {
            return null;
        }
    }

    @Override
    public String deleteVolunteer(Long id) {
        logger.info("Deleting volunteer with id " + id);
        List<Long> ids = volunteerRepository.findAll().stream().map(Volunteer::getId).toList();
        if (ids.contains(id)) {
            volunteerRepository.deleteById(id);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    public String deleteVolunteer(String name) {
        logger.info("Deleting volunteer with name " + name);
        List<String> names = volunteerRepository.findAll().stream().map(Volunteer::getName).toList();
        if (names.contains(name)) {
            volunteerRepository.deleteVolunteerByName(name);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    public List<Volunteer> allVolunteers() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        if (volunteers.isEmpty()) {
            logger.info("There are no volunteers in data base");
            return null;
        }
        return volunteers;
    }



}
