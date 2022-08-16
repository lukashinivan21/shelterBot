package tgbots.shelterbot.service.storeage;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RepositoryIds {

    private final Map<Long, Long> firstMapWithKeyUserId = new HashMap<>();

    private final Map<Long, Long> secondMapWithKeyVolunteerId = new HashMap<>();

    public void addIdsInFirstMap(Long userId, Long volId) {
        firstMapWithKeyUserId.put(userId, volId);
    }

    public void addIdsInSecondMap(Long volId, Long userId) {
        secondMapWithKeyVolunteerId.put(volId, userId);
    }

    public void deleteFromFirstMap(Long userId) {
        firstMapWithKeyUserId.remove(userId);
    }

    public void deleteFromSecondMap(Long volId) {
        secondMapWithKeyVolunteerId.remove(volId);
    }

    public Map<Long, Long> firstMap() {
        return firstMapWithKeyUserId;
    }

    public Map<Long, Long> secondMap() {
        return secondMapWithKeyVolunteerId;
    }
}
