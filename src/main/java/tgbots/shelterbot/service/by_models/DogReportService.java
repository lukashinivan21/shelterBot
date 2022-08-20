package tgbots.shelterbot.service.by_models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgbots.shelterbot.models.DogReport;
import tgbots.shelterbot.repository.ReportDogRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис, отвечающий за взаимодействие с базой отчетов приюта для собак
 */
@Service
public class DogReportService implements ReportService<DogReport> {

    private final Logger logger = LoggerFactory.getLogger(DogReportService.class);

    private final ReportDogRepository reportDogRepository;

    public DogReportService(ReportDogRepository reportDogRepository) {
        this.reportDogRepository = reportDogRepository;
    }


    @Override
    @Transactional
    public List<DogReport> getReportByCandidateId(Long id) {
        logger.info("Was request method for getting reports of users with id: {}", id );
        List<DogReport> result = reportDogRepository.findDogReportByDogCandidate_Id(id);
        if (result.isEmpty()) {
            logger.info("Candidate with id: {} hasn't reports", id);
            return null;
        }
        return result;
    }

    @Override
    public List<DogReport> getAllReports() {
        logger.info("Was request method for getting all reports in dog data base");
        List<DogReport> result = reportDogRepository.findAll();
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    @Transactional
    public List<Long> idsOfReportsByCandidateIdAndDate(Long id, LocalDate date) {
        List<DogReport> resultD = reportDogRepository.findDogReportsByDateReportAndDogCandidate_Id(date, id);
        List<Long> result = new ArrayList<>();
        for (DogReport dogReport : resultD) {
            result.add(dogReport.getIdReport());
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    public DogReport findReportByReportId(Long id) {
        logger.info("Search dog report with id " + id);
        DogReport result = reportDogRepository.findById(id).orElse(null);
        if (result == null) {
            logger.info("There is not report with id " + id);
        }
        return result;
    }

    @Override
    public String getReportCaption(Long id) {
        logger.info("Request for getting caption of report with id " + id);
        String caption = null;
        Optional<DogReport> report = reportDogRepository.findById(id);
        if (report.isPresent()) {
            caption = report.get().getCaption();
        }
        return caption;
    }

    @Override
    @Transactional
    public String deleteReportsByCandidateId(Long id) {
        List<Long> ids = reportDogRepository.findAll().stream().map(DogReport::getIdReport).toList();
        if (ids.contains(id)) {
            reportDogRepository.deleteDogReportsByDogCandidateId(id);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    @Transactional
    public String deleteReportsByCandidateIdAndDateReport(Long id, LocalDate date) {
        List<Long> ids = reportDogRepository.findAll().stream().map(DogReport::getIdReport).toList();
        List<LocalDate> dates = reportDogRepository.findAll().stream().map(DogReport::getDateReport).toList();
        if (ids.contains(id) && dates.contains(date)) {
            reportDogRepository.deleteDogReportsByDogCandidateIdAndDateReport(id, date);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    @Transactional
    public String deleteReportsByDate(LocalDate date) {
        List<LocalDate> dates = reportDogRepository.findAll().stream().map(DogReport::getDateReport).toList();
        if (dates.contains(date)) {
            reportDogRepository.deleteDogReportsByDateReport(date);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    public void clear() {
        reportDogRepository.deleteAll();
    }
}
