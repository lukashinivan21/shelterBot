package tgbots.shelterbot.service.by_models;

import tgbots.shelterbot.models.Report;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс, содержащий методы для взаимодействия с базой отчетов
 */
public interface ReportService <T extends Report> {

    List<T> getReportByCandidateId(Long id);

    List<T> getAllReports();

    List<Long> idsOfReportsByCandidateIdAndDate(Long id, LocalDate date);

    T findReportByReportId(Long id);

    String deleteReportsByCandidateIdAndDateReport(Long id, LocalDate date);

    String deleteReportsByCandidateId(Long id);

    String deleteReportsByDate(LocalDate date);

    void clear();

    String getReportCaption(Long id);


}
