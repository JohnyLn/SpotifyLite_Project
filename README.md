
# SpotifyLite School Project for Android Studio

This project regroup several functionality of Spotify.
we use the document [Spotify for Developers](https://developer.spotify.com/documentation/android/quick-start/)
We use also [Retrofit](http://square.github.io/retrofit/) to create Java interfaces from API .

## Libaries

You need to add somes libaries.
Retrofit, glide and converter-gson is already add on the project.

The Spotify player API [others versions](https://github.com/spotify/android-sdk/releases)
[spotify-player-24-noconnect-2.20b](https://gitlab.cs.dartmouth.edu/wubalub/LitList/raw/3fff5cbb9f01588da7f732568e89ef54154be8c7/app/libs/spotify-player-24-noconnect-2.20b.aar)
[spotify-app-remote-release-0.6.0.aar](https://github.com/spotify/android-sdk/releases/download/v0.6.0-appremote_v1.1.0-auth/spotify-app-remote-release-0.6.0.aar)


The Spotify Web API [others versions](https://github.com/spotify/android-sdk/releases)
[spotify-auth-release-1.2.3](https://github.com/spotify/android-sdk/releases/download/v7.0.0-appremote_v1.2.3-auth/spotify-auth-release-1.2.3.aar)

On android studio, add .aar with the functionnality, Add New Project on your project (SpotifyLite Project)

Libaries is already add on the `build.gradle` file in your app:

```groovy

dependencies {
     //Player API and APP REMOTE
    implementation project(':spotify-app-remote-release-0.6.0')
    implementation project(':spotify-player-24-noconnect-2.20b')
    //WEB API OAuth
    implementation project(':spotify-auth-release-1.2.3')

    // Other dependencies your app might use
    implementation 'com.squareup.retrofit2:retrofit:2.5.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.5.0'

    implementation 'com.github.bumptech.glide:glide:4.9.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.9.0'


}
