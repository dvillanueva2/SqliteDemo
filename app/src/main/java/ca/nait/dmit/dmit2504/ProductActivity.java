package ca.nait.dmit.dmit2504;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import ca.nait.dmit.dmit2504.databinding.ActivityProductBinding;

public class ProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_product);
        // access spinner directly using binding
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DatabaseHelper dbHelper = new DatabaseHelper(this); // need access to database helper to fetch categories
        Cursor categoryQueryResultCursor = dbHelper.getCategoriesCursor(); // call a method that returns a cursor. getCategoriesCursor() - executes query and returns a cursor to the results set. useful if you plan to use a cursor adapter to populate the adapter view
        String[] fromColumnNames = {DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME}; // populate the spinner
        int[] toViewIds = {android.R.id.text1};// find array of resource Id's. this is an array initializer. They use extended spinner view. This includes an id called text1. text1 is a default of a default layout of spinner
        int flags = 0;
        SimpleCursorAdapter categoryAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                categoryQueryResultCursor,
                fromColumnNames,
                toViewIds,
                flags);
        binding.activityProductCategorySpinner.setAdapter(categoryAdapter); // access the spinner, set the adapter.
        // to test this without using the options menu we go to the android manifest and change the starting activity

        // populate the list view
        binding.activityProductCategorySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int categoryId = i == 0 ? 1 : 3;
        DatabaseHelper dbHelper = new DatabaseHelper(this); // need access to database helper to fetch categories
        Cursor categoryProductQueryResultCursor = dbHelper.getProductsByCategoryId(categoryId); // call a method that returns a cursor. getCategoriesCursor() - executes query and returns a cursor to the results set. useful if you plan to use a cursor adapter to populate the adapter view
        String[] fromColumnNames = {DatabaseContract.ProductEntry.COLUMN_NAME_PRODUCTNAME}; // populate the spinner
        int[] toViewIds = {android.R.id.text1};// find array of resource Id's. this is an array initializer. They use extended spinner view. This includes an id called text1. text1 is a default of a default layout of spinner
        int flags = 0;
        SimpleCursorAdapter categoryProductAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                categoryProductQueryResultCursor,
                fromColumnNames,
                toViewIds,
                flags);
        binding.activityProductListview.setAdapter(categoryProductAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}