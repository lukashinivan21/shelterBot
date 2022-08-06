package tgbots.shelterbot.constants;

import java.util.ArrayList;
import java.util.List;

public class StringConstants {

    public static final String START = "/start";

    public static final String PHONE_NUMBER = "/myPhoneNumber";

    public static final String DOG_SHELTER = "Приют для собак";
    public static final String CALLBACK_DOG = "Пёс";
    public static final String MESS_DOG = "Пользователь, вы выбрали питомник для собак...";

    public static final String CAT_SHELTER = "Приют для кошек";
    public static final String CALLBACK_CAT = "Кот";
    public static final String MESS_CAT = "Пользователь, вы выбрали питомник для кошек...";

    public static final String MESS_FOR_CAT_OWNER = "Так как вы уже выбрали питомник для кошек, то выбрать питомник для собак вы не можете...";

    public static final String MESS_FOR_DOG_OWNER = "Так как вы уже выбрали питомник для собак, то выбрать питомник для кошек вы не можете...";

    public static final String MAIN_GREETING = "Привет, пользователь. Я помогу тебе подобрать самого веселого и замечательного четвероного друга из нашего питомника." +
            " Выбери один из пунктов главного меню. \nВ любой момент при необходимости возврата к главному меню " +
            "введи команду \"/start\" (без кавычек)." +
            "\nВ списке ниже представлены другие мои функции: \n/myPhoneNumber - могу напомнить введенный тобой номер твоего телефона для связи" +
            "\n/";

    public static final String MESS_DEFAULT = "Воспользуйтесь главным меню";

    public static final String NO_FIRST_VISIT = "Пользователь, я рад, что ты снова к нам вернулся. Выбери один из пунктов главного меню..." +
            "\nНапоминаю, что в любой момент при необходимости возврата к главному меню " +
            "введи команду \"/start\" (без кавычек).";

    public static final String CALLBACK_BUTTON4 = "Волонтер";
    public static final String MESS_FOR_BUTTON4 = "Волонтер подключиться к чату в ближайшее время...";

    public static final String TEXT_BUTTON1 = "Узнать информацию о приюте";

    public static final String TEXT_BUTTON2 = "Как взять собаку из приюта";

    public static final String TEXT_BUTTON3 = "Прислать отчет о питомце";
    public static final String MESS_FOR_BUTTON3 = "Выберите один из пунктов меню для ознакомления с инструкциями и рекомендациями. Если же вы готовы отправить" +
            " отчет, то отправьте фото питомца с текстовым описанием одним сообщением";

    public static final String TEXT_BUTTON4 = "Позвать волонтера";

    public static final String GREETING_STEP1 = "Привет, пользователь. В меню ниже ты можешь узнать основную информацию о приюте. Выбери один из пунктов...";

    public static final String TEXT_BUTTON5 = "Общая информация о приюте";
    public static final String CALLBACK_BUTTON5 = "Инфо";
    public static final String MESS_FOR_BUTTON5 = "Пользователь, в файле ты найдешь полную информацию о нашем приюте.";

    public static final String TEXT_BUTTON6 = "Расписание работы и адрес приюта";
    public static final String CALLBACK_BUTTON6 = "Адрес";
    public static final String MESS_FOR_BUTTON6 = "Пользователь, в файле ты найдешь информацию о графике работы приюта, адрес и номер телефона для связи";

    public static final String TEXT_BUTTON7 = "Техника безопасности на территории приюта";
    public static final String CALLBACK_BUTTON7 = "Рекомендация";
    public static final String MESS_FOR_BUTTON7 = "Пользователь, в файле ты найдешь необходимые рекомендации о технике безопасности на территории приюта.";

    public static final String TEXT_BUTTON8 = "Могу записать ваши контактные данные для связи";
    public static final String CALLBACK_BUTTON8 = "Контакт";
    public static final String MESS_FOR_BUTTON8 = "В ответном сообщении введи свои контакты в виде: \"+xxxxxxxxxxx ФИО\", где x - цифра от 0 до 9. " +
            "Ответное сообщение без кавычек." + " Номер телефона и ФИО обязательны для заполнения. ФИО может содержать фамилию и имя или же только имя";

    public static final String GREETING_STEP2 = "Привет, пользователь. Меню ниже поможет тебе получить ответы на наиболее популярные вопросы. Выбери один из пунктов...";

    public static final String TEXT_BUTTON9 = "Правила знакомства с питомцем";
    public static final String CALLBACK_BUTTON9 = "Знакомство";
    public static final String MESS_FOR_BUTTON9 = "В файле находятся правила знакомства с питомцем";

