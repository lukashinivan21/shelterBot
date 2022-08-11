package tgbots.shelterbot.service.bymodels;

import tgbots.shelterbot.models.Candidate;
import tgbots.shelterbot.models.DogCandidate;

import java.util.List;

public interface CandidateService {

    Candidate getCandidateById(Long id);

    Candidate getCandidateByUserName(String userName);

    Candidate updateCandidate(Candidate Candidate);

    String deleteCandidateById(Long id);

    String deleteCandidateByUserName(String userName);

    List<Candidate> allCandidates();
}
