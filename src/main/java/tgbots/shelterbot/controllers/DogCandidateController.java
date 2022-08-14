package tgbots.shelterbot.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tgbots.shelterbot.constants.Verification;
import tgbots.shelterbot.models.Candidate;
import tgbots.shelterbot.service.by_models.CandidateService;

import java.util.List;

@RestController
@RequestMapping("/dogCandidate")
public class DogCandidateController {

    private final CandidateService dogCandidateImpl;

    public DogCandidateController(@Qualifier("dogCandidateImpl") CandidateService dogCandidateImpl) {
        this.dogCandidateImpl = dogCandidateImpl;
    }

    @GetMapping("{id}")
    public ResponseEntity<Candidate> getDogCandidateById(@PathVariable Long id) {
        Candidate dogCandidate = dogCandidateImpl.getCandidateById(id);
        if (dogCandidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dogCandidate);
    }

    @GetMapping("/userName{userName}")
    public ResponseEntity<Candidate> getDogCandidateByUserName(@PathVariable String userName) {
        Candidate dogCandidate = dogCandidateImpl.getCandidateByUserName(userName);
        if (dogCandidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dogCandidate);
    }

    @PutMapping
    public ResponseEntity<Candidate> updateDogCandidate(@RequestParam Long id,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String userName,
                                                        @RequestParam(required = false) String phoneNumber) {
        Candidate dogCandidate = new Candidate();
        dogCandidate.setId(id);
        if (name != null) {
            dogCandidate.setName(name);
        }
        if (userName != null) {
            dogCandidate.setUserName(userName);
        }
        if (phoneNumber != null && Verification.CHECK_PHONE_NUMBER.matcher(phoneNumber).matches()) {
            dogCandidate.setPhoneNumber(phoneNumber);
        } else if (phoneNumber != null && !Verification.CHECK_PHONE_NUMBER.matcher(phoneNumber).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Candidate result = dogCandidateImpl.updateCandidate(dogCandidate);
        if (result == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDogCandidateById(@PathVariable Long id) {
        String result = dogCandidateImpl.deleteCandidateById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/userName")
    public ResponseEntity<String> deleteDogCandidateByUserName(@RequestParam String userName) {
        String result = dogCandidateImpl.deleteCandidateByUserName(userName);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<? extends Candidate>> allDogCandidates() {
        List<? extends Candidate> result = dogCandidateImpl.allCandidates();
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        dogCandidateImpl.clear();
        return ResponseEntity.ok().build();
    }
}
