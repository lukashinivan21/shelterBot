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
@RequestMapping("/catCandidate")
public class CatCandidateController {

    private final CandidateService catCandidateImpl;

    public CatCandidateController(@Qualifier("catCandidateImpl") CandidateService catCandidateImpl) {
        this.catCandidateImpl = catCandidateImpl;
    }

    @GetMapping("{id}")
    public ResponseEntity<Candidate> getCatCandidateById(@PathVariable Long id) {
        Candidate catCandidate = catCandidateImpl.getCandidateById(id);
        if (catCandidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(catCandidate);
    }

    @GetMapping("/userName{userName}")
    public ResponseEntity<Candidate> getCatCandidateByUserName(@PathVariable String userName) {
        Candidate catCandidate = catCandidateImpl.getCandidateByUserName(userName);
        if (catCandidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(catCandidate);
    }

    @PutMapping
    public ResponseEntity<Candidate> updateCatCandidate(@RequestParam Long id,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String userName,
                                                        @RequestParam(required = false) String phoneNumber) {
        Candidate catCandidate = new Candidate();
        catCandidate.setId(id);
        if (name != null) {
            catCandidate.setName(name);
        }
        if (userName != null) {
            catCandidate.setUserName(userName);
        }
        if (phoneNumber != null && Verification.CHECK_PHONE_NUMBER.matcher(phoneNumber).matches()) {
            catCandidate.setPhoneNumber(phoneNumber);
        } else if (phoneNumber != null && !Verification.CHECK_PHONE_NUMBER.matcher(phoneNumber).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Candidate result = catCandidateImpl.updateCandidate(catCandidate);
        if (result == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCatCandidateById(@PathVariable Long id) {
        String result = catCandidateImpl.deleteCandidateById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/userName")
    public ResponseEntity<String> deleteCatCandidateByUserName(@RequestParam String userName) {
        String result = catCandidateImpl.deleteCandidateByUserName(userName);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<? extends Candidate>> allCatCandidates() {
        List<? extends Candidate> result = catCandidateImpl.allCandidates();
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<List<Candidate>> deleteAll() {
        catCandidateImpl.clear();
        return ResponseEntity.ok().build();
    }

}
