package tv.inditv.home;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

import java.util.Random;

/**
 * A soft tanpura drone for the Darshan screensaver, made on the TV itself (no recording): four
 * plucked strings tuned Pa, Sa, Sa and low Sa, played in the usual slow cycle, with a little
 * room echo. Sa is C# (about 138.6 Hz), close to the 136.1 Hz often used for Om. Quiet, and
 * off unless turned on in indiTV Settings > Wallpaper.
 */
final class Tanpura {

    private static final int RATE = 22_050;
    private static final float SA = 138.59f;
    /** Pa (low), Sa, Sa, Sa (low): the tanpura's four strings, in playing order. */
    private static final float[] STRINGS = {SA * 0.75f, SA, SA, SA / 2f};
    private static final float PLUCK_EVERY_S = 1.25f, PAUSE_S = 0.9f;
    private static final float VOLUME = 0.2f;

    private volatile boolean running;
    private Thread thread;

    synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this::play, "tanpura");
        thread.start();
    }

    synchronized void stop() {
        running = false;
        thread = null;
    }

    /** A Karplus-Strong string: a short delay line fed with soft noise, filtered as it loops. */
    private static final class Str {
        final float[] line;
        int at;

        Str(float freq) {
            line = new float[Math.max(2, Math.round(RATE / freq))];
        }

        void pluck(Random r) {
            for (int i = 0; i < line.length; i++) line[i] = r.nextFloat() * 2 - 1;
            for (int pass = 0; pass < 3; pass++) { // softer, rounder attack
                for (int i = 1; i < line.length; i++) line[i] = (line[i] + line[i - 1]) * 0.5f;
            }
        }

        float next() {
            int n = (at + 1) % line.length;
            float out = line[at];
            float v = 0.9985f * (0.55f * line[at] + 0.45f * line[n]);
            // A touch of the bridge's buzz (jawari): bends the loudest part of the wave a little.
            v += 0.04f * v * Math.abs(v);
            line[at] = v;
            at = n;
            return out;
        }
    }

    private void play() {
        int min = AudioTrack.getMinBufferSize(RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioTrack track;
        try {
            track = new AudioTrack.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                    .setAudioFormat(new AudioFormat.Builder().setSampleRate(RATE)
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                    .setBufferSizeInBytes(Math.max(min, RATE / 4 * 2))
                    .setTransferMode(AudioTrack.MODE_STREAM).build();
            track.play();
        } catch (Exception e) {
            running = false;
            return;
        }
        Random r = new Random();
        Str[] strings = new Str[STRINGS.length];
        for (int i = 0; i < strings.length; i++) strings[i] = new Str(STRINGS[i] * (1f + (r.nextFloat() - 0.5f) * 0.002f));
        float[] echoA = new float[(int) (RATE * 0.113f)], echoB = new float[(int) (RATE * 0.171f)];
        int ea = 0, eb = 0;
        short[] buf = new short[1024];
        long sample = 0, nextPluck = 0;
        int which = 0;
        float level = 0f, fadeIn = 1f / (RATE * 6f), fadeOut = 1f / (RATE * 1.2f);
        try {
            while (true) {
                boolean on = running;
                for (int i = 0; i < buf.length; i++, sample++) {
                    if (on && sample >= nextPluck) {
                        strings[which].pluck(r);
                        which = (which + 1) % strings.length;
                        float gap = PLUCK_EVERY_S + (which == 0 ? PAUSE_S : 0f);
                        nextPluck = sample + (long) (RATE * gap * (0.97f + r.nextFloat() * 0.06f));
                    }
                    float dry = 0f;
                    for (Str s : strings) dry += s.next();
                    dry *= 0.32f;
                    float wet = echoA[ea] * 0.32f + echoB[eb] * 0.26f;
                    echoA[ea] = dry + echoA[ea] * 0.35f;
                    echoB[eb] = dry + echoB[eb] * 0.3f;
                    ea = (ea + 1) % echoA.length;
                    eb = (eb + 1) % echoB.length;
                    level = on ? Math.min(1f, level + fadeIn) : Math.max(0f, level - fadeOut);
                    float out = (dry + wet) * VOLUME * level;
                    out = Math.max(-1f, Math.min(1f, out));
                    buf[i] = (short) (out * 32767);
                }
                track.write(buf, 0, buf.length);
                if (!on && level <= 0f) break;
            }
        } catch (Exception ignored) {
        } finally {
            try {
                track.stop();
            } catch (Exception ignored) { }
            track.release();
        }
    }
}
