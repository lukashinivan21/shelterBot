package tgbots.shelterbot.service.by_models;

import tgbots.shelterbot.models.Candidate;

import java.util.List;

/**
 * Интерфейс, содержащий методы для взаимодействие с базой пользователей
 */
public interface CandidateService {

    Candidate getCandidateById(Long id);

    Candidate getCandidateByUserName(String userName);

    Candidate updateCandidate(Candidate candidate);

    String deleteCandidateById(Long id);

    String deleteCandidateByUserName(String userName);

    List<? extends Candidate> allCandidates();

    void clear();


}
