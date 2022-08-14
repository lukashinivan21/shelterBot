package tgbots.shelterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgbots.shelterbot.models.DogReport;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportDogRepository extends JpaRepository<DogReport, Long> {

    DogReport findDogReportByIdReport(Long id);

    List<DogReport> findDogReportsByDateReportAndDogCandidate_Id(LocalDate date, Long id);

    List<DogReport> findDogReportByDogCandidate_Id(Long id);

    void deleteDogReportsByDogCandidateIdAndDateReport(Long id, LocalDate date);

    void deleteDogReportsByDateReport(LocalDate date);

    void deleteDogReportsByDogCandidateId(Long id);
}
