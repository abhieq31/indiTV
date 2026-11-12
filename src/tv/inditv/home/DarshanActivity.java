package tv.inditv.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Darshan on demand, from the Darshan card on Home: for when guests are in the room. Stays on
 * until any button on the remote is pressed.
 */
public class DarshanActivity extends Activity {

    private Darshan darshan;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        darshan = new Darshan(this);
        darshan.root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(darshan.root);
    }

    @Override protected void onResume() {
        super.onResume();
        darshan.start();
        OnScreen.darshan = true;
    }

    @Override protected void onPause() {
        OnScreen.darshan = false;
        darshan.stop();
        super.onPause();
    }

    @Override protected void onDestroy() {
        darshan.release();
        super.onDestroy();
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) return super.onKeyDown(keyCode, event);
        finish();
        return true;
    }
}
