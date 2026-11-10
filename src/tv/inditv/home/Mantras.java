package tv.inditv.home;

/**
 * The verse shown on the Darshan screensaver with each wallpaper: the original (Sanskrit,
 * Awadhi or Gujarati), where it comes from, and its meaning in English. Matched by the start of
 * the wallpaper's file name, so a new wallpaper named e.g. "night-ganesha-xyz.jpg" can get its
 * own line here; anything unmatched gets a general one.
 */
final class Mantras {

    static final class Verse {
        final String text, source, meaning;

        Verse(String text, String source, String meaning) {
            this.text = text;
            this.source = source;
            this.meaning = meaning;
        }
    }

    private static final String[][] BY_WALLPAPER = {
            {"morning-hanuman", "जय हनुमान ज्ञान गुन सागर।\nजय कपीस तिहुँ लोक उजागर॥", "Hanuman Chalisa",
                    "Victory to Hanuman, ocean of wisdom and virtue, who lights up the three worlds."},
            {"morning-into-the-light", "असतो मा सद्गमय। तमसो मा ज्योतिर्गमय।\nमृत्योर्मा अमृतं गमय॥", "Brihadaranyaka Upanishad",
                    "Lead me from the unreal to the real, from darkness to light, from death to immortality."},
            {"morning-krishnas-gaze", "ॐ नमो भगवते वासुदेवाय", "Dvadashakshari mantra",
                    "I bow to Lord Vasudeva, Shri Krishna."},
            {"morning-wheel-of-dharma", "कर्मण्येवाधिकारस्ते\nमा फलेषु कदाचन।", "Bhagavad Gita 2.47",
                    "Your right is to your work alone, never to its fruits."},
            {"day-chariot-of-fire", "यदा यदा हि धर्मस्य\nग्लानिर्भवति भारत।", "Bhagavad Gita 4.7",
                    "Whenever dharma declines, O Bharata, I come forth."},
            {"day-sunset-together", "ॐ उमामहेश्वराभ्यां नमः", "Shiva and Parvati",
                    "Salutations to Uma and Maheshvara, together as one."},
            {"day-swing-in-the-shadows", "શ્રી કૃષ્ણઃ શરણં મમ", "Ashtakshar mantra",
                    "Shri Krishna is my refuge."},
            {"day-lost-in-her-eyes", "हरे कृष्ण हरे कृष्ण कृष्ण कृष्ण हरे हरे।\nहरे राम हरे राम राम राम हरे हरे॥", "Maha mantra",
                    "The great chant of the names of Krishna and Rama."},
            {"night-moonlit-lotus-lake", "ॐ नमः शिवाय", "Panchakshari mantra",
                    "I bow to Shiva, the auspicious one."},
            {"night-starlit-nap", "ॐ शान्तिः शान्तिः शान्तिः", "Shanti mantra",
                    "Peace within, peace around, peace everywhere."},
            {"night-vishnu", "शान्ताकारं भुजगशयनं\nपद्मनाभं सुरेशम्।", "Vishnu Dhyana shloka",
                    "Serene in form, resting on the cosmic serpent, the lotus-naveled lord of the gods."},
    };

    private static final Verse DEFAULT = new Verse("જય શ્રી કૃષ્ણ", "", "Jai Shri Krishna.");

    private Mantras() { }

    /** The verse for a wallpaper source like "asset:wallpapers/night-starlit-nap.jpg". */
    static Verse forWallpaper(String src) {
        String name = src.substring(src.lastIndexOf('/') + 1);
        for (String[] v : BY_WALLPAPER) if (name.startsWith(v[0])) return new Verse(v[1], v[2], v[3]);
        return DEFAULT;
    }
}
