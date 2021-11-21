package ca.nait.dmit.dmit2504;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ca.nait.dmit.dmit2504.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // View Binding Step 2
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // View Binding Step 3
        // setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        rebindRecycleView(); // execute  this when the activity is created
    }

    // to populate the recycler view
    private void rebindRecycleView() {
        // get the list of categories first. we have to create the instance of database helper again
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Category> categories = dbHelper.getCategoriesList();
        // create a data adapter
        CategoryRecyclerViewAdapter recyclerViewAdapter = new CategoryRecyclerViewAdapter(this, categories);
        binding.activityMainCategoriesRecyclerview.setAdapter(recyclerViewAdapter);
        binding.activityMainCategoriesRecyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onSaveButtonClick(View view) {
        String categoryName = binding.activityMainCategorynameEdittext.getText().toString();
        if (categoryName.isEmpty())
        {
            Toast.makeText(this, "Category Name is required", Toast.LENGTH_SHORT).show();
        }
        else
        { // need to make an addCategory first. Refer to CategoryDbHelper class for this
            Category newCategory = new Category();
            newCategory.setCategoryName(categoryName);
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            long categoryId = dbHelper.addCategory(newCategory);
            String message = String.format("Save successful with id %s", categoryId);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            binding.activityMainCategorynameEdittext.setText(""); // empty the text
            rebindRecycleView(); // execute this after we add a new item
        }
    }


    public static final String INTENT_ACTION_CATEGORY_DELETE = "ca.nait.dmit.dmit2504.CATEGORY_DELETE"; // DEFINE STRING CONSTANTS TO DEFINE OUR ACTIONS
    public static final String EXTRA_CATEGORY_CATEGORYID = "ca.nait.dmit.dmit2504.CATEGORY_ID"; // define a constant that we may use as a key for the "category on delete"

    // define a nested class which is a broadcast receiver
    class DeleteCategoryBroadcastReceiver extends BroadcastReceiver {
        // Must implement the onReceive method alt-insert - implement method , select onReceive method

        @Override
        public void onReceive(Context context, Intent intent) {
            //  which component is sending the message? It is intent. Intent contains information, such as data for the message. This will be the handler of the broadcast messages
            // WE HAVE  to make sure the message is for the appropriate action
            if (intent.getAction().equals(INTENT_ACTION_CATEGORY_DELETE))
            {
                // TO DELETE AN ITEM, WE NEED A PRIMARY KEY. Extras are collection of key and name value pairs
                int categoryId = intent.getIntExtra(EXTRA_CATEGORY_CATEGORYID, 0);
                if (categoryId > 0 ) // of it's not greater than zero there is no item to delete
                {
                    DatabaseHelper dbHelper = new DatabaseHelper(context); // instantiating a DatabaseHelper class
                    dbHelper.deleteCategory(categoryId); // calling the DatabaseHelper method
                    rebindRecycleView();// we need to remove an item from the recycleView. This is how we do it. rebindRecycleView() will fetch all the categories from the database again
                    Toast.makeText(context, "Delete was successful", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}


// OBJECTIVE: Store, read, display data on a SQL database

// https://nait.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d845b5d8-77af-4e53-afc2-adc70155d3b4 -- video source
// we wat to create an app that can manage a catalog for us. we're gonna store category name initially
// 1. we need to make a contract. Contract defines the table names and column names we're gonna use. See CategoryContract.java for details



// BINDING, AKA VIEW BINDING:
// We don't want to MANUALLY find the CategoryName and RecyclerListView
// We don't need to define each view anymore when we're using BINDING


// RecyclerView
// List view only can display in column layout. RecyclerView can be displayed in different layout and appearances like linear, grid or multiple columns