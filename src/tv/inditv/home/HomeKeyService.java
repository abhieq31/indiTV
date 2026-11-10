package tv.inditv.home;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

/**
 * Google TV sends the Home key straight to its own launcher, ignoring the default
 * home app. This service catches Home from the remote and opens indiTV, and also opens indiTV
 * whenever Google TV's home screen appears some other way. It only receives events from the
 * Google TV launcher (see home_key_service.xml).
 */
public class HomeKeyService extends AccessibilityService {

    private static final String GOOGLE_TV_HOME = "com.google.android.apps.tv.launcherx";
    /** The Android TV launcher (Android 9 TVs such as TCL's): it's the Home app there. */
    private static final String ANDROID_TV_HOME = "com.google.android.tvlauncher";

    /**
     * Filtering the remote's keys is only needed on Google TV, which grabs Home before any app.
     * Everywhere else indiTV just watches for the stock home screen opening, and leaves every key
     * to the TV, so holding Volume up or down keeps repeating as it should.
     */
    @Override protected void onServiceConnected() {
        super.onServiceConnected();
        boolean googleTv;
        try {
            getPackageManager().getPackageInfo(GOOGLE_TV_HOME, 0);
            googleTv = true;
        } catch (Exception e) {
            googleTv = false;
        }
        if (googleTv) return;
        android.accessibilityservice.AccessibilityServiceInfo info = getServiceInfo();
        if (info == null) return;
        info.flags &= ~android.accessibilityservice.AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
        info.packageNames = new String[]{ANDROID_TV_HOME, GOOGLE_TV_HOME};
        setServiceInfo(info);
    }

    @Override public boolean onKeyEvent(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.KEYCODE_HOME) return false;
        // Some remotes (TCL's, for one) mark ordinary presses as long presses, so every press opens indiTV.
        if (e.getAction() == KeyEvent.ACTION_UP) openHaven();
        return true;
    }

    @Override public void onAccessibilityEvent(AccessibilityEvent ev) {
        if (ev.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                && (GOOGLE_TV_HOME.contentEquals(String.valueOf(ev.getPackageName()))
                        && String.valueOf(ev.getClassName()).endsWith(".HomeActivity")
                    || ANDROID_TV_HOME.contentEquals(String.valueOf(ev.getPackageName()))
                        && String.valueOf(ev.getClassName()).endsWith(".MainActivity"))) {
            openHaven();
        }
    }

    @Override public void onInterrupt() { }

    private void openHaven() {
        startActivity(new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .setClass(this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
    }
}
