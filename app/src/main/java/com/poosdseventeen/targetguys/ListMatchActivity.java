package com.poosdseventeen.targetguys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 11/23/2015.
 */
public class ListMatchActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_matches);

        String currentUserId = ParseUser.getCurrentUser().getObjectId();
        final ArrayList<String> matches = new ArrayList<String>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        //don't include yourself
        query.whereNotEqualTo("objectId", currentUserId);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < userList.size(); i++) {
                        matches.add(userList.get(i).getUsername().toString());
                    }
                    ListView matchView = (ListView) findViewById(R.id.ViewMatches);
                    ArrayAdapter namesArrayAdapter =
                            new ArrayAdapter<String>(getApplicationContext(),
                                    R.layout.matchlistitem, matches);
                   matchView.setAdapter(namesArrayAdapter);
                    matchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                            openConversation(matches, i);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error loading user list",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void openConversation(ArrayList<String> matches, int pos) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", matches.get(pos));
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> user, ParseException e) {
                if (e == null) {
                    //start the messaging activity
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    startService(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error finding that user",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
