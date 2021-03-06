package com.poosdseventeen.targetguys;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.ExpandableCategoryListAdapter;



/**
 * Created by Kourtney Bosshardt on 10/25/2015.
 */

public class CategoryActivity extends Activity {

    ExpandableCategoryListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listCategories;
    HashMap<String, List<String>> listInterests;
    List<String> userInterests = new ArrayList<String>();
    private EditText distanceEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        distanceEditText = (EditText) findViewById(R.id.distanceEditText);


        getCategoriesFromDatabase();


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



    // query the categories from the database
    private void getCategoriesFromDatabase(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.whereExists("name");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, ParseException e) {
                if (e == null) {
                    Log.d("category", "Retrieved " + categoryList.size() + " categories");
                    populateCategoriesList(categoryList);

                } else {
                    Log.d("category", "Error: " + e.getMessage());
                }
            }
        });
    }


    // populate the List<String> categories with the categories from the database
    private void populateCategoriesList(List<ParseObject> categoryList){
        listCategories = new ArrayList<String>();
        for (ParseObject category : categoryList) {
            Log.d("categoryList", category.getString("name"));
            listCategories.add(category.getString("name"));
        }

        getInterestsFromDatabase(categoryList);
    }


    // populate the List<String> interests with the interests from the database
    private void getInterestsFromDatabase(final List<ParseObject> categoryList){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Interests");
        query.whereExists("name");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> interestsList, ParseException e) {
                if (e == null) {
                    Log.d("interests", "Retrieved " + interestsList.size() + " interests");
                    relateInterestsToCategories(interestsList, categoryList);

                } else {
                    Log.d("interests", "Error: " + e.getMessage());
                }
            }
        });

    }

    // relate the interest to their designated category in the database
    private void relateInterestsToCategories(List<ParseObject> interestsList, List<ParseObject> categoryList){
        ParseRelation<ParseObject> relation;
        ParseObject category;
        ParseObject interest;
        listCategories = new ArrayList<String>();
        listInterests = new HashMap<String, List<String>>();


        for(int i = 0; i < categoryList.size(); i++) {

            //get current category
            category = categoryList.get(i);

            // put current category as header
            listCategories.add(category.getString("name"));

            List<String> interests = new ArrayList<String>();


            for (int j = 0; j < interestsList.size(); j++) {



                relation = category.getRelation("interests");

                interest = interestsList.get(j);

                // put relation in database
                if( i == interest.getInt("categoryNumber") ) {

                    relation.add(interest);

                    category.saveInBackground();

                    // add relation to interests list
                    interests.add(interest.getString("name"));

                }

            }

            // put interests list as child list for current category
            listInterests.put(listCategories.get(i), interests);
        }

        setUpAdapter();

    }


    // put the interest under each category in the expandable list adapter
    private void setUpAdapter(){
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expList);

        listAdapter = new ExpandableCategoryListAdapter(this, listCategories, listInterests);

        //setting list adapter
        expListView.setAdapter(listAdapter);


       // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
//                 Toast.makeText(getApplicationContext(),
//                 "Group Clicked " + listCategories.get(groupPosition),
//                 Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listCategories.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listCategories.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Toast.makeText(
                        getApplicationContext(),
                        listCategories.get(groupPosition)
                                + " : "
                                + listInterests.get(
                                listCategories.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();

                userInterests.add(listInterests.get(
                        listCategories.get(groupPosition)).get(
                        childPosition));



                return false;
            }
        });
    }

    // save the users interest in the database and go to the home screen
    public void saveInterests(View v){

        for (String interestName : userInterests) {
            Log.d("User chose", interestName);


            final ParseUser currentUser = ParseUser.getCurrentUser();
            final ParseRelation<ParseObject> relation = currentUser.getRelation("interests");
            ParseQuery interestQuery = ParseQuery.getQuery("Interests");
            interestQuery.whereEqualTo("name", interestName);
            interestQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object == null) {
                        Log.d("interest", "The getFirst request failed.");
                    } else {
                        relation.add(object);
                        currentUser.saveInBackground();
                    }
                }
            });

        }

        Intent intent = new Intent(CategoryActivity.this, HomeActivity.class);


        // Use TaskStackBuilder to build the back stack and get the PendingIntent
        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(intent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);


        startActivity(intent);
        finish();


    }

}