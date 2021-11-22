package ca.nait.dmit.dmit2504;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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



    // define a nested class which is a broadcast receiver. go to adapter to see all these blocks of code below in action
    public static final String INTENT_ACTION_CATEGORY_DELETE = "ca.nait.dmit.dmit2504.CATEGORY_DELETE"; // DEFINE STRING CONSTANTS TO DEFINE OUR ACTIONS
    public static final String INTENT_ACTION_CATEGORY_EDIT = "ca.nait.dmit.dmit2504.CATEGORY_EDIT";
    public static final String EXTRA_CATEGORY_CATEGORYID = "ca.nait.dmit.dmit2504.CATEGORY_ID"; // define a constant that we may use as a key for the "category on delete"


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
                    // create a delete confirmation button dialog window popup using a an alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Confirmation")
                            .setMessage("Are you sure you want to delete item " + categoryId + "?")
                            // lambda expression - press ctrl-space and select the lambda expression
                            .setPositiveButton("Yes I am sure", (dialogInterface, i) -> {
                                DatabaseHelper dbHelper = new DatabaseHelper(context); // instantiating a DatabaseHelper class
                                dbHelper.deleteCategory(categoryId); // calling the DatabaseHelper method
                                rebindRecycleView();// we need to remove an item from the recycleView. This is how we do it. rebindRecycleView() will fetch all the categories from the database again
                                Toast.makeText(context, "Delete was successful", Toast.LENGTH_SHORT).show(); // show result feedback
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> {
                                Toast.makeText(context, "Delete was cancelled", Toast.LENGTH_SHORT).show();
                            });
                    // show the confirmation dialog
                    builder.show();

                    /* move this code to the where we delete our database .setPositiveButton
                    DatabaseHelper dbHelper = new DatabaseHelper(context); // instantiating a DatabaseHelper class
                    dbHelper.deleteCategory(categoryId); // calling the DatabaseHelper method
                    rebindRecycleView();// we need to remove an item from the recycleView. This is how we do it. rebindRecycleView() will fetch all the categories from the database again
                    Toast.makeText(context, "Delete was successful", Toast.LENGTH_SHORT).show(); // show result feedback
                    */
                }
            }
        }
    }

    // Broadcast Receiver STEP 1: Define the BroadcastReceiver class
    class EditCategoryBroadcastReceiver extends BroadcastReceiver
    {
        // implement this method. Use alt-enter and then click onReceive
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(INTENT_ACTION_CATEGORY_EDIT)) // access the intent that's passed in to the broadcast receiver. Intent is a communication mechanism in Android that is used to communicate with different components. it is also used to send to send instructions to the Android system for some tasks performed
            {
                int categoryId = intent.getIntExtra(EXTRA_CATEGORY_CATEGORYID, 0);
                if (categoryId > 0) // verify categoryId is greater than zero. If it is zero, there is no category to edit
                {
                    // Toast.makeText(context, "Edit category received", Toast.LENGTH_SHORT).show(); to test if the broadcast receiver works
                    DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this); // we can use the categoryId passed in and use the DatabaseHelper to find the record
                    Category editCategory = dbHelper.findOneCategoryById(categoryId);
                    DialogCategoryEdit editDialog = new DialogCategoryEdit(editCategory, MainActivity.this);
                    editDialog.show(getSupportFragmentManager(), "MainActivity");
                }
            }
        }
    }


    // to use the broadcast class we need to register it. we can do that on the the resumed event of the activity. on activity resume we are interested in listening to the delete category broadcast receiver event
    private DeleteCategoryBroadcastReceiver currentDeleteCategoryBroadcastReceiver = new DeleteCategoryBroadcastReceiver();
    // Broadcast Receiver Step 2: Create an instance of the custom BroadCastReceiver class
    private EditCategoryBroadcastReceiver currentEditCategoryBroadcastReceiver = new EditCategoryBroadcastReceiver();

    // register this object on onReceive event of the activity. register the receiver using this method
    @Override
    protected void onResume()
    {
        super.onResume();
        // register the delete receiver with the Android operating system.
        IntentFilter categoryDeleteIntentFilter = new IntentFilter();  // type of receiver we're interested in listening
        categoryDeleteIntentFilter.addAction(INTENT_ACTION_CATEGORY_DELETE);// in our IntentFilter we need an action. This is a string
        registerReceiver(currentDeleteCategoryBroadcastReceiver, categoryDeleteIntentFilter); // register the intent filter. we are registering a receiver that can receive information about these types of broadcast messages
        // Broadcast Receiver Step 3: Create an IntentFilter and register the edit broadcast receiver
        IntentFilter categoryEditIntentFilter = new IntentFilter();
        categoryEditIntentFilter.addAction(INTENT_ACTION_CATEGORY_EDIT);
        registerReceiver(currentEditCategoryBroadcastReceiver, categoryEditIntentFilter); // STEP 3a: register the receiver, send the categoryEditIntentFilter
    }

    // as good pratice: on pause, we can unregister the receiver
    @Override
    protected void onPause()
    {
        super.onPause();

        // Broadcast Receiver Step 3b:  Unregister all broadcast receivers
        unregisterReceiver(currentDeleteCategoryBroadcastReceiver);
        unregisterReceiver(currentEditCategoryBroadcastReceiver);
    }

    public void updateCategory(int categoryId, Category updatedCategory)
    {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        if (dbHelper.updateCategory(categoryId, updatedCategory) > 0 )
        { // if successful, the category will be updated
            rebindRecycleView(); // once this runs, it will update its content and we will see the category changed
            Toast.makeText(this, "Update was successful", Toast.LENGTH_SHORT).show();
        }
        else
        { // otherwise, it fails
            Toast.makeText(this, "Update was not successful", Toast.LENGTH_SHORT).show();
        }
    }
}


// OBJECTIVE 1: Store, read, display data on a SQL database

// https://nait.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d845b5d8-77af-4e53-afc2-adc70155d3b4 -- video source
// we wat to create an app that can manage a catalog for us. we're gonna store category name initially
// 1. we need to make a contract. Contract defines the table names and column names we're gonna use. See CategoryContract.java for details



// BINDING, AKA VIEW BINDING:
// We don't want to MANUALLY find the CategoryName and RecyclerListView
// We don't need to define each view anymore when we're using BINDING


// RecyclerView
// List view only can display in column layout. RecyclerView can be displayed in different layout and appearances like linear, grid or multiple columns

// OBJECTIVE 2: how to modify our Sqlite demo app so we can edit and update a selected category, then add a user interface so we can add a new product and how to bind
// data to a spinner using a simple cursor adapter
// video source: https://naitca.sharepoint.com/teams/dmit2504-1211-oa01-oa02/Shared%20Documents/Forms/AllItems.aspx?id=%2Fteams%2Fdmit2504%2D1211%2Doa01%2Doa02%2FShared%20Documents%2FDMIT2504%20Oct%2026%20%2D%20CRUD%20UI%20and%20SimpleCursorAdapter%2FRecordings%2FView%20Only%2FDMIT2504%20Tuesday%2C%20Oct%2026%20OPTIONAL%20Meeting%2D20211026%5F081852%2DMeeting%20Recording%2Emp4&parent=%2Fteams%2Fdmit2504%2D1211%2Doa01%2Doa02%2FShared%20Documents%2FDMIT2504%20Oct%2026%20%2D%20CRUD%20UI%20and%20SimpleCursorAdapter%2FRecordings%2FView%20Only

