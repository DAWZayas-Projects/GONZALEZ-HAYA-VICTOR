/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myproject.faces.actions;

import com.myproject.db.Home;
import java.util.Arrays;
import java.util.Date;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import org.bson.Document;

/**
 *
 * @author vghaya
 */

@Named
@Stateless
public class AddFace {
    private String uid;
    private String username;
    private String texto;
    private String departament;
    private double state;
    private Date calendarIn;
    private Date calendarOut;
    
    public void addElement () {
        
        System.out.println(uid + "//" + username + "//" + texto + "//" + calendarIn + "//" + calendarOut + "//" + departament + "//" + state);

        Home home = new Home();
        Document doc = new Document("userId", uid)
                .append("userName", username)
                .append("description", texto)
                .append("status", state)
                .append("departament", departament)
                .append("temas", Arrays.asList("HTML5"))
                .append("dateIn", calendarIn)
                .append("dateOut", calendarOut);
        
        home.insertElement(doc);
    }
    
    public String getUid (){
        return uid;
    }
    
    public void setUid(String uid){
        this.uid = uid;
    }
    
    public String getUsername (){
        return username;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getTexto (){
        return texto;
    }
    
    public void setTexto(String txt){
        this.texto = txt;
    }
    
    public Date getCalendarIn() {
        return calendarIn;
    }
    
    public void setCalendarIn(Date calendarIn) {
        this.calendarIn = calendarIn;
    }
    
    public Date getCalendarOut() {
        return calendarOut;
    }
    
    public void setCalendarOut(Date calendarOut) {
        this.calendarOut = calendarOut;
    }
    
    public String getDepartament() {
        return departament;
    }
    
    public void setDepartament(String departament) {
        this.departament = departament;
    }
    
    public double getState() {
        return state;
    }
    
    public void setState(double state) {
        this.state = state;
    }
    
}
