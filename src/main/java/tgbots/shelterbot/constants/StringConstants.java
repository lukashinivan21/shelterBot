package tgbots.shelterbot.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, хранящий в себе строковые константы, используемые при функционировании бота
 */
public class StringConstants {

    public static final String START = "/start";
    public static final String FINISH = "/finish";

    public static final String VOL_IS_FREE = "Благодарю за качественно проведенную консультацию." +
            "\nТеперь можно передохнуть и попить чаю или кофе";

    public static final String DOG_SHELTER = "Приют для собак";
    public static final String CALLBACK_DOG = "Пёс";
    public static final String MESS_DOG = "Пользователь, вы выбрали питомник для собак...";

    public static final String CAT_SHELTER = "Приют для кошек";
    public static final String CALLBACK_CAT = "Кот";
    public static final String MESS_CAT = "Пользователь, вы выбрали питомник для кошек...";

    public static final String MESS_FOR_CAT_OWNER = "Так как вы уже выбрали питомник для кошек, то выбрать питомник для собак вы не можете...";

    public static final String MESS_FOR_DOG_OWNER = "Так как вы уже выбрали питомник для собак, то выбрать питомник для кошек вы не можете...";

    public static final String MAIN_GREETING = """
            Привет, пользователь. Я помогу тебе подобрать самого веселого и замечательного четвероного друга из нашего питомника. Выбери один из приютов: приют для собак или приют для кошек.\s
            Хочу предупредить, что после выбора одного из приютов выбрать другой уже не получится...
            Если же вы являетесь волонтером нашего питомника, то введите секретный пароль в ответном сообщении и следуйте дальнейшим указаниям.""";

    public static final String SECOND_GREETING = """
            Ниже вы можете видеть главное меню. Выберите один из его пунктов...
            В любой момент при необходимости возврата к главному меню введите команду /start""";


    public static final String MESS_DEFAULT = "Воспользуйтесь главным меню";

    public static final String NO_FIRST_VISIT = "Пользователь, я рад, что ты снова к нам вернулся. Выбери один из пунктов главного меню..." +
            "\nНапоминаю, что в любой момент при необходимости возврата к главному меню " +
            "введи команду /start";

    public static final String CALLBACK_BUTTON4 = "Консультация";
    public static final String MESS_FOR_BUTTON4 = "Минуточку, произвожу поиск свободного волонтера...";

    public static final String TEXT_BUTTON1 = "Узнать информацию о приюте";

    public static final String TEXT_BUTTON2 = "Как взять питомца из приюта";

    public static final String TEXT_BUTTON3 = "Прислать отчет о питомце";
    public static final String MESS_FOR_BUTTON3 = "Выберите один из пунктов меню для ознакомления с инструкциями и рекомендациями. Если же вы готовы отправить" +
            " отчет, то отправьте одно фото питомца, на котором его хорошо видно, с текстовым описанием (подписью). Все это должно быть отправлено одним сообщением.";

    public static final String TEXT_BUTTON4 = "Позвать волонтера";

    public static final String GREETING_STEP1 = "Привет, пользователь. В меню ниже ты можешь узнать основную информацию о приюте. Выбери один из пунктов...";

    public static final String TEXT_BUTTON5 = "Общая информация о приюте";
    public static final String CALLBACK_BUTTON5 = "Инфо";
    public static final String MESS_FOR_BUTTON5 = "Пользователь, в файле ты найдешь полную информацию о нашем приюте.";

    public static final String TEXT_BUTTON6 = "Расписание работы и адрес приюта";
    public static final String CALLBACK_BUTTON6 = "Адрес";
    public static final String MESS_FOR_BUTTON6 = "Пользователь, в файле ты найдешь информацию о графике работы приюта, адрес и номер телефона для связи";

    public static final String TEXT_BUTTON7 = "Техника безопасности на территории\nприюта";
    public static final String CALLBACK_BUTTON7 = "Рекомендация";
    public static final String MESS_FOR_BUTTON7 = "Пользователь, в файле ты найдешь необходимые рекомендации о технике безопасности на территории приюта.";

