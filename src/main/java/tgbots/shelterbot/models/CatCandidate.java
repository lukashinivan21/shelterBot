package tgbots.shelterbot.models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Класс, описывающий пользователя приюта для кошек
 */
@Entity
@Table(name = "candidate_cat_shelter")
public class CatCandidate extends Candidate {

    @OneToMany
    private Set<CatReport> catReports;

    public CatCandidate() {
        super();
    }

    public CatCandidate(Long id, String name, String userName, String phoneNumber, String botState) {
        super(id, name, userName, phoneNumber, botState);
    }
}
