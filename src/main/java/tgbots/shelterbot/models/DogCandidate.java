package tgbots.shelterbot.models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Класс, описывающий пользователя приюта для собак
 */
@Entity
@Table(name = "candidate_dog_shelter")
public class DogCandidate extends Candidate{

    @OneToMany
    private Set<DogReport> dogReports;

    public DogCandidate() {
        super();
    }

    public DogCandidate(Long id, String name, String userName, String phoneNumber, String botState) {
        super(id, name, userName, phoneNumber, botState);
    }

}
