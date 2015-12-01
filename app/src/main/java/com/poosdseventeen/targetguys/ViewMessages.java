package com.poosdseventeen.targetguys;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kourtney Bosshardt on 11/23/2015.
 */
public class ViewMessages extends Activity{

    private static final String TAG = ChatActivity.class.getName();
    private static String sUserId;
    public static final String USER_ID_KEY = "userId";
    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;

    ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        // User login
        if (currentUser != null) { // start with existing user
            startWithCurrentUser();




            ParseQuery<Message> query = ParseQuery.getQuery("Message");
            query.whereMatches("toUserId", currentUser.getUsername());
            // Configure limit and sort order
            query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
            query.orderByAscending("createdAt");
            // Execute query to fetch all messages from Parse asynchronously
            // This is equivalent to a SELECT query with SQL
            query.findInBackground(new FindCallback<Message>() {
                public void done(List<Message> messages, ParseException e) {
                    if (e == null) {
                        mMessages.clear();
                        mMessages.addAll(messages);
                        mAdapter.notifyDataSetChanged(); // update adapter
                        // Scroll to the bottom of the list on initial load
                        if (mFirstLoad) {
                            lvChat.setSelection(mAdapter.getCount() - 1);
                            mFirstLoad = false;
                        }

                    } else {
                        Log.d("message", "Error: " + e.getMessage());
                    }
                }
            });

        } else { // If not logged in, login as a new anonymous user
            login();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        sUserId = currentUser.getObjectId();
        setupMessagePosting();
    }

    private void setupMessagePosting() {
        lvChat = (ListView) findViewById(R.id.chat);
        mMessages = new ArrayList<Message>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(ViewMessages.this, sUserId, mMessages);
        lvChat.setAdapter(mAdapter);
    }

    private void login() {
        Intent intent = new Intent(ViewMessages.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }





}