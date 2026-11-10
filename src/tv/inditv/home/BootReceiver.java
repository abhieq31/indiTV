package tv.inditv.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** After a cold boot, puts the Home-button service and the settings watcher back (see Keeper). */
public class BootReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context c, Intent i) {
        Schedule.arm(c); // the daily aarti / news times
        if (!Intent.ACTION_BOOT_COMPLETED.equals(i.getAction())) return;
        PendingResult r = goAsync();
        Keeper.schedule(c);
        Keeper.ensure(c, true, r::finish);
    }
}
