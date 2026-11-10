package tv.inditv.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import java.util.Calendar;

/**
 * The daily automatic moments in {@link Family#AUTO}: the 7 PM aarti, the 8:30 PM switch to Tata
 * Play, and the optional morning Hanuman Chalisa. Each is an inexact alarm (within a minute, no
 * special permission). When it goes off and the TV screen is on, indiTV opens it over whatever is
 * playing; if the TV is in standby nothing happens. indiTV's Home-button service lets it start
 * the video from the background.
 */
final class Schedule {

    private static final String EXTRA = "auto";
    private static final long WINDOW_MS = 60_000;

    private Schedule() { }

    /** Sets (or clears) the next alarm for every automatic moment. Safe to call often. */
    static void arm(Context c) {
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;
        for (int i = 0; i < Family.AUTO.length; i++) {
            Family.Auto a = Family.AUTO[i];
            PendingIntent pi = PendingIntent.getBroadcast(c, 4100 + i,
                    new Intent(c, Receiver.class).putExtra(EXTRA, a.id),
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            am.cancel(pi);
            if (!Family.autoOn(c, a)) continue;
            am.setWindow(AlarmManager.RTC_WAKEUP, next(a.hour, a.minute), WINDOW_MS, pi);
        }
    }

    /** The next time it is hour:minute, today or tomorrow. */
    static long next(int hour, int minute) {
        Calendar t = Calendar.getInstance();
        t.set(Calendar.HOUR_OF_DAY, hour);
        t.set(Calendar.MINUTE, minute);
        t.set(Calendar.SECOND, 0);
        t.set(Calendar.MILLISECOND, 0);
        if (t.getTimeInMillis() <= System.currentTimeMillis() + 5_000) t.add(Calendar.DAY_OF_MONTH, 1);
        return t.getTimeInMillis();
    }

    /** Opens what an automatic moment plays. */
    static void run(Context c, Family.Auto a) {
        Intent i = Family.intent(c, a.card);
        if (i == null) return;
        try {
            c.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception e) {
            try {
                Toast.makeText(c, a.label + ": couldn't open it", Toast.LENGTH_LONG).show();
            } catch (Exception ignored) { }
        }
    }

    public static class Receiver extends BroadcastReceiver {
        @Override public void onReceive(Context c, Intent intent) {
            String id = intent.getStringExtra(EXTRA);
            for (Family.Auto a : Family.AUTO) {
                if (!a.id.equals(id) || !Family.autoOn(c, a)) continue;
                PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
                if (pm == null || pm.isInteractive()) run(c, a); // only when the TV is on
            }
            arm(c); // tomorrow's
        }
    }
}
