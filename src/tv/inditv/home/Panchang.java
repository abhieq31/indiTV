package tv.inditv.home;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * The Gujarati calendar (Vikram Samvat, amanta months from Kartak) worked out on the TV from the
 * Sun and Moon, so it never needs updating: today's tithi ("Kartak Sud Agiyaras"), the month,
 * the year, and the festivals Gujarati families keep.
 *
 * The tithi of a day is the one at sunrise in Ahmedabad, as in a printed panchang. Diwali and
 * Maha Shivratri go by the evening and midnight, as they are kept. Positions use Meeus' short
 * series (good to a few hundredths of a degree), so a tithi that changes within minutes of
 * sunrise can, rarely, come out a day off from a printed calendar.
 */
final class Panchang {

    static final String[] MONTHS = {"Chaitra", "Vaishakh", "Jeth", "Ashadh", "Shravan", "Bhadarvo", "Aso",
            "Kartak", "Magshar", "Posh", "Maha", "Fagan"};
    private static final String[] TITHIS = {"Ekam", "Bij", "Trij", "Choth", "Pancham", "Chhath", "Satam", "Atham",
            "Nom", "Dasham", "Agiyaras", "Baras", "Teras", "Chaudas"};
    // The same in Gujarati, as the family reads it on a wall calendar.
    private static final String[] MONTHS_GU = {"ચૈત્ર", "વૈશાખ", "જેઠ", "અષાઢ", "શ્રાવણ", "ભાદરવો", "આસો",
            "કારતક", "માગશર", "પોષ", "મહા", "ફાગણ"};
    private static final String[] TITHIS_GU = {"એકમ", "બીજ", "ત્રીજ", "ચોથ", "પાંચમ", "છઠ", "સાતમ", "આઠમ",
            "નોમ", "દશમ", "અગિયારસ", "બારસ", "તેરસ", "ચૌદસ"};
    private static final String[][] FESTIVALS_GU = {
            {"Uttarayan", "ઉત્તરાયણ"}, {"Vasi Uttarayan", "વાસી ઉત્તરાયણ"}, {"Diwali", "દિવાળી"},
            {"Maha Shivratri", "મહાશિવરાત્રી"}, {"Bestu Varas · New Year", "બેસતું વર્ષ"}, {"Bhai Bij", "ભાઈબીજ"},
            {"Dev Uthi Agiyaras", "દેવઊઠી અગિયારસ"}, {"Dev Diwali", "દેવ દિવાળી"}, {"Gita Jayanti", "ગીતા જયંતી"},
            {"Vasant Panchami", "વસંત પંચમી"}, {"Holi", "હોળી"}, {"Dhuleti", "ધુળેટી"},
            {"Chaitra Navratri", "ચૈત્રી નવરાત્રી"}, {"Ram Navami", "રામનવમી"}, {"Hanuman Jayanti", "હનુમાન જયંતી"},
            {"Akshaya Tritiya", "અખાત્રીજ"}, {"Nirjala Agiyaras", "નિર્જળા અગિયારસ"}, {"Rath Yatra", "રથયાત્રા"},
            {"Devshayani Agiyaras", "દેવશયની અગિયારસ"}, {"Guru Purnima", "ગુરુપૂર્ણિમા"},
            {"Raksha Bandhan", "રક્ષાબંધન"}, {"Randhan Chhath", "રાંધણ છઠ"}, {"Shitala Satam", "શીતળા સાતમ"},
            {"Janmashtami", "જન્માષ્ટમી"}, {"Ganesh Chaturthi", "ગણેશ ચતુર્થી"}, {"Sarvapitri Amas", "સર્વપિતૃ અમાસ"},
            {"Dussehra", "દશેરા"}, {"Sharad Punam", "શરદ પૂનમ"}, {"Vagh Baras", "વાઘ બારસ"},
            {"Dhanteras", "ધનતેરસ"}, {"Kali Chaudas", "કાળી ચૌદસ"},
    };

    /** 2083 to ૨૦૮૩. */
    static String digitsGu(int n) {
        StringBuilder b = new StringBuilder();
        for (char ch : String.valueOf(n).toCharArray()) b.append(ch >= '0' && ch <= '9' ? (char) ('૦' + (ch - '0')) : ch);
        return b.toString();
    }

