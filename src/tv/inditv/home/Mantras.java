package tv.inditv.home;

/**
 * The shloka written out on the Darshan screensaver with each painting, and its meaning in one
 * short line. These are the ones sung in Gujarati homes every day. Matched by the start of the
 * wallpaper's file name, so a new painting named e.g. "night-ganesha-xyz.jpg" can get its own
 * line here. On festivals the day's own shloka comes every other painting.
 */
final class Mantras {

    static final class Verse {
        final String text, meaning;

        Verse(String text, String meaning) {
            this.text = text;
            this.meaning = meaning;
        }
    }

    private static final Verse VAKRATUNDA = new Verse(
            "वक्रतुण्ड महाकाय सूर्यकोटि समप्रभ।\nनिर्विघ्नं कुरु मे देव सर्वकार्येषु सर्वदा॥",
            "Lord Ganesha, radiant as a million suns, clear the way in all I do.");
    private static final Verse KARAGRE = new Verse(
            "कराग्रे वसते लक्ष्मीः करमध्ये सरस्वती।\nकरमूले तु गोविन्दः प्रभाते करदर्शनम्॥",
            "Lakshmi at the fingertips, Saraswati in the palm, Govinda at its base.");
    private static final Verse MANOJAVAM = new Verse(
            "मनोजवं मारुततुल्यवेगं\nजितेन्द्रियं बुद्धिमतां वरिष्ठम्।",
            "Swift as the mind, fast as the wind, wisest of the wise.");
    private static final Verse KRISHNAM_VANDE = new Verse(
            "वसुदेवसुतं देवं कंसचाणूरमर्दनम्।\nदेवकीपरमानन्दं कृष्णं वन्दे जगद्गुरुम्॥",
            "I bow to Krishna, joy of Devaki, teacher of the world.");
    private static final Verse GITA_2_47 = new Verse(
            "कर्मण्येवाधिकारस्ते\nमा फलेषु कदाचन।",
            "Your right is to your work alone, never to its fruits.");
    private static final Verse GITA_4_7 = new Verse(
            "यदा यदा हि धर्मस्य\nग्लानिर्भवति भारत।",
            "Whenever dharma fades, I come forth.");
    private static final Verse KARPURA_GAURAM = new Verse(
            "सदा वसन्तं हृदयारविन्दे\nभवं भवानीसहितं नमामि॥",
            "I bow to Shiva and Bhavani, who live in the lotus of the heart.");
    private static final Verse SHARANAM = new Verse(
            "श्री कृष्णः शरणं मम",
            "Shri Krishna is my refuge.");
    private static final Verse TVAMEVA = new Verse(
            "त्वमेव माता च पिता त्वमेव।\nत्वमेव बन्धुश्च सखा त्वमेव॥",
            "You are my mother and my father, my family and my friend.");
    private static final Verse TRYAMBAKAM = new Verse(
            "ॐ त्र्यम्बकं यजामहे\nसुगन्धिं पुष्टिवर्धनम्।",
            "We worship the three-eyed Lord, who nourishes all life.");
    private static final Verse KRISHNAYA = new Verse(
            "कृष्णाय वासुदेवाय हरये परमात्मने।\nप्रणतः क्लेशनाशाय गोविन्दाय नमो नमः॥",
            "Again and again I bow to Govinda, who takes away all sorrow.");
    private static final Verse SHANTAKARAM = new Verse(
            "शान्ताकारं भुजगशयनं\nपद्मनाभं सुरेशम्।",
            "Serene, resting on the cosmic serpent, lord of the gods.");

    // Festivals
    private static final Verse SARVA_MANGALA = new Verse(
            "सर्वमङ्गलमाङ्गल्ये शिवे सर्वार्थसाधिके।\nशरण्ये त्र्यम्बके गौरि नारायणि नमोऽस्तु ते॥",
            "Salutations, Mother, giver of every blessing.");
    private static final Verse SHUBHAM_KAROTI = new Verse(
            "शुभं करोति कल्याणम् आरोग्यं धनसम्पदा।\nशत्रुबुद्धिविनाशाय दीपज्योतिर्नमोऽस्तु ते॥",
            "I bow to the light of the lamp, bringer of goodness, health and plenty.");
    private static final Verse SURYA = new Verse(
            "जपाकुसुमसङ्काशं काश्यपेयं महाद्युतिम्।\nतमोऽरिं सर्वपापघ्नं प्रणतोऽस्मि दिवाकरम्॥",
            "I bow to the Sun, the great light that ends all darkness.");

    private static final Object[][] BY_WALLPAPER = {
            {"morning-hanuman", MANOJAVAM},
            {"morning-into-the-light", KARAGRE},
            {"morning-krishnas-gaze", KRISHNAM_VANDE},
            {"morning-wheel-of-dharma", GITA_2_47},
            {"day-chariot-of-fire", GITA_4_7},
            {"day-sunset-together", KARPURA_GAURAM},
            {"day-swing-in-the-shadows", SHARANAM},
            {"day-lost-in-her-eyes", TVAMEVA},
            {"night-moonlit-lotus-lake", TRYAMBAKAM},
            {"night-starlit-nap", KRISHNAYA},
            {"night-vishnu", SHANTAKARAM},
    };

    private Mantras() { }

    /** The shloka for a wallpaper source like "wallpapers/night-starlit-nap.jpg". */
    static Verse forWallpaper(String src) {
        String name = src.substring(src.lastIndexOf('/') + 1);
        for (Object[] v : BY_WALLPAPER) if (name.startsWith((String) v[0])) return (Verse) v[1];
        return VAKRATUNDA;
    }

    /** The day's own shloka on a festival, or null. */
    static Verse forDay(Panchang.Day day) {
        if (day.festival == null && day.mood() != Panchang.Mood.GARBA) return null;
        switch (day.mood()) {
            case GARBA: return SARVA_MANGALA;
            case DIYA: return SHUBHAM_KAROTI;
            case KITE: return SURYA;
            case SHIVA: return KARPURA_GAURAM;
            case KRISHNA:
            case HOLI: return KRISHNAM_VANDE;
            default: return null;
        }
    }
}
