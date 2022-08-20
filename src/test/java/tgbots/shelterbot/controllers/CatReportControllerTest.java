package tgbots.shelterbot.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tgbots.shelterbot.models.CatCandidate;
import tgbots.shelterbot.models.CatReport;
import tgbots.shelterbot.repository.CatCandidateRepository;
import tgbots.shelterbot.repository.ReportCatRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static tgbots.shelterbot.TestConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatReportControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReportCatRepository reportCatRepository;

    @Autowired
    private CatCandidateRepository catCandidateRepository;

    @Autowired
    private CatReportController catReportController;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        catCandidateRepository.save(catCandidate1);
        catCandidateRepository.save(catCandidate2);
        catCandidateRepository.save(catCandidate3);
        catReport1.setCatCandidate(catCandidate1);
        catReport2.setCatCandidate(catCandidate2);
        catReport3.setCatCandidate(catCandidate3);
        reportCatRepository.save(catReport1);
        reportCatRepository.save(catReport2);
        reportCatRepository.save(catReport3);
    }

    @AfterEach
    void deleteAfter() {
        List<CatReport> reports = reportCatRepository.findAll();
        List<Long> ids = new ArrayList<>();
        for (CatReport report : reports) {
            if (report.getIdReport() < 1000) {
                ids.add(report.getIdReport());
            }
        }

        for (Long id : ids) {
            reportCatRepository.deleteById(id);
        }

        List<CatCandidate> catCandidates = catCandidateRepository.findAll();
        List<Long> catIds = new ArrayList<>();
        for (CatCandidate candidate : catCandidates) {
            if (candidate.getId() < 100) {
                catIds.add(candidate.getId());
            }
        }

        for (Long id : catIds) {
            catCandidateRepository.deleteById(id);
        }
    }

    @Test
    void checkController() {
        Assertions.assertThat(catReportController).isNotNull();
    }

    @Test
    void checkRepository() {
        Assertions.assertThat(catCandidateRepository).isNotNull();
        Assertions.assertThat(reportCatRepository).isNotNull();
    }

    @Test
    void getReportsByCandidateId() {
        Long id = catCandidate1.getId();
        ResponseEntity<List<CatReport>> response = restTemplate.exchange("http://localhost:" + port + "/catReport/" + id, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(1);
        Assertions.assertThat(response.getBody().get(0).getCaption()).isEqualTo("Cat is good");
        Assertions.assertThat(response.getBody().get(0).getDateReport()).isEqualTo(LocalDate.of(2022, Month.JUNE, 8));
    }

    @Test
    void getAllReports() {
        ResponseEntity<List<CatReport>> response = restTemplate.exchange("http://localhost:" + port + "/catReport", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(3);
        Assertions.assertThat(response.getBody().get(0).getCaption()).isEqualTo("Cat is good");
        Assertions.assertThat(response.getBody().get(1).getDateReport()).isEqualTo(LocalDate.of(2022, Month.JULY, 16));
        Assertions.assertThat(response.getBody().get(2).getFileSize()).isEqualTo(10267L);
    }

    @Test
    void getIdsOfReports() {
        Long id = catCandidate1.getId();
        String date = LocalDate.of(2022, Month.JUNE, 8).toString();
        ResponseEntity<List<Long>> response = restTemplate.exchange("http://localhost:" + port + "/catReport/idsOfReports?id=" + id + "&dateS=" + date,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(1);

    }

    @Test
    void getReportCaption() {
        long id = 64L;
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/catReport/caption?id=" + id, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    void deleteCatReportsByCandidateId() {
        long id = catCandidate2.getId();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/catReport/deleteByCandidateId/" + id, HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }

    @Test
    void deleteCatReportsByDate() {
        String date = LocalDate.of(2022, Month.AUGUST, 2).toString();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/catReport/deleteByDate/" + date, HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }

    @Test
    void deleteCatReportsByCandidateIdAndDate() {
        long id = catCandidate1.getId();
        String date = LocalDate.of(2022, Month.JUNE, 8).toString();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/catReport/deleteByCandidateIdAndDate?id=" + id + "&date=" + date,
                HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");

    }

    @Test
    void clear() {
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/catReport/clear", HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }
}