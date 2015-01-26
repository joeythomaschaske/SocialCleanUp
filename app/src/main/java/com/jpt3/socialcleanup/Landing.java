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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.List;
import java.util.regex.Pattern;

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
            Button clearVulgarity = null;
            View rootView = null;

            try {
                rootView = inflater.inflate(R.layout.fragment_landing, container, false);
                clearVulgarity = (Button) rootView.findViewById(R.id.vulgar_button);
                clearVulgarity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processTweets();
                    }
                });
            }
            catch (Exception e){
                Fabric.getLogger().e("Landing Exception(onCreateView) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
            }

            return rootView;
        }

        private int processTweets(){
            final TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            final StatusesService statusesService = twitterApiClient.getStatusesService();
            final TwitterSession session = Twitter.getSessionManager().getActiveSession();
            int processedTweets = -1;

            try {
                statusesService.userTimeline(session.getId(), null, null, 50L, null, null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        DictionaryService ds = new DictionaryService(getActivity());
                        List<Tweet> tweets = listResult.data;
                        List<String> contents = ds.getAllItems();
                        for (final Tweet tweet : tweets) {
                            for (String vulgar: contents) {
                                if (tweet.text.toLowerCase().contains(vulgar)) {
                                    TweetUtils.loadTweet(tweet.id, new LoadCallback<Tweet>() {
                                        @Override
                                        public void success(Tweet tweet) {
                                            FrameLayout rel = (FrameLayout) getActivity().findViewById(R.id.landing_fragment_layout);
                                            rel.addView(new TweetView(getActivity(), tweet));
                                        }

                                        @Override
                                        public void failure(TwitterException e) {
                                            Fabric.getLogger().e("Landing Exception(failure)" + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage());
                                        }
                                    });
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
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Fabric.getLogger().e("Landing Exception(failure)" + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage());
                    }
                });
            }
            catch (Exception e){
                Fabric.getLogger().e("Landing Exception(processTweets) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
            }
            return processedTweets;
        }

    }
}