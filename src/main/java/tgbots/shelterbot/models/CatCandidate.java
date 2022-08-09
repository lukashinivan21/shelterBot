package tgbots.shelterbot.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "candidate_cat_shelter")
public class CatCandidate extends Candidate {

    public CatCandidate() {
        super();
    }

    public CatCandidate(Long id, String name, String userName, String phoneNumber, String botState) {
        super(id, name, userName, phoneNumber, botState);
    }
}