    public static final String TEXT_BUTTON8 = "Могу записать ваши контактные\nданные для связи";
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

    public static final String TEXT_BUTTON12 = "Рекомендации по обустройству\nдома для детеныша";
    public static final String CALLBACK_BUTTON12 = "Щенок";
    public static final String MESS_FOR_BUTTON12 = "В файле находятся рекомендации по обустройству дома для детеныша";

    public static final String TEXT_BUTTON13 = "Рекомендации по обустройству дома\nдля взрослого животного";
    public static final String CALLBACK_BUTTON13 = "Взрослая";
    public static final String MESS_FOR_BUTTON13 = "В файле находятся рекомендации по обустройству дома для взрослого животного";

    public static final String TEXT_BUTTON14 = "Рекомендации по обустройству дома\nдля животного с ограниченными возможностями";
    public static final String CALLBACK_BUTTON14 = "Обустройство";
    public static final String MESS_FOR_BUTTON14 = "В файле находятся рекомендации по обустройству дома для животного с ограниченными возможностями";

    public static final String TEXT_BUTTON15 = "Советы кинолога по первичному\nобщению с собакой";
    public static final String CALLBACK_BUTTON15 = "Советы";
    public static final String MESS_FOR_BUTTON15 = "В файле находятся рекомендации кинологов для успешного первичного знакомства с собакой";

    public static final String TEXT_BUTTON16 = "Список проверенных кинологов\nдля дальнейших консультаций";
    public static final String CALLBACK_BUTTON16 = "Кинологи";
    public static final String MESS_FOR_BUTTON16 = "В файле находится список кинологов, проверенных сотрудниками нашего питомника";

    public static final String TEXT_BUTTON17 = "Причины отказа в заборе питомца из приюта";
    public static final String CALLBACK_BUTTON17 = "Отказ";
    public static final String MESS_FOR_BUTTON17 = "В файле находятся основные причины отказа в заборе питомца из приюта";

    public static final String TEXT_BUTTON18 = "Форма ежедневного отчета";
    public static final String CALLBACK_BUTTON18 = "Форма";
    public static final String MESS_FOR_BUTTON18 = "Форма отчета";

    public static final String TEXT_BUTTON19 = "Инструкция по заполнению\nи отправке отчета";
    public static final String CALLBACK_BUTTON19 = "Заполнение";
    public static final String MESS_FOR_BUTTON19 = "Инструкция";

    public static final String DEFAULT_CAPTION = "Кажется произошла ошибка...";

    public static final String CHECK_MESS = "Проверьте правильность введенных данных или введите команду /start для отмены ввода контактных данных и " +
            "возврата в главное меню";

    public static final String SUCCESS_ADD = "Ваши контакты были успешно сохранены";


    public static final String REPORT_OK = "Ваш отчет полностью соотвествует требованиям. Полученные данные были успешно сохранены." +
            "\nНапоминаю, что отчеты необходимо отправлять ежедневно в течение всего испытательного срока";

    public static final String REPORT_NOT_FULL = "В отправленном отчете не хватает текстового описания. Будьте внимательнее! И в ответном" +
            " сообщении отправьте фото вместе с текстовым описанием одним сообщением";

    public static final String REPORT_WITHOUT_PHOTO = "В отправленном отчете не хватает фото. Будьте внимательнее! И в ответном" +
            " сообщении отправьте фото вместе с текстовым описанием одним сообщением";

    public static final String ERROR = "Произошла ошибка. Попробуйте отправить отчет позднее...";

    private static final String[] callBacks = {CALLBACK_BUTTON5, CALLBACK_BUTTON6, CALLBACK_BUTTON7, CALLBACK_BUTTON9, CALLBACK_BUTTON10, CALLBACK_BUTTON11, CALLBACK_BUTTON12,
            CALLBACK_BUTTON13, CALLBACK_BUTTON14, CALLBACK_BUTTON15, CALLBACK_BUTTON16, CALLBACK_BUTTON17, CALLBACK_BUTTON18, CALLBACK_BUTTON19};

