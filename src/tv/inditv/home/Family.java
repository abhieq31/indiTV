package tv.inditv.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Everything personal about this TV lives in this one file: who watches what, and when.
 *
 * Edit it on github.com (pencil icon), commit, and GitHub builds a new indiTV release. The TV
 * offers the update by itself within 12 hours, or straight away from indiTV Settings > About >
 * Check for updates. No computer needed.
 *
 * To play one exact video instead of a YouTube search, put its link in the {@code video} field,
 * e.g. "https://www.youtube.com/watch?v=XXXXXXXXXXX" (copy it from Share > Copy link on your phone).
 */
final class Family {

    // ------------------------------------------------------------------ what to watch

    static final String YOUTUBE = "com.google.android.youtube.tv";

    /** Shown in the greeting: "Good evening, Patel family". */
    static final String FAMILY_NAME = "Patel";

    /** One card in the "Right now" row. */
    static final class Card {
        final String id, title, subtitle, who;
        /** YouTube search, used when {@link #video} is empty. */
        final String query;
        /** Exact YouTube link to play; "" to use {@link #query}. */
        final String video;
        /** HDMI port number to switch to (1 = HDMI 1), or 0. */
        final int hdmi;
        /** Apps to try first, in order (e.g. Crunchyroll for anime), before falling back. */
        final String[] apps;
        /** Wallpaper (assets/wallpapers/NAME.jpg) used as the card picture, or null for a colour. */
        final String art;
        final int color;
        /**
         * A YouTube channel handle ("@TV9GujaratiNews") whose live stream (or newest video) the card
         * plays; indiTV looks it up each time Home opens, so news is always today's. "" if none.
         */
        String channel = "";

        Card live(String handle) {
            channel = handle;
            return this;
        }

        Card(String id, String title, String subtitle, String who, String query, String video,
             int hdmi, String[] apps, String art, int color) {
            this.id = id;
            this.title = title;
            this.subtitle = subtitle;
            this.who = who;
            this.query = query;
            this.video = video;
            this.hdmi = hdmi;
            this.apps = apps;
            this.art = art;
            this.color = color;
        }
    }

    private static final String[] NONE = {};
    /** For {@code apps}: open Google TV's search for the card's words (it lists every app that has them). */
    private static final String[] GOOGLE_SEARCH = {"google-search"};

    // Cards with a video play that exact video with one press and show its own YouTube thumbnail.
    // Cards with a search open YouTube's results and show the app's own banner.
    static final Card HANUMAN_CHALISA = new Card("chalisa", "Shree Hanuman Chalisa", "Hariharan · T-Series Bhakti Sagar", "Mummy",
            "hanuman chalisa", "https://www.youtube.com/watch?v=LUx8wlA_dk8", 0, NONE, null, 0xFFB5651D);
    static final Card MORNING_AARTI = new Card("morning", "Om Jai Jagdish Hare", "Anuradha Paudwal · Morning aarti", "Mummy",
            "om jai jagdish hare aarti", "https://www.youtube.com/watch?v=3ucCEjXS9n8", 0, NONE, null, 0xFFC98A2B);
    static final Card BHAJAN = new Card("bhajan", "Krishna Bhajans", "Hun To Vari Re Girdharlal · Non-stop", "Mummy",
            "gujarati krishna bhajan", "https://www.youtube.com/watch?v=YuvUv8KDYGA", 0, NONE, null, 0xFF7A4E9C);
    static final Card ADHYASHAKTI = new Card("aarti", "Jay Adhyashakti Aarti", "Kinjal Dave · Ambe Maa aarti", "Everyone · 7 PM",
            "jay adhyashakti aarti", "https://www.youtube.com/watch?v=SU8aghkgeCQ", 0, NONE, null, 0xFFD9822B);
    static final Card NEWS = new Card("news", "News on Tata Play", "Set-top box · HDMI 1", "Papa & Dada · 8:30 PM",
            "", "", 1, NONE, null, 0xFF1F5FA8);
    static final Card GUJARATI_NEWS = new Card("gjnews", "Gujarati News Live", "YouTube", "Papa & Dada",
            "gujarati news live", "", 0, NONE, null, 0xFF2D6E8E);
    static final Card GUJARATI_FUN = new Card("gujarati", "Rang Chhe Rajja", "Siddharth Randeria · Comedy natak", "Papa & Dada",
            "gujarati comedy natak", "https://www.youtube.com/watch?v=oieLUJIyT0Y", 0, NONE, null, 0xFF3C7A55);
    static final Card GUJARATI_SONGS = new Card("songs", "Killol Nonstop Garba", "Kinjal Dave · Gujarati songs", "Everyone",
            "gujarati songs", "https://www.youtube.com/watch?v=XN5MYcku-wQ", 0, NONE, null, 0xFF9C4A6B);
    static final Card AOT = new Card("aot", "Attack on Titan", "Anime", "Little brother",
            "attack on titan", "", 0, new String[]{"com.crunchyroll.crunchyroid", "com.netflix.ninja"}, null, 0xFF5A3A2E);
    static final Card SHORTS = new Card("shorts", "YouTube Shorts", "YouTube", "Little brother",
            "", "https://www.youtube.com/shorts", 0, NONE, null, 0xFFCC2B2B);
    static final Card NETFLIX = new Card("netflix", "Netflix", "Movies & series", "Everyone",
            "", "", 0, new String[]{"com.netflix.ninja"}, null, 0xFF8E1B1F);
    static final Card FAMILY_MOVIE = new Card("movie", "Movie Night", "JioHotstar", "Everyone",
            "", "", 0, new String[]{"in.startv.hotstar", "in.startv.hotstar.dplus.tv", "com.jio.media.ondemand"}, null, 0xFF6E3B8C);
    /** Opens the Darshan screensaver straight away, for when guests are in the room. */
    static final Card DARSHAN = new Card("darshan", "Darshan", "Devotional screensaver", "For guests",
            "", "", 0, NONE, "night-vishnu-among-the-stars", 0xFF2A2350);

