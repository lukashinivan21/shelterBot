package tgbots.shelterbot.service.by_models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tgbots.shelterbot.models.Candidate;
import tgbots.shelterbot.models.CatCandidate;
import tgbots.shelterbot.repository.CatCandidateRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CatCandidateImpl implements CandidateService{

    private final Logger logger = LoggerFactory.getLogger(CatCandidateImpl.class);

    private final CatCandidateRepository catCandidateRepository;

    public CatCandidateImpl(CatCandidateRepository catCandidateRepository) {
        this.catCandidateRepository = catCandidateRepository;
    }

    @Override
    public Candidate getCandidateById(Long id) {
        CatCandidate catCandidate = catCandidateRepository.findById(id).orElse(null);
        if (catCandidate == null) {
            logger.info("Cat candidate with id {} doesn't exist", id);
        }
        return catCandidate;
    }

    @Override
    public Candidate getCandidateByUserName(String userName) {
        CatCandidate catCandidate = catCandidateRepository.findCatCandidateByUserName(userName);
        if (catCandidate == null) {
            logger.info("Cat candidate with user name {} doesn't exist", userName);
        }
        return catCandidate;
    }

    @Override
    public Candidate updateCandidate(Candidate candidate) {
        logger.info("Was requested method for update cat candidate");
        Optional<CatCandidate> catCandidateOptional = catCandidateRepository.findById(candidate.getId());
        if (catCandidateOptional.isPresent()) {
            CatCandidate result = catCandidateOptional.get();
            result.setName(candidate.getName());
            result.setUserName(candidate.getUserName());
            result.setPhoneNumber(candidate.getPhoneNumber());
            return catCandidateRepository.save(result);
        } else {
            logger.info("Cat candidate with id {} doesn't exist", candidate.getId());
            return null;
        }
    }

    @Override
    public String deleteCandidateById(Long id) {
        logger.info("Deleting cat candidate with id " + id);
        List<Long> ids = catCandidateRepository.findAll().stream().map(CatCandidate::getId).toList();
        if (ids.contains(id)) {
            catCandidateRepository.deleteById(id);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    public String deleteCandidateByUserName(String userName) {
        logger.info("Deleting cat candidate with user name " + userName);
        List<String> userNames = catCandidateRepository.findAll().stream().map(CatCandidate::getUserName).toList();
        if (userNames.contains(userName)) {
            catCandidateRepository.deleteCatCandidateByUserName(userName);
            return "SUCCESS";
        }
        return null;
    }

    @Override
    public List<CatCandidate> allCandidates() {
        List<CatCandidate> allCatCandidates = catCandidateRepository.findAll();
        if (allCatCandidates.isEmpty()) {
            logger.info("There are no cat candidates in data base");
            return null;
        }
        return allCatCandidates;
    }

    @Override
    public void clear() {
        catCandidateRepository.deleteAll();
    }
}