    public static final List<String> LIST_CALLBACKS = new ArrayList<>(List.of(callBacks));

    public static final String MENTION_TO_SEND_REPORT = "Приветствую, пользователь. Я заметил, что с момента отправки тобой последнего отчета прошло более одного дня." +
            "\nХочу напомнить, что в течение всего испытательного срока отчеты необходимо отправлять каждый день. Это является одним из условий успешного прохождения " +
            "испытательного срока, поэтому не забудь отправить отчет в ближайшее время";

    public static final String VOLUNTEER = "волонтер3.0";

    public static final String SUCCESS_ADD_VOLUNTEER = "Вы были успешно добавлены в список волонтеров нашего приюта. При желании в ответном сообщении введите" +
            " свое имя, например \"Михаил\" (без кавычек), чтобы я знал как можно к вам обращаться";

    public static final String CONGRATULATION_VOL = "Ваше имя было успешно сохранено. При необходимости я буду с вами связываться, если клиентам питомника потребуется" +
            " какая-либо консультация";

    public static String msgToVol(String userName) {
        return String.format("""
                Пользователю с ником @%s нужна консультация. Прошу вас связаться с ним в ближайшее время.
                Первый способ связи с пользователем: найти его по указанному нику.
                Второй способ связи: написать мне сообщение - и я отправлю его пользователю.
                После завершения консультации с пользователем прошу вас отправить мне команду /finish. Иначе я не смогу отпустить вас""", userName);
    }

    public static String mentionToVol1(String dogCandidates, String catCandidates) {
        return String.format("""
                Следующие пользователи, взявшие из приюта питомцев себе домой и находящиеся на испытательном сроке, не присылают отчеты уже более двух дней.
                Приют для собак: %s.
                Приют для кошек: %s.
                Прошу связаться с данными пользователями напрямую для выяснения ситуации.
                Информация о пользователе представлена в следуюшем виде: номер id чата, @ник пользователя в телеграм, имя пользователя, номер телефона.
                Null обозначает, что информация по данному пункту отсутствует.""", dogCandidates, catCandidates);
    }

    public static String mentionToVol2(String dogCandidates, String catCandidates) {
        return String.format("""
                Необходимо принять решение по следующим пользователям, проходящим испытательный срок.
                Приют для собак: %s.
                Приют для кошек: %s.
                Информация о пользователе представлена в следуюшем виде: номер id чата, @ник пользователя в телеграм, имя пользователя, номер телефона.
                Null обозначает, что информация по данному пункту отсутствует.
                После принятия решения прошу прислать мне отдельное сообщение по каждому пользователю, сообщения могут быть трех видов:
                - если пользователь успешно прошел испытательный срок: "номер id чата SUCCESS", например "478512469 SUCCESS";
                - если пользователь не прошел испытательный срок: "номер id чата LOSE", например "478512469 LOSE";
                - если было принято решение о продлении испытательного срока: "номер id чата LONG XX", например "478512469 LONG 12",
                где XX - количество дней на которые был продлен испытательный срок.
                Сообщения отправлять без кавычек""", dogCandidates, catCandidates);
    }

    public static final String CON_USER = "Поздравляю вас, пользователь. Вы успешно прошли испытательный срок";

    public static final String LOSE = "К сожалению вы не прошли испытательный срок";

    public static final String NOTICE = """
            Дорогой усыновитель! Мы заметили, что ты заполняешь отчет не так подробно, как необходимо.
            Пожалуйста, подойди ответственнее к этому занятию. В противном случае, волонтеры приюта будут обязаны самолично проверять
            условия содержания питомца.""";


    public static String periodProLong(String days) {
        return String.format("Ваш испытательный срок был продлен на %s дней", days);
    }

}