    private static final int CHAITRA = 0, VAISHAKH = 1, JETH = 2, ASHADH = 3, SHRAVAN = 4, BHADARVO = 5, ASO = 6,
            KARTAK = 7, MAGSHAR = 8, MAHA = 10, FAGAN = 11;
    private static final int SUNRISE = 0, EVENING = 1, MIDNIGHT = 2;
    private static final double LAT = 23.03, LON = 72.58; // Ahmedabad
    private static final TimeZone IST = TimeZone.getTimeZone("Asia/Kolkata");

    /** A festival: its name, a greeting for the day, and how it looks (see {@link Mood}). */
    static final class Festival {
        final String name, greeting;
        final Mood mood;
        /** A day when everyone is usually home. */
        final boolean holiday;

        Festival(String name, String greeting, Mood mood, boolean holiday) {
            this.name = name;
            this.greeting = greeting;
            this.mood = mood;
            this.holiday = holiday;
        }
    }

    /** The feel of a day, for the Darshan sparks and the accent colour. */
    enum Mood { CALM, DIYA, GARBA, HOLI, KITE, KRISHNA, SHIVA }

    /** One day of the calendar. */
    static final class Day {
        /** 0 = Chaitra ... 7 = Kartak ... 11 = Fagan. */
        final int month;
        /** 0..29: 0-14 Sud (Ekam..Punam), 15-29 Vad (Ekam..Amas). */
        final int tithi;
        final boolean adhik;
        final int samvat;
        final Festival festival;

        Day(int month, int tithi, boolean adhik, int samvat, Festival festival) {
            this.month = month;
            this.tithi = tithi;
            this.adhik = adhik;
            this.samvat = samvat;
            this.festival = festival;
        }

        boolean sud() {
            return tithi < 15;
        }

        /** "Agiyaras", "Punam", "Amas"... */
        String tithiName() {
            if (tithi == 14) return "Punam";
            if (tithi == 29) return "Amas";
            return TITHIS[tithi % 15];
        }

        /** "Kartak Sud Agiyaras" (or "Adhik Jeth Vad Bij"). */
        String label() {
            String p = tithi == 14 || tithi == 29 ? "" : (sud() ? " Sud" : " Vad");
            return (adhik ? "Adhik " : "") + MONTHS[month] + p + " " + tithiName();
        }

        /** "કારતક સુદ અગિયારસ" (or "અધિક જેઠ વદ બીજ"). */
        String labelGu() {
            String name = tithi == 14 ? "પૂનમ" : tithi == 29 ? "અમાસ" : TITHIS_GU[tithi % 15];
            String p = tithi == 14 || tithi == 29 ? "" : (sud() ? " સુદ" : " વદ");
            return (adhik ? "અધિક " : "") + MONTHS_GU[month] + p + " " + name;
        }

        /** "વિ.સં. ૨૦૮૩" */
        String samvatGu() {
            return "વિ.સં. " + digitsGu(samvat);
        }

        /** The festival's name in Gujarati ("બેસતું વર્ષ"), or "" on an ordinary day. */
        String festivalGu() {
            if (festival == null) return "";
            if (festival.name.startsWith("Navratri · Day ")) {
                return "નવરાત્રી · નોરતું " + digitsGu(Integer.parseInt(festival.name.substring(15)));
            }
            for (String[] f : FESTIVALS_GU) if (f[0].equals(festival.name)) return f[1];
            return festival.name;
        }

        /** Agiyaras, Punam and Amas are kept with a fast or a visit to the mandir. */
        boolean special() {
            return tithi % 15 == 10 || tithi == 14 || tithi == 29;
        }

        Mood mood() {
            if (festival != null) return festival.mood;
            if (month == SHRAVAN) return Mood.SHIVA; // the month of Shiva
            if (month == ASO && tithi <= 8) return Mood.GARBA;
            return Mood.CALM;
        }
    }

    private static Day cached;
    private static String cachedFor = "";

    private Panchang() { }

