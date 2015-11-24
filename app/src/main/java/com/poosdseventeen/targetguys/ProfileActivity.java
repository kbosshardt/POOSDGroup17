package com.poosdseventeen.targetguys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private String userName;
    private String userUsername;
    private String userGender;
    private ImageView userPicture;
    private ParseFile profilePictureFile;
    private TextView nameTextView;
    private TextView usernameTextView;
    private TextView genderTextView;
    ParseFile photoFile;
    protected ParseUser currentUser;
    protected ParseUser user;
    protected User parseUser;
    private ParseRelation relation;
    private ParseQuery interestQuery;

    private Spinner interestSpinner;

    static final int RESULT_LOAD_IMAGE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = (TextView) findViewById(R.id.userName);
        usernameTextView = (TextView) findViewById(R.id.userUsername);
        genderTextView = (TextView) findViewById(R.id.userGender);
        userPicture = (ImageView) findViewById(R.id.profilePicture);




        // Check if user is logged in
        currentUser = ParseUser.getCurrentUser();

        Intent intent1 = getIntent();
        userName = intent1.getStringExtra("selectedUser");


        if(currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

//        if(!(userName == currentUser.getString("name"))) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", userName);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser userFound, ParseException e) {
                if (e == null) {
                    userUsername = userFound.getString("username");
                    userGender = userFound.getString("gender");
                    profilePictureFile = userFound.getParseFile("profilePicture");
                    loadImages(profilePictureFile, userPicture);
                    relation = userFound.getRelation("interests");
                    Log.d("User Found", userFound.getString("name"));
                    Log.d("User Found", userFound.getEmail());
                    Log.d("User Found", userFound.getString("gender"));
                    interestQuery = relation.getQuery();
                    addItemsOnSpinner2();

                    nameTextView.setText(userName);
                    usernameTextView.setText(userUsername);
                    genderTextView.setText(userGender);

                } else {
                    // Something went wrong.
                    Log.d("User", "Error: " + e.getMessage());
                }
            }
        });


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


    // load profile picture from database
    private void loadImages(ParseFile thumbnail, final ImageView img) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        img.setImageBitmap(bmp);
                    }
                }
            });
        }
    }


    public void logout(final View v){
        currentUser.logOut();

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // option to change picture
    public void getNewProfilePicture(View v){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Change Profile Picture");

        // get an existing photo form the gallery
        alertDialogBuilder.setPositiveButton("Get existing photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(ProfileActivity.this, "You clicked existing button", Toast.LENGTH_LONG).show();
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        // get new photo from camera
        alertDialogBuilder.setNegativeButton("Take photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProfileActivity.this, "You clicked take photo button", Toast.LENGTH_LONG).show();

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);


            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }





    // get results from gallery or camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == RESULT_LOAD_IMAGE && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);
                userPicture.setImageBitmap(thumbnail);
                saveImage(thumbnail);

                saveImage(thumbnail);
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                saveImage(thumbnail);
                userPicture.setImageBitmap(thumbnail);
                saveImage(thumbnail);
            }

        }



    }


    private void saveImage(Bitmap thumbnail) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);


        byte[] scaledData = bytes.toByteArray();

        // Save the scaled image to Parse
        photoFile = new ParseFile("ProfilePicture.PNG", scaledData);
        photoFile.saveInBackground();
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("profilePicture", photoFile);
        currentUser.saveInBackground();
    }



    // add interests to spinner
    // add items into spinner dynamically
    public void addItemsOnSpinner2() {



        interestSpinner = (Spinner) findViewById(R.id.interestSpinner);

        final ArrayList<String> list = new ArrayList<String>();
        list.add("Click to view interests");


        interestQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> interestsList, ParseException e) {
                if (e == null) {
                    Log.d("User interests", "Retrieved " + interestsList.size() + " interests for this user");
                    for(int i = 0; i < interestsList.size(); i++) {

                        //get current category
                        ParseObject userInterest = interestsList.get(i);

                        // put current category as header
                        list.add(userInterest.getString("name"));
                    }

                } else {
                    Log.d("interests", "Error: " + e.getMessage());
                }
            }
        });


        // Create an adapter from the string array resource and use
        // android's inbuilt layout file simple_spinner_item
        // that represents the default spinner in the UI
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        // Set the layout to use for each dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        interestSpinner.setAdapter(adapter);
    }

}