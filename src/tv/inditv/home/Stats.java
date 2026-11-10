package tv.inditv.home;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * What the family actually watches, and when: kept on the TV only, a few numbers per video.
 * Home uses it to put the videos they come back to first at the time of day they watch them,
 * to bring in one they haven't tried, and to say something kind about the habit
 * ("Prayers on 5 of the last 7 days").
 */
final class Stats {

    private static final String KEY = "watchStats", DAYS = "watchDays";
    private static final long DAY_MS = 24 * 3600_000L;
    private static JSONObject cache;

    private Stats() { }

    private static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences("haven", Context.MODE_PRIVATE);
    }

    private static synchronized JSONObject all(Context c) {
        if (cache == null) {
            try {
                cache = new JSONObject(prefs(c).getString(KEY, "{}"));
            } catch (Exception e) {
                cache = new JSONObject();
            }
        }
        return cache;
    }

    /** A card was opened in this part of the day (0-3). */
    static synchronized void played(Context c, Family.Card card, int slot) {
        try {
            JSONObject all = all(c);
            JSONObject o = all.optJSONObject(card.id);
            if (o == null) o = new JSONObject();
            o.put("n", o.optInt("n") + 1);
            o.put("s" + slot, o.optInt("s" + slot) + 1);
            o.put("t", System.currentTimeMillis());
            all.put(card.id, o);
            // One entry per day and kind, for the last two weeks: {"2026-11-10": "PT"}
            JSONObject days = new JSONObject(prefs(c).getString(DAYS, "{}"));
            String today = Family.today();
            String kinds = days.optString(today, "");
            if (kinds.indexOf(card.kind) < 0) kinds += card.kind;
            days.put(today, kinds);
            JSONArray names = days.names();
            for (int i = 0; names != null && i < names.length(); i++) {
                String d = names.getString(i);
                if (d.compareTo(dayBefore(14)) < 0) days.remove(d);
            }
            prefs(c).edit().putString(KEY, all.toString()).putString(DAYS, days.toString())
                    .putInt("watchVersion", prefs(c).getInt("watchVersion", 0) + 1).apply();
        } catch (Exception ignored) { }
    }

    /** Changes whenever something is played, so Home knows to re-order. */
    static int version(Context c) {
        return prefs(c).getInt("watchVersion", 0);
    }

    static int plays(Context c, Family.Card card) {
        JSONObject o = all(c).optJSONObject(card.id);
        return o == null ? 0 : o.optInt("n");
    }

    static long lastPlayed(Context c, Family.Card card) {
        JSONObject o = all(c).optJSONObject(card.id);
        return o == null ? 0 : o.optLong("t");
    }

    /**
     * How much the family wants this card now: plays at this time of day count most, plays at
     * other times a little, and something watched in the last few days gets a small lift.
     */
    static double score(Context c, Family.Card card, int slot) {
        JSONObject o = all(c).optJSONObject(card.id);
        if (o == null) return 0;
        double s = o.optInt("s" + slot) * 3.0 + o.optInt("n") * 0.5;
        long age = System.currentTimeMillis() - o.optLong("t");
        if (age < 3 * DAY_MS) s += 1.5;
        return s;
    }

    /** On how many of the last seven days something of this kind ('P' prayer, 'T' talk) was watched. */
    static int daysWith(Context c, char kind) {
        try {
            JSONObject days = new JSONObject(prefs(c).getString(DAYS, "{}"));
            int n = 0;
            for (int i = 0; i < 7; i++) if (days.optString(dayBefore(i), "").indexOf(kind) >= 0) n++;
            return n;
        } catch (Exception e) {
            return 0;
        }
    }

    private static String dayBefore(int days) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ROOT)
                .format(new java.util.Date(System.currentTimeMillis() - days * DAY_MS));
    }
}
