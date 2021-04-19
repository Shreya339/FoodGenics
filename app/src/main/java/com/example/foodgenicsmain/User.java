package com.example.foodgenicsmain;

public class User {

        public String username,email,disease;

        public User(String username, String email, String disease) {
            this.username = username;
            this.email = email;
            this.disease = disease;
        }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getDisease() {
        return disease;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }
}
