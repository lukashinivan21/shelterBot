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
import tgbots.shelterbot.models.DogCandidate;
import tgbots.shelterbot.models.DogReport;
import tgbots.shelterbot.repository.DogCandidateRepository;
import tgbots.shelterbot.repository.ReportDogRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static tgbots.shelterbot.TestConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DogReportControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReportDogRepository reportDogRepository;

    @Autowired
    private DogCandidateRepository dogCandidateRepository;

    @Autowired
    private DogReportController dogReportController;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        dogCandidateRepository.save(dogCandidate1);
        dogCandidateRepository.save(dogCandidate2);
        dogCandidateRepository.save(dogCandidate3);
        dogReport1.setDogCandidate(dogCandidate1);
        dogReport2.setDogCandidate(dogCandidate2);
        dogReport3.setDogCandidate(dogCandidate3);
        reportDogRepository.save(dogReport1);
        reportDogRepository.save(dogReport2);
        reportDogRepository.save(dogReport3);
    }

    @AfterEach
    void deleteAfter() {

        List<DogReport> reports = reportDogRepository.findAll();
        List<Long> ids = new ArrayList<>();

        for (DogReport report : reports) {
            if (report.getIdReport() < 1000) {
                ids.add(report.getIdReport());
            }
        }

        for (Long id : ids) {
            reportDogRepository.deleteById(id);
        }

        List<DogCandidate> candidates = dogCandidateRepository.findAll();
        List<Long> dogIds = new ArrayList<>();
        for (DogCandidate candidate : candidates) {
            if (candidate.getId() < 100) {
                dogIds.add(candidate.getId());
            }
        }

        for (Long id : dogIds) {
            dogCandidateRepository.deleteById(id);
        }
    }

    @Test
    void checkController() {
        Assertions.assertThat(dogReportController).isNotNull();
    }

    @Test
    void checkRepository() {
        Assertions.assertThat(dogCandidateRepository).isNotNull();
        Assertions.assertThat(reportDogRepository).isNotNull();
    }

    @Test
    void getReportsByCandidateId() {
        Long id = dogCandidate1.getId();
        ResponseEntity<List<DogReport>> response = restTemplate.exchange("http://localhost:" + port + "/dogReport/" + id, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(1);
        Assertions.assertThat(response.getBody().get(0).getCaption()).isEqualTo("Dog is good");
        Assertions.assertThat(response.getBody().get(0).getDateReport()).isEqualTo(LocalDate.of(2022, Month.JUNE, 8));
    }

    @Test
    void getAllReports() {
        ResponseEntity<List<DogReport>> response = restTemplate.exchange("http://localhost:" + port + "/dogReport", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(3);
        Assertions.assertThat(response.getBody().get(0).getCaption()).isEqualTo("Dog is good");
        Assertions.assertThat(response.getBody().get(1).getDateReport()).isEqualTo(LocalDate.of(2022, Month.JULY, 16));
        Assertions.assertThat(response.getBody().get(2).getFileSize()).isEqualTo(10335L);
    }

    @Test
    void getIdsOfReports() {
        long id = dogCandidate1.getId();
        String date = LocalDate.of(2022, Month.JUNE, 8).toString();
        ResponseEntity<List<Long>> response = restTemplate.exchange("http://localhost:" + port + "/dogReport/idsOfReports?id=" + id + "&date=" + date,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(1);
    }

    @Test
    void getReportCaption() {
        long id = 64L;
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/dogReport/caption?id=" + id, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteDogReportsByCandidateId() {
        long id = dogCandidate1.getId();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dogReport/deleteByCandidateId/" + id, HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }

    @Test
    void deleteDogReportsByDate() {
        String date = LocalDate.of(2022, Month.AUGUST, 2).toString();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dogReport/deleteByDate/" + date, HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");

    }

    @Test
    void deleteDogReportsByCandidateIdAndDate() {
        long id = dogCandidate2.getId();
        String date = LocalDate.of(2022, Month.JULY, 16).toString();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dogReport/deleteByCandidateIdAndDate?id=" + id + "&date=" + date,
                HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }

    @Test
    void clear() {
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dogReport/clear", HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }
}