package com.poosdseventeen.targetguys;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

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
    }


}
