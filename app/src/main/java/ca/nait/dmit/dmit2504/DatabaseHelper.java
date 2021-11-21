package ca.nait.dmit.dmit2504;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper { // implement methods - select onCreate and onUpgrade

    private static final String TAG = "DatabaseHelper"; // for defining purposes
    public static final int DATABASE_VERSION = 1; // useful to determine to when to call the onCreate and onUpgrade method (1)
    public static final String DATABASE_NAME = "SqliteDemo.db"; // if the database does not exist, the it's gonna call the OnCreate method. if the version does not match, the onUpgrade method will be created for us

    // strings for creating tables
    private static final String SQL_CREATE_CATEGORY_ENTRIES = "CREATE TABLE " + DatabaseContract.CategoryEntry.TABLE_NAME + "("
            + DatabaseContract.CategoryEntry._ID + " INTEGER PRIMARY KEY, "
            + DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME + " TEXT"
            + ")";

    private static final String SQL_DELETE_CATEGORY_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.CategoryEntry.TABLE_NAME; // database contract has all the tables names we need and all the columns for each table name

    // we should have a contructor that passes a context. we have to call the constructor in SQLiteOpenHelper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // delete our tables first. accessing sqlite database pass in this method:
        sqLiteDatabase.execSQL(SQL_DELETE_CATEGORY_ENTRIES);
        onCreate(sqLiteDatabase);
    } // implement a method after entering this exact line

    // insert or CREATE a record to Sqlite database
    public long addCategory(Category newCategory) { // add a Category that represents a new Category
        SQLiteDatabase db = getWritableDatabase(); // reference to database we can write to // Gets the data repository in write mode
        ContentValues values = new ContentValues(); // construct content values. We only have one property that is set manually which is CategoryName . ContentValues objects are a collection of key value pairs where key is the column name and value as the value in the column
        // Create a new map of values, where column names are the keys
        values.put(DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME, newCategory.getCategoryName());
        return db.insert(DatabaseContract.CategoryEntry.TABLE_NAME, null, values); // nullColumnHack - put null because Sqlite does not allow to explicitly insert nulls into a column
    }

    // READ or query from the database
    public Cursor getCategoriesCursor() { // the cursor is used in conjunction with a cursor adapter. Cursors can be use to interface the results. It allows you to process one row at time
        // specify the columns you want to return from the query
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                DatabaseContract.CategoryEntry._ID,
                DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME
        };
        // Specify the WHERE clause. Filter results WHERE "title" = 'My Title'
        String selection = null;
        // values for our "where" clause
        String[] selectionArgs = null; // "where" clause. Specify the where clause argument. The arguments is the question mark and the where clause which means the placeholder

        String groupBy = null;
        String having = null;
        // How you want the results sorted in the resulting Cursor
        String orderBy = DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME + " ASC";

        return db.query(
                DatabaseContract.CategoryEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy
        );
    }

    // READ or find one record using this method
    public Cursor findOneCategoryCursorById(int categoryId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                DatabaseContract.CategoryEntry._ID,
                DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = DatabaseContract.CategoryEntry._ID + " = ?"; // "where" calls ... we want to find a record where this column = ? ... The question mark is a placeholder where we can specify a value
        String[] selectionArgs = {String.valueOf(categoryId)}; // values for our "where" calls ... array of values for the "where" clause. Create an array java initializer. selectionArgs must be string values
        // SQL query
        String groupBy = null;
        String having = null;
        // How you want the results sorted in the resulting Cursor
        String orderBy = null; // we only need to get one order, so we set this to null
        // the query returns a cursor
        return db.query(
                DatabaseContract.CategoryEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy
        );
    }

    // the code on how to convert the row in a cursor to an object. This is a method mapToCategory thay can map a cursor to a category object. it makes application more useful to return category objects then return a cursor
    private Category mapCursorToCategory(Cursor queryResultCursor) { // pass in a cursor that points to a current row where we need to extract data
        Category currentCategory = new Category(); // we need a category to return
// hardcoded version of line 117 to 120
//        currentCategory.setCategoryId(queryResultCursor.getInt(0));
//        currentCategory.setCategoryName(queryResultCursor.getString(1));
        // start copying values located in properties of the Java object
        int columnIndexCategoryId = queryResultCursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry._ID);
        currentCategory.setCategoryId(queryResultCursor.getInt(columnIndexCategoryId));
        int columnIndexCategoryName = queryResultCursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME);
        currentCategory.setCategoryName(queryResultCursor.getString(columnIndexCategoryName));
        // determine of there is a row we call moveToNext() method
        return currentCategory;
    } // we are going to use this method to find and we need to find a single category on many categories we are going to re-use this method

    // create the list and add data. returns a list of objects
    public List<Category> getCategoriesList() {
        Cursor queryResultCursor = getCategoriesCursor();
        // create the category lists to be returned
        List<Category> categories = new ArrayList<>();
        // process the cursor by calling moveToNext using a while loop. We can move to the next cursor by calling moveToNext
        while (queryResultCursor.moveToNext()) { // moveToNext moves the cursor to the next row. if there is not a row it returns true. Otherwise, it returns false
            // hardcoding is a no-no here.
            // Category currentCategory = new Category();
            // manually copy a data from a cursor to currentCategory objects
            //currentCategory.setCategoryId(queryResultCursor.getInt(0)); // we have to know the datatype. call the getInt . There is a way to hardcode this so we can follow best practices but instructor told us to explore this on our own. WHY? Isn't looking this up for us the reason he's getting paid?
            //currentCategory.setCategoryName(queryResultCursor.getString(1)); // we're getting string data

            // NEW manually copy a data from a cursor to currentCategory objects
            Category currentCategory = mapCursorToCategory(queryResultCursor); // this is how we re-use the method above
            categories.add(currentCategory);
        }
        return categories;
    }

    // a method that an return a single category object
    public Category findOneCategoryById(int categoryId) {
        Category existingCategory = null;

        Cursor queryResultCursor = findOneCategoryCursorById(categoryId);
        // to determine if the cursor contains any data, use an if statement (because we only need one record returned. we don't need to loop)
        if (queryResultCursor.moveToNext()) {
            existingCategory = mapCursorToCategory(queryResultCursor);
        }

        return existingCategory;
    }

    // UPDATE an existing category, passing the primary key of the category you wanna update and category object that contains all the updated value
    public int updateCategory(int categoryId, Category updatedCategory) {
        SQLiteDatabase db = getWritableDatabase(); // reference to database we can write to // Gets the data repository in write mode
        ContentValues values = new ContentValues(); // construct content values. We only have one property that is set manually which is CategoryName . ContentValues objects are a collection of key value pairs where key is the column name and value as the value in the column
        // Create a new map of values, where column names are the keys
        values.put(DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME, updatedCategory.getCategoryName());
        // we don't need to update the id which is the primary key
        final String whereClause = DatabaseContract.CategoryEntry._ID + " = ?";
        final String[] whereArgs = {String.valueOf(categoryId)}; // the values for all the question mark placeholders. the sequence is based on order if the question marks
        return db.update(DatabaseContract.CategoryEntry.TABLE_NAME, values, whereClause, whereArgs);

    }

    // DELETE a category
    public int deleteCategory(int categoryId) {
        SQLiteDatabase db = getWritableDatabase();
        final String whereClause = DatabaseContract.CategoryEntry._ID + " = ?";
        final String[] whereArgs = {String.valueOf(categoryId)};
        return db.delete(DatabaseContract.CategoryEntry.TABLE_NAME, whereClause, whereArgs);
    }
}

