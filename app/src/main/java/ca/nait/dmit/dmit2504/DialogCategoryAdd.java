package ca.nait.dmit.dmit2504;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogCategoryAdd extends DialogFragment {

    // type of data this dialog need. A reference to our main activity
    private MainActivity mainActivity;
    // generate parametered constructor. force it to to pass in our Main Activity reference
    // alt - insert - DialogFragment() - mainActivity
    public DialogCategoryAdd(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        //return super.onCreateDialog(savedInstanceState); --> useless
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // Builder is an inner class - a class within a class. The Builder class is used to build a dialog. It requires a context. we can either use getActivity() or pass in the mainActivity
        LayoutInflater inflater = getActivity().getLayoutInflater();// specify the layout for this dialog. Our custom layout is defined in an xml layout file. convert the layout file to a view object. let's use an inflater
        View dialogView = inflater.inflate(R.layout.dialog_category_edit, null);

        EditText categoryNameEditText = dialogView.findViewById(R.id.dialog_category_edit_categoryname_edittext); // specify EditText
        categoryNameEditText.requestFocus(); // the keyboard cursor is hovering on that view
        // find cancel button and save button
        Button cancelButton = dialogView.findViewById(R.id.dialog_category_edit_cancel_button);
        Button saveButton = dialogView.findViewById(R.id.dialog_category_edit_save_button);
        // dynamically assign an event handler... longhand code of assigning a listener here
        cancelButton.setOnClickListener(new View.OnClickListener() { // pass in an object that implements OnClickListener interface
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        // dynamically assign an event handler... shorthand way here LAMBDA allows you to implement an interface that has only a single method [1]
        saveButton.setOnClickListener(view -> {
            String categoryName = categoryNameEditText.getText().toString();
            if (categoryName.isEmpty())
            {
                Toast.makeText(getActivity(),"CategoryName is required", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Category newCategory = new Category();
                newCategory.setCategoryName(categoryName);
                mainActivity.addCategory(newCategory);
                dismiss(); // close the dialog
            }
        });
        // how to we launch this dialog? we need to add a Floating Action button to our layout. go to activity_main


        builder.setView(dialogView).setTitle("New Category"); // display custom dialog view
        return builder.create();
    }
}


// Fragment is a reusable UI - your activities can display many fragments
// convert this class to a dialog fragment. we want to inherit from "extends DialogFragment"

// 1 lambda implements this code
//new View.OnClickListener() { // pass in an object that implements OnClickListener interface
//@Override
//public void onClick(View view) {
//        dismiss();
//        }
//}