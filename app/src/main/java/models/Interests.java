package models;

import java.util.ArrayList;

/**
 * Created by jamesBryant on 10/23/15.
 */
public class Interests {
    private String mInterestName;
    private ArrayList<String> mSubInterests;


    public Interests( String interestName )
    {
        mInterestName = interestName;
    }

    /**
     * Set the interest name.
     * @return nothing
     */
    public String getInterestName() {
        return mInterestName;
    }

    public void setInterestName(String interestName) {
        this.mInterestName = interestName;
    }

    /**
     *
     * @return number of sub-interests
     */
    public int numberOfSubInterests()
    {
        return mSubInterests.size();
    }
}
