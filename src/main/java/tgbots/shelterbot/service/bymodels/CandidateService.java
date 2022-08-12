package tgbots.shelterbot.service.bymodels;

import tgbots.shelterbot.models.Candidate;

import java.util.List;


public interface CandidateService {

    Candidate getCandidateById(Long id);

    Candidate getCandidateByUserName(String userName);

    Candidate updateCandidate(Candidate candidate);

    String deleteCandidateById(Long id);

    String deleteCandidateByUserName(String userName);

    List<? extends Candidate> allCandidates();


}