// Continue on 20:32 on second recording
// OBJECTIVE: CRUD
// source videos: https://naitca.sharepoint.com/teams/dmit2504-1211-oa01-oa02/Shared%20Documents/Forms/AllItems.aspx?id=%2Fteams%2Fdmit2504%2D1211%2Doa01%2Doa02%2FShared%20Documents%2FDMIT2504%20Oct%2025%20%2D%20Sqlite%20and%20Broadcast%20Receivers%2FRecordings%2FView%20Only%2FDMIT2504%20Monday%2C%20October%2025%20OPTIONAL%20live%20meeting%2D20211025%5F081349%2DMeeting%20Recording%2Emp4&parent=%2Fteams%2Fdmit2504%2D1211%2Doa01%2Doa02%2FShared%20Documents%2FDMIT2504%20Oct%2025%20%2D%20Sqlite%20and%20Broadcast%20Receivers%2FRecordings%2FView%20Only
// and https://naitca.sharepoint.com/teams/dmit2504-1211-oa01-oa02/Shared%20Documents/Forms/AllItems.aspx?id=%2Fteams%2Fdmit2504%2D1211%2Doa01%2Doa02%2FShared%20Documents%2FDMIT2504%20Oct%2025%20%2D%20Sqlite%20and%20Broadcast%20Receivers%2FRecordings%2FView%20Only%2FDMIT2504%20Monday%2C%20October%2025%20OPTIONAL%20live%20meeting%2D20211025%5F090018%2DMeeting%20Recording%2Emp4&parent=%2Fteams%2Fdmit2504%2D1211%2Doa01%2Doa02%2FShared%20Documents%2FDMIT2504%20Oct%2025%20%2D%20Sqlite%20and%20Broadcast%20Receivers%2FRecordings%2FView%20Only
// reference: https://developer.android.com/training/data-storage/sqlite
// read a database
// we use a cursor - it allows up to manually process one row at a time - cursor points before the first row.

// RecyclerView - enforces best standards on how to find the view for each item inside the adapter view

// 1 it first detects if the database file is on the device. It it does not, the onCreate method will create the database tables we need.
// if the schema changes or if we need to make changes or recreate the table, what we need to do is change the database_version number

