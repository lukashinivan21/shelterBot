package tgbots.shelterbot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgbots.shelterbot.constants.StringConstants;
import tgbots.shelterbot.models.CatCandidate;
import tgbots.shelterbot.models.CatReport;
import tgbots.shelterbot.models.DogCandidate;
import tgbots.shelterbot.models.DogReport;
import tgbots.shelterbot.repository.CatCandidateRepository;
import tgbots.shelterbot.repository.DogCandidateRepository;
import tgbots.shelterbot.repository.ReportCatRepository;
import tgbots.shelterbot.repository.ReportDogRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MentionImpl implements Mention {

    private static final int DAYS_TEST_PERIOD = 30;
    private static final int EXTRA_DAYS = 14;
    private static final int LOWER_BORDER = 1;
    private static final int UPPER_BORDER = 3;
    private static final int LOWER_BORDER_FOR_VOL = 2;
    private static final int UPPER_BORDER_FOR_VOL = 5;

    private final DogCandidateRepository dogCandidateRepository;
    private final CatCandidateRepository catCandidateRepository;
    private final ReportDogRepository reportDogRepository;
    private final ReportCatRepository reportCatRepository;

    public MentionImpl(DogCandidateRepository dogCandidateRepository,
                       CatCandidateRepository catCandidateRepository,
                       ReportDogRepository reportDogRepository,
                       ReportCatRepository reportCatRepository) {
        this.dogCandidateRepository = dogCandidateRepository;
        this.catCandidateRepository = catCandidateRepository;
        this.reportDogRepository = reportDogRepository;
        this.reportCatRepository = reportCatRepository;
    }

    /**
     * Метод, формирующий список id пользователей, которые, находясь на испытательном сроке,
     * не отправляют отчеты более одного дня и <= 3 дней, и которым нужно выслать напоминание.
     */
    @Override
    @Transactional
    public List<Long> idsForMentionToSendReport() {

        LocalDate rightNow = LocalDate.now();
        List<Long> resultIds = new ArrayList<>();

        List<Long> dogIds = idsDogCandidate();

        List<Long> catIds = idsCatCandidate();

        if (!dogIds.isEmpty()) {
            for (Long id : dogIds) {
                int diff = getPeriodDog(rightNow, id);
                if (diff > LOWER_BORDER && diff <= UPPER_BORDER) {
                    resultIds.add(id);
                }
            }
        }

        if (!catIds.isEmpty()) {
            for (Long id : catIds) {
                int diff = getPeriodCat(rightNow, id);
                if (diff > LOWER_BORDER && diff <= UPPER_BORDER) {
                    resultIds.add(id);
                }
            }
        }

        return resultIds;
    }

    /**
     * Метод, формирующий сообщение волонтеру со списком тех пользователей, которые,
     * находясь на испытательном сроке, не отправляют отчеты более 2 и <=  дней, и с которым нужно связяться напрямую.
     */
    @Override
    @Transactional
    public String mentionForVolunteer() {

        LocalDate rightNow = LocalDate.now();

        List<Long> resultDogIds = new ArrayList<>();
        List<Long> resultCatIds = new ArrayList<>();

        List<Long> dogIds = idsDogCandidate();
        List<Long> catIds = idsCatCandidate();

        if (!dogIds.isEmpty()) {
            for (Long id : dogIds) {
                int diff = getPeriodDog(rightNow, id);
                if (diff > LOWER_BORDER_FOR_VOL && diff <= UPPER_BORDER_FOR_VOL) {
                    resultDogIds.add(id);
                }
            }
        }

        if (!catIds.isEmpty()) {
            for (Long id : catIds) {
                int diff = getPeriodCat(rightNow, id);
                if (diff > LOWER_BORDER_FOR_VOL && diff <= UPPER_BORDER_FOR_VOL) {
                    resultCatIds.add(id);
                }
            }
        }

        StringBuilder sbDog = new StringBuilder();
        StringBuilder sbCat = new StringBuilder();

        if (!resultDogIds.isEmpty()) {
            for (Long id : resultDogIds) {
                DogCandidate candidate = dogCandidateRepository.findDogCandidateById(id);
                sbDog.append("\n").append(candidate.toString()).append(";");
            }
        }

        if (!resultCatIds.isEmpty()) {
            for (Long id : resultCatIds) {
                CatCandidate candidate = catCandidateRepository.findCatCandidateById(id);
                sbCat.append("\n").append(candidate.toString()).append(";");
            }
        }

        String dogResult = sbDog.toString();
        String catResult = sbCat.toString();

        String result = "";

        if (!dogResult.isEmpty() && !catResult.isEmpty()) {
            result = StringConstants.mentionToVol1(dogResult, catResult);
        }

        if (dogResult.isEmpty() && !catResult.isEmpty()) {
            result = StringConstants.mentionToVol1("У пользователей питомника нет задолжностей по отчетам", catResult);
        }

        if (!dogResult.isEmpty() && catResult.isEmpty()) {
            result = StringConstants.mentionToVol1(dogResult, "У пользователей питомника нет задолжностей по отчетам");
        }

        return result;
    }

    /**
     * Метод, формирующий сообщение волонтеру со списком тех пользователей, у которых подходит
     * к концу испытательный срок, и по которым нужно принять решение пройден испытательный срок или нет
     */
    @Override
    @Transactional
    public String mentionForVolunteerAboutTestPeriod() {

        LocalDate rightNow = LocalDate.now();

        List<Long> resultDogIds = new ArrayList<>();
        List<Long> resultCatIds = new ArrayList<>();

        List<Long> dogIds = idsDogCandidate();
        List<Long> catIds = idsCatCandidate();

        if (!dogIds.isEmpty()) {
            for (Long id : dogIds) {
                List<DogReport> dogReports = reportDogRepository.findDogReportByDogCandidate_Id(id).stream().sorted(Comparator.comparing(DogReport::getDateReport)).toList();
                LocalDate date = dogReports.get(0).getDateReport();
                Period period = Period.between(rightNow, date);
                int diff = Math.abs(period.getDays());
                if (diff == DAYS_TEST_PERIOD || diff == DAYS_TEST_PERIOD + EXTRA_DAYS || diff == DAYS_TEST_PERIOD + DAYS_TEST_PERIOD) {
                    resultDogIds.add(id);
                }
            }
        }

        if (!catIds.isEmpty()) {
            for (Long id : catIds) {
                List<CatReport> catReports = reportCatRepository.findCatReportByCatCandidate_Id(id).stream().sorted(Comparator.comparing(CatReport::getDateReport)).toList();
                LocalDate date = catReports.get(0).getDateReport();
                Period period = Period.between(rightNow, date);
                int diff = Math.abs(period.getDays());
                if (diff == DAYS_TEST_PERIOD || diff == DAYS_TEST_PERIOD + EXTRA_DAYS || diff == DAYS_TEST_PERIOD + DAYS_TEST_PERIOD) {
                    resultCatIds.add(id);
                }
            }
        }

        StringBuilder sbDog = new StringBuilder();
        StringBuilder sbCat = new StringBuilder();

        if (!resultDogIds.isEmpty()) {
            for (Long id : resultDogIds) {
                DogCandidate candidate = dogCandidateRepository.findDogCandidateById(id);
                sbDog.append("\n").append(candidate.toString()).append(";");
            }
        }

        if (!resultCatIds.isEmpty()) {
            for (Long id : resultCatIds) {
                CatCandidate candidate = catCandidateRepository.findCatCandidateById(id);
                sbCat.append("\n").append(candidate.toString()).append(";");
            }
        }

        String dogResult = sbDog.toString();
        String catResult = sbCat.toString();

        String result = "";

        if (!dogResult.isEmpty() && !catResult.isEmpty()) {
            result = StringConstants.mentionToVol2(dogResult, catResult);
        }

        if (dogResult.isEmpty() && !catResult.isEmpty()) {
            result = StringConstants.mentionToVol2("Нет кандидатов для принятия решения", catResult);
        }

        if (!dogResult.isEmpty() && catResult.isEmpty()) {
            result = StringConstants.mentionToVol1(dogResult, "Нет кандидатов для принятия решения");
        }

        return result;
    }

//    Метод для вычисления разницы между текущей датой и датой последнего отправленного отчета пользователем приюта для кошек
    private int getPeriodCat(LocalDate rightNow, Long id) {
        List<CatReport> catReports = reportCatRepository.findCatReportByCatCandidate_Id(id).stream().sorted(Comparator.comparing(CatReport::getDateReport)).toList();
        LocalDate date = catReports.get(catReports.size() - 1).getDateReport();
        Period period = Period.between(rightNow, date);
        return Math.abs(period.getDays());
    }

//    Метод для вычисления разницы между текущей датой и датой последнего отправленного отчета пользователем приюта для собак
    private int getPeriodDog(LocalDate rightNow, Long id) {
        List<DogReport> dogReports = reportDogRepository.findDogReportByDogCandidate_Id(id).stream().sorted(Comparator.comparing(DogReport::getDateReport)).toList();
        LocalDate date = dogReports.get(dogReports.size() - 1).getDateReport();
        Period period = Period.between(rightNow, date);
        return Math.abs(period.getDays());
    }

//     Метод для опредеоения id всех пользователей приюта для собак, у которых есть один и более отправленный отчет
    private List<Long> idsDogCandidate() {
        return dogCandidateRepository.findAll()
                .stream()
                .map(DogCandidate::getId)
                .filter(id -> !reportDogRepository.findDogReportByDogCandidate_Id(id).isEmpty()).toList();
    }

//    Метод для опредеоения id всех пользователей приюта для кошек, у которых есть один и более отправленный отчет
    private List<Long> idsCatCandidate() {
        return catCandidateRepository.findAll()
                .stream()
                .map(CatCandidate::getId)
                .filter(id -> !reportCatRepository.findCatReportByCatCandidate_Id(id).isEmpty()).toList();
    }
}
