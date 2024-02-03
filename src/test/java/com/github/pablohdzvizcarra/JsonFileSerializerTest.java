package com.github.pablohdzvizcarra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class JsonFileSerializerTest {
    private JsonFileSerializer jsonFileSerializer;
    private DatabaseUtils databaseUtils;
    private static final String ROOT_FOLDER = "database";
    private static final String COLLECTION_TEST = "test_database";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUpAll() {
        if (Files.notExists(Paths.get(ROOT_FOLDER))) {
            try {
                Files.createDirectory(Paths.get(ROOT_FOLDER));
            } catch (Exception ignored) {
            }
        }

        if (Files.notExists(Paths.get(ROOT_FOLDER, COLLECTION_TEST))) {
            try {
                Files.createDirectory(Paths.get(ROOT_FOLDER, COLLECTION_TEST));
            } catch (Exception ignored) {
            }
        }
    }

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
        Path filepath = Paths.get(ROOT_FOLDER, COLLECTION_TEST, documentId);
        jsonFileSerializer.save(userJson, filepath);
        String json = jsonFileSerializer.deserializeFileIntoJson(filepath);

        assertThat(json)
                .isNotEmpty()
                .contains("James")
                .contains("Gosling")
                .contains("james@java.com")
                .contains("java_master");

        jsonFileSerializer.deleteJsonFile(filepath);
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
        Path filepathOne = Paths.get(ROOT_FOLDER, COLLECTION_TEST, documentOne);
        String documentTwo = databaseUtils.createDocumentId();
        Path filepathTwo = Paths.get(ROOT_FOLDER, COLLECTION_TEST, documentTwo);

        // Act
        jsonFileSerializer.save(userOne, filepathOne);
        jsonFileSerializer.save(userTwo, filepathTwo);
        String jsonOne = jsonFileSerializer.deserializeFileIntoJson(filepathOne);
        String jsonTwo = jsonFileSerializer.deserializeFileIntoJson(filepathTwo);

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
        
        jsonFileSerializer.deleteJsonFile(filepathOne);
        jsonFileSerializer.deleteJsonFile(filepathTwo);
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
        Path filepath = Paths.get(ROOT_FOLDER, COLLECTION_TEST, documentId);
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
        
        jsonFileSerializer.deleteJsonFile(filepath);
    }

    @Test
    void shouldDeleteJsonFile() {
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
        Path filepath = Paths.get(ROOT_FOLDER, COLLECTION_TEST, documentId);
        jsonFileSerializer.save(userJson, filepath);

        // Act
        jsonFileSerializer.deleteJsonFile(filepath);

        // Assert
        assertThat(Files.exists(filepath))
                .isFalse();
    }

    @Test
    void shouldReturnJsonWithAllFilesInsideFolder() throws JsonMappingException, JsonProcessingException {
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
        
        String documentIdOne = databaseUtils.createDocumentId();
        String documentIdTwo = databaseUtils.createDocumentId();
        Path filepathOne = Paths.get(ROOT_FOLDER, COLLECTION_TEST, documentIdOne);
        Path filepathTwo = Paths.get(ROOT_FOLDER, COLLECTION_TEST, documentIdTwo);
        
        jsonFileSerializer.save(userOne, filepathOne);
        jsonFileSerializer.save(userTwo, filepathTwo);
        Path collectionPath = Paths.get(ROOT_FOLDER, COLLECTION_TEST);

        // Act
        String jsonData = jsonFileSerializer.deserializeFilesInFolderIntoJson(collectionPath);
        
        // Deserialize the jsonData into a list of Maps
        List<Map<String, Object>> documents = objectMapper.readValue(jsonData, new TypeReference<>() {});

        // Assert
        assertThat(documents)
                .isNotEmpty()
                .satisfies(list -> {
                    Map<String, Object> firstUser = list.get(0);
                    assertThat(firstUser).contains(entry("name", "James"));
                    assertThat(firstUser).contains(entry("lastName", "Gosling"));
                    assertThat(firstUser).contains(entry("email", "james@java.com"));
                    assertThat(firstUser).contains(entry("nickname", "java_master"));
                })
                .hasSize(2)
                .extracting(jsonDataMap -> jsonDataMap.get("name"))
                .contains("James", "Brendan")
                .hasSize(2);

        // Clean up
        jsonFileSerializer.deleteJsonFile(filepathOne);
        jsonFileSerializer.deleteJsonFile(filepathTwo);
    }
}
