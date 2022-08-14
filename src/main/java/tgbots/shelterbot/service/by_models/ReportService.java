package tgbots.shelterbot.service.by_models;

import tgbots.shelterbot.models.Report;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    List<? extends Report> getReportByCandidateId(Long id);

    List<? extends  Report> getAllReports();

    List<Long> idsOfReportsByCandidateIdAndDate(Long id, LocalDate date);

    Report findReportByReportId(Long id);

    String deleteReportsByCandidateIdAndDateReport(Long id, LocalDate date);

    String deleteReportsByCandidateId(Long id);

    String deleteReportsByDate(LocalDate date);

    void clear();
}
