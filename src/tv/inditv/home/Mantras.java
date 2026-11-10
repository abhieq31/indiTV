package tv.inditv.home;

/**
 * The shloka written out on the Darshan screensaver with each painting, with its meaning in
 * Gujarati and in English. Only verses that are chanted in homes everywhere, each chosen for
 * what its painting shows; where no such verse fits, one from the Bhagavad Gita. Matched by the
 * start of the wallpaper's file name, so a new painting named e.g. "night-ganesha-xyz.jpg" can
 * get its own line here.
 */
final class Mantras {

    static final class Verse {
        final String text, gu, en;

        Verse(String text, String gu, String en) {
            this.text = text;
            this.gu = gu;
            this.en = en;
        }
    }

    // Hanuman in meditation
    private static final Verse HANUMAN_CHALISA = new Verse(
            "जय हनुमान ज्ञान गुन सागर।\nजय कपीस तिहुँ लोक उजागर॥",
            "જ્ઞાન અને ગુણના સાગર હનુમાનજીનો જય; ત્રણે લોકને અજવાળનાર કપીશ્વરનો જય.",
            "Victory to Hanuman, ocean of wisdom and virtue, light of the three worlds.");
    // A figure rising into the morning sun
    private static final Verse GAYATRI = new Verse(
            "ॐ भूर्भुवः स्वः तत्सवितुर्वरेण्यं।\nभर्गो देवस्य धीमहि धियो यो नः प्रचोदयात्॥",
            "એ પરમ તેજસ્વી સવિતા દેવનું અમે ધ્યાન કરીએ છીએ; તે અમારી બુદ્ધિને સન્માર્ગે પ્રેરે.",
            "We meditate on the radiant light of the divine; may it guide our minds.");
    // Krishna standing on the field of Kurukshetra
    private static final Verse GITA_4_8 = new Verse(
            "परित्राणाय साधूनां विनाशाय च दुष्कृताम्।\nधर्मसंस्थापनार्थाय सम्भवामि युगे युगे॥",
            "સજ્જનોના રક્ષણ, દુષ્ટોના નાશ અને ધર્મની સ્થાપના માટે હું યુગે યુગે પ્રગટ થાઉં છું.",
            "To protect the good and restore dharma, I come in every age.  — Gita 4.8");
    // The raised chariot wheel
    private static final Verse GITA_2_47 = new Verse(
            "कर्मण्येवाधिकारस्ते मा फलेषु कदाचन।\nमा कर्मफलहेतुर्भूर्मा ते सङ्गोऽस्त्वकर्मणि॥",
            "તારો અધિકાર માત્ર કર્મ કરવામાં છે, તેના ફળમાં કદી નહીં.",
            "Your right is to the work alone, never to its fruits.  — Gita 2.47");
    // The chariot of Krishna and Arjuna
    private static final Verse GITA_4_7 = new Verse(
            "यदा यदा हि धर्मस्य ग्लानिर्भवति भारत।\nअभ्युत्थानमधर्मस्य तदात्मानं सृजाम्यहम्॥",
            "હે ભારત! જ્યારે જ્યારે ધર્મની હાનિ અને અધર્મની વૃદ્ધિ થાય છે, ત્યારે હું પ્રગટ થાઉં છું.",
            "Whenever dharma fades and adharma rises, I come forth.  — Gita 4.7");
    // Shiva and Parvati at sunset
    private static final Verse KARPURA_GAURAM = new Verse(
            "कर्पूरगौरं करुणावतारं संसारसारं भुजगेन्द्रहारम्।\nसदा वसन्तं हृदयारविन्दे भवं भवानीसहितं नमामि॥",
            "કપૂર જેવા ગૌર, કરુણાના અવતાર, હૃદયકમળમાં સદા વસનાર શિવ-પાર્વતીને નમન.",
            "I bow to Shiva, pure as camphor, compassion itself, who lives in the heart with Bhavani.");
    // Radha and Krishna on the swing
    private static final Verse SHARANAM = new Verse(
            "श्री कृष्णः शरणं मम",
            "શ્રીકૃષ્ણ જ મારું શરણ છે.",
            "Shri Krishna is my refuge.");
    // Radha's eyes, Krishna within them
    private static final Verse MAHA_MANTRA = new Verse(
            "हरे कृष्ण हरे कृष्ण कृष्ण कृष्ण हरे हरे।\nहरे राम हरे राम राम राम हरे हरे॥",
            "હરિ, કૃષ્ણ અને રામના નામનો મહામંત્ર.",
            "The great chant of the names of Hari, Krishna and Rama.");
    // Shiva and Parvati on the moonlit lake
    private static final Verse TRYAMBAKAM = new Verse(
            "ॐ त्र्यम्बकं यजामहे सुगन्धिं पुष्टिवर्धनम्।\nउर्वारुकमिव बन्धनान्मृत्योर्मुक्षीय माऽमृतात्॥",
            "સૌનું પોષણ કરનાર ત્રિનેત્રધારી શિવને અમે પૂજીએ છીએ; તે અમને મૃત્યુના બંધનથી મુક્ત કરે.",
            "We worship the three-eyed Lord who nourishes all; may he free us from death.");
    // Little Krishna and little Shiva asleep under the stars
    private static final Verse TVAMEVA = new Verse(
            "त्वमेव माता च पिता त्वमेव।\nत्वमेव बन्धुश्च सखा त्वमेव॥",
            "તમે જ માતા છો, તમે જ પિતા; તમે જ બંધુ અને તમે જ સખા.",
            "You are my mother and my father, my family and my friend.");
    // Vishnu resting among the stars
    private static final Verse SHANTAKARAM = new Verse(
            "शान्ताकारं भुजगशयनं पद्मनाभं सुरेशम्।\nविश्वाधारं गगनसदृशं मेघवर्णं शुभाङ्गम्॥",
            "શાંત સ્વરૂપ, શેષશાયી, પદ્મનાભ, દેવોના ઈશ્વર અને વિશ્વના આધાર વિષ્ણુને વંદન.",
            "Serene, resting on the serpent, lotus-naveled lord who holds up the universe.");
    // Any painting without a line of its own
    private static final Verse VAKRATUNDA = new Verse(
            "वक्रतुण्ड महाकाय सूर्यकोटि समप्रभ।\nनिर्विघ्नं कुरु मे देव सर्वकार्येषु सर्वदा॥",
            "કરોડો સૂર્ય જેવા તેજસ્વી શ્રી ગણેશ, મારાં સર્વ કાર્યો સદા નિર્વિઘ્ન કરો.",
            "Lord Ganesha, bright as a million suns, clear the way in all I do.");

