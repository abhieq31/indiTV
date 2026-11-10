package tv.inditv.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;

/**
 * Darshan: indiTV's screensaver. The devotional paintings drift slowly (a Ken Burns move) and
 * dissolve into one another, golden sparks rise like the glow of a diya, and each painting's
 * verse appears beneath it in its own script, with its meaning. A small Om breathes in the
 * corner; the clock and the date sit quietly at the right. Optionally a soft
 * tanpura plays (see {@link Tanpura}).
 *
 * Used by the screensaver ({@link DarshanDream}) and by the Darshan card on Home
 * ({@link DarshanActivity}), for when guests are in the room.
 */
final class Darshan {

    static final String PREF_VERSES = "darshanVerses", PREF_SOUND = "darshanSound";
    private static final long SLIDE_MS = 42_000, FADE_MS = 4_500;
    /** How long each letter of a shloka takes to be written, and how many letters the ink takes to settle. */
    private static final long LETTER_MS = 110;
    private static final float INK = 3f;

    final FrameLayout root;
    private final Context c;
    private final Scene scene;
    private final LinearLayout verseBox;
    private final TextView verse, meaning, greet, date, tithi, festival, om;
    private final Handler ui = new Handler(Looper.getMainLooper());
    private final Tanpura tanpura = new Tanpura();
    private final Random random = new Random();
    private final int ink = 0xFFF4DDA8;
    private boolean running;
    private int slides;
    private String writing = "";
    private int[] clusters = new int[0];
    private long writeStart;

    private final Runnable dateTick = new Runnable() {
        @Override public void run() {
            showDay();
            ui.postDelayed(this, 60_000);
        }
    };

