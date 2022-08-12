package tgbots.shelterbot.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tgbots.shelterbot.models.Volunteer;
import tgbots.shelterbot.service.bymodels.VolunteerService;

import java.util.List;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    private final VolunteerService volunteerService;


    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Volunteer> getVolunteer(@PathVariable Long id) {
        Volunteer foundVolunteer = volunteerService.getVolunteer(id);
        if (foundVolunteer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(foundVolunteer);
    }

    @GetMapping("/name{name}")
    public ResponseEntity<Volunteer> getVolunteerByName(@PathVariable String name) {
        Volunteer result = volunteerService.getVolunteer(name);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<Volunteer> updateVolunteer(@RequestParam Long id,
                                                     @RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String userName) {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(id);
        volunteer.setName(name);
        volunteer.setUserName(userName);
        Volunteer result = volunteerService.updateVolunteer(volunteer);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteVolunteer(@PathVariable Long id) {
        String result = volunteerService.deleteVolunteer(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/name")
    public ResponseEntity<String> deleteVolunteerByName(@RequestParam String name) {
        String result = volunteerService.deleteVolunteer(name);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        List<Volunteer> result = volunteerService.allVolunteers();
        if (result == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }
}
