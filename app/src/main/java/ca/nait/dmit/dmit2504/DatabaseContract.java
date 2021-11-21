package ca.nait.dmit.dmit2504;

import android.provider.BaseColumns;

// we do not want this class to be inherited. put final
public final class DatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseContract() {
    }

    /* Inner class that defines the table contents */
    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "category_table";
        public static final String COLUMN_NAME_CATEGORYNAME = "category_name";
    }

    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "product_table";
        public static final String COLUMN_NAME_PRODUCTNAME = "product_name";
        public static final String COLUMN_NAME_UNITPRICE = "unit_price";
        public static final String COLUMN_NAME_CATEGORYID = "category_id";
    }
    // define a  SQLiteOpenHelper class

}

// A contract class contains constants for all the tables you're gonna use and defines the columns of all tables you're gonna use
// BaseColumns include a column called _ID - useful for simple cursor adapter or binding through adapter view like spinner or list view. For RecycleView, not too much
