package com.poosdseventeen.targetguys;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by Kourtney on 11/23/15.
 */

/**
 * Data model for a post.
 */
@ParseClassName("User")
public class User extends ParseObject {
    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String value) {
        put("username", value);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String value) {
        put("name", value);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String value) {
        put("email", value);
    }

    public String getGender() {
        return getString("gender");
    }

    public void setGender(String value) {
        put("gender", value);
    }

    public ParseRelation getInterests(ParseUser user) {
        return user.getRelation("interests");
    }

    public static ParseQuery<User> getQuery() {
        return ParseQuery.getQuery(User.class);
    }
}

