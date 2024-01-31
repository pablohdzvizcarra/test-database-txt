package com.github.pablohdzvizcarra;

import com.fasterxml.jackson.annotation.JsonAlias;

public class User {
        private String name;
        private String lastName;
        private String email;
        private String nickname;
        @JsonAlias("_id")
        private String id;

        public User() {
        }

        public User(String name, String lastName, String email, String nickname) {
                this.name = name;
                this.lastName = lastName;
                this.email = email;
                this.nickname = nickname;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getNickname() {
                return nickname;
        }

        public void setNickname(String nickname) {
                this.nickname = nickname;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

}
