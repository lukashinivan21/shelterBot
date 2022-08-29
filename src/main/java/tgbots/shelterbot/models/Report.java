package tgbots.shelterbot.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс, содержащий сущность, которая описывает отчет, отправляемый пользователем
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_report")
    private Long idReport;

    @Column(name = "caption")
    private String caption;

    @Column(name = "report_date")
    private LocalDate dateReport;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "report_data")
    @Lob
    private byte[] data;

    public Report(Long idReport, String caption, LocalDate dateReport, String filePath, long fileSize, byte[] data) {
        this.idReport = idReport;
        this.caption = caption;
        this.dateReport = dateReport;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.data = data;
    }

    public Report() {

    }

    public Long getIdReport() {
        return idReport;
    }

    public String getCaption() {
        return caption;
    }

    public LocalDate getDateReport() {
        return dateReport;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public byte[] getData() {
        return data;
    }

    public void setIdReport(Long idReport) {
        this.idReport = idReport;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setDateReport(LocalDate dateReport) {
        this.dateReport = dateReport;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return idReport.equals(report.idReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReport);
    }
}
