package tv.inditv.home;

/**
 * Whether indiTV itself is what's on the TV right now: the home screen, or Darshan (the
 * screensaver, or opened from Home). The automatic aarti and the Tata Play switch only happen
 * then, so they never take over a video, a match or the news someone is already watching.
 */
final class OnScreen {

    static volatile boolean home, darshan;

    private OnScreen() { }

    static boolean indiTv() {
        return home || darshan;
    }
}
