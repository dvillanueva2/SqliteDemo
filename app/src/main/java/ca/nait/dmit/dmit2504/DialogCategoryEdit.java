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

public class DialogCategoryEdit extends DialogFragment {

    // data the dialog view needs:
    private Category editCategory; // category we are editing. this is only for the user interface
    private MainActivity mainActivity; // the code we put to actually update the category information is here. this is the datafield that references the main activity

    // generate a parametered instructor. In order to create a dialog edit, we have to force it. we have to pass in a category and a reference to the main activity
    // Alt-insert - constructor - select DialogFragment - select both Category and MainActivity to display this DialogCategoryEdit constructor below
    public DialogCategoryEdit(Category editCategory, MainActivity mainActivity) {
        this.editCategory = editCategory; // edit field
        this.mainActivity = mainActivity; // mainActivity field
    }

    // to display the dialog we must override the onCreateDialog method. this is responsible to creating a dialog to display on our app
    // this is almost the same as the delete alert in the main
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // return super.onCreateDialog(savedInstanceState); not needed
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater(); // defines an xml to convert to an object we can use --> INFLATER
        View dialogView = layoutInflater.inflate(R.layout.dialog_category_edit, null); // finally converts the xml file to a view object
        // prepopulate our current category name anb edit text field
        EditText categoryNameEditText = dialogView.findViewById(R.id.dialog_category_edit_categoryname_edittext); // define an editText for our category name
        categoryNameEditText.setText(editCategory.getCategoryName()); // set the text for categoryNameEditText
        categoryNameEditText.requestFocus(); // focus on the category name edit text field
        Button cancelButton = dialogView.findViewById(R.id.dialog_category_edit_cancel_button); // find our cancel and delete button
        Button saveButton = dialogView.findViewById(R.id.dialog_category_edit_save_button);

        // LAMBDA EXPRESSION: this means we're gonna pass a block of code, where a parameter is a view
        // and where the block of code in the curly braces is the block of code that gets executed when the button is clicked on
        cancelButton.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "Cancel edit", Toast.LENGTH_SHORT).show();
            dismiss(); // to close the dialog pop-up
        });

        // LAMBDA EXPRESSION: this means we're gonna pass a block of code, where a parameter is a view
        // and where the block of code in the curly braces is the block of code that gets executed when the button is clicked on
        saveButton.setOnClickListener(view -> {
            // call a method in MainActivity that actually updates the category. we do not want to update the category using this onClick listener directly
            // because the MainActivity UI won't display the updated information entered. it is best to update the category in the main activity
            // doing it here instead

            // validate to show if a category name is entered
            String categoryName = categoryNameEditText.getText().toString();
            if (categoryName.isEmpty()) {
                Toast.makeText(getActivity(), "CategoryName is required", Toast.LENGTH_SHORT).show();
            }
            else {
                editCategory.setCategoryName(categoryName);
                mainActivity.updateCategory(editCategory.getCategoryId(), editCategory);
                dismiss(); // to close the dialog pop-up
            }
        });

        return builder.create();
    }
}
