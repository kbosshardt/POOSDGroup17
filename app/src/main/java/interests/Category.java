package interests;

import java.util.ArrayList;

/**
 * Created by jamesBryant on 10/24/15.
 */
    /* Category subclass - Make a separate class if needed */
public class Category
{
    private String mCategoryName;
    public ArrayList<Interest> mInterestOptions;

    /* I want to eventually change this to a background image with a transparent overlay */
    private String BackgroundColor;

    public Category( String name )
    {
        this.mCategoryName = name;
        mInterestOptions = new ArrayList<Interest>();
    }

    public String getCategoryName()
    {
        return mCategoryName;
    }

    public boolean isSelected()
    {
        return isSelected();
    }
}