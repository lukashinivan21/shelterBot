package tgbots.shelterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgbots.shelterbot.models.DogReport;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportDogRepository extends JpaRepository<DogReport, Long> {

    DogReport findDogReportByIdReport(Long id);

    DogReport findDogReportByDateReportAndDogCandidate_Id(LocalDate date, Long id);

    List<DogReport> findDogReportByDogCandidate_Id(Long id);
}
