package com.poosdseventeen.targetguys;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by Kourtney on 10/23/15.
 * Sets up the parse database.
 */
public class ParseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this, "ryKjCwddLi2AQf0O2WGq9R3SJeMmKWLc7vud3BkJ", "jbQl8HDhB4Q4LhUPcuAURRV1tUuQHzBiyCT5fsUG");
        ParseFacebookUtils.initialize(getApplicationContext());
    }


}
