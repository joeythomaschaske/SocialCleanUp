package com.jpt3.socialcleanup;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


public class MainActivity extends ActionBarActivity {

    private static final String TWITTER_KEY = "5itdtl3F7tUUECjNoMPfoileR";
    private static final String TWITTER_SECRET = "tpMxSWJBehMVyNiAEfHi8isFxQ4P3ZGio0jARZ4R0cvdd9ZCvu";
    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TwitterAuthConfig authConfig = null;

        try{
            super.onCreate(savedInstanceState);
            authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(this, new Twitter(authConfig), new Crashlytics());
            setContentView(R.layout.activity_main);
            loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    Intent landingIntent = new Intent(MainActivity.this, Landing.class);
                    startActivity(landingIntent);
                }

                @Override
                public void failure(TwitterException e) {
                    Fabric.getLogger().e("MainActivity Exception(failure)", e.getMessage());
                }
            });
        }
        catch(Exception e){
            Fabric.getLogger().e("MainActicivity Exception(onCreate) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);
            loginButton.onActivityResult(requestCode, resultCode, data);
        }
        catch (Exception e){
            Fabric.getLogger().e("MainActicivity Exception(onActivityResult) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        catch (Exception e){
            Fabric.getLogger().e("MainActicivity Exception(onCreateOptionsMenu) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = -1;

        try {
            id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
        }
        catch (Exception e){
            Fabric.getLogger().e("MainActicivity Exception(onOptionsItemSelected) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
        }
        return super.onOptionsItemSelected(item);
    }
}
