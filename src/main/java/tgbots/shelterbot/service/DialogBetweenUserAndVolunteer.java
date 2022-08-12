package tgbots.shelterbot.service;

import com.pengrad.telegrambot.model.Message;

import java.util.List;

public interface DialogBetweenUserAndVolunteer {

    List<Long> firstMessage(Long id, String userName);
}
