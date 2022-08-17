package tgbots.shelterbot.models;

/**
 * Enum, хранящий в себе 4 состояния бота при взаимодействии с пользователем
 */
public enum BotState {

    // Взаимодействие через нажатие кнопок клавиатур
    DIALOG,

    // Получение от пользователя информации: номер телефона, имя и т.д.
    GET_INFO,

    // Прлучение отчетов от пользователя, находящегося на испытательном сроке
    GET_REPORT,

    // Консультация пользователя волонтером
    DIALOG_WITH_VOL
}
