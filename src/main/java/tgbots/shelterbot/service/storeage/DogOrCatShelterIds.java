package tgbots.shelterbot.service.storeage;

import org.springframework.stereotype.Repository;
import tgbots.shelterbot.models.BotState;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс, хранящий id пользователей, в зависисмости от того каким приютом
 * пользователь интересуется: приютом для собак или кошек.
 */
@Repository
public class DogOrCatShelterIds {

    private final Map<Long, BotState> idsDogShelter = new HashMap<>();

    private final Map<Long, BotState> idsCatShelter = new HashMap<>();

    public void addUpdateToDogShelter(Long chatId, BotState botState) {
        idsDogShelter.put(chatId, botState);
    }

    public void removeFromDogShelter(Long chatId) {
        idsDogShelter.remove(chatId);
        idsDogShelter.keySet().remove(chatId);
    }

    public boolean dogShelterContainsId(Long chatId) {
        return idsDogShelter.containsKey(chatId);
    }

    public void addUpdateToCatShelter(Long chatId, BotState botState) {
        idsCatShelter.put(chatId, botState);
    }


    public void removeCatShelter(Long chatId) {
        idsCatShelter.remove(chatId);
        idsCatShelter.keySet().remove(chatId);
    }

    public boolean catShelterContainsId(Long chatId) {
        return idsCatShelter.containsKey(chatId);
    }

}
