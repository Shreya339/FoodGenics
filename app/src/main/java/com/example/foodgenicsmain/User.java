package com.example.foodgenicsmain;

public class User {

        public String username,email,disease;
        public int profile_img_id;

        public User(String username, String email, String disease,int profile_img_id) {
            this.username = username;
            this.email = email;
            this.disease = disease;
            this.profile_img_id = profile_img_id;
        }

    public int getProfile_img_id() {
        return profile_img_id;
    }

    public void setProfile_img_id(int profile_img_id) {
        this.profile_img_id = profile_img_id;
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
