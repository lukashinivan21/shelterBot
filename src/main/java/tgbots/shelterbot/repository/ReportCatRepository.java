package tgbots.shelterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgbots.shelterbot.models.CatReport;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportCatRepository extends JpaRepository<CatReport, Long> {

    CatReport findCatReportByIdReport(Long id);

    List <CatReport> findCatReportsByDateReportAndCatCandidate_Id(LocalDate date, Long id);

    List<CatReport> findCatReportByCatCandidate_Id(Long id);

    void deleteCatReportsByCatCandidateIdAndDateReport(Long id, LocalDate date);

    void deleteCatReportsByDateReport(LocalDate date);

    void deleteCatReportsByCatCandidateId(Long id);
}
