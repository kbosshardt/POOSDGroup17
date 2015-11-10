package com.poosdseventeen.targetguys;

import android.app.ExpandableListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import adapters.CategoryAdapter;
import adapters.ExpandableCategoryListAdapter;
import interests.Category;
import interests.Interest;


public class CategoryActivity extends AppCompatActivity {
    private ListView categoryListView;
    private TextView emptyTextView;
    private Category[] categoryList;
    private Button continueButton;
    private ExpandableListView mExpandableListView;
    private ExpandableCategoryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        // Temporary Category List of categories.
        // We can eventually pull these items from a database?
        // This is primarily for UI layouts
        categoryList = inflateCategoryList();

        /* Gonna try sometning new below this....
        //final ListView listView = (ListView) findViewById(R.id.list)
        categoryListView = (ListView) findViewById(android.R.id.list);
        emptyTextView = (TextView) findViewById(android.R.id.empty);


        //Set adapter on the listview
        CategoryAdapter adapter = new CategoryAdapter(this, categoryList);
        categoryListView.setAdapter( adapter );
        categoryListView.setEmptyView(emptyTextView);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CategoryActivity.this, categoryList[position].getCategoryName(), Toast.LENGTH_LONG );
            }
        });

        // Add a footer if there are categories
        if( categoryList.length > 0  )
        {
            // Puts a button at the footer of the list only if categories are available
            LinearLayout footerLayout = (LinearLayout) getLayoutInflater().inflate( R.layout.activity_category_footer, null);
            continueButton = (Button) footerLayout.findViewById(R.id.continueBtn);
            categoryListView.addFooterView( footerLayout );
        }*/

        mExpandableListView = (ExpandableListView) findViewById(R.id.expandableCategoryList);
        mAdapter = new ExpandableCategoryListAdapter( this, categoryList );
        mExpandableListView.setAdapter( mAdapter );

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.v("CA*", categoryList[groupPosition].getCategoryName() + " groupposition: " + groupPosition);
                Log.v("\tSubInterest", categoryList[groupPosition].mInterestOptions.size() + " groupposition: " + groupPosition);
//                Toast.makeText(CategoryActivity.this,
//                        categoryList[groupPosition]
//                                .mInterestOptions
//                                .get(groupPosition-1).getName(), Toast.LENGTH_LONG ).show();
            }
        });
        {

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Temporary Category list */
    public Category[] inflateCategoryList()
    {
        ArrayList<String> categorySet = new ArrayList<String>();
        categorySet.add("Books & Writing");
        categorySet.add("Business and Career");
        categorySet.add("Crafts & Hobbies");
        categorySet.add("Education");
        categorySet.add("Fitness");
        categorySet.add("Food & Drink");
        categorySet.add("Games (Video and Otherwise)");
        categorySet.add("Men");
        categorySet.add("Music");
        categorySet.add("Outdoors");
        categorySet.add("Pets");
        categorySet.add("Photo & Films");
        categorySet.add("Social");
        categorySet.add("Technology");
        categorySet.add("Social");
        categorySet.add("Spiritual");
        categorySet.add("Women");

        Category[] categories = new Category[categorySet.size()];

        int pos = 0;

        for ( String entry : categorySet  )
        {
            Category category = new Category( entry );

            for( int j = 0; j < 3; j++ )
            {
                category.mInterestOptions.add( new Interest("Interest number " + j) );
            }

            categories[pos] = category;
            pos++;
        }

        Log.d("CategoryActivity", Arrays.toString(categories));

        return categories;
    }
}
