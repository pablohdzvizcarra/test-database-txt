package com.github.pablohdzvizcarra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonFileSerializerTest {
    private JsonFileSerializer<User> jsonFileSerializer;
    private Path mockDatabasePath;

    @BeforeEach
    void setUp() {
        jsonFileSerializer = new JsonFileSerializer<>();
        prepareMockFile();
    }

    private void prepareMockFile() {
        try {
            mockDatabasePath = Files.createTempFile("mock", ".json");
        } catch (IOException ignored) {
        }
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

        jsonFileSerializer.save(userJson, mockDatabasePath);
        List<User> users = jsonFileSerializer.deserialize(mockDatabasePath, User.class);
        User user = users.get(0);

        assertEquals("James", user.name());
        assertEquals("Gosling", user.lastName());
        assertEquals("james@java.com", user.email());
        assertEquals("java_master", user.nickname());
    }

    @Test
    void shouldSaveTwoJsonStringToFile() {
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

        jsonFileSerializer.save(userOne, mockDatabasePath);
        jsonFileSerializer.save(userTwo, mockDatabasePath);
        List<User> users = jsonFileSerializer.deserialize(mockDatabasePath, User.class);

        assertThat(users)
                .hasSize(2);
    }
}
