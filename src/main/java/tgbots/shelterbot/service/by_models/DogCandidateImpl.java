package tgbots.shelterbot.service.by_models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tgbots.shelterbot.models.Candidate;
import tgbots.shelterbot.models.DogCandidate;
import tgbots.shelterbot.repository.DogCandidateRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DogCandidateImpl implements CandidateService{

    private final Logger logger = LoggerFactory.getLogger(DogCandidateImpl.class);

    private final DogCandidateRepository dogCandidateRepository;

    public DogCandidateImpl(DogCandidateRepository dogCandidateRepository) {
        this.dogCandidateRepository = dogCandidateRepository;
    }

    @Override
    public Candidate getCandidateById(Long id) {
        DogCandidate dogCandidate = dogCandidateRepository.findById(id).orElse(null);
        if (dogCandidate == null) {
            logger.info("Dog candidate with id {} doesn't exist", id);
        }
        return dogCandidate;
    }

    @Override
    public Candidate getCandidateByUserName(String userName) {
        DogCandidate dogCandidate = dogCandidateRepository.findDogCandidateByUserName(userName);
        if (dogCandidate == null) {
            logger.info("Dog candidate with user name {} doesn't exist", userName);
        }
        return dogCandidate;
    }

    @Override
    public Candidate updateCandidate(Candidate candidate) {
        logger.info("Was requested method for update dog candidate");
        Optional<DogCandidate> candidateOptional = dogCandidateRepository.findById(candidate.getId());
        if (candidateOptional.isPresent()) {
            DogCandidate result = candidateOptional.get();
            result.setName(candidate.getName());
            result.setUserName(candidate.getUserName());
            result.setPhoneNumber(candidate.getPhoneNumber());
            return dogCandidateRepository.save(result);
        } else {
            logger.info("Dog candidate with id {} doesn't exist", candidate.getId());
            return null;
        }
    }

    @Override
    @Transactional
    public String deleteCandidateById(Long id) {
        logger.info("Deleting dog candidate with id " + id);
        List<Long> ids = dogCandidateRepository.findAll().stream().map(DogCandidate::getId).toList();
        if (ids.contains(id)) {
            dogCandidateRepository.deleteById(id);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    @Transactional
    public String deleteCandidateByUserName(String userName) {
        logger.info("Deleting dog candidate with user name " + userName);
        List<String> userNames = dogCandidateRepository.findAll().stream().map(DogCandidate::getUserName).toList();
        if (userNames.contains(userName)) {
            dogCandidateRepository.deleteDogCandidateByUserName(userName);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    public List<DogCandidate> allCandidates() {
        List<DogCandidate> allDogCandidates = dogCandidateRepository.findAll();
        if (allDogCandidates.isEmpty()) {
            logger.info("There are no dog candidates in data base");
            return null;
        }
        return allDogCandidates;
    }

    @Override
    public void clear() {
        dogCandidateRepository.deleteAll();
    }
}
