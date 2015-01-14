package com.jpt3.socialcleanup;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;


public class Landing extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_landing, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.landing_text_view);
            final TwitterSession session = Twitter.getSessionManager().getActiveSession();
            final TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            final StatusesService statusesService = twitterApiClient.getStatusesService();
            Long x = 200L;
            statusesService.userTimeline(session.getId(), null, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                @Override
                public void success(Result<List<Tweet>> listResult) {
                    System.out.println("awesome");
                    List<Tweet> tweets = listResult.data;
                    for(Tweet tweet: tweets){
                        statusesService.destroy(tweet.id, false, new Callback<Tweet>() {
                            @Override
                            public void success(Result<Tweet> tweetResult) {
                                System.out.print("");
                            }
                            @Override
                            public void failure(TwitterException e) {
                                System.out.print("");
                            }
                        });
                    }
                }

                @Override
                public void failure(TwitterException e) {
                    System.out.println("sweeeeeet");
                }
            });
            return rootView;
        }

    }
}