    static final Card ASHAPURA = new Card("ashapura", "Aashapura Maa Aarti", "Hemant Chauhan · Utaro Aarti", "Everyone",
            "ashapura maa aarti", "https://www.youtube.com/watch?v=sOQejx9us8U", 0, NONE, null, 0xFFB03A2E);
    static final Card SUNDARKAND = new Card("sundarkand", "Sampoorna Sundarkand", "Ashwin Kumar Pathak", "Mummy",
            "sundarkand", "https://www.youtube.com/watch?v=EQn9Yzy0jq8", 0, NONE, null, 0xFFB5651D);
    static final Card KATHA = new Card("katha", "Ram Katha · Morari Bapu", "Gujarati prasang · Chitrakutdham", "Everyone",
            "morari bapu ram katha", "https://www.youtube.com/watch?v=WXWgTI0cpXw", 0, NONE, null, 0xFF7A4E9C);
    static final Card TV9 = new Card("tv9", "TV9 Gujarati", "Live news", "Papa & Dada",
            "", "https://www.youtube.com/@TV9GujaratiNews/live", 0, NONE, null, 0xFF1F5FA8).live("@TV9GujaratiNews");
    static final Card SANDESH = new Card("sandesh", "Sandesh News", "Live news", "Papa & Dada",
            "", "https://www.youtube.com/@SandeshNewsTV/live", 0, NONE, null, 0xFF2D6E8E).live("@SandeshNewsTV");

    // Brain gym: thoughtful talks in Gujarati, from the speakers' own channels.
    static final Card JV_SENIORS = new Card("jv-seniors", "Nivrutti Pachhini Niraant", "Jay Vasavada", "Papa, Mummy, Dada, Ba",
            "jay vasavada", "https://www.youtube.com/watch?v=-6oESgxTIMQ", 0, NONE, null, 0xFF2D6E8E);
    static final Card JV_ART = new Card("jv-art", "Art and the Mind", "Jay Vasavada", "Everyone",
            "jay vasavada", "https://www.youtube.com/watch?v=dLiY1e07pk4", 0, NONE, null, 0xFF2D6E8E);
    static final Card GITA = new Card("gita", "Bhagavad Gita in Gujarati", "Chapter 12 · Bhakti Yoga", "Everyone",
            "bhagavad gita gujarati", "https://www.youtube.com/watch?v=8YIN_Pw3svY", 0, NONE, null, 0xFF7A4E9C);

