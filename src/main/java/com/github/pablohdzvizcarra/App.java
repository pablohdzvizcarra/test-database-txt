package com.github.pablohdzvizcarra;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(9182);
        app.get("/", ctx -> ctx.result("Hello World"));
    }
}
