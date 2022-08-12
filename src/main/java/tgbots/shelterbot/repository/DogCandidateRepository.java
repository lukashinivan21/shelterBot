package tgbots.shelterbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tgbots.shelterbot.models.DogCandidate;

@Repository
public interface DogCandidateRepository extends JpaRepository<DogCandidate, Long> {

    DogCandidate findDogCandidateById(Long id);

    DogCandidate findDogCandidateByUserName(String userName);

    void deleteDogCandidateByUserName(String userName);
}
