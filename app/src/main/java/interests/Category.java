package interests;

/**
 * Created by jamesBryant on 10/24/15.
 */
    /* Category subclass - Make a separate class if needed */
public class Category
{
    private String CategoryName;
    private boolean IsCategorySelected;

    /* I want to eventually changed this to a background image with a transparent overlay */
    private String BackgroundColor;

    public Category( String name, String background)
    {
        this.CategoryName = name;
        this.BackgroundColor = background;
        this.IsCategorySelected = false;
    }

    public String getCategoryName()
    {
        return CategoryName;
    }

    public String getCategoryColor()
    {
        return BackgroundColor;
    }

    public boolean isSelected()
    {
        return isSelected();
    }
}