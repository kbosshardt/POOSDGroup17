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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    private String userName;
    private String userEmail;
    private String userGender;
    private ImageView userPicture;
    private ParseFile profilePictureFile;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView genderTextView;

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // Check if user is logged in
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        userName = currentUser.getString("name");
        userEmail = currentUser.getEmail();
        userGender = currentUser.getString("gender");




        nameTextView = (TextView) findViewById(R.id.userName);
        emailTextView = (TextView) findViewById(R.id.userEmail);
        genderTextView = (TextView) findViewById(R.id.userGender);
        userPicture = (ImageView) findViewById(R.id.profilePicture);

        nameTextView.setText(userName);
        emailTextView.setText(userEmail);
        genderTextView.setText(userGender);


        profilePictureFile = currentUser.getParseFile("profilePicture");

        loadImages(profilePictureFile, userPicture);


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
        } else {
            //get new picture
            Toast.makeText(ProfileActivity.this,
                    "keep old!", Toast.LENGTH_SHORT).show();
        }
    }// load image


    public void logout(final View v){
        ParseUser user = ParseUser.getCurrentUser();
        user.logOut();

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void getNewProfilePicture(View v){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Change Profile Picture");

        alertDialogBuilder.setPositiveButton("Get existing photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(ProfileActivity.this, "You clicked existing button", Toast.LENGTH_LONG).show();
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        alertDialogBuilder.setNegativeButton("Take photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProfileActivity.this, "You clicked take photo button", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    // get results from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            userPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }
}