    /** Today in India. */
    static synchronized Day today() {
        Calendar now = Calendar.getInstance(IST);
        String key = now.get(Calendar.YEAR) + "-" + now.get(Calendar.DAY_OF_YEAR);
        if (!key.equals(cachedFor)) {
            cached = day(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            cachedFor = key;
        }
        return cached;
    }

    /** {@code month} 0-based, as in Calendar. */
    static Day day(int y, int m, int d) {
        double rise = sunriseJd(y, m, d);
        int t = tithiAt(rise);
        double nm = newMoonBefore(rise);
        int sign = siderealSign(nm);
        int signNext = siderealSign(newMoonBefore(nm + 35));
        int month = (sign + 1) % 12;
        boolean adhik = sign == signNext;
        int samvat = month >= KARTAK && m >= Calendar.SEPTEMBER ? y + 57 : y + 56;
        Festival f = adhik ? null : festival(y, m, d, month, t);
        return new Day(month, t, adhik, samvat, f);
    }

    // ------------------------------------------------------------------ festivals

    private static Festival festival(int y, int m, int d, int month, int t) {
        // Solar: Uttarayan and Vasi Uttarayan, on fixed dates.
        if (m == Calendar.JANUARY && d == 14) return new Festival("Uttarayan", "Happy Uttarayan", Mood.KITE, true);
        if (m == Calendar.JANUARY && d == 15) return new Festival("Vasi Uttarayan", "Happy Uttarayan", Mood.KITE, true);
        // Kept in the evening or at midnight.
        if (month == ASO && keptAt(y, m, d, 29, EVENING)) return new Festival("Diwali", "Happy Diwali", Mood.DIYA, true);
        if (month == MAHA && keptAt(y, m, d, 28, MIDNIGHT))
            return new Festival("Maha Shivratri", "Har Har Mahadev", Mood.SHIVA, false);
        if (!keptAt(y, m, d, t, SUNRISE)) return null; // not the first day of this tithi
        switch (month) {
            case KARTAK:
                if (t == 0) return new Festival("Bestu Varas · New Year", "Saal Mubarak", Mood.DIYA, true);
                if (t == 1) return new Festival("Bhai Bij", "Happy Bhai Bij", Mood.DIYA, true);
                if (t == 10) return new Festival("Dev Uthi Agiyaras", "Jai Shri Krishna", Mood.KRISHNA, false);
                if (t == 14) return new Festival("Dev Diwali", "Happy Dev Diwali", Mood.DIYA, false);
                break;
            case MAGSHAR:
                if (t == 10) return new Festival("Gita Jayanti", "Jai Shri Krishna", Mood.KRISHNA, false);
                break;
            case MAHA:
                if (t == 4) return new Festival("Vasant Panchami", "Happy Vasant Panchami", Mood.CALM, false);
                break;
            case FAGAN:
                if (t == 14) return new Festival("Holi", "Happy Holi", Mood.HOLI, true);
                if (t == 15) return new Festival("Dhuleti", "Happy Holi", Mood.HOLI, true);
                break;
            case CHAITRA:
                if (t == 0) return new Festival("Chaitra Navratri", "Jai Mataji", Mood.GARBA, false);
                if (t == 8) return new Festival("Ram Navami", "Jai Shri Ram", Mood.CALM, false);
                if (t == 14) return new Festival("Hanuman Jayanti", "Jai Bajrangbali", Mood.CALM, false);
                break;
            case VAISHAKH:
                if (t == 2) return new Festival("Akshaya Tritiya", "Happy Akha Trij", Mood.DIYA, false);
                break;
            case JETH:
                if (t == 10) return new Festival("Nirjala Agiyaras", "Jai Shri Krishna", Mood.KRISHNA, false);
                break;
            case ASHADH:
                if (t == 1) return new Festival("Rath Yatra", "Jai Jagannath", Mood.KRISHNA, false);
                if (t == 10) return new Festival("Devshayani Agiyaras", "Jai Shri Krishna", Mood.KRISHNA, false);
                if (t == 14) return new Festival("Guru Purnima", "Happy Guru Purnima", Mood.CALM, false);
                break;
            case SHRAVAN:
                if (t == 14) return new Festival("Raksha Bandhan", "Happy Raksha Bandhan", Mood.DIYA, true);
                if (t == 20) return new Festival("Randhan Chhath", "Jai Shri Krishna", Mood.CALM, false);
                if (t == 21) return new Festival("Shitala Satam", "Jai Shitala Maa", Mood.CALM, false);
                if (t == 22) return new Festival("Janmashtami", "Jai Shri Krishna", Mood.KRISHNA, true);
                break;
            case BHADARVO:
                if (t == 3) return new Festival("Ganesh Chaturthi", "Ganpati Bappa Morya", Mood.DIYA, false);
                if (t == 29) return new Festival("Sarvapitri Amas", "", Mood.CALM, false);
                break;
            case ASO:
                if (t <= 8) return new Festival("Navratri · Day " + (t + 1), "Jai Ambe Maa", Mood.GARBA, false);
                if (t == 9) return new Festival("Dussehra", "Happy Dussehra", Mood.DIYA, true);
                if (t == 14) return new Festival("Sharad Punam", "Jai Ambe Maa", Mood.GARBA, false);
                if (t == 26) return new Festival("Vagh Baras", "Happy Diwali", Mood.DIYA, false);
                if (t == 27) return new Festival("Dhanteras", "Happy Dhanteras", Mood.DIYA, true);
                if (t == 28) return new Festival("Kali Chaudas", "Happy Diwali", Mood.DIYA, true);
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * Whether a festival on tithi {@code t} is kept on this day: the tithi holds at that time today
     * and didn't yesterday (a tithi that lasts two sunrises is kept on the first), or it was
     * skipped between yesterday and today.
     */
    private static boolean keptAt(int y, int m, int d, int t, int when) {
        Calendar c = Calendar.getInstance(IST);
        c.clear();
        c.set(y, m, d);
        c.add(Calendar.DAY_OF_MONTH, -1);
        double today = when(y, m, d, when);
        double before = when(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), when);
        int now = tithiAt(today), prev = tithiAt(before);
        if (now == t) return prev != t;
        return now == (t + 1) % 30 && prev == (t + 29) % 30; // skipped (kshaya) tithi
    }

    private static double when(int y, int m, int d, int when) {
        if (when == SUNRISE) return sunriseJd(y, m, d);
        // 7:30 PM or 11:59 PM India time.
        double hours = when == EVENING ? 19.5 : 23.98;
        return jdUtc(y, m, d, hours - 5.5);
    }

    // ------------------------------------------------------------------ astronomy

    static int tithiAt(double jd) {
        return (int) Math.floor(elongation(jd) / 12.0) % 30;
    }

    private static double elongation(double jd) {
        return norm(moon(jd) - sun(jd));
    }

    private static int siderealSign(double jd) {
        double ayanamsa = 23.853 + 0.013963 * ((jd - 2451545.0) / 365.25); // Lahiri
        return (int) Math.floor(norm(sun(jd) - ayanamsa) / 30.0) % 12;
    }

    private static double newMoonBefore(double jd) {
        double t = jd - elongation(jd) / 12.19;
        for (int i = 0; i < 6; i++) {
            double e = elongation(t);
            if (e > 180) e -= 360;
            t -= e / 12.19;
        }
        return t;
    }

    private static double jdUtc(int y, int m, int d, double hoursUtc) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.clear();
        c.set(y, m, d, 0, 0, 0);
        return c.getTimeInMillis() / 86_400_000.0 + 2440587.5 + hoursUtc / 24.0;
    }

    /**
     * Sunrise or sunset at a place, in minutes after midnight India time (NOAA's short formula,
     * good to a minute or two). {@code month} 0-based, as in Calendar.
     */
    static int sunMinutes(int y, int m, int d, boolean rise, double lat, double lon) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.clear();
        c.set(y, m, d);
        double g = 2 * Math.PI / 365 * (c.get(Calendar.DAY_OF_YEAR) - 1);
        double eqt = 229.18 * (0.000075 + 0.001868 * Math.cos(g) - 0.032077 * Math.sin(g)
                - 0.014615 * Math.cos(2 * g) - 0.040849 * Math.sin(2 * g));
        double decl = 0.006918 - 0.399912 * Math.cos(g) + 0.070257 * Math.sin(g) - 0.006758 * Math.cos(2 * g)
                + 0.000907 * Math.sin(2 * g) - 0.002697 * Math.cos(3 * g) + 0.00148 * Math.sin(3 * g);
        double la = Math.toRadians(lat);
        double x = Math.cos(Math.toRadians(90.833)) / (Math.cos(la) * Math.cos(decl)) - Math.tan(la) * Math.tan(decl);
        double ha = Math.toDegrees(Math.acos(Math.max(-1, Math.min(1, x))));
        double utc = 720 - 4 * (lon + (rise ? ha : -ha)) - eqt;
        return (int) Math.round(utc + 330); // IST is UTC+5:30
    }

