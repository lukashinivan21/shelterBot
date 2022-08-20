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
import tgbots.shelterbot.models.DogCandidate;
import tgbots.shelterbot.repository.DogCandidateRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static tgbots.shelterbot.TestConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DogCandidateControllerTest extends Candidate {

    @LocalServerPort
    private int port;

    @Autowired
    private DogCandidateRepository dogCandidateRepository;

    @Autowired
    private DogCandidateController dogCandidateController;

    @Autowired
    private TestRestTemplate restTemplate;



    @BeforeEach
    void setUp() {
        dogCandidateRepository.save(dogCandidate1);
        dogCandidateRepository.save(dogCandidate2);
        dogCandidateRepository.save(dogCandidate3);
        dogCandidateRepository.save(dogCandidate4);
    }

    @AfterEach
    void deleteAfter() {
        List<DogCandidate> dogCandidates = dogCandidateRepository.findAll();
        List<Long> ids = new ArrayList<>();

        for (DogCandidate candidate : dogCandidates) {
            if (candidate.getId() < 100) {
                ids.add(candidate.getId());
            }
        }

        for (Long id : ids) {
            dogCandidateRepository.deleteById(id);
        }
    }

    @Test
    void checkController() {
        Assertions.assertThat(dogCandidateController).isNotNull();
    }

    @Test
    void checkRepository() {
        Assertions.assertThat(dogCandidateRepository).isNotNull();
    }

    @Test
    void getDogCandidateById() {
        List<DogCandidate> dogCandidates = dogCandidateRepository.findAll();
        dogCandidates.sort(Comparator.comparing(DogCandidate::getId));
        long dogCandidateId = dogCandidates.get(INDEX_0).getId();
        ResponseEntity<DogCandidate> response = restTemplate.getForEntity("http://localhost:" + port + "/dogCandidate/" + dogCandidateId, DogCandidate.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Alex");
        Assertions.assertThat(response.getBody().getUserName()).isEqualTo("alex91");
        Assertions.assertThat(response.getBody().getPhoneNumber()).isEqualTo("+79687523141");
    }

    @Test
    void getDogCandidateByUserName() {
        List<DogCandidate> dogCandidates = dogCandidateRepository.findAll();
        dogCandidates.sort(Comparator.comparing(DogCandidate::getId));
        String dogCandidateUserName = dogCandidates.get(INDEX_1).getUserName();
        ResponseEntity<DogCandidate> response = restTemplate.getForEntity("http://localhost:" + port + "/dogCandidate/userName" + dogCandidateUserName, DogCandidate.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getId()).isEqualTo(25L);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Jim");
        Assertions.assertThat(response.getBody().getPhoneNumber()).isEqualTo("+78321472315");
        Assertions.assertThat(response.getBody().getBotState()).isEqualTo(BotState.DIALOG.toString());
    }

    @Test
    void updateDogCandidate() {
        long id = 12L;
        String name = "Ronald";
        String userName = "ron_u";
        ResponseEntity<DogCandidate> response = restTemplate.exchange("http://localhost:" + port + "/dogCandidate" +
                "?id=" + id + "&name=" + name + "&userName=" + userName, HttpMethod.PUT, null, DogCandidate.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Ronald");
        Assertions.assertThat(response.getBody().getUserName()).isEqualTo("ron_u");
    }

    @Test
    void deleteDogCandidateById() {
        List<DogCandidate> dogCandidates = dogCandidateRepository.findAll();
        dogCandidates.sort(Comparator.comparing(DogCandidate::getId));
        long dogCandidateId = dogCandidates.get(INDEX_0).getId();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dogCandidate/" + dogCandidateId, HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");

    }

    @Test
    void deleteDogCandidateByUserName() {
        List<DogCandidate> dogCandidates = dogCandidateRepository.findAll();
        dogCandidates.sort(Comparator.comparing(DogCandidate::getId));
        String userName = dogCandidates.get(INDEX_2).getUserName();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dogCandidate/userName?userName=" + userName, HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }

    @Test
    void allDogCandidates() {
        ResponseEntity<List<DogCandidate>> response = restTemplate.exchange("http://localhost:" + port + "/dogCandidate", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).get(INDEX_3).getId()).isEqualTo(25L);
        Assertions.assertThat(response.getBody().get(INDEX_0).getName()).isEqualTo("Alex");
        Assertions.assertThat(response.getBody().get(INDEX_2).getUserName()).isEqualTo("st17");
        Assertions.assertThat(response.getBody().get(INDEX_1).getPhoneNumber()).isEqualTo("+79210587310");
        Assertions.assertThat(response.getBody().get(4).getBotState()).isEqualTo(BotState.DIALOG.toString());
    }

}