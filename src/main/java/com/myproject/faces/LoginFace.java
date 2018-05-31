package com.myproject.faces;

import java.io.IOException;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import com.myproject.db.Login;

@Named
@Dependent
public class LoginFace implements Serializable {

    private String username;
    private String password;

    public void doLogin() {

        FacesContext context = FacesContext.getCurrentInstance();
        Login log = new Login();

        if (log.Authentication(this.username, this.password)) {

            context.getExternalContext().getSessionMap().put("user", username);
            context.getExternalContext().getSessionMap().put("userId", new Login().getUserId(username));

            try {
                context.getExternalContext().redirect("Test.xhtml");
            } catch (IOException e) {
                // e.printStackTrace();
            }
        } else {
            //Send an error message on Login Failure 
            context.addMessage(null, new FacesMessage("Authentication Failed. Check username or password."));

        }

    }

    public void doLogout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        try {
            context.getExternalContext().redirect("Login.xhtml");
        } catch (IOException e) {
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
