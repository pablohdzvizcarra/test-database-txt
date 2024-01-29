package com.github.pablohdzvizcarra;

public class DatabaseUtils {
    public String getDatabaseNameFormatted(String databaseName) {
        if (databaseName == null)
            throw new NullPointerException("Database name is null");
        return databaseName + ".txt";
    }
}
