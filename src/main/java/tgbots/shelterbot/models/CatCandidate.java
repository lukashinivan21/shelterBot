package tgbots.shelterbot.models;

import javax.persistence.Entity;

@Entity
public class CatCandidate extends Candidate {

    public CatCandidate() {
        super();
    }

    public CatCandidate(Long id, String name, String userName, String phoneNumber, String botState) {
        super(id, name, userName, phoneNumber, botState);
    }
}
