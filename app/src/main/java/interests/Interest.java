package interests;

/**
 * Created by jamesBryant on 11/9/15.
 */
public class Interest {
    private String mName;
    private boolean mIsSelected;

    public Interest( String name )
    {
        mName = name;
        mIsSelected = false; // Originally not selected
    }

    public String getName()
    {
        return mName;
    }

    public void setName( String newName )
    {
        mName = newName;
    }
}