    /** Writes the shloka a letter at a time, as if by hand, each letter's ink settling as it goes. */
    private final Runnable pen = new Runnable() {
        @Override public void run() {
            float at = (android.os.SystemClock.uptimeMillis() - writeStart) / (float) LETTER_MS;
            int n = clusters.length;
            android.text.SpannableString sp = new android.text.SpannableString(writing);
            int start = 0;
            for (int i = 0; i < n; i++) {
                int end = clusters[i];
                float a = Math.max(0f, Math.min(1f, (at - i) / INK));
                if (a < 1f) {
                    int color = ((int) (255 * a) << 24) | (ink & 0xFFFFFF);
                    sp.setSpan(new android.text.style.ForegroundColorSpan(color), start, end,
                            android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                start = end;
            }
            verse.setText(sp);
            if (at < n + INK) {
                ui.postDelayed(this, 33);
            } else {
                meaning.animate().alpha(1f).setDuration(1_800).start();
            }
        }
    };

    Darshan(Context c) {
        this.c = c;
        Ui.init(c);
        root = new FrameLayout(c);
        root.setBackgroundColor(Color.BLACK);
        scene = new Scene(c, this::onSlide);
        root.addView(scene, new FrameLayout.LayoutParams(-1, -1));

        int side = Ui.dp(c, 64);
        Typeface serif = Typeface.create("serif", Typeface.NORMAL);

        // The shloka, lower left: the verse, and its meaning in one quiet line.
        verseBox = new LinearLayout(c);
        verseBox.setOrientation(LinearLayout.VERTICAL);
        verse = Ui.text(c, "", 30, serif);
        verse.setTextColor(ink);
        verse.setLineSpacing(Ui.dp(c, 6), 1f);
        verse.setShadowLayer(Ui.dp(c, 16), 0, 0, 0xE6000000);
        meaning = Ui.text(c, "", 14, Ui.light);
        meaning.setTextColor(0xB3FFFFFF);
        meaning.setMaxWidth(Ui.dp(c, 560));
        meaning.setShadowLayer(Ui.dp(c, 8), 0, 0, 0xCC000000);
        verseBox.addView(verse, new LinearLayout.LayoutParams(-2, -2));
        LinearLayout.LayoutParams mlp = new LinearLayout.LayoutParams(-2, -2);
        mlp.topMargin = Ui.dp(c, 14);
        verseBox.addView(meaning, mlp);
        FrameLayout.LayoutParams blp = new FrameLayout.LayoutParams(-2, -2, Gravity.BOTTOM | Gravity.START);
        blp.leftMargin = side;
        blp.bottomMargin = Ui.dp(c, 56);
        root.addView(verseBox, blp);

        // Clock, date and the day in the Gujarati calendar, lower right.
        LinearLayout clock = new LinearLayout(c);
        clock.setOrientation(LinearLayout.VERTICAL);
        clock.setGravity(Gravity.END);
        TextClock time = new TextClock(c);
        time.setFormat12Hour("h:mm");
        time.setFormat24Hour("HH:mm");
        Ui.style(time, 46, Ui.light);
        time.setTextColor(0xF2FFFFFF);
        time.setIncludeFontPadding(false);
        date = Ui.text(c, "", 14, Ui.regular);
        date.setTextColor(0xCCFFFFFF);
        tithi = Ui.text(c, "", 14, Ui.regular);
        tithi.setTextColor(0xD9F4DDA8);
        festival = Ui.text(c, "", 16, Ui.medium);
        festival.setTextColor(ink);
        greet = Ui.text(c, "", 15, Ui.light);
        greet.setTextColor(0xCCFFFFFF);
        LinearLayout.LayoutParams glp = new LinearLayout.LayoutParams(-2, -2);
        glp.bottomMargin = Ui.dp(c, 4);
        clock.addView(greet, glp);
        clock.addView(time, new LinearLayout.LayoutParams(-2, -2));
        clock.addView(date, new LinearLayout.LayoutParams(-2, -2));
        LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(-2, -2);
        tlp.topMargin = Ui.dp(c, 2);
        clock.addView(tithi, tlp);
        clock.addView(festival, new LinearLayout.LayoutParams(-2, -2));
        FrameLayout.LayoutParams clp = new FrameLayout.LayoutParams(-2, -2, Gravity.BOTTOM | Gravity.END);
        clp.rightMargin = side;
        clp.bottomMargin = Ui.dp(c, 56);
        root.addView(clock, clp);

        // A small Om that breathes, upper left.
        om = Ui.text(c, "ॐ", 28, serif);
        om.setTextColor(ink);
        om.setShadowLayer(Ui.dp(c, 18), 0, 0, 0xFFE0A040);
        om.setAlpha(0.3f);
        om.setPadding(Ui.dp(c, 24), Ui.dp(c, 24), Ui.dp(c, 24), Ui.dp(c, 24)); // room for its glow
        FrameLayout.LayoutParams olp = new FrameLayout.LayoutParams(-2, -2, Gravity.TOP | Gravity.START);
        olp.leftMargin = side - Ui.dp(c, 24);
        olp.topMargin = Ui.dp(c, 16);
        root.addView(om, olp);
        showDay();
    }

    void start() {
        if (running) return;
        running = true;
        scene.start();
        breathe(true);
        ui.post(dateTick);
        if (c.getSharedPreferences("haven", Context.MODE_PRIVATE).getBoolean(PREF_SOUND, false)) tanpura.start();
    }

    void stop() {
        running = false;
        scene.stop();
        om.animate().cancel();
        ui.removeCallbacksAndMessages(null);
        tanpura.stop();
    }

    void release() {
        stop();
        scene.release();
    }

    private void breathe(boolean in) {
        if (!running) return;
        om.animate().alpha(in ? 0.7f : 0.25f).setDuration(4_000)
                .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                .withEndAction(() -> breathe(!in)).start();
    }

    /** The date, today's tithi, and the festival if there is one; the sparks take the day's mood. */
    private void showDay() {
        greet.setText(Family.greeting()); // "Good evening, Patel family"
        date.setText(new java.text.SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH).format(new java.util.Date()));
        try {
            Panchang.Day d = Panchang.today();
            tithi.setText(d.label() + " · VS " + d.samvat);
            festival.setText(d.festival != null ? d.festival.name : "");
            festival.setVisibility(d.festival != null ? View.VISIBLE : View.GONE);
            scene.setMood(d.mood());
        } catch (RuntimeException e) {
            tithi.setVisibility(View.GONE);
            festival.setVisibility(View.GONE);
        }
    }

    /** A new painting is fading in: write its shloka (or, on a festival, every other time the day's own). */
    private void onSlide(String src) {
        boolean show = c.getSharedPreferences("haven", Context.MODE_PRIVATE).getBoolean(PREF_VERSES, true);
        ui.removeCallbacks(pen);
        slides++;
        verseBox.animate().cancel();
        verseBox.animate().alpha(0f).setDuration(1_200).withEndAction(() -> {
            if (!show) return;
            Mantras.Verse v = Mantras.forWallpaper(src);
            try {
                Mantras.Verse f = Mantras.forDay(Panchang.today());
                if (f != null && slides % 2 == 1) v = f;
            } catch (RuntimeException ignored) { }
            writing = v.text;
            clusters = clusterEnds(v.text);
            meaning.setText(v.meaning);
            meaning.setAlpha(0f);
            verse.setText("");
            verseBox.setTranslationX(Ui.dp(c, random.nextInt(25)));
            verseBox.setTranslationY(-Ui.dp(c, random.nextInt(18)));
            verseBox.setAlpha(1f);
            writeStart = android.os.SystemClock.uptimeMillis() + 1_600; // once the painting has settled
            ui.postDelayed(pen, 1_600);
        }).start();
    }

    /** Where each letter ends, keeping Devanagari conjuncts and matras whole (never half a letter). */
    private static int[] clusterEnds(String s) {
        android.icu.text.BreakIterator b = android.icu.text.BreakIterator.getCharacterInstance();
        b.setText(s);
        java.util.List<Integer> ends = new ArrayList<>();
        for (int e = b.next(); e != android.icu.text.BreakIterator.DONE; e = b.next()) ends.add(e);
        int[] out = new int[ends.size()];
        for (int i = 0; i < out.length; i++) out[i] = ends.get(i);
        return out;
    }

    // ------------------------------------------------------------------ the moving picture

    /** The paintings with their slow drift and dissolve, the vignette, and the rising sparks. */
    private static final class Scene extends View {

        interface OnSlide {
            void onSlide(String src);
        }

        private static final class Slide {
            final String src;
            final Bitmap bitmap;
            final float s0, s1, x0, x1, y0, y1;

            Slide(String src, Bitmap bitmap, Random r) {
                this.src = src;
                this.bitmap = bitmap;
                boolean in = r.nextBoolean(); // zoom in or out, alternately at random
                float lo = 1.04f, hi = 1.16f;
                s0 = in ? lo : hi;
                s1 = in ? hi : lo;
                x0 = r.nextFloat() * 2 - 1;
                x1 = -x0 * 0.6f;
                y0 = (r.nextFloat() * 2 - 1) * 0.6f;
                y1 = -y0 * 0.5f;
            }
        }

        private static final class Spark {
            float x, y, vy, sway, phase, size, life, age;
            int kind;
        }

        private final OnSlide listener;
        private final ExecutorService bg = HavenBitmaps.background(1, "darshan");
        private final Random random = new Random();
        private final Paint photo = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        private final Paint shade = new Paint(), bottom = new Paint(), sparkPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        private final Matrix m = new Matrix();
        private final List<String> order = new ArrayList<>();
        private final List<Spark> sparks = new ArrayList<>();
        private Bitmap glow;
        private Bitmap[] glows;
        private Panchang.Mood mood;
        private Slide cur, next;
        private int at = -1;
        private long slideStart, lastFrame, fadeStart;
        private boolean running, loading;

        Scene(Context c, OnSlide listener) {
            super(c);
            this.listener = listener;
            glow = makeGlow(Ui.dp(c, 18), 0xFFFFF4D6, 0xCCFFC46B, 0x55FF9A2E);
            glows = new Bitmap[]{glow};
        }

        void start() {
            running = true;
            lastFrame = SystemClock.uptimeMillis();
            if (cur == null) loadNext(true);
            postInvalidateOnAnimation();
        }

        void stop() {
            running = false;
        }

        void release() {
            running = false;
            bg.shutdownNow();
            cur = next = null;
        }

        /** This part of the day's paintings first (morning, day, night), then the rest. */
        private void makeOrder() {
            order.clear();
            List<String> now = new ArrayList<>(), rest = new ArrayList<>();
            String p = Family.wallpaperPeriod();
            try {
                String[] names = getContext().getAssets().list("wallpapers");
                if (names != null) for (String n : names) (n.startsWith(p + "-") ? now : rest).add("wallpapers/" + n);
            } catch (Exception ignored) { }
            Collections.shuffle(now, random);
            Collections.shuffle(rest, random);
            order.addAll(now);
            order.addAll(rest);
        }

        private void loadNext(boolean first) {
            if (loading) return;
            loading = true;
            bg.execute(() -> {
                if (order.isEmpty() || at + 1 >= order.size()) {
                    makeOrder();
                    at = -1;
                }
                Slide s = null;
                for (int tries = 0; s == null && tries < order.size(); tries++) {
                    at = (at + 1) % Math.max(1, order.size());
                    String src = order.get(at);
                    Bitmap b = decode(src);
                    if (b != null) s = new Slide(src, b, random);
                }
                Slide got = s;
                post(() -> {
                    loading = false;
                    if (got == null) return;
                    if (first || cur == null) {
                        cur = got;
                        slideStart = SystemClock.uptimeMillis();
                        listener.onSlide(got.src);
                        loadNext(false);
                    } else {
                        next = got;
                    }
                });
            });
        }

        private Bitmap decode(String asset) {
            try (InputStream in = getContext().getAssets().open(asset)) {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inPreferredConfig = Bitmap.Config.ARGB_8888; // no banding in the dark skies
                return BitmapFactory.decodeStream(in, null, o);
            } catch (Exception | OutOfMemoryError e) {
                return null;
            }
        }

        @Override protected void onSizeChanged(int w, int h, int ow, int oh) {
            float r = (float) Math.hypot(w, h) / 2f;
            shade.setShader(new RadialGradient(w / 2f, h * 0.45f, r,
                    new int[]{0x00000000, 0x00000000, 0x66000000, 0xD9000000}, new float[]{0f, 0.45f, 0.78f, 1f},
                    Shader.TileMode.CLAMP));
            bottom.setShader(new LinearGradient(0, h, 0, h * 0.55f, 0xB3000000, 0x00000000, Shader.TileMode.CLAMP));
            sparks.clear();
            int count = Math.max(24, w / Ui.dp(getContext(), 34));
            if (mood == Panchang.Mood.DIYA) count = count * 3 / 2;
            for (int i = 0; i < count; i++) sparks.add(spawn(w, h, true));
        }

        @Override protected void onDraw(Canvas canvas) {
            long now = SystemClock.uptimeMillis();
            float dt = Math.min(0.1f, (now - lastFrame) / 1000f);
            lastFrame = now;
            int w = getWidth(), h = getHeight();
            if (cur != null) {
                long age = now - slideStart;
                draw(canvas, cur, Math.min(1f, age / (float) SLIDE_MS), 255);
                // The next painting dissolves in over the last seconds (later if it's still loading).
                if (fadeStart == 0 && next != null && age > SLIDE_MS - FADE_MS) fadeStart = now;
                if (fadeStart > 0 && next != null) {
                    float f = Math.min(1f, (now - fadeStart) / (float) FADE_MS);
                    float ease = f * f * (3 - 2 * f);
                    draw(canvas, next, (now - fadeStart) / (float) SLIDE_MS, (int) (255 * ease));
                    if (f >= 1f) {
                        cur = next;
                        next = null;
                        slideStart = fadeStart; // its drift carries on without a jump
                        fadeStart = 0;
                        listener.onSlide(cur.src);
                        loadNext(false);
                    }
                }
            }
            canvas.drawRect(0, 0, w, h, shade);
            canvas.drawRect(0, h * 0.55f, w, h, bottom);
            drawSparks(canvas, w, h, dt);
            if (running) postInvalidateOnAnimation();
        }

        /** One painting, filling the screen, at {@code t} (0..1) through its slow drift. */
        private void draw(Canvas canvas, Slide s, float t, int alpha) {
            int w = getWidth(), h = getHeight();
            Bitmap b = s.bitmap;
            float e = t * t * (3 - 2 * t) * 0.6f + t * 0.4f; // gentle at both ends
            float fill = Math.max(w / (float) b.getWidth(), h / (float) b.getHeight());
            float scale = fill * (s.s0 + (s.s1 - s.s0) * e);
            float bw = b.getWidth() * scale, bh = b.getHeight() * scale;
            float spareX = (bw - w) / 2f, spareY = (bh - h) / 2f;
            float ox = (s.x0 + (s.x1 - s.x0) * e) * spareX, oy = (s.y0 + (s.y1 - s.y0) * e) * spareY;
            m.reset();
            m.setScale(scale, scale);
            m.postTranslate((w - bw) / 2f + ox, (h - bh) / 2f + oy);
            photo.setAlpha(alpha);
            canvas.drawBitmap(b, m, photo);
        }

        // ------------------------------------------------------------ sparks

        /**
         * The day's sparks: diya gold most days; reds and oranges for Navratri, every colour for
         * Holi, peacock blue among the gold for Krishna's days, moonlight for Shiva's, a bright
         * sky for Uttarayan. Diwali and the New Year get more of them.
         */
        void setMood(Panchang.Mood m) {
            if (m == mood) return;
            mood = m;
            int s = glow.getWidth();
            switch (m) {
                case GARBA: glows = new Bitmap[]{glow, makeGlow(s, 0xFFFFE0D0, 0xCCFF5A3C, 0x55D01E3A),
                        makeGlow(s, 0xFFFFF0C8, 0xCCFF9F1A, 0x55E0561A)}; break;
                case HOLI: glows = new Bitmap[]{makeGlow(s, 0xFFFFE6F2, 0xCCFF4FA3, 0x55E0287A),
                        makeGlow(s, 0xFFE6FFF0, 0xCC3CE08A, 0x5520A060), makeGlow(s, 0xFFE6F0FF, 0xCC4F8CFF, 0x552850D0),
                        makeGlow(s, 0xFFFFF6D6, 0xCCFFD23C, 0x55E0A010)}; break;
                case KRISHNA: glows = new Bitmap[]{glow, glow, makeGlow(s, 0xFFE0F6FF, 0xCC2FB5C8, 0x55206E9A)}; break;
                case SHIVA: glows = new Bitmap[]{makeGlow(s, 0xFFFFFFFF, 0xCCD6E4FF, 0x557C95D0), glow}; break;
                case KITE: glows = new Bitmap[]{makeGlow(s, 0xFFFFFFFF, 0xCCBFE6FF, 0x5560A8E0), glow}; break;
                default: glows = new Bitmap[]{glow}; break;
            }
            if (getWidth() > 0) onSizeChanged(getWidth(), getHeight(), 0, 0);
        }

        private Spark spawn(int w, int h, boolean anywhere) {
            Spark p = new Spark();
            p.kind = random.nextInt(glows.length);
            float d = getResources().getDisplayMetrics().density;
            p.x = random.nextFloat() * w;
            p.y = anywhere ? random.nextFloat() * h : h + random.nextFloat() * 40 * d;
            p.vy = (10 + random.nextFloat() * 22) * d;
            p.sway = (6 + random.nextFloat() * 18) * d;
            p.phase = random.nextFloat() * 6.28f;
            p.size = 0.35f + random.nextFloat() * 0.9f;
            p.life = 9 + random.nextFloat() * 14;
            p.age = anywhere ? random.nextFloat() * p.life : 0;
            return p;
        }

        private void drawSparks(Canvas canvas, int w, int h, float dt) {
            float gw = glow.getWidth();
            for (int i = 0; i < sparks.size(); i++) {
                Spark p = sparks.get(i);
                p.age += dt;
                p.y -= p.vy * dt;
                p.phase += dt * 0.7f;
                if (p.age > p.life || p.y < -gw) {
                    sparks.set(i, spawn(w, h, false));
                    continue;
                }
                float life = p.age / p.life;
                float fade = Math.min(1f, Math.min(life * 5f, (1f - life) * 3f));
                float twinkle = 0.65f + 0.35f * (float) Math.sin(p.phase * 3.1f);
                // Brighter low in the frame, like warmth rising from a lamp, fading as they climb.
                float height = Math.max(0f, Math.min(1f, p.y / h));
                int a = (int) (255 * fade * twinkle * (0.25f + 0.75f * height));
                if (a <= 2) continue;
                float x = p.x + (float) Math.sin(p.phase) * p.sway;
                float size = gw * p.size;
                sparkPaint.setAlpha(a);
                canvas.save();
                canvas.translate(x - size / 2f, p.y - size / 2f);
                canvas.scale(p.size, p.size);
                canvas.drawBitmap(glows[Math.min(p.kind, glows.length - 1)], 0, 0, sparkPaint);
                canvas.restore();
            }
        }

        /** A soft golden point of light. */
        private static Bitmap makeGlow(int size, int core, int mid, int edge) {
            Bitmap b = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(b);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            float r = size / 2f;
            p.setShader(new RadialGradient(r, r, r,
                    new int[]{core, mid, edge, edge & 0x00FFFFFF}, new float[]{0f, 0.18f, 0.45f, 1f},
                    Shader.TileMode.CLAMP));
            cv.drawCircle(r, r, r, p);
            return b;
        }
    }
}
