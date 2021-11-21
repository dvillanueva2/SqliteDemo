package ca.nait.dmit.dmit2504;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;


// Step 2: extends RecyclerView.Adapter super class. hit alt-enter on CategoryRecyclerViewAdapter then choose Implement methods
public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder>{

    // Step 3: Define fields for the data source
    private Context context; // a component which uses the recyclerview. context allows us to track which is using our RecyclerView
    private List<Category> categories; // a RecyclerView needs a list of data

    // Step 4: Create a parameterized constructor - alt insert then select the Context and List<Category> and one more time and selecting Context alone


    public CategoryRecyclerViewAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    public CategoryRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void addItem(Category newCategory) {
        categories.add(newCategory);
        notifyDataSetChanged(); // we need to notify that there is new content
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // to find the template for all your items using this method
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_category, parent, false);
        // create an instance of the CategoryViewHolder class
        CategoryViewHolder viewHolder = new CategoryViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) { // populate the content using this method
        Category currentCategory = categories.get(position);
        holder.categoryIdTextView.setText("" + currentCategory.getCategoryId()); // categoryId is a number not a string, we must concatenate the ID
        holder.categoryNameTextView.setText(currentCategory.getCategoryName());
    }

    @Override
    public int getItemCount() { // method gets the size
        return categories.size(); // determines the size
    }


    // Step 1: Create a ViewHolder class that defines the views for a single item
    // Create a constructor. - hit alt-enter then Create constructor matching super. This is responsible for finding all individual views and our list item
    public static class CategoryViewHolder extends ViewHolder {
        // declare view DATA FIELDS for the list items
        public TextView categoryIdTextView;
        public TextView categoryNameTextView;
        public ImageButton deleteButton;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryIdTextView = itemView.findViewById(R.id.list_item_category_id_textview);
            categoryNameTextView = itemView.findViewById(R.id.list_item_category_categoryname_textview);
            deleteButton = itemView.findViewById(R.id.list_item_category_delete_button);

            // lambda expresssion - ctrl + space then select the option. you need to manually enter the curly braces
            deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition(); // get the position
                Toast.makeText(itemView.getContext(), "Delete category at index " + position, Toast.LENGTH_SHORT).show();
                // we can actually delete items using Broadcast Receivers, which can send "messages to the MainActivity.java". we need to define a broadcast receiver class from there
            });
        } // import ViewHolder - hit alt- enter to ViewHolder



    }
}

// after making the
