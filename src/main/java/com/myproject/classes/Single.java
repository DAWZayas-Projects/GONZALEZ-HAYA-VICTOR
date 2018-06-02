package com.myproject.classes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bson.types.ObjectId;

public class Single {

    private ObjectId postid;
    private String userid;
    private String departament;
    private String username;
    private String text;
    private double state;
    private Object tags;
    private Date dateIn;
    private Date dateOut;
    private double avatar;

    private int days;

    public Single(ObjectId pid, String uid, String departament, String username, double avatar, String text, double state, Date datein, Date dateout, Object tags) {
        this.postid = pid;
        this.userid = uid;
        this.departament = departament;
        this.username = username;
        this.text = text;
        this.state = state;
        this.dateIn = datein;
        this.dateOut = dateout;
        this.tags = tags;
        this.avatar = avatar;
    }

    public ObjectId getPid() {
        return postid;
    }

    public String getPidString() {
        return postid.toString();
    }

    public String getId() {
        return userid;
    }

    public String getUserName() {
        return username;
    }

    public String getText() {
        return text;
    }

    public String getTextShort() {
        if (text.length() < 10) {
            return text;
        } else {
            return text.substring(0, 45) + "..";
        }
    }

    public String getDateIn() throws ParseException {
        DateFormat formatter;
        formatter = new SimpleDateFormat("dd/MM");
        return formatter.format(dateIn);
    }

    public String getDateOut() throws ParseException {
        DateFormat formatter;
        formatter = new SimpleDateFormat("dd/MM");
        return formatter.format(dateOut);
    }

    public Object getTags() {
        return tags;
    }

    public int getDays() {
        Date today = new Date();
        int days2 = (int) ((this.dateOut.getTime() - today.getTime()) / 86400000);
        return days2;
    }

    public String getState() {

        String color = "";

        if ((int) this.state == 0) {
            color = "assigned";
        }
        if ((int) this.state == 1) {
            color = "process";
        }
        if ((int) this.state == 2) {
            color = "completed";
        }

        if ((int) this.state == 3) {
            color = "outoftime";
        }

        return color;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public void setState(double state) {
        this.state = state;
    }

    public void setPid(ObjectId pid) {
        this.postid = pid;
    }

    public void setId(String id) {
        this.userid = id;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    public void setDateIn(Date datein) {
        this.dateIn = datein;
    }

    public void setDateOut(Date dateout) {
        this.dateOut = dateout;
    }

    public int getAvatar() {
        return (int) avatar;
    }

    public void setAvatar(double avatar) {
        this.avatar = avatar;
    }
}