    public static final String TEXT_BUTTON10 = "Список необходимых документов";
    public static final String CALLBACK_BUTTON10 = "Список";
    public static final String MESS_FOR_BUTTON10 = "В файле находится перечень документов, необходимых для того, чтобы забрать питомца из приюта";

    public static final String TEXT_BUTTON11 = "Рекомендации по транспортировке животного";
    public static final String CALLBACK_BUTTON11 = "Транспорт";
    public static final String MESS_FOR_BUTTON11 = "В файле находятся рекомендации для безопасной транспортировки животного";

    public static final String TEXT_BUTTON12 = "Рекомендации по обустройству дома для детеныша";
    public static final String CALLBACK_BUTTON12 = "Щенок";
    public static final String MESS_FOR_BUTTON12 = "В файле находятся рекомендации по обустройству дома для детеныша";

    public static final String TEXT_BUTTON13 = "Рекомендации по обустройству дома взрослого животного";
    public static final String CALLBACK_BUTTON13 = "Взрослая";
    public static final String MESS_FOR_BUTTON13 = "В файле находятся рекомендации по обустройству дома для взрослого животного";

    public static final String TEXT_BUTTON14 = "Рекомендации по обустройству дома для животного с ограниченными возможностями";
    public static final String CALLBACK_BUTTON14 = "Обустройство";
    public static final String MESS_FOR_BUTTON14 = "В файле находятся рекомендации по обустройству дома для животного с ограниченными возможностями";

    public static final String TEXT_BUTTON15 = "Советы кинолога по первичному общению с собакой";
    public static final String CALLBACK_BUTTON15 = "Советы";
    public static final String MESS_FOR_BUTTON15 = "В файле находятся рекомендации кинологов для успешного первичного знакомства с собакой";

    public static final String TEXT_BUTTON16 = "Список проверенных кинологов для дальнейших консультаций";
    public static final String CALLBACK_BUTTON16 = "Кинологи";
    public static final String MESS_FOR_BUTTON16 = "В файле находится список кинологов, проверенных сотрудниками нашего питомника";

    public static final String TEXT_BUTTON17 = "Причины отказа в заборе питомца из приюта";
    public static final String CALLBACK_BUTTON17 = "Отказ";
    public static final String MESS_FOR_BUTTON17 = "В файле находятся основные причины отказа в заборе питомца из приюта";

    public static final String TEXT_BUTTON18 = "Форма ежедневного отчета";
    public static final String CALLBACK_BUTTON18 = "Форма";
    public static final String MESS_FOR_BUTTON18 = "Форма отчета";

    public static final String TEXT_BUTTON19 = "Инструкция по заполнению и отправки отчета";
    public static final String CALLBACK_BUTTON19 = "Заполнение";
    public static final String MESS_FOR_BUTTON19 = "Инструкция";

    public static final String CHECK_MESS = "Проверьте правильность введенных данных или введите команду \"/start\" для отмены ввода контактных данных и " +
            "возврата в главное меню";

    public static final String SUCCESS_ADD = "Ваши контакты были успешно сохранены";

//    public static final String REPORT_OK_MESS = "Благодарю. Полученное текстовое описание было успешно сохранено. Теперь отправьте фото питомца.";

    public static final String REPORT_OK = "Ваш отчет полностью соотвествует требованиям. Полученные данные были успешно сохранены." +
            "\nНапоминаю, что отчеты необходимо отправлять ежедневно в течение всего испытательного срока";

//    public static final String REPORT_OK_PHOTO = "Ваше фото было успешно получено. Напоминаю, что отчеты необходимо отправлять ежедневно в течение всего испытательного срока";

    public static final String REPORT_NOT_FULL = "В отправленном отчете не хватает либо фото, либо текстового описания. Будьте внимательнее! И в ответном" +
            " сообщении отправьте фото вместе с текстовым описанием одним сообщением";

    private static final String[] callBacks = {CALLBACK_BUTTON5, CALLBACK_BUTTON6, CALLBACK_BUTTON7, CALLBACK_BUTTON9, CALLBACK_BUTTON10, CALLBACK_BUTTON11, CALLBACK_BUTTON12,
            CALLBACK_BUTTON13, CALLBACK_BUTTON14, CALLBACK_BUTTON15, CALLBACK_BUTTON16, CALLBACK_BUTTON17, CALLBACK_BUTTON18, CALLBACK_BUTTON19};

    public static final List<String> LIST_CALLBACKS = new ArrayList<>(List.of(callBacks));

}
