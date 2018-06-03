package com.myproject.autocomplete;

import com.mongodb.MongoTimeoutException;
import com.mongodb.client.FindIterable;
import com.myproject.db.Home;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import org.bson.Document;


@Named
@Stateless
public class ThemeService {

    private List<Theme> themes;
    FindIterable<Document> f;
     
    @PostConstruct
    public void init() {
        
        try {
        Home h = new Home();
        f = h.getUsers();
        themes = new ArrayList<Theme>();
        themes.add(new Theme(100,"Contabilidad","contabilidad"));
        themes.add(new Theme(101,"Movil","movil"));
        themes.add(new Theme(102,"Marketing","marketing"));
        themes.add(new Theme(103,"Administracion","administracion"));
        themes.add(new Theme(104,"Finanzas","finanzas"));
        themes.add(new Theme(105,"Mantenimiento","mantenimiento"));
        themes.add(new Theme(106,"Sistemas","sistemas"));
        for (Document doc : f)  themes.add(new Theme(doc.getDouble("avatar").intValue(), doc.getString("name"),  doc.getString("name").toLowerCase())); 
        
        h.closeMongo();

        } catch (MongoTimeoutException e) {
            System.err.println("**VDEx**" + e);
        }
    }
    
    
    public List<Theme> getThemes() {
        return themes;
    } 
}

