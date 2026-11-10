# indiTV

A home screen for Indian family TVs, made for a TCL 55" Google TV. It follows the family's day:
aarti and Hanuman Chalisa in the morning, Jay Adhyashakti Aarti at 7 PM, the news on Tata Play
(HDMI 1) at 8:30 PM, and Gujarati entertainment on YouTube late at night, with devotional
wallpapers behind it all.

indiTV is a fork of [Haven](https://github.com/joydeepsjce/haven-tv) (MIT), a fast, ad-free
Google TV launcher, with ideas from other open-source TV launchers:
[Afnon](https://github.com/egyjs/afnon-android-tv-launcher) (the screen comes up by itself at
prayer time), [LTvLauncher](https://github.com/leanbitlab-org/LtvLauncher) (day and night
wallpapers) and [Glass TV](https://github.com/aqmangge/glass-tv-home) (a home for each person).

![Night wallpaper: Vishnu Among the Stars](docs/wallpaper-night.jpg)

**Download:** [inditv.apk](https://github.com/abhieq31/indiTV/releases/latest/download/inditv.apk)
· [All releases](https://github.com/abhieq31/indiTV/releases)

## What's on the home screen

Top to bottom:

1. **Right now** changes through the day. The cards marked **NOW** come first:

   | Time | Cards |
   |---|---|
   | 4:30 AM – 11 AM | Hanuman Chalisa, Morning Aarti, Bhajan & Satsang (Mummy) |
   | 11 AM – 6:45 PM | YouTube, Bhajan & Satsang, YouTube Shorts, Attack on Titan |
   | 6:45 PM – 8:15 PM | **Jay Adhyashakti Aarti** (first card, so Home then OK plays it) |
   | 8:15 PM – 10 PM | **News on Tata Play** (HDMI 1), Gujarati News Live |
   | 10 PM – 4:30 AM | Gujarati Comedy & Natak, Gujarati Songs |

   The rest of the family's cards follow, so the little brother's **Attack on Titan** and
   **YouTube Shorts** are always one press away. Attack on Titan opens Crunchyroll if it's
   installed, otherwise Google TV's search, which shows every app that has it.
2. **Up next**: what you were watching, with Resume.
3. **Favourites**: YouTube, JioHotstar, Prime Video, Netflix, Apple TV, then SonyLIV,
   Crunchyroll, ZEE5 and JioTV if installed. Disney's films and shows are inside JioHotstar in
   India, so there's no separate Disney tile unless the Disney+ app is installed.
4. **Inputs**: HDMI 1 (Tata Play) first.
5. **Apps**: everything else.

**Wallpapers:** 11 devotional paintings are built in. Morning shows Hanuman, Krishna and the
Wheel of Dharma; daytime shows Radha-Krishna and the Chariot of Fire; night shows Vishnu Among
the Stars and the Moonlit Lotus Lake. They change every 45 seconds and blur while you use the
remote. You can switch to Bing or Wikimedia photos, add your own, or use a Projectivy plugin in
indiTV Settings > Wallpaper. The screensaver shows them too.

## Automatic times

When the TV is on, indiTV does these by itself, over whatever is playing:

| Time | What happens | Default |
|---|---|---|
| 6:30 AM | Hanuman Chalisa on YouTube | Off |
| 7:00 PM | Jay Adhyashakti Aarti on YouTube | On |
| 8:30 PM | Switch to Tata Play (HDMI 1) | On |

Turn each one on or off in **indiTV Settings > Home screen**. If the TV is in standby nothing
happens: an app can't switch a TV on.

## Change it from your phone

Everything personal is in one file:
[`src/tv/inditv/home/Family.java`](src/tv/inditv/home/Family.java). You can change the times,
the YouTube searches, the cards and the app order there.

1. Open the file on github.com in your phone's browser and tap the pencil (Edit).
2. Change what you want, for example `19, 0` to `19, 15` to move the aarti to 7:15.
   To play one exact video instead of a search, paste its link (YouTube app > Share > Copy link)
   into the card's `video` field, the empty `""` after the search words.
3. Tap **Commit changes**. GitHub builds a new version in about 2 minutes
   (the **Actions** tab shows it).
4. The TV offers the update within 12 hours, or straight away from
   indiTV Settings > About > **Check for updates**. Android asks you to confirm it.

---

## Set it up

### Which path is my TV?

| Your TV | What runs on it | Path |
|---|---|---|
| **TCL** / iFFALCON 2021 or newer, Sony Bravia, Hisense, Acer, Vu GloLED, Xiaomi / Redmi with Google TV | Google TV | [A](#a-google-tv-tcl-sony-hisense-and-others) |
| Mi TV (PatchWall), OnePlus, Realme, Thomson, Kodak, Nokia, older TCL, Vu | Android TV 9–11 | [A](#a-google-tv-tcl-sony-hisense-and-others), same steps |
| Amazon Fire TV Stick, Fire TV Edition TVs (Redmi, Onida, Toshiba Fire TV) | Fire OS | [B](#b-fire-tv) |
| Samsung (Tizen), LG (webOS), Panasonic (my Home Screen) | Not Android | [C](#c-samsung-lg-and-other-non-android-tvs) |
| A TV that isn't smart | Nothing | [C](#c-samsung-lg-and-other-non-android-tvs) |

To check: Settings > System (or Device Preferences) > About. "Android TV OS" or "Google TV"
means path A.

### A. Google TV (TCL, Sony, Hisense and others)

This is the TCL 55" path. You need the TV remote for about 5 minutes, or the phone remote below.

**Use your phone as the remote (optional).** Install the **Google TV** app on your phone, sign in
with the TV's Google account, tap **Remote**, pick the TV and enter the code it shows. You need
the same Wi-Fi only this first time. Every step below then works from the phone, including
typing.

1. **Turn on Developer options.** Settings > System > About, then press OK on
   **Android TV OS build** seven times until it says you're a developer.
2. **Install Downloader** (by AFTVnews) from the Play Store on the TV.
3. **Let Downloader install apps.** Settings > Apps > Security & restrictions >
   Unknown sources > turn on **Downloader**. Some TCL models put this under
   Settings > Privacy > Security & restrictions.
4. **Download indiTV.** Open Downloader, choose the address box and type:

   ```
   github.com/abhieq31/indiTV/releases/latest/download/inditv.apk
   ```

   Press **Go**, then **Install**, then **Open**.
5. **Set up indiTV** opens. Turn on each item with OK:
   - **Home button**: choose **Fix**. indiTV asks you to turn on **Network debugging**
     (or **Wireless debugging**) in Settings > System > Developer options, then unlocks itself.
     When the TV asks *Allow USB debugging?*, tick **Always allow** and choose **Allow**. You can
     turn debugging off again afterwards.
   - **Continue watching**, **Weather** and **Recent apps**: choose **Allow** when asked.
   - **Install updates**: lets indiTV install its own updates; you confirm each one.
   - **Keep the Home button on**: so TCL's *Safety Guard* cleaner can't switch it off.
6. **Press Home.** indiTV opens. The 7 PM aarti and 8:30 PM news switch are already on.
7. **On the TV, once:** Settings > Channels & Inputs > Inputs: rename HDMI 1 to **Tata Play**
   and turn on **HDMI-CEC** (TCL calls it **T-Link**), so one remote can control the TV's power
   and volume with the Tata Play box.

**From a Mac or PC instead (fully remote, over Wi-Fi).** After step 1, turn on
**Network debugging** and note the TV's IP address (Settings > Network). Then, on the computer:

```sh
brew install android-platform-tools          # Windows: install "SDK Platform Tools" from Google
curl -LO https://github.com/abhieq31/indiTV/releases/latest/download/inditv.apk
git clone https://github.com/abhieq31/indiTV && cd indiTV
sh setup.sh 192.168.1.50 ../inditv.apk       # your TV's IP
```

The TV asks once to allow the computer. The script installs indiTV and grants everything in
step 5 in one go. To control the TV from outside the house, connect to your home network first
(for example with your router's VPN or Tailscale on a device at home).

**Go back to the TCL home screen:** indiTV Settings > System > **Uninstall indiTV**.

### B. Fire TV

Install **Downloader** from the Amazon Appstore, then turn on
Settings > My Fire TV > Developer options > **Install unknown apps** > Downloader. Download the
same address as step 4 above. indiTV runs as an app with everything above. Fire OS keeps the
Home button for Amazon's own home screen, so open indiTV from your apps or hold Home > Apps.
The automatic aarti and news times may not start over other apps on Fire TV; the Right now
row works as usual.

### C. Samsung, LG and other non-Android TVs

Samsung, LG and Panasonic TVs can't install Android apps, so indiTV can't run on them. Two
options:

- **Add a streaming stick** to a free HDMI port: a Google TV Streamer, a Chromecast with Google
  TV, a Fire TV Stick or a Mi TV Stick. Then follow path A or B on the stick. This is also the
  way to make an older, non-smart TV work.
- **Use the TV's own home screen:** move YouTube to the first place (Samsung: highlight it,
  press Down > Move; LG: hold OK on it > Edit), and set the 7 PM aarti as a routine in SmartThings
  (Samsung) or ThinQ (LG) if your model supports it.

---

## Permissions

indiTV asks for the same permissions as Haven: an accessibility service only for the Home button,
the TV's Continue watching rows, rough location for the weather, usage access for the Recent row,
and installing its own updates. It adds one thing: alarms for the automatic times, which need no
permission. There are no ads, no tracking and no accounts. Nothing is sent anywhere except the
weather, the optional daily photos and the update check on GitHub.

## Build

The app is built on GitHub by [`.github/workflows/build.yml`](.github/workflows/build.yml) on
every change to the app's files. It needs two repository secrets: `KEYSTORE_B64` (the signing
key, base64) and `KS_PASS` (its password). Keep a copy of the key somewhere safe: Android only
installs updates signed with the same key.

To build on your own computer you need JDK 11+, the Android SDK build-tools and
`platforms;android-34`:

```sh
ANDROID_HOME=/path/to/android-sdk sh build.sh
```

## Licence

The code is under the MIT licence ([LICENSE](LICENSE)), with Haven's copyright kept. The
wallpapers, the Inter font and the Projectivy plugin interface are covered in
[NOTICE.md](NOTICE.md). indiTV isn't affiliated with Google, TCL, Tata Play, YouTube or any
streaming service.
