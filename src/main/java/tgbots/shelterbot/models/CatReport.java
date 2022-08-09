package tgbots.shelterbot.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "report_cat")
public class CatReport extends Report{

    public CatReport() {
        super();
    }

    public CatReport(Long idReport, String caption, LocalDate dateReport, String filePath, Long fileSize, byte[] data) {
        super(idReport, caption, dateReport, filePath, fileSize, data);
    }
}
