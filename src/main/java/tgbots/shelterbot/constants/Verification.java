package tgbots.shelterbot.constants;

import java.util.regex.Pattern;

/**
 * Класс, содержащий регулярное выражение для проверки правильности номера телефона при обновлении данных пользователя
 */
public class Verification {

    public static final Pattern CHECK_PHONE_NUMBER = Pattern.compile("[+0-9]{10,}");
}
