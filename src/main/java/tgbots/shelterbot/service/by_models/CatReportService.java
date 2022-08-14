package tgbots.shelterbot.service.by_models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgbots.shelterbot.models.CatReport;
import tgbots.shelterbot.models.Report;
import tgbots.shelterbot.repository.ReportCatRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CatReportService implements ReportService{

    private final Logger logger = LoggerFactory.getLogger(CatReportService.class);

    private final ReportCatRepository reportCatRepository;

    public CatReportService(ReportCatRepository reportCatRepository) {
        this.reportCatRepository = reportCatRepository;
    }


    @Override
    @Transactional
    public List<? extends Report> getReportByCandidateId(Long id) {
        logger.info("Was request method for getting reports of users with id: {}", id );
        List<CatReport> result = reportCatRepository.findCatReportByCatCandidate_Id(id);
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    public List<? extends Report> getAllReports() {
        logger.info("Was request method for getting all reports in cat data base");
        List<CatReport> result = reportCatRepository.findAll();
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    @Transactional
    public List<Long> idsOfReportsByCandidateIdAndDate(Long id, LocalDate date) {
        List<CatReport> resultD = reportCatRepository.findCatReportsByDateReportAndCatCandidate_Id(date, id);
        List<Long> result = new ArrayList<>();
        for (CatReport catReport : resultD) {
            result.add(catReport.getIdReport());
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    public Report findReportByReportId(Long id) {
        logger.info("Search cat report with id " + id);
        Report result = reportCatRepository.findById(id).orElse(null);
        if (result == null) {
            logger.info("There is not report with id " + id);
        }
        return result;
    }

    @Override
    @Transactional
    public String deleteReportsByCandidateId(Long id) {
        List<Long> ids = reportCatRepository.findAll().stream().map(CatReport::getIdReport).toList();
        if (ids.contains(id)) {
            reportCatRepository.deleteCatReportsByCatCandidateId(id);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    @Transactional
    public String deleteReportsByCandidateIdAndDateReport(Long id, LocalDate date) {
        List<Long> ids = reportCatRepository.findAll().stream().map(CatReport::getIdReport).toList();
        List<LocalDate> dates = reportCatRepository.findAll().stream().map(CatReport::getDateReport).toList();
        if (ids.contains(id) && dates.contains(date)) {
            reportCatRepository.deleteCatReportsByCatCandidateIdAndDateReport(id, date);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    @Transactional
    public String deleteReportsByDate(LocalDate date) {
        List<LocalDate> dates = reportCatRepository.findAll().stream().map(CatReport::getDateReport).toList();
        if (dates.contains(date)) {
            reportCatRepository.deleteCatReportsByDateReport(date);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    public void clear() {
        reportCatRepository.deleteAll();
    }
}
