package tgbots.shelterbot.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "candidate_dog_shelter")
public class DogCandidate extends Candidate{

    public DogCandidate() {
        super();
    }

    public DogCandidate(Long id, String name, String userName, String phoneNumber, String botState) {
        super(id, name, userName, phoneNumber, botState);
    }

}
