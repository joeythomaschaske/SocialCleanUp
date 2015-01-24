package com.jpt3.socialcleanup;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class Landing extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_landing);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
            }
        }
        catch (Exception e){
            Fabric.getLogger().e("Landing Exception(onCreate) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            getMenuInflater().inflate(R.menu.menu_landing, menu);
        }
        catch (Exception e){
            Fabric.getLogger().e("Landing Exception(onCreateOptionsMenu) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
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
            Fabric.getLogger().e("Landing Exception(onOptionsItemSelected) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
        }
        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            final StatusesService statusesService = twitterApiClient.getStatusesService();
            final TwitterSession session = Twitter.getSessionManager().getActiveSession();
            View rootView = null;

            try {
                rootView = inflater.inflate(R.layout.fragment_landing, container, false);
                statusesService.userTimeline(session.getId(), null, null, null, 200L, null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        List<Tweet> tweets = listResult.data;
                        for (Tweet tweet : tweets) {
                            statusesService.destroy(tweet.id, false, new Callback<Tweet>() {
                                @Override
                                public void success(Result<Tweet> tweetResult) {

                                }

                                @Override
                                public void failure(TwitterException e) {
                                    Fabric.getLogger().e("Landing Exception(failure)" + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage());
                                }
                            });
                        }
                    }
                    @Override
                    public void failure(TwitterException e) {
                        Fabric.getLogger().e("Landing Exception(failure)" + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage());
                    }
                });
            }
            catch (Exception e){
                Fabric.getLogger().e("Landing Exception(onCreateView) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
            }

            return rootView;
        }

    }
}