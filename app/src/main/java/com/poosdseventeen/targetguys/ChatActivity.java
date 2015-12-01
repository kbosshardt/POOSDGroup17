package com.poosdseventeen.targetguys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 11/23/2015.
 */
public class ChatActivity extends Activity{
        //extends Service implements SinchClientListener{
   /* private static final String APP_KEY = "630ec13e-771c-4e28-b06a-ccef9b1d31b9";
    private static final String APP_SECRET = "tCYDvA0OLUObZrDWF8pbPQ==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    private final ChatInterface serviceInterface = new ChatInterface();
    private SinchClient sinchClient = null;
    private MessageClient messageClient = null;
    private String currentUserId;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //get the current user id from Parse
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        if (currentUserId != null && !isSinchClientStarted()) {
            startSinchClient(currentUserId);
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public void startSinchClient(String username) {
        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(username)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();
        //this client listener requires that you define
        //a few methods below
        sinchClient.addSinchClientListener(this);
        //messaging is "turned-on", but calling is not
        sinchClient.setSupportMessaging(true);
        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.checkManifest();
        sinchClient.start();
    }
    private boolean isSinchClientStarted() {
        return sinchClient != null && sinchClient.isStarted();
    }
    //The next 5 methods are for the sinch client listener
    @Override
    public void onClientFailed(SinchClient client, SinchError error) {
        sinchClient = null;
    }
    @Override
    public void onClientStarted(SinchClient client) {
        client.startListeningOnActiveConnection();
        messageClient = client.getMessageClient();
    }
    @Override
    public void onClientStopped(SinchClient client) {
        sinchClient = null;
    }
    @Override
    public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration clientRegistration) {}
    @Override
    public void onLogMessage(int level, String area, String message) {}
    @Override
    public IBinder onBind(Intent intent) {
        return serviceInterface;
    }
    public void sendMessage(String recipientUserId, String textBody) {
        if (messageClient != null) {
            WritableMessage message = new WritableMessage(recipientUserId, textBody);
            messageClient.send(message);
        }
    }
    public void addMessageClientListener(MessageClientListener listener) {
        if (messageClient != null) {
            messageClient.addMessageClientListener(listener);
        }
    }
    public void removeMessageClientListener(MessageClientListener listener) {
        if (messageClient != null) {
            messageClient.removeMessageClientListener(listener);
        }
    }
    @Override
    public void onDestroy() {
        sinchClient.stopListeningOnActiveConnection();
        sinchClient.terminate();
    }
    //public interface for ListUsersActivity & MessagingActivity
    public class ChatInterface extends Binder {
        public void sendMessage(String recipientUserId, String textBody) {
            ChatActivity.this.sendMessage(recipientUserId, textBody);
        }
        public void addMessageClientListener(MessageClientListener listener) {
            ChatActivity.this.addMessageClientListener(listener);
        }
        public void removeMessageClientListener(MessageClientListener listener) {
            ChatActivity.this.removeMessageClientListener(listener);
        }
        public boolean isSinchClientStarted() {
            return ChatActivity.this.isSinchClientStarted();
        }
    }*/

    private static final String TAG = ChatActivity.class.getName();
    private static String sUserId;
    public static final String USER_ID_KEY = "userId";
    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;
    private EditText etMessage;
    private Button btSend;



    private static final String TO_USER_ID_KEY = "toUserId";
    private String toUserId;
    private String fromUserId;
    ParseUser currentUser = ParseUser.getCurrentUser();
    private Button viewMessagesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // User login
        if (currentUser != null) { // start with existing user
            Intent intent1 = getIntent();
            toUserId = intent1.getStringExtra("selectedUser");
            fromUserId = intent1.getStringExtra("fromUser");

            startWithCurrentUser();
            receiveMessage();
        } else { // If not logged in, login as a new anonymous user
            login();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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


    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        sUserId = currentUser.getObjectId();
        setupMessagePosting();
    }

    private void setupMessagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<Message>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(ChatActivity.this, sUserId, mMessages);
        lvChat.setAdapter(mAdapter);


        btSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                // Use Message model to create new messages now
                Message message = new Message();
                message.setUserId(sUserId);
                message.put("toUserId", toUserId);
                message.put("fromUserId", fromUserId);
                message.saveInBackground();
                message.setBody(body);
                message.pinInBackground();
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        reload();

                        //receiveMessage();

                    }
                });
                etMessage.setText("");


            }
        });


    }

    private void reload(){
        ParseQuery<Message> query = ParseQuery.getQuery("Message");
        query.whereMatches("toUserId", toUserId);
        query.whereMatches("fromUserId", currentUser.getUsername());
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    //mMessages.clear();
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
    }





    private void receiveMessage() {
        // Construct query to execute
        //ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        ParseQuery<Message> query = ParseQuery.getQuery("Message");
        query.whereMatches("toUserId", toUserId);
        query.whereMatches("fromUserId", currentUser.getUsername());
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
    }

    private void login() {
        Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
        //};
   // }
//}