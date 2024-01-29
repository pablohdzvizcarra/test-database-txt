package com.github.pablohdzvizcarra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonDataSaverTest {
    private String validUserRecord = """
            {
                "name": "James",
                "lastName": "Gosling",
                "email": "james@java.com",
                "nickname": "java_master"
            }
            """;

    private JsonDataSaver jsonDataSaver;
    private String nonExistsFilepath = "nonExistsFilepath";
    private String validFilepath = "database/test_database.json";

    @BeforeAll
    static void setUpAll() {
        if (Files.notExists(Paths.get("database"))) {
            try {
                Files.createDirectory(Paths.get("database"));
            } catch (Exception ignored) {
            }
        }

        if (Files.notExists(Paths.get("database/test_database.json"))) {
            try {
                Files.createFile(Paths.get("database/test_database.json"));
            } catch (Exception ignored) {
            }
        }
    }

    @BeforeEach
    void setUp() {
        jsonDataSaver = new JsonDataSaver();
    }

    @Test
    void shouldSaveDataInJsonFile() {
        boolean wasCreated = jsonDataSaver.save(validUserRecord, validFilepath);
        assertTrue(wasCreated);
    }

    @Test
    void shouldThrownExceptionWhenTheRecordIsNull() {
        try {
            jsonDataSaver.save(null, validFilepath);
        } catch (NullPointerException e) {
            assertTrue(e.getMessage().contains("The record provided is null"));
        }
    }

    @Test
    void shouldThrownIllegalStateExceptionWhenTheRecordHasInvalidJsonFormat() {
        String filename = "filename";
        try {
            jsonDataSaver.save("invalid json", filename);
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("The record provided is not a valid JSON"));
        }
    }

    @Test
    void shouldThrownExceptionWhenFilepathIsNull() {
        try {
            jsonDataSaver.save(validUserRecord, null);
        } catch (NullPointerException e) {
            assertTrue(e.getMessage().contains("The filepath provided is null"));
        }
    }

    @Test
    void shouldThrownExceptionWhenTheFilenameDoesNotExists() {
        try {
            jsonDataSaver.save(validUserRecord, nonExistsFilepath);
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("The filepath provided does not exists"));
        }
    }

    @Test
    void shouldWriteJsonDataInFile() {
        String userOne = """
                {
                    "name": "James",
                    "lastName": "Gosling",
                    "email": "james@java.com",
                    "nickname": "java_master"
                }
                """;
        jsonDataSaver.save(userOne, validFilepath);
        String fileContent = readContentFileToString();

        assertTrue(fileContent.contains("James"));
        assertTrue(fileContent.contains("Gosling"));
        assertTrue(fileContent.contains("james@java.com"));
        assertTrue(fileContent.contains("java_master"));
    }

    @Test
    void shouldWriteMultipleJsonDataInFile() {
        String userOne = """
                {
                    "name": "James",
                    "lastName": "Gosling",
                    "email": "james@java.com",
                    "nickname": "java_master"
                }
                """;
        String userTwo = """
                {
                    "name": "Brendan",
                    "lastName": "Eich",
                    "email": "brendan@javascript.com",
                    "nickname": "javascript_master"
                }
                """;
        jsonDataSaver.save(userOne, validFilepath);
        jsonDataSaver.save(userTwo, validFilepath);

        String fileContent = readContentFileToString();
        assertTrue(fileContent.contains("James"));
        assertTrue(fileContent.contains("Gosling"));
        assertTrue(fileContent.contains("james@java.com"));
        assertTrue(fileContent.contains("java_master"));

        assertTrue(fileContent.contains("Brendan"));
        assertTrue(fileContent.contains("Eich"));
        assertTrue(fileContent.contains("brendan@javascript.com"));
        assertTrue(fileContent.contains("javascript_master"));
    }

    private String readContentFileToString() {
        try {
            return Files.readString(Paths.get(validFilepath));
        } catch (Exception e) {
            return "";
        }

    }
}
