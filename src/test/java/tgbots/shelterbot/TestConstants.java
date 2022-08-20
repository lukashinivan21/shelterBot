package tgbots.shelterbot;

import tgbots.shelterbot.models.*;

import java.time.LocalDate;
import java.time.Month;

public class TestConstants {

    public static DogCandidate dogCandidate1 = new DogCandidate(12L, "Alex", "alex91", "+79687523141", BotState.DIALOG.toString());
    public static DogCandidate dogCandidate2 = new DogCandidate(37L, "John", "j_oe4r", "+79210587310", BotState.DIALOG.toString());
    public static DogCandidate dogCandidate3 = new DogCandidate(68L, "Steve", "st17", "+79496301275", BotState.DIALOG.toString());
    public static DogCandidate dogCandidate4 = new DogCandidate(25L, "Jim", "jimmy_rt8", "+78321472315", BotState.DIALOG.toString());

    public static CatCandidate catCandidate1 = new CatCandidate(14L, "Anna", "ani_777", "+79621432020", BotState.DIALOG.toString());
    public static CatCandidate catCandidate2 = new CatCandidate(29L, "Kate", "katy16", "+79534120876", BotState.DIALOG.toString());
    public static CatCandidate catCandidate3 = new CatCandidate(41L, "Elena", "helen8", "+79253103791", BotState.DIALOG.toString());
    public static CatCandidate catCandidate4 = new CatCandidate(70L, "Irina", "ira74", "+79201423017", BotState.DIALOG.toString());

    public static Volunteer volunteer1 = new Volunteer(11L, "rom99", "Roman", true);
    public static Volunteer volunteer2 = new Volunteer(23L, "me_gre12", "Greg", true);
    public static Volunteer volunteer3 = new Volunteer(45L, "jes_LL", "Jessica", true);
    public static Volunteer volunteer4 = new Volunteer(52L, "dani_11", "Daniel", true);

    private static final LocalDate date1 = LocalDate.of(2022, Month.JUNE, 8);
    private static final byte[] array1 = new byte[6322];
    public static CatReport catReport1 = new CatReport(5L, "Cat is good", date1, "/!allReports/catReport1", 12586L, array1);

    private static final LocalDate date2 = LocalDate.of(2022, Month.JULY, 16);
    private static final byte[] array2 = new byte[5437];
    public static CatReport catReport2 = new CatReport(15L, "All is ok", date2, "/!allReports/catReport2", 11287L, array2);

    private static final LocalDate date3 = LocalDate.of(2022, Month.AUGUST, 2);
    private static final byte[] array3 = new byte[4238];
    public static CatReport catReport3 = new CatReport(26L, "Cat is ill", date3, "/!allReports/catReport3", 10267L, array3);


    public static DogReport dogReport1 = new DogReport(125L, "Dog is good", date1, "/!allReports/dogReport1", 12321L, array1);
    public static DogReport dogReport2 = new DogReport(138L, "All is ok", date2, "/!allReports/dogReport2", 11746L, array2);
    public static DogReport dogReport3 = new DogReport(149L, "Dog is ill", date3, "/!allReports/dogReport3", 10335L, array3);


    public static final int INDEX_1 = 1;
    public static final int INDEX_2 = 2;
    public static final int INDEX_3 = 3;
    public static final int INDEX_0 = 0;
}
