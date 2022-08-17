package tgbots.shelterbot.constants;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;


import static tgbots.shelterbot.constants.Emoji.CHECK;
import static tgbots.shelterbot.constants.StringConstants.*;

/**
 * Класс, хранящий в себе клавиатуры, используемые ботом.
 */
public class Keyboards {

    /**
     * Метод для создания клавиатуры, необходимой для выбора приюта: приют для собак или приют для кошек
     * @return {@InlineKeyboardMarkup}
     */
    public static InlineKeyboardMarkup chooseShelter() {

        InlineKeyboardMarkup keyboard1 = new InlineKeyboardMarkup();

        InlineKeyboardButton dogButton = new InlineKeyboardButton(CHECK + " " + DOG_SHELTER).callbackData(CALLBACK_DOG);
        InlineKeyboardButton catButton = new InlineKeyboardButton(CHECK + " " + CAT_SHELTER).callbackData(CALLBACK_CAT);

        keyboard1.addRow(dogButton);
        keyboard1.addRow(catButton);

        return keyboard1;
    }

    /**
     * Метод для создания клавиатуры главного меню
     * @return {@ReplyKeyboardMarkup}
     */
    public static ReplyKeyboardMarkup mainKeyboard() {

        String[][] rows = new String[4][1];

        rows[0][0] = TEXT_BUTTON1;
        rows[1][0] = TEXT_BUTTON2;
        rows[2][0] = TEXT_BUTTON3;
        rows[3][0] = TEXT_BUTTON4;

        return new ReplyKeyboardMarkup(rows, true, false, true);
    }

    /**
     * Метод для создания клавиатуры, содержащей кнопки для получения основной информации о приюте
     * @return {@InlineKeyboardMarkup}
     */
    public static InlineKeyboardMarkup keyboard1() {

        InlineKeyboardMarkup keyboard2 = new InlineKeyboardMarkup();

        InlineKeyboardButton button5 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON5).callbackData(CALLBACK_BUTTON5);
        InlineKeyboardButton button6 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON6).callbackData(CALLBACK_BUTTON6);
        InlineKeyboardButton button7 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON7).callbackData(CALLBACK_BUTTON7);
        InlineKeyboardButton button8 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON8).callbackData(CALLBACK_BUTTON8);
        InlineKeyboardButton button9 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON4).callbackData(CALLBACK_BUTTON4);

        keyboard2.addRow(button5);
        keyboard2.addRow(button6);
        keyboard2.addRow(button7);
        keyboard2.addRow(button8);
        keyboard2.addRow(button9);

        return keyboard2;
    }

    /**
     * Метод для создания клавиатуры,содержащей кнопки для получения информации необходимой для
     * получения питомца из приюта для собак
     * @return {@InlineKeyboardMarkup}
     */
    public static InlineKeyboardMarkup keyboard2Dog() {

        InlineKeyboardMarkup keyboard3 = new InlineKeyboardMarkup();

        InlineKeyboardButton button9 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON9).callbackData(CALLBACK_BUTTON9);
        InlineKeyboardButton button10 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON10).callbackData(CALLBACK_BUTTON10);
        InlineKeyboardButton button11 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON11).callbackData(CALLBACK_BUTTON11);
        InlineKeyboardButton button12 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON12).callbackData(CALLBACK_BUTTON12);
        InlineKeyboardButton button13 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON13).callbackData(CALLBACK_BUTTON13);
        InlineKeyboardButton button14 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON14).callbackData(CALLBACK_BUTTON14);
        InlineKeyboardButton button15 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON15).callbackData(CALLBACK_BUTTON15);
        InlineKeyboardButton button16 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON16).callbackData(CALLBACK_BUTTON16);
        InlineKeyboardButton button17 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON17).callbackData(CALLBACK_BUTTON17);
        InlineKeyboardButton button18 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON8).callbackData(CALLBACK_BUTTON8);
        InlineKeyboardButton button19 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON4).callbackData(CALLBACK_BUTTON4);

        keyboard3.addRow(button9);
        keyboard3.addRow(button10);
        keyboard3.addRow(button11);
        keyboard3.addRow(button12);
        keyboard3.addRow(button13);
        keyboard3.addRow(button14);
        keyboard3.addRow(button15);
        keyboard3.addRow(button16);
        keyboard3.addRow(button17);
        keyboard3.addRow(button18);
        keyboard3.addRow(button19);

        return keyboard3;
    }

    /**
     * Метод для создания клавиатуры, содержащей кнопки для получения информации необходимой для
     * получения питомца из приюта для кошек
     * @return {@InlineKeyboardMarkup}
     */
    public static InlineKeyboardMarkup keyboard2Cat() {

        InlineKeyboardMarkup keyboard4 = new InlineKeyboardMarkup();

        InlineKeyboardButton button9 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON9).callbackData(CALLBACK_BUTTON9);
        InlineKeyboardButton button10 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON10).callbackData(CALLBACK_BUTTON10);
        InlineKeyboardButton button11 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON11).callbackData(CALLBACK_BUTTON11);
        InlineKeyboardButton button12 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON12).callbackData(CALLBACK_BUTTON12);
        InlineKeyboardButton button13 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON13).callbackData(CALLBACK_BUTTON13);
        InlineKeyboardButton button14 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON14).callbackData(CALLBACK_BUTTON14);
        InlineKeyboardButton button17 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON17).callbackData(CALLBACK_BUTTON17);
        InlineKeyboardButton button18 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON8).callbackData(CALLBACK_BUTTON8);
        InlineKeyboardButton button19 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON4).callbackData(CALLBACK_BUTTON4);

        keyboard4.addRow(button9);
        keyboard4.addRow(button10);
        keyboard4.addRow(button11);
        keyboard4.addRow(button12);
        keyboard4.addRow(button13);
        keyboard4.addRow(button14);
        keyboard4.addRow(button17);
        keyboard4.addRow(button18);
        keyboard4.addRow(button19);

        return keyboard4;
    }

    /**
     * Метод для создания клавиатуры, которая может пригодится пользователю при прохождении испытательного срока.
     * @return {@InlineKeyboardMarkup}
     */
    public static InlineKeyboardMarkup keyboard3() {

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        InlineKeyboardButton button20 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON18).callbackData(CALLBACK_BUTTON18);
        InlineKeyboardButton button21 = new InlineKeyboardButton(CHECK + " " + TEXT_BUTTON19).callbackData(CALLBACK_BUTTON19);

        keyboard.addRow(button20);
        keyboard.addRow(button21);

        return keyboard;
    }


}
