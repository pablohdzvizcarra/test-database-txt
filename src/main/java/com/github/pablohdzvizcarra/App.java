package com.github.pablohdzvizcarra;

import com.github.pablohdzvizcarra.util.Database;
import com.github.pablohdzvizcarra.util.User;

import io.javalin.Javalin;

public class App {
    private static final Database<User> database = new Database<>("users");
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(9182);
        app.get("/", ctx -> ctx.result("Welcome to JSON database!"));
        app.post("/api/v1/{collection}", ctx -> {
            String collection = ctx.pathParam("collection");
            String body = ctx.body();
            try {
                String documentId = database.createDocument(collection, body);
                ctx.result("The record was created in the collection " + collection + " with the id " + documentId);
            } catch (Exception e) {
                ctx.status(400);
                ctx.result("The record cannot be created message error: " + e.getMessage());
            }
        });

        app.get("/api/v1/{collection}/{id}", ctx -> {
            String collection = ctx.pathParam("collection");
            String id = ctx.pathParam("id");
            try {
                String document = database.readDocument(collection, id);
                ctx.contentType("application/json");
                ctx.result(document);
            } catch (Exception e) {
                ctx.status(404);
                ctx.result("The record cannot be retrieved message error: " + e.getMessage());
            }
        });

        app.delete("api/v1/{collection}/{id}", ctx -> {
            String collection = ctx.pathParam("collection");
            String id = ctx.pathParam("id");
            try {
                database.deleteDocument(collection, id);
                ctx.result("The record was deleted");
            } catch (Exception e) {
                ctx.status(400);
                ctx.result("The record cannot be deleted message error: " + e.getMessage());
            }
        });

        app.put("api/v1/{collection}/{id}", ctx -> {
            String collection = ctx.pathParam("collection");
            String id = ctx.pathParam("id");
            String body = ctx.body();
            try {
                database.updateDocument(collection, id, body);
                ctx.result("The record was updated");
            } catch (Exception e) {
                ctx.status(400);
                ctx.result("The record cannot be updated message error: " + e.getMessage());
            }
        });

        app.get("api/v1/{collection}", ctx -> {
            String collection = ctx.pathParam("collection");
            try {
                String documents = database.readAllDocumentsFromCollection(collection);
                ctx.contentType("application/json");
                ctx.result(documents);
            } catch (Exception e) {
                ctx.status(404);
                ctx.result("The records cannot be retrieved message error: " + e.getMessage());
            }
        });
    }

}
