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
import tgbots.shelterbot.models.BotState;
import tgbots.shelterbot.models.Candidate;
import tgbots.shelterbot.models.CatCandidate;
import tgbots.shelterbot.repository.CatCandidateRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static tgbots.shelterbot.TestConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatCandidateControllerTest extends Candidate {

    @LocalServerPort
    private int port;

    @Autowired
    private CatCandidateRepository catCandidateRepository;

    @Autowired
    private CatCandidateController catCandidateController;

    @Autowired
    private TestRestTemplate restTemplate;



    @BeforeEach
    void setUp() {
        catCandidateRepository.save(catCandidate1);
        catCandidateRepository.save(catCandidate2);
        catCandidateRepository.save(catCandidate3);
        catCandidateRepository.save(catCandidate4);
    }

    @AfterEach
    void deleteAfter() {
        List<CatCandidate> catCandidates = catCandidateRepository.findAll();
        List<Long> ids = new ArrayList<>();

        for (CatCandidate candidate : catCandidates) {
            if (candidate.getId() < 100) {
                ids.add(candidate.getId());
            }
        }

        for (Long id : ids) {
            catCandidateRepository.deleteById(id);
        }
    }

    @Test
    void checkController() {
        Assertions.assertThat(catCandidateController).isNotNull();
    }

    @Test
    void checkRepository() {
        Assertions.assertThat(catCandidateRepository).isNotNull();
    }

    @Test
    void getCatCandidateById() {
        List<CatCandidate> catCandidates = catCandidateRepository.findAll();
        catCandidates.sort(Comparator.comparing(CatCandidate::getId));
        long id = catCandidates.get(INDEX_0).getId();
        ResponseEntity<CatCandidate> response = restTemplate.getForEntity("http://localhost:" + port + "/catCandidate/" + id, CatCandidate.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Anna");
        Assertions.assertThat(response.getBody().getUserName()).isEqualTo("ani_777");
        Assertions.assertThat(response.getBody().getPhoneNumber()).isEqualTo("+79621432020");
    }

    @Test
    void getCatCandidateByUserName() {
        List<CatCandidate> catCandidates = catCandidateRepository.findAll();
        catCandidates.sort(Comparator.comparing(CatCandidate::getId));
        String userName = catCandidates.get(INDEX_1).getUserName();
        ResponseEntity<CatCandidate> response = restTemplate.getForEntity("http://localhost:" + port + "/catCandidate/username/" + userName, CatCandidate.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getId()).isEqualTo(29L);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Kate");
        Assertions.assertThat(response.getBody().getPhoneNumber()).isEqualTo("+79534120876");
    }

    @Test
    void updateCatCandidate() {
        long id = 29L;
        String name = "Susan";
        String userName = "siu19";
        ResponseEntity<CatCandidate> response = restTemplate.exchange("http://localhost:" + port + "/catCandidate" +
                "?id=" + id + "&name=" + name + "&userName=" + userName, HttpMethod.PUT, null, CatCandidate.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Susan");
        Assertions.assertThat(response.getBody().getUserName()).isEqualTo("siu19");
    }

    @Test
    void deleteCatCandidateById() {
        List<CatCandidate> catCandidates = catCandidateRepository.findAll();
        catCandidates.sort(Comparator.comparing(CatCandidate::getId));
        long id = catCandidates.get(INDEX_2).getId();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/catCandidate/" + id, HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }

    @Test
    void deleteCatCandidateByUserName() {
        List<CatCandidate> catCandidates = catCandidateRepository.findAll();
        catCandidates.sort(Comparator.comparing(CatCandidate::getId));
        String userName = catCandidates.get(INDEX_3).getUserName();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/catCandidate/userNameDelete?userName=" + userName, HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }

    @Test
    void allCatCandidates() {
        ResponseEntity<List<CatCandidate>> response = restTemplate.exchange("http://localhost:" + port + "/catCandidate", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).get(INDEX_3).getId()).isEqualTo(70L);
        Assertions.assertThat(response.getBody().get(INDEX_0).getName()).isEqualTo("Anna");
        Assertions.assertThat(response.getBody().get(INDEX_2).getUserName()).isEqualTo("helen8");
        Assertions.assertThat(response.getBody().get(INDEX_1).getPhoneNumber()).isEqualTo("+79534120876");
        Assertions.assertThat(response.getBody().get(INDEX_3).getBotState()).isEqualTo(BotState.DIALOG.toString());
    }


}