# Third-party content

indiTV is based on [Haven](https://github.com/joydeepsjce/haven-tv) by Joydeep Paul (MIT). The MIT licence covers the code only. The following are included under their own licences.

## Wallpapers

`assets/wallpapers/` (and `docs/`, the app banner and icon, which are made from them) are the
owner's own devotional wallpapers, included for this family's TV. They are not covered by the
MIT licence; ask before reusing them.

The optional daily photos are not bundled. Bing's photo of the day and Wikimedia's Picture of the Day are fetched from
the internet while shown, and each photo's own credit and licence are displayed on screen.

## TMDB

Netflix titles and artwork in Top picks come from [TMDB](https://www.themoviedb.org) when Haven is
built with an API key. This product uses the TMDB API but is not endorsed or certified by TMDB.

## Font (`assets/fonts/`)

[Inter](https://rsms.me/inter/) by Rasmus Andersson, under the SIL Open Font License 1.1
([text](https://openfontlicense.org/open-font-license-official-text/)).

## Projectivy wallpaper plugin API (`aidl/`, `src/tv/projectivy/`)

The interface and data classes for Projectivy Launcher's wallpaper plugins come from
[spocky/projectivy-plugin-wallpaper-provider](https://github.com/spocky/projectivy-plugin-wallpaper-provider)
(Apache License 2.0). The `.aidl` file is copied as published. `Wallpaper` and `Event` are Java
rewrites of the Kotlin originals with the same parcel format. Haven isn't affiliated with Projectivy.

## Sound (`res/raw/tick.wav`)

Generated for Haven; covered by the MIT licence.
