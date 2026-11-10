package tv.inditv.home;

/** Checks the Gujarati calendar against dates from printed panchangs. Run by the build. */
public final class PanchangCheck {

    private static int failed;

    public static void main(String[] args) {
        // {year, month (1-12), day, expected label, expected festival or ""}
        check(2025, 10, 22, "Kartak Sud Ekam", "Bestu Varas · New Year");
        check(2025, 10, 20, "Aso Vad Chaudas", "Diwali");
        check(2025, 8, 16, "Shravan Vad Atham", "Janmashtami");
        check(2025, 8, 9, "Shravan Punam", "Raksha Bandhan");
        check(2025, 9, 22, "Aso Sud Ekam", "Navratri · Day 1");
        check(2025, 10, 2, "Aso Sud Dasham", "Dussehra");
        check(2026, 2, 15, "Maha Vad Teras", "Maha Shivratri");
        check(2026, 3, 3, "Fagan Punam", "Holi");
        check(2026, 3, 4, "Fagan Vad Ekam", "Dhuleti");
        check(2026, 1, 14, null, "Uttarayan");
        check(2026, 11, 8, "Aso Vad Chaudas", "Diwali");
        check(2026, 11, 10, "Kartak Sud Ekam", "Bestu Varas · New Year");
        check(2026, 5, 20, "Adhik Jeth Sud Choth", "");
        Panchang.Day ny = Panchang.day(2026, 10, 10);
        boolean gu = "કારતક સુદ એકમ".equals(ny.labelGu()) && "વિ.સં. ૨૦૮૩".equals(ny.samvatGu()) && "બેસતું વર્ષ".equals(ny.festivalGu());
        System.out.println((gu ? "ok   " : "FAIL ") + ny.labelGu() + "  " + ny.samvatGu() + "  " + ny.festivalGu());
        if (!gu) failed++;
        // Sunset in Ahmedabad on 10 Nov 2026 is 5:57 PM; the family's aarti that evening was at 6:05.
        int set = Panchang.sunMinutes(2026, 10, 10, false);
        int aarti = Math.round((set + 10) / 5f) * 5;
        boolean sunOk = Math.abs(set - (17 * 60 + 57)) <= 2 && aarti == 18 * 60 + 5;
        System.out.println((sunOk ? "ok   " : "FAIL ") + "sunset " + set / 60 + ":" + set % 60 + "  aarti " + aarti / 60 + ":" + aarti % 60);
        if (!sunOk) failed++;
        int june = Panchang.sunMinutes(2027, 5, 21, false);
        System.out.println("     sunset 21 Jun 2027 " + june / 60 + ":" + june % 60 + "  aarti " + Math.round((june + 10) / 5f) * 5 / 60 + ":" + Math.round((june + 10) / 5f) * 5 % 60);
        if (failed > 0) {
            System.out.println(failed + " calendar check(s) failed");
            System.exit(1);
        }
        System.out.println("Calendar checks passed");
    }

    private static void check(int y, int m, int d, String label, String festival) {
        Panchang.Day day = Panchang.day(y, m - 1, d);
        String got = day.festival == null ? "" : day.festival.name;
        boolean ok = (label == null || label.equals(day.label())) && festival.equals(got);
        System.out.println((ok ? "ok   " : "FAIL ") + y + "-" + m + "-" + d + "  " + day.label() + "  [" + got + "]  VS " + day.samvat);
        if (!ok) failed++;
    }
}
