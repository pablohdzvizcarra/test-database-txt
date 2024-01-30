package com.github.pablohdzvizcarra;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonFileSerializerTest {
    private JsonFileSerializer jsonFileSerializer;
    private DatabaseUtils databaseUtils;

    @BeforeEach
    void setUp() {
        jsonFileSerializer = new JsonFileSerializer();
        databaseUtils = new DatabaseUtils();
    }

    @Test
    void shouldSaveJsonStringToFile() {
        String userJson = """
                {
                    "name": "James",
                    "lastName": "Gosling",
                    "email": "james@java.com",
                    "nickname": "java_master"
                }
                """;

        String documentId = databaseUtils.createDocumentId();
        Path filepath = Paths.get("database", "example", documentId);
        jsonFileSerializer.save(userJson, filepath);
        String json = jsonFileSerializer.readJson(filepath);

        assertThat(json)
                .isNotEmpty()
                .contains("James")
                .contains("Gosling")
                .contains("james@java.com")
                .contains("java_master");
    }

    @Test
    void shouldSaveTwoJsonStringToFile() {
        // Arrange
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
        String documentOne = databaseUtils.createDocumentId();
        Path filepathOne = Paths.get("database", "example", documentOne);
        String documentTwo = databaseUtils.createDocumentId();
        Path filepathTwo = Paths.get("database", "example", documentTwo);

        // Act
        jsonFileSerializer.save(userOne, filepathOne);
        jsonFileSerializer.save(userTwo, filepathTwo);
        String jsonOne = jsonFileSerializer.readJson(filepathOne);
        String jsonTwo = jsonFileSerializer.readJson(filepathTwo);

        // Assert
        assertThat(jsonOne)
                .isNotEmpty()
                .contains("James")
                .contains("Gosling")
                .contains("james@java.com")
                .contains("java_master");

        assertThat(jsonTwo)
                .isNotEmpty()
                .contains("Brendan")
                .contains("Eich")
                .contains("brendan@javascript.com")
                .contains("javascript_master");
    }

    @Test
    void shouldDeserializeFileIntoJsonString() {
        // Arrange
        String userJson = """
                {
                    "name": "James",
                    "lastName": "Gosling",
                    "email": "james@java.com",
                    "nickname": "java_master"
                }
                """;

        String documentId = databaseUtils.createDocumentId();
        Path filepath = Paths.get("database", "example", documentId);
        jsonFileSerializer.save(userJson, filepath);

        // Act
        String json = jsonFileSerializer.deserializeFileIntoJson(filepath);

        // Assert
        assertThat(json)
                .isNotEmpty()
                .contains("James")
                .contains("Gosling")
                .contains("james@java.com")
                .contains("java_master");

    }
}