    static int sunMinutes(int y, int m, int d, boolean rise) {
        return sunMinutes(y, m, d, rise, LAT, LON);
    }

    /** Sunrise in Ahmedabad (NOAA's short formula), as a Julian day. */
    private static double sunriseJd(int y, int m, int d) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.clear();
        c.set(y, m, d);
        double g = 2 * Math.PI / 365 * (c.get(Calendar.DAY_OF_YEAR) - 1);
        double eqt = 229.18 * (0.000075 + 0.001868 * Math.cos(g) - 0.032077 * Math.sin(g)
                - 0.014615 * Math.cos(2 * g) - 0.040849 * Math.sin(2 * g));
        double decl = 0.006918 - 0.399912 * Math.cos(g) + 0.070257 * Math.sin(g) - 0.006758 * Math.cos(2 * g)
                + 0.000907 * Math.sin(2 * g) - 0.002697 * Math.cos(3 * g) + 0.00148 * Math.sin(3 * g);
        double lat = Math.toRadians(LAT);
        double ha = Math.toDegrees(Math.acos(Math.cos(Math.toRadians(90.833)) / (Math.cos(lat) * Math.cos(decl))
                - Math.tan(lat) * Math.tan(decl)));
        double minutes = 720 - 4 * (LON + ha) - eqt;
        return jdUtc(y, m, d, minutes / 60.0);
    }

    private static double sun(double jd) {
        double t = (jd - 2451545.0) / 36525.0;
        double l0 = 280.46646 + 36000.76983 * t, mm = Math.toRadians(357.52911 + 35999.05029 * t);
        double c = (1.914602 - 0.004817 * t) * Math.sin(mm) + (0.019993 - 0.000101 * t) * Math.sin(2 * mm)
                + 0.000289 * Math.sin(3 * mm);
        return norm(l0 + c - 0.00569 - 0.00478 * Math.sin(Math.toRadians(125.04 - 1934.136 * t)));
    }

    /** {D, M, M', F multipliers, coefficient in millionths of a degree}: Meeus table 47.A. */
    private static final int[][] MOON = {
            {0, 0, 1, 0, 6288774}, {2, 0, -1, 0, 1274027}, {2, 0, 0, 0, 658314}, {0, 0, 2, 0, 213618},
            {0, 1, 0, 0, -185116}, {0, 0, 0, 2, -114332}, {2, 0, -2, 0, 58793}, {2, -1, -1, 0, 57066},
            {2, 0, 1, 0, 53322}, {2, -1, 0, 0, 45758}, {0, 1, -1, 0, -40923}, {1, 0, 0, 0, -34720},
            {0, 1, 1, 0, -30383}, {2, 0, 0, -2, 15327}, {0, 0, 1, 2, -12528}, {0, 0, 1, -2, 10980},
            {4, 0, -1, 0, 10675}, {0, 0, 3, 0, 10034}, {4, 0, -2, 0, 8548}, {2, 1, -1, 0, -7888},
            {2, 1, 0, 0, -6766}, {1, 0, -1, 0, -5163}, {1, 1, 0, 0, 4987}, {2, -1, 1, 0, 4036},
            {2, 0, 2, 0, 3994}, {4, 0, 0, 0, 3861}, {2, 0, -3, 0, 3665}, {0, 1, -2, 0, -2689},
            {2, 0, -1, 2, -2602}, {2, -1, -2, 0, 2390}, {1, 0, 1, 0, -2348}, {2, -2, 0, 0, 2236},
            {0, 1, 2, 0, -2120}, {0, 2, 0, 0, -2069},
    };

    private static double moon(double jd) {
        double t = (jd - 2451545.0) / 36525.0;
        double lp = 218.3164477 + 481267.88123421 * t, dd = 297.8501921 + 445267.1114034 * t;
        double ms = 357.5291092 + 35999.0502909 * t, mp = 134.9633964 + 477198.8675055 * t;
        double ff = 93.2720950 + 483202.0175233 * t, e = 1 - 0.002516 * t;
        double sum = 0;
        for (int[] r : MOON) {
            double arg = Math.toRadians(r[0] * dd + r[1] * ms + r[2] * mp + r[3] * ff);
            double k = r[1] == 0 ? 1 : Math.abs(r[1]) == 1 ? e : e * e;
            sum += r[4] * k * Math.sin(arg);
        }
        sum += 3958 * Math.sin(Math.toRadians(119.75 + 131.849 * t)) + 1962 * Math.sin(Math.toRadians(lp - ff))
                + 318 * Math.sin(Math.toRadians(53.09 + 479264.29 * t));
        return norm(lp + sum / 1e6 - 0.00478 * Math.sin(Math.toRadians(125.04 - 1934.136 * t)));
    }

    private static double norm(double a) {
        a %= 360;
        return a < 0 ? a + 360 : a;
    }
}
