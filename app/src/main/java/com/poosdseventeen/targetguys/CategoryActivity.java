package com.poosdseventeen.targetguys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import adapters.CategoryAdapter;
import interests.Category;


public class CategoryActivity extends AppCompatActivity {
    private ListView categoryListView;
    private TextView emptyTextView;
    private Category[] categoryList;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        // Temporary Category List of categories.
        // We can eventually pull these items from a database?
        // This is primarily for UI layouts
        categoryList = inflateCategoryList();

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
        Map<String, String> categorySet = new HashMap<String, String>();
        categorySet.put("Books & Writing","#401abc9c");
        categorySet.put("Business and Career","#40D24D57");
        categorySet.put("Crafts & Hobbies","#40E67E22");
        categorySet.put("Education","#406C7A89");
        categorySet.put("Fitness","#40049372");
        categorySet.put("Food & Drink","#40F5D76E");
        categorySet.put("Games (Video and Otherwise)","#55F9690E");
        categorySet.put("Men","#40C8F7C5");
        categorySet.put("Music","#4067809F");
        categorySet.put("Outdoors","#40C5EFF7");
        categorySet.put("Pets","#40913D88");
        categorySet.put("Photo & Films","#40AEA8D3");
        categorySet.put("Social","#40E26A6A");
        categorySet.put("Technology","#40D64541");
        categorySet.put("Social","#4086E2D5");
        categorySet.put("Spiritual","#40336E7B");
        categorySet.put("Women","#408E44AD");

        Category[] categories = new Category[categorySet.size()];

        int pos = 0;

        for (Map.Entry<String,String> entry : categorySet.entrySet() )
        {
            Log.d("CategoryActivity", entry.getKey() );
            categories[pos++] = new Category( entry.getKey(), entry.getValue() );
        }

        Log.d("CategoryActivity", Arrays.toString(categories));

        return categories;
    }
}
