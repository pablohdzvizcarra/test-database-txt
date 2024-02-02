package com.github.pablohdzvizcarra;

import io.javalin.Javalin;

public class App {
    private static final Database<User> database = new Database<>("users");
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(9182);
        app.get("/", ctx -> ctx.result("Hello World"));
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
    }
}
