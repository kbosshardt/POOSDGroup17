package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.poosdseventeen.targetguys.R;

import java.util.HashMap;

import interests.Category;

/**
 * Created by jamesBryant on 11/9/15.
 */
public class ExpandableCategoryListAdapter extends BaseExpandableListAdapter {
    private String tag = "ECLA";
    private Context mContext;
    private Category[] mCategoryList;

    public ExpandableCategoryListAdapter( Context context,
                                          Category[] categories )
    {
        Log.v(tag, "IS this even working?");

        this.mContext = context;
        this.mCategoryList = categories;
    }

    @Override
    public int getGroupCount() {
        return mCategoryList.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mCategoryList[ groupPosition ].mInterestOptions.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCategoryList[ groupPosition ];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.v(tag, "getchild");

        Object test = mCategoryList[groupPosition].mInterestOptions.get(childPosition);
        Log.v(tag, test.toString() );

        return test;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView( int categoryPosition, boolean isExpanded,
                              View convertView, ViewGroup parent)
    {
        String groupTitle = mCategoryList[ categoryPosition ].getCategoryName();

        if( convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate( R.layout.category_parent_layout, parent, false);
        }

        TextView categoryTextView = (TextView) convertView.findViewById(R.id.categoryNameLabel );

        categoryTextView.setText( groupTitle );


//        Log.v(tag, groupTitle);
        return convertView;
    }

    @Override
    public View getChildView( int groupPosition,
                              int childPosition,
                              boolean isLastChild,
                              View convertView,
                              ViewGroup parent )
    {
        Log.v(tag, "parent view:"+ parent.getTag() );
        String interestTitle = mCategoryList[ groupPosition ]
                .mInterestOptions.get( childPosition ).getName();

        if( convertView == null )
        {
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_interest_layout, parent, false);
        }
        TextView childTextView = (TextView) convertView.findViewById( R.id.childInterestTextView);
        childTextView.setText( interestTitle );

        Log.v(tag, interestTitle);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
