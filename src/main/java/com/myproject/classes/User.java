package com.myproject.classes;

import org.bson.Document;
import org.bson.types.ObjectId;

public class User {

    private ObjectId uid;
    private String name;
    private String mail;
    private int edad;
    private int avatar = 1;

    public User(Document doc) {
        this.uid = doc.getObjectId("_id");
        this.name = doc.getString("name");
        this.mail = doc.getString("email");
        this.edad = doc.getDouble("edad").intValue();
        this.avatar = doc.getDouble("avatar").intValue();
    }
    
    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

}
