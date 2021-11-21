package ca.nait.dmit.dmit2504;

public class Category {

    private int categoryId;
    private String categoryName;

    // alt-insert then choose category to generate a constructor
    public Category() {

    }
    // alt-insert then choose both categoryId and categoryName to generate a constructor for them
    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // alt-insert then choose both categoryId and categoryName to generate getters and setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}


// make constructos, getters and setters. next, we need to make a Java class that uses Sqlite to manage the categories.
// 1. we need to make a contract. Contract defines the table names and column names we're gonna use