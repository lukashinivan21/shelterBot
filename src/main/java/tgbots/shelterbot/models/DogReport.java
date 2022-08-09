package tgbots.shelterbot.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "report_dog")
public class DogReport extends Report {

    public DogReport() {
        super();
    }

    public DogReport(Long idReport, String caption, LocalDate dateReport, String filePath, Long fileSize, byte[] data) {
        super(idReport, caption, dateReport, filePath, fileSize, data);

    }
}
