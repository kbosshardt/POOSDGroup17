package com.poosdseventeen.targetguys;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by Kourtney on 10/23/15.
 */
public class ParseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ryKjCwddLi2AQf0O2WGq9R3SJeMmKWLc7vud3BkJ", "jbQl8HDhB4Q4LhUPcuAURRV1tUuQHzBiyCT5fsUG");
        ParseFacebookUtils.initialize(getApplicationContext());

        initializeInterests();
    }

    public void initializeInterests(){
        // Put interests into database
        ParseObject interest = new ParseObject("Interests");
        interest.put("name", "Fitness");
        interest.saveInBackground();

        ParseObject interest2 = new ParseObject("Interests");
        interest2.put("name", "Photo & Films");
        interest2.saveInBackground();
    }

}
