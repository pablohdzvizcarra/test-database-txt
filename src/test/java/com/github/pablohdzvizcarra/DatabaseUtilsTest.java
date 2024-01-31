package com.github.pablohdzvizcarra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseUtilsTest {
    private DatabaseUtils databaseUtils;

    @BeforeEach
    void setUp() {
        databaseUtils = new DatabaseUtils();
    }

    @Test
    void shouldReturnDatabaseNameFormatted() {
        String databaseName = "example";
        String databaseNameFormatted = "example.txt";
        String databaseNameFormattedResult = databaseUtils.getDatabaseNameFormatted(databaseName);
        assertEquals(databaseNameFormatted, databaseNameFormattedResult);
    }

    @Test
    void shouldThrownExceptionWhenDatabaseNameIsNull() {
        String databaseName = null;
        try {
            databaseUtils.getDatabaseNameFormatted(databaseName);
        } catch (NullPointerException e) {
            assertEquals("Database name is null", e.getMessage());
        }
    }

    @Test
    void shouldCreateRandomId() {
        String id = databaseUtils.createDocumentId();
        assertThat(id)
                .isNotEmpty()
                .hasSize(36)
                .doesNotContain("-");
    }

    @Test
    void shouldAddIdValueToJson() {
        // Arrange
        String data = "{\"name\":\"James\"}";
        String id = databaseUtils.createDocumentId();

        // Act
        String result = databaseUtils.addIdValueToJson(data, id);

        // Assert
        assertThat(result)
                .isNotEmpty()
                .contains(id)
                .contains("_id");
    }
}