    // Festivals: shown once, with the first painting.
    private static final Verse SARVA_MANGALA = new Verse(
            "सर्वमङ्गलमाङ्गल्ये शिवे सर्वार्थसाधिके।\nशरण्ये त्र्यम्बके गौरि नारायणि नमोऽस्तु ते॥",
            "સર્વ મંગળ કરનારી, સર્વ મનોરથ પૂર્ણ કરનારી મા નારાયણી, તમને નમસ્કાર.",
            "Salutations, Mother Narayani, giver of every blessing.");
    private static final Verse SHUBHAM_KAROTI = new Verse(
            "शुभं करोति कल्याणम् आरोग्यं धनसम्पदा।\nशत्रुबुद्धिविनाशाय दीपज्योतिर्नमोऽस्तु ते॥",
            "શુભ, કલ્યાણ, આરોગ્ય અને ધનસંપત્તિ આપનારી દીપજ્યોતિને નમસ્કાર.",
            "I bow to the light of the lamp, bringer of goodness, health and plenty.");
    private static final Verse SURYA = new Verse(
            "जपाकुसुमसङ्काशं काश्यपेयं महाद्युतिम्।\nतमोऽरिं सर्वपापघ्नं प्रणतोऽस्मि दिवाकरम्॥",
            "અંધકારનો નાશ કરનાર મહાતેજસ્વી સૂર્યનારાયણને હું પ્રણામ કરું છું.",
            "I bow to the Sun, the great light that ends all darkness.");

    private static final Object[][] BY_WALLPAPER = {
            {"morning-hanuman", HANUMAN_CHALISA},
            {"morning-into-the-light", GAYATRI},
            {"morning-krishnas-gaze", GITA_4_8},
            {"morning-wheel-of-dharma", GITA_2_47},
            {"day-chariot-of-fire", GITA_4_7},
            {"day-sunset-together", KARPURA_GAURAM},
            {"day-swing-in-the-shadows", SHARANAM},
            {"day-lost-in-her-eyes", MAHA_MANTRA},
            {"night-moonlit-lotus-lake", TRYAMBAKAM},
            {"night-starlit-nap", TVAMEVA},
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
            default: return null; // Shiva's and Krishna's days: their paintings already carry their verses
        }
    }
}
