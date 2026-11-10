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
import android.text.TextUtils;
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
 * corner; the clock and the date in Gujarati sit quietly at the right. Optionally a soft
 * tanpura plays (see {@link Tanpura}).
 *
 * Used by the screensaver ({@link DarshanDream}) and by the Darshan card on Home
 * ({@link DarshanActivity}), for when guests are in the room.
 */
final class Darshan {

    static final String PREF_VERSES = "darshanVerses", PREF_SOUND = "darshanSound";
    private static final long SLIDE_MS = 42_000, FADE_MS = 4_500;

    final FrameLayout root;
    private final Context c;
    private final Scene scene;
    private final LinearLayout verseBox;
    private final TextView verse, source, meaning, date, om;
    private final Handler ui = new Handler(Looper.getMainLooper());
    private final Tanpura tanpura = new Tanpura();
    private final Random random = new Random();
    private boolean running;

    private final Runnable dateTick = new Runnable() {
        @Override public void run() {
            date.setText(gujaratiDate());
            ui.postDelayed(this, 60_000);
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

        // The verse, lower left.
        verseBox = new LinearLayout(c);
        verseBox.setOrientation(LinearLayout.VERTICAL);
        source = Ui.text(c, "", 11, Ui.medium);
        source.setLetterSpacing(0.22f);
        source.setTextColor(0xB3E8C887);
        source.setAllCaps(true);
        verse = Ui.text(c, "", 30, serif);
        verse.setTextColor(0xFFF4DDA8);
        verse.setLineSpacing(Ui.dp(c, 4), 1f);
        verse.setShadowLayer(Ui.dp(c, 14), 0, 0, 0xCC000000);
        View rule = new View(c);
        rule.setBackgroundColor(0x80E8C887);
        meaning = Ui.text(c, "", 15, Ui.light);
        meaning.setTextColor(0xD9FFFFFF);
        meaning.setMaxWidth(Ui.dp(c, 520));
        meaning.setShadowLayer(Ui.dp(c, 8), 0, 0, 0xCC000000);
        verseBox.addView(source, new LinearLayout.LayoutParams(-2, -2));
        LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(-2, -2);
        vlp.topMargin = Ui.dp(c, 8);
        verseBox.addView(verse, vlp);
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(Ui.dp(c, 44), Ui.dp(c, 1));
        rlp.topMargin = Ui.dp(c, 14);
        rlp.bottomMargin = Ui.dp(c, 12);
        verseBox.addView(rule, rlp);
        verseBox.addView(meaning, new LinearLayout.LayoutParams(-2, -2));
        verseBox.setAlpha(0f);
        FrameLayout.LayoutParams blp = new FrameLayout.LayoutParams(-2, -2, Gravity.BOTTOM | Gravity.START);
        blp.leftMargin = side;
        blp.bottomMargin = Ui.dp(c, 52);
        root.addView(verseBox, blp);

        // Clock and Gujarati date, lower right.
        LinearLayout clock = new LinearLayout(c);
        clock.setOrientation(LinearLayout.VERTICAL);
        clock.setGravity(Gravity.END);
        TextClock time = new TextClock(c);
        time.setFormat12Hour("h:mm");
        time.setFormat24Hour("HH:mm");
        Ui.style(time, 52, Ui.light);
        time.setTextColor(0xF2FFFFFF);
        time.setIncludeFontPadding(false);
        date = Ui.text(c, gujaratiDate(), 15, Ui.regular);
        date.setTextColor(0xCCF4DDA8);
        clock.addView(time, new LinearLayout.LayoutParams(-2, -2));
        clock.addView(date, new LinearLayout.LayoutParams(-2, -2));
        FrameLayout.LayoutParams clp = new FrameLayout.LayoutParams(-2, -2, Gravity.BOTTOM | Gravity.END);
        clp.rightMargin = side;
        clp.bottomMargin = Ui.dp(c, 52);
        root.addView(clock, clp);

        // A small Om that breathes, upper left.
        om = Ui.text(c, "ॐ", 30, serif);
        om.setTextColor(0xFFF4DDA8);
        om.setShadowLayer(Ui.dp(c, 18), 0, 0, 0xFFE0A040);
        om.setAlpha(0.35f);
        FrameLayout.LayoutParams olp = new FrameLayout.LayoutParams(-2, -2, Gravity.TOP | Gravity.START);
        olp.leftMargin = side;
        olp.topMargin = Ui.dp(c, 40);
        root.addView(om, olp);
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
        om.animate().alpha(in ? 0.75f : 0.3f).setDuration(4_000)
                .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                .withEndAction(() -> breathe(!in)).start();
    }

    /** A new painting is fading in: change the verse with it, and nudge it a little (screen care). */
    private void onSlide(String src) {
        boolean show = c.getSharedPreferences("haven", Context.MODE_PRIVATE).getBoolean(PREF_VERSES, true);
        verseBox.animate().cancel();
        verseBox.animate().alpha(0f).setDuration(1_200).withEndAction(() -> {
            if (!show) return;
            Mantras.Verse v = Mantras.forWallpaper(src);
            verse.setText(v.text);
            source.setText(v.source);
            source.setVisibility(TextUtils.isEmpty(v.source) ? View.GONE : View.VISIBLE);
            meaning.setText(v.meaning);
            verseBox.setTranslationX(Ui.dp(c, random.nextInt(25)));
            verseBox.setTranslationY(-Ui.dp(c, random.nextInt(18)));
            verseBox.animate().alpha(1f).setStartDelay(1_800).setDuration(2_600).start();
        }).start();
    }

    /** "મંગળવાર, 10 નવેમ્બર" */
    private static String gujaratiDate() {
        return new java.text.SimpleDateFormat("EEEE, d MMMM", new Locale("gu", "IN")).format(new java.util.Date());
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
        private Slide cur, next;
        private int at = -1;
        private long slideStart, lastFrame, fadeStart;
        private boolean running, loading;

        Scene(Context c, OnSlide listener) {
            super(c);
            this.listener = listener;
            glow = makeGlow(Ui.dp(c, 18));
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

        private Spark spawn(int w, int h, boolean anywhere) {
            Spark p = new Spark();
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
                canvas.drawBitmap(glow, 0, 0, sparkPaint);
                canvas.restore();
            }
        }

        /** A soft golden point of light. */
        private static Bitmap makeGlow(int size) {
            Bitmap b = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(b);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            float r = size / 2f;
            p.setShader(new RadialGradient(r, r, r,
                    new int[]{0xFFFFF4D6, 0xCCFFC46B, 0x55FF9A2E, 0x00FF8A1E}, new float[]{0f, 0.18f, 0.45f, 1f},
                    Shader.TileMode.CLAMP));
            cv.drawCircle(r, r, r, p);
            return b;
        }
    }
}
