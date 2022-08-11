package tgbots.shelterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgbots.shelterbot.models.Volunteer;

import java.util.List;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Volunteer findVolunteerById(Long id);

    Volunteer findVolunteerByName(String name);

    void deleteVolunteerByName(String name);

    List<Volunteer> findVolunteersByFree(boolean condition);


}
