package tgbots.shelterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgbots.shelterbot.models.CatCandidate;

@Repository
public interface CatCandidateRepository extends JpaRepository<CatCandidate, Long> {

    CatCandidate findCatCandidateById(Long id);

    CatCandidate findCatCandidateByUserName(String userName);

    void deleteCatCandidateByUserName(String userName);
}
