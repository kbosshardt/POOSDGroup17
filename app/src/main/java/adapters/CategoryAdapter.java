package adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.poosdseventeen.targetguys.R;

import java.util.Arrays;

import interests.Category;

/**
 * Created by jamesBryant on 10/24/15.
 */
public class CategoryAdapter extends BaseAdapter {

    private static String TAG = "CategoryAdapter";

    private Context mContext;
    private Category[] mCategories;

    public CategoryAdapter( Context context, Category[] categories)
    {
        this.mContext = context;
        this.mCategories = categories;
    }

    @Override
    public int getCount() {
        return mCategories.length;
    }

    @Override
    public Object getItem(int position) {
        return mCategories[position];
    }

    // Not using
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Create a new adapter
        if( convertView == null )
        {
            convertView = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.category_list_item, null);
            holder = new ViewHolder();
            holder.categoryNameLabel = (TextView) convertView.findViewById(R.id.categoryNameLabel);
            convertView.setTag( holder );
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the Category Name
        Category category = mCategories[position];

        holder.categoryNameLabel.setText(category.getCategoryName());

        convertView.setBackgroundColor(Color.parseColor( category.getCategoryColor()));
        return convertView;

    } // end getView


    // Helper class - hold all component views
    private static class ViewHolder
    {
        TextView categoryNameLabel;
    }


}
