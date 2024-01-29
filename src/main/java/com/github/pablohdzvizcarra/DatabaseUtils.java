package com.github.pablohdzvizcarra;

import java.util.UUID;

public class DatabaseUtils {
    public String getDatabaseNameFormatted(String databaseName) {
        if (databaseName == null)
            throw new NullPointerException("Database name is null");
        return databaseName + ".txt";
    }

    public String createDocumentId() {
        return UUID.randomUUID().toString().replace("-", "_");
    }
}
