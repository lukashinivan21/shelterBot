package tgbots.shelterbot.service.by_models;

import tgbots.shelterbot.models.Candidate;

import java.util.List;

/**
 * Интерфейс, содержащий методы для взаимодействие с базой пользователей
 */
public interface CandidateService <T extends Candidate> {

    T getCandidateById(Long id);

    T getCandidateByUserName(String userName);

    T updateCandidate(T candidate);

    String deleteCandidateById(Long id);

    String deleteCandidateByUserName(String userName);

    List<T> allCandidates();

    void clear();


}
