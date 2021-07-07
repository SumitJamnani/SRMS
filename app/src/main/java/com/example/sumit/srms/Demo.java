package com.example.sumit.srms;

public class Demo {
    public String Email;
    public String Password;

    public Demo()
    {

    }
    public Demo(String email, String password)
    {
        this.Email = email;
        this.Password = Password;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
