package tv.inditv.home;

import android.service.dreams.DreamService;

/** The TV's screensaver: Darshan (see {@link Darshan}). */
public class DarshanDream extends DreamService {

    private Darshan darshan;

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);
        setFullscreen(true);
        darshan = new Darshan(this);
        setContentView(darshan.root);
    }

    @Override public void onDreamingStarted() {
        super.onDreamingStarted();
        darshan.start();
        OnScreen.darshan = true;
    }

    @Override public void onDreamingStopped() {
        OnScreen.darshan = false;
        darshan.stop();
        super.onDreamingStopped();
    }

    @Override public void onDetachedFromWindow() {
        if (darshan != null) darshan.release();
        super.onDetachedFromWindow();
    }
}
