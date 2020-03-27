package fr.esilv.spotifylite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.content.Intent;
import android.view.View;
import android.widget.Button;


import android.util.Log;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;


import com.spotify.protocol.types.PlayerState;


import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;


import android.view.Menu;
import android.widget.ImageView;


public class MenuActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback{

    //API Information with the ID KEY___________________________
    //https://developer.spotify.com/dashboard/applications

    private static final String CLIENT_ID = "3b5446bb2fee48b787fef966f279592d";
    private static final String REDIRECT_URI = "fr.esilv.spotifylite://callback";
    private static final int REQUEST_CODE = 1337;

    //Scope will be use in the authentification
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private";


    //Button
    Button playbtn;

    Button pausebtn;
    Button resumebtn;




    //The spotify Player___________________________________
    public static Player mPlayer;
    private SpotifyAppRemote mSpotifyAppRemote = null;
    private Player.OperationCallback spotifyPlayerOperationCallback;

    //Set Player set https://github.com/spotify/android-sdk/blob/master/app-remote-sample/src/main/java/com/spotify/sdk/demo/RemotePlayerActivity.java
    PlayerState mPlayerState;





    static public Player getmPlayer()
    {
        return mPlayer;
    }


    String URI_MUSIC = "spotify:playlist:6u95z1h0zjH3k9n7phR04x";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Fragment selectedFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();


        //Spotify Auth
        // First step
        // Authenticate the User
        //Only work with a Premium account
        //To see your connexion https://accounts.spotify.com
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        //The setScopes method defines what scopes your app needs the user to approve.

        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);

        //The Android SDK uses the same authentication and authorization process as the Spotify Web API. For detailed information, see the Web API Authorization.


        // Player Listener Pause | Play | Resume
        //Pause
        playbtn = findViewById(R.id.playbtn);
        resumebtn = findViewById(R.id.resumebtn);
        pausebtn = findViewById(R.id.pausebtn);

        pausebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.pause(spotifyPlayerOperationCallback);
                //mPlayer.playUri(null, "spotify:playlist:6u95z1h0zjH3k9n7phR04x", 0, 0);

                resumebtn.setVisibility(View.VISIBLE);
                pausebtn.setVisibility(View.INVISIBLE);

            }
        });

        //Play

        playbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.playUri(null, URI_MUSIC, 0, 0);
                pausebtn.setVisibility(View.VISIBLE);
                // Now you can start interacting with App Remote


            }
        });

        //Resume
        resumebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.resume(spotifyPlayerOperationCallback);
                pausebtn.setVisibility(View.VISIBLE);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.overflow_about){
            Intent myIntent = new Intent(MenuActivity.this, About.class);
            startActivity(myIntent);
            return false;
        }
        if(id == R.id.overflow_deco){
            onDestroy();
            Intent myIntent = new Intent(MenuActivity.this, MainActivity.class);
           // mPlayer.logout();
            startActivity(myIntent);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;


                    switch(item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            //startActivity(new Intent(MenuActivity.this, MenuActivity.class));
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();

                            break;


                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };


    //SPOTIFY


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthorizationResponse.Type.TOKEN) {
                //The Config object received the OAuth access token provied by the callback
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                //We can get the player by using the SPotify class : https://spotify.github.io/android-sdk/player/com/spotify/sdk/android/player/Spotify.html
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MenuActivity.this);
                        mPlayer.addNotificationCallback(MenuActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("Connexion", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }


    //After Login
    /*
    All calls are asynchronous. The player runs in a separate thread from your main app thread, so it’s safe to call player methods from your GUI.
    There may be a delay in execution. Due to non-blocking requests and our SDK having commands to process on initialisation, there might be an initial small delay.
    You can now play a song. You’ll need to call mPlayer.playURI with a Spotify URI (Example: spotify:track:...) to play a song. This is what URIs we support.
    Destroy the player when you’re done. When you don’t need the player, dispose of it using Spotify.destroyPlayer. Typically it is done inside of the onDestroy callback.
     */

    //APP REMOTE ___________________________________________________________
    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");


        // This is the line that plays a song.
        Log.d("MainActivity", "Start Music");


        //Getting Track Info --> On Pause not use__________________
        //The Spotify Android SDK is composed by other small APIs, for example ImagesApi which combined with the PlayerApi
        // allow you can get the image of the current track playing
        //https://tolkiana.com/how-to-use-spotifys-sdk-in-kotlin/
        //https://github.com/spotify/android-sdk/tree/master/app-remote-lib
        //https://github.com/spotify/android-sdk/blob/master/app-remote-sample/src/main/java/com/spotify/sdk/demo/RemotePlayerActivity.java
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);


        ConnectionParams connectionParams = new ConnectionParams.Builder(CLIENT_ID).setRedirectUri(REDIRECT_URI).build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");


                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });

        ///Not Use ////_________________________________________________

    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error var1) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }















}
