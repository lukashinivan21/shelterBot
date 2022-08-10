package tgbots.shelterbot.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "report_cat")
public class CatReport extends Report{

    @ManyToOne
    @JoinColumn(name = "cat_candidate_id_candidate")
    private CatCandidate catCandidate;

    public CatCandidate getCatCandidate() {
        return catCandidate;
    }

    public void setCatCandidate(CatCandidate catCandidate) {
        this.catCandidate = catCandidate;
    }

    public CatReport() {
        super();
    }

    public CatReport(Long idReport, String caption, LocalDate dateReport, String filePath, Long fileSize, byte[] data) {
        super(idReport, caption, dateReport, filePath, fileSize, data);
    }
}
