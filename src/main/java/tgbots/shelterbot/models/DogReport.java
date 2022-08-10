package tgbots.shelterbot.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "report_dog")
public class DogReport extends Report {

    @ManyToOne
    @JoinColumn(name = "dog_candidate_id_candidate")
    private DogCandidate dogCandidate;

    public DogCandidate getDogCandidate() {
        return dogCandidate;
    }

    public void setDogCandidate(DogCandidate dogCandidate) {
        this.dogCandidate = dogCandidate;
    }

    public DogReport() {
        super();
    }

    public DogReport(Long idReport, String caption, LocalDate dateReport, String filePath, Long fileSize, byte[] data) {
        super(idReport, caption, dateReport, filePath, fileSize, data);

    }
}
