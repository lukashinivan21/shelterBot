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
import tgbots.shelterbot.models.Volunteer;
import tgbots.shelterbot.repository.VolunteerRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static tgbots.shelterbot.TestConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VolunteerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerController volunteerController;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        volunteerRepository.save(volunteer1);
        volunteerRepository.save(volunteer2);
        volunteerRepository.save(volunteer3);
        volunteerRepository.save(volunteer4);
    }

    @AfterEach
    void deleteAfter() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        List<Long> ids = new ArrayList<>();


        for (Volunteer vol : volunteers) {
            if (vol.getId() < 100) {
                ids.add(vol.getId());
            }
        }

        for (Long id : ids) {
            volunteerRepository.deleteById(id);
        }
    }

    @Test
    void checkController() {
        Assertions.assertThat(volunteerController).isNotNull();
    }

    @Test
    void checkRepository() {
        Assertions.assertThat(volunteerRepository).isNotNull();
    }

    @Test
    void getVolunteerById() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        volunteers.sort(Comparator.comparing(Volunteer::getId));
        long volunteerId = volunteers.get(INDEX_0).getId();
        ResponseEntity<Volunteer> response = restTemplate.getForEntity("http://localhost:" + port + "/volunteer/" + volunteerId, Volunteer.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Roman");
        Assertions.assertThat(response.getBody().getUserName()).isEqualTo("rom99");
        Assertions.assertThat(response.getBody().isFree()).isEqualTo(true);
    }

    @Test
    void getVolunteerByName() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        volunteers.sort(Comparator.comparing(Volunteer::getId));
        String name = volunteers.get(INDEX_1).getName();
        ResponseEntity<Volunteer> response = restTemplate.getForEntity("http://localhost:" + port + "/volunteer/name" + name, Volunteer.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getId()).isEqualTo(23L);
        Assertions.assertThat(response.getBody().getUserName()).isEqualTo("me_gre12");
        Assertions.assertThat(response.getBody().isFree()).isEqualTo(true);
    }

    @Test
    void updateVolunteer() {
        long id = 45L;
        String name = "Tessa";
        String userName = "asset_23";
        ResponseEntity<Volunteer> response = restTemplate.exchange("http://localhost:" + port + "/volunteer" +
                "?id=" + id + "&name=" + name + "&userName=" + userName, HttpMethod.PUT, null, Volunteer.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Tessa");
        Assertions.assertThat(response.getBody().getUserName()).isEqualTo("asset_23");

    }

    @Test
    void deleteVolunteerById() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        volunteers.sort(Comparator.comparing(Volunteer::getId));
        long volunteerId = volunteers.get(INDEX_2).getId();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/volunteer/" + volunteerId, HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }

    @Test
    void deleteVolunteerByUserName() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        volunteers.sort(Comparator.comparing(Volunteer::getId));
        String userName = volunteers.get(INDEX_3).getUserName();
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/volunteer/userNameDelete?userName=" + userName, HttpMethod.DELETE, null, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo("SUCCESS");
    }

    @Test
    void allVolunteers() {
        ResponseEntity<List<Volunteer>> response = restTemplate.exchange("http://localhost:" + port + "/volunteer", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).get(INDEX_0).getId()).isEqualTo(11L);
        Assertions.assertThat(response.getBody().get(INDEX_1).getName()).isEqualTo("Greg");
        Assertions.assertThat(response.getBody().get(INDEX_2).getUserName()).isEqualTo("jes_LL");
        Assertions.assertThat(response.getBody().get(INDEX_3).getName()).isEqualTo("Daniel");
    }


}
