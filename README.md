# indiTV

A home screen for Indian family TVs, made for a TCL 55" Google TV. It follows the family's day:
aarti and Hanuman Chalisa in the morning, Jay Adhyashakti Aarti at 7 PM, the news on Tata Play
(HDMI 1) at 8:30 PM, and Gujarati entertainment on YouTube late at night. Weekdays get a simple
home for the elders; weekends and holidays open up for everyone. When the TV rests, **Darshan**
fills the room with drifting devotional paintings, rising diya sparks and the verse for each one.

indiTV is a fork of [Haven](https://github.com/joydeepsjce/haven-tv) (MIT), a fast, ad-free
Google TV launcher, with ideas from other open-source TV launchers:
[Afnon](https://github.com/egyjs/afnon-android-tv-launcher) (the screen comes up by itself at
prayer time), [LTvLauncher](https://github.com/leanbitlab-org/LtvLauncher) (day and night
wallpapers) and [Glass TV](https://github.com/aqmangge/glass-tv-home) (a home for each person).

![Night wallpaper: Vishnu Among the Stars](docs/wallpaper-night.jpg)

**Download:** [inditv.apk](https://github.com/abhieq31/indiTV/releases/latest/download/inditv.apk)
· [All releases](https://github.com/abhieq31/indiTV/releases)

## The home screen

Calm and simple, on purpose. The top has two buttons, **Home** and **Tata Play** (HDMI 1). Then
one greeting ("Good evening, Patel family", or the festival's greeting), one row of videos for
this part of the day, the five apps, and Apps further down (indiTV Settings is its last tile).
The clock shows the time, the date and the day in the Gujarati calendar. The painting behind
is kept softly out of focus and melts into a deep shade of its own colour, so the cards sit on
calm ground.

**The row learns.** Each part of the day has a pool of hand-picked videos: prayer in the
morning and evening, thoughtful Gujarati talks and clean humour in the afternoon, gentle
entertainment at night. Home shows five of them. The ones the family plays most at that time
of day move to the front, and each day one they haven't tried yet is offered, marked **NEW**.
What is watched stays on the TV (a few counts per video; see
[Stats.java](src/tv/inditv/home/Stats.java)). When a habit forms, the heading says so quietly:
"Prayers on 5 of the last 7 days".

| Time | The pool |
|---|---|
| 4:30 AM – 11 AM | Hanuman Chalisa, Gayatri Mantra, Sundarkand, Aashapura Maa Aarti, Vaishnav Jan To, Bhagavad Gita in Gujarati, Ram Katha (Morari Bapu), Om Jai Jagdish Hare, Shrinathji dhun, Krishna bhajans |
| 11 AM – 45 min before sunset | Jay Vasavada (for senior citizens; on art), Bhagavad Gita, Ram Katha, Shahbuddin Rathod, Mayabhai Ahir, Rang Chhe Rajja, Vaishnav Jan To |
| 45 min before sunset – an hour after the aarti | **Jay Adhyashakti Aarti** (first from half an hour before the aarti), Aashapura Maa Aarti, Hanuman Chalisa, Om Jai Jagdish Hare, Shrinathji dhun, Sundarkand, Gayatri Mantra |
| After the evening aarti – 4:30 AM | **News on Tata Play** (first until 9:30), Shahbuddin Rathod, Rang Chhe Rajja, Mayabhai Ahir, Jay Vasavada, Killol garba, Ram Katha; Naval's *How to Get Rich* on weekend nights |

The row ends with **Darshan**, which starts the screensaver. **Favourites**: YouTube,
JioHotstar, SonyLIV, Apple TV, Netflix, then **All apps**, which opens the full list (and
indiTV Settings, its last tile); Home or Back puts the list away again. The family name and
every video are in [Family.java](src/tv/inditv/home/Family.java).

If indiTV ever crashes three times within three minutes, it stops taking over the Home button
for half an hour, so the TV's own home screen works and nobody is locked out.

indiTV only changes the home screen. The TV's own settings stay as they are: on Android TV it
doesn't filter the remote's keys (holding Volume repeats as usual), and it leaves the TV's
animation speed alone.

**Wallpapers:** 11 devotional paintings are built in, chosen by the time of day.

## The Gujarati calendar

indiTV keeps the Gujarati calendar itself, worked out from the Sun and the Moon, so it never
needs updating. Under the date it shows the day in Gujarati: **કારતક સુદ એકમ**, **આસો વદ બારસ**,
and so on, marked ✦ on Agiyaras, Punam and Amas. Adhik maas and the Vikram Samvat year are included.

On festivals the whole TV changes with the day: the greeting (Saal Mubarak, Jai Ambe Maa, Happy
Uttarayan, Har Har Mahadev…), the heading, Darshan's shloka and the colour of its sparks.
Diwali, Bestu Varas, Bhai Bij, Dhanteras, Kali Chaudas, Holi, Dhuleti, Uttarayan, Raksha Bandhan,
Janmashtami and Dussehra also count as everyone-home days. Also included: Navratri (each day),
Sharad Punam, Vagh Baras, Dev Uthi Agiyaras, Dev Diwali, Gita Jayanti, Vasant Panchami, Maha
Shivratri, Chaitra Navratri, Ram Navami, Hanuman Jayanti, Akha Trij, Nirjala Agiyaras, Rath
Yatra, Devshayani Agiyaras, Guru Purnima, Randhan Chhath, Shitala Satam, Ganesh Chaturthi and
Sarvapitri Amas.

The tithi is the one at sunrise in Ahmedabad, as in a printed panchang; Diwali and Maha
Shivratri go by the evening and midnight. Every build checks it against known dates
([PanchangCheck](test/tv/inditv/home/PanchangCheck.java)). Rarely, when a tithi changes within
minutes of sunrise, it can be a day off from a printed calendar.

## Darshan, the screensaver

When nothing is playing, the TV shows **Darshan**. It is kept quiet on purpose:

- the paintings drift slowly and dissolve into one another, about every 40 seconds;
- golden sparks rise like the glow of a diya (in the day's colours on festivals: red and orange
  for Navratri, every colour for Holi, more of them for Diwali);
- with each painting, the shloka that belongs to it is **written out letter by letter**; a fine
  gold line draws itself, then its meaning appears in Gujarati and in English. Only verses
  chanted everywhere, else the Gita: Hanuman Chalisa with Hanuman, the Gayatri Mantra with the
  figure rising into the sun, *paritranaya sadhunam* (4.8) with Krishna on the battlefield,
  *karmanye vadhikaraste* (2.47) with the raised wheel, *yada yada hi dharmasya* (4.7) with the
  chariot, Karpura Gauram and Om Tryambakam with Shiva and Parvati, Shri Krishna Sharanam Mama
  with the swing, the Hare Krishna maha mantra with Radha's eyes, Tvameva Mata with the sleeping
  children, Shantakaram with Vishnu. On a festival its own verse comes once, at the start;
- a small ॐ breathes in the corner; the clock, the date and one line in Gujarati (the festival,
  the tithi and the year) sit at the lower right. No greetings;
- after 10 PM it turns down: a dimmer picture and half the sparks;
- optionally, a soft **tanpura** drone (Pa Sa Sa Sa, tuned to C#), made on the TV itself.

indiTV makes Darshan the TV's screensaver by itself once Set up is done. For guests, open it any
time from the **Darshan** card on Home; any button closes it, and the volume buttons still work.
Options are in **indiTV Settings > Wallpaper**. To change a shloka, edit
[Mantras.java](src/tv/inditv/home/Mantras.java).

## Automatic times

These happen by themselves only when the TV is showing indiTV (Home, or the Darshan
screensaver). If someone is watching anything else (YouTube, Tata Play, a film), nothing
changes; the aarti or the news is simply the first card on Home when they come back.

| Time | What happens | Default |
|---|---|---|
| Sunrise | Hanuman Chalisa on YouTube | Off |
| 10 minutes after sunset | Jay Adhyashakti Aarti on YouTube (6:05 PM in November, about 7:35 PM in late June) | On |
| 8:30 PM | Switch to Tata Play (HDMI 1) | On |

Turn each one on or off in **indiTV Settings > Home screen**. If the TV is in standby nothing
happens: an app can't switch a TV on.

## Change it from your phone

Everything personal is in one file:
[`src/tv/inditv/home/Family.java`](src/tv/inditv/home/Family.java). You can change the times,
the YouTube searches, the cards and the app order there.

1. Open the file on github.com in your phone's browser and tap the pencil (Edit).
2. Change what you want, for example `19, 0` to `19, 15` to move the aarti to 7:15.
   To change which video a card plays, paste a new link (YouTube app > Share > Copy link)
   into the card's `video` field. Its thumbnail changes with it.
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
6. **Press Home.** indiTV opens. The 7 PM aarti and 8:30 PM news switch are already on, and
   Darshan is the screensaver. To choose how soon it starts: Settings > System > Ambient mode
   (or Screen saver) > When to start.
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

## Tested on

TCL 55" Android TV 9 (BeyondTV2): home screen, Home button, Darshan screensaver. The code also
handles Android 8 and Google TV on Android 10-14.

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