    /** The YouTube video id in a card's link ("LUx8wlA_dk8"), or null. */
    static String videoId(Card card) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("[?&]v=([A-Za-z0-9_-]{11})").matcher(card.video);
        return m.find() ? m.group(1) : null;
    }

    /** The app whose own banner stands for a card that has no video thumbnail, or null. */
    static String appFor(Context c, Card card) {
        PackageManager pm = c.getPackageManager();
        for (String pkg : card.apps) if (installed(pm, pkg)) return pkg;
        if (card.apps.length == 0 && (!card.query.isEmpty() || !card.video.isEmpty())) return YOUTUBE;
        return null;
    }

    /** A part of the day and the cards that come first in it. Times are 24-hour, start inclusive. */
    private static final class Slot {
        final int from, to; // minutes after midnight
        final String name;
        final Card[] cards;

        Slot(int fromH, int fromM, int toH, int toM, String name, Card... cards) {
            this.from = fromH * 60 + fromM;
            this.to = toH * 60 + toM;
            this.name = name;
            this.cards = cards;
        }

        boolean has(int m) {
            return from <= to ? m >= from && m < to : m >= from || m < to; // wraps past midnight
        }
    }

    /**
     * Mornings and evenings are for prayer; afternoons and nights for local news and
     * entertainment. The row shows only the cards for this part of the day.
     */
    private static final Slot[] DAY = {
            new Slot(4, 30, 11, 0, "Morning prayers", HANUMAN_CHALISA, SUNDARKAND, ASHAPURA, GITA, KATHA),
            new Slot(11, 0, 17, 30, "Think and learn", JV_SENIORS, GITA, KATHA, JV_ART, GUJARATI_FUN),
            new Slot(17, 30, 20, 15, "Evening aarti", ADHYASHAKTI, ASHAPURA, HANUMAN_CHALISA, SUNDARKAND, MORNING_AARTI),
            new Slot(20, 15, 4, 30, "Evening", NEWS, GUJARATI_FUN, JV_SENIORS, GUJARATI_SONGS),
    };

    /**
     * The elders' cards (Papa, Mummy, Dada, Ba), in the order they fill the row after the current
     * part of the day's cards. Kept short on purpose: this is what's on screen most days.
     */
    private static final Card[] ELDERS = {ADHYASHAKTI, NEWS, HANUMAN_CHALISA, BHAJAN, GUJARATI_FUN, GUJARATI_SONGS};

    /** The weekend / holiday row, when the young ones are home too. */
    static final Card[] WEEKEND = {AOT, SHORTS, NETFLIX, FAMILY_MOVIE};

    // ------------------------------------------------------------------ who's home

    /**
     * Holidays when everyone is home, besides Saturdays and Sundays, as "yyyy-MM-dd"
     * (e.g. "2027-03-22"). On the TV you can also turn on indiTV Settings > Home screen >
     * Holiday today; it switches itself off at midnight.
     */
    static final String[] HOLIDAYS = {};

    /** Weekdays get the simple home (elders); weekends and holidays add the young ones' row. */
    static boolean everyoneHome(Context c) {
        Calendar now = Calendar.getInstance();
        int d = now.get(Calendar.DAY_OF_WEEK);
        if (d == Calendar.SATURDAY || d == Calendar.SUNDAY) return true;
        String today = today();
        for (String h : HOLIDAYS) if (h.equals(today)) return true;
        try {
            Panchang.Day p = Panchang.today();
            if (p.festival != null && p.festival.holiday) return true; // Diwali, New Year, Holi, Uttarayan...
        } catch (RuntimeException ignored) { }
        return today.equals(c.getSharedPreferences("haven", Context.MODE_PRIVATE).getString("holidayDate", ""));
    }

    static boolean holidayToday(Context c) {
        return today().equals(c.getSharedPreferences("haven", Context.MODE_PRIVATE).getString("holidayDate", ""));
    }

    static void setHolidayToday(Context c, boolean on) {
        c.getSharedPreferences("haven", Context.MODE_PRIVATE).edit().putString("holidayDate", on ? today() : "").apply();
    }

    static String today() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ROOT).format(new java.util.Date());
    }

    /** A greeting for the time of day, shown above the first row. */
    static String greeting() {
        try {
            Panchang.Day d = Panchang.today();
            if (d.festival != null && !d.festival.greeting.isEmpty()) return d.festival.greeting + ", " + FAMILY_NAME + " family";
        } catch (RuntimeException ignored) { }
        int h = minuteOfDay() / 60;
        if (h >= 4 && h < 12) return "Good morning" + ", " + FAMILY_NAME + " family";
        if (h >= 12 && h < 17) return "Good afternoon" + ", " + FAMILY_NAME + " family";
        if (h >= 17 && h < 22) return "Good evening" + ", " + FAMILY_NAME + " family";
        return "Good night" + ", " + FAMILY_NAME + " family";
    }

    /** Favourites: whichever are installed, in this order. Raise DOCK_VERSION to apply a new list. */
    static final int DOCK_VERSION = 2;
    /** Each group is one tile: the first of its apps that's installed. */
    static final String[][] DOCK = {
            {YOUTUBE},
            {"in.startv.hotstar", "in.startv.hotstar.dplus.tv", "com.jio.media.stb.ondemand", "com.jio.media.ondemand"}, // JioHotstar
            {"com.sonyliv", "com.sonyliv.tv"},                                                // SonyLIV
            {"com.apple.atve.androidtv.appletv"},                                             // Apple TV
            {"com.netflix.ninja"},                                                            // Netflix
    };

    // ------------------------------------------------------------------ automatic

    /** Things the TV does by itself at a set time, if it is switched on. */
    static final class Auto {
        final String id, label, detail;
        final int hour, minute;
        final boolean onByDefault;
        final Card card;

        Auto(String id, String label, String detail, int hour, int minute, boolean onByDefault, Card card) {
            this.id = id;
            this.label = label;
            this.detail = detail;
            this.hour = hour;
            this.minute = minute;
            this.onByDefault = onByDefault;
            this.card = card;
        }

        String time() {
            int h = hour % 12 == 0 ? 12 : hour % 12;
            return h + ":" + (minute < 10 ? "0" : "") + minute + (hour < 12 ? " AM" : " PM");
        }
    }

    static final Auto[] AUTO = {
            new Auto("auto.morning", "Morning Hanuman Chalisa", "Plays on YouTube if the TV is on", 6, 30, false, HANUMAN_CHALISA),
            new Auto("auto.aarti", "Jay Adhyashakti Aarti", "Plays on YouTube if the TV is on", 19, 0, true, ADHYASHAKTI),
            new Auto("auto.news", "Switch to Tata Play", "Opens HDMI 1 for the news", 20, 30, true, NEWS),
    };

    static boolean autoOn(Context c, Auto a) {
        return c.getSharedPreferences("haven", Context.MODE_PRIVATE).getBoolean(a.id, a.onByDefault);
    }

    static void setAuto(Context c, Auto a, boolean on) {
        c.getSharedPreferences("haven", Context.MODE_PRIVATE).edit().putBoolean(a.id, on).apply();
    }

    // ------------------------------------------------------------------ helpers

    private Family() { }

    static int minuteOfDay() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
    }

    /** What the first row's heading says after the greeting: the festival, or the part of the day. */
    static String headline() {
        try {
            Panchang.Day d = Panchang.today();
            if (d.festival != null) return d.festival.name;
        } catch (RuntimeException ignored) { }
        return ""; // a calm heading: just the greeting
    }

    /** The part of the day it is now ("Aarti time"). */
    static String slotName() {
        int m = minuteOfDay();
        for (Slot s : DAY) if (s.has(m)) return s.name;
        return "";
    }

    /** Cards for the row: this part of the day's first (marked as now), then the rest. */
    static List<Card> cardsNow(Context c) {
        int m = minuteOfDay();
        List<Card> out = new ArrayList<>();
        for (Slot sl : DAY) {
            if (!sl.has(m)) continue;
            for (Card k : sl.cards) if (!out.contains(k)) out.add(k);
        }
        out.add(DARSHAN);
        return out;
    }

    /** Which wallpapers suit this time of day: "morning", "day" or "night". */
    static String wallpaperPeriod() {
        int h = minuteOfDay() / 60;
        if (h >= 4 && h < 11) return "morning";
        if (h >= 11 && h < 19) return "day";
        return "night";
    }

    /** What to start for a card. Null if nothing on this TV can open it. */
    static Intent intent(Context c, Card card) {
        PackageManager pm = c.getPackageManager();
        if (card.hdmi > 0) {
            String port = hdmiPort(c, card.hdmi);
            if (port != null) return Inputs.open(port);
            return new Intent("com.android.tv.action.VIEW_INPUTS").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        for (String pkg : card.apps) {
            if (pkg.equals("com.netflix.ninja") && !card.query.isEmpty() || pm.getLaunchIntentForPackage(pkg) == null
                    && pm.getLeanbackLaunchIntentForPackage(pkg) == null) continue;
            Intent i = pm.getLeanbackLaunchIntentForPackage(pkg);
            if (i == null) i = pm.getLaunchIntentForPackage(pkg);
            return i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (card.apps.length > 0 && !card.query.isEmpty()) {
            // Google TV's search shows every app that has the show (Netflix, Crunchyroll, Prime...).
            return new Intent("android.search.action.GLOBAL_SEARCH")
                    .putExtra(android.app.SearchManager.QUERY, card.query).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        String url = !card.video.isEmpty() ? card.video
                : "https://www.youtube.com/results?search_query=" + Uri.encode(card.query);
        Intent yt = new Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String ytPkg = installed(pm, YOUTUBE) ? YOUTUBE
                : installed(pm, "com.amazon.firetv.youtube") ? "com.amazon.firetv.youtube" : null;
        if (ytPkg != null) yt.setPackage(ytPkg);
        return yt;
    }

    private static boolean installed(PackageManager pm, String pkg) {
        try {
            pm.getPackageInfo(pkg, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /** The input id of HDMI {@code n}: by its name ("HDMI 1"), else the n-th HDMI port, else the set-top box's. */
    static String hdmiPort(Context c, int n) {
        List<Inputs.Port> ports = Inputs.ports(c);
        for (Inputs.Port p : ports) {
            String l = p.label.toLowerCase(java.util.Locale.ROOT).replace(" ", "");
            if (l.equals("hdmi" + n) || l.startsWith("hdmi" + n + "(") || l.endsWith("hdmi" + n)) return p.id;
        }
        if (ports.size() >= n) return ports.get(n - 1).id;
        return Inputs.liveTvPort(c);
    }
}
