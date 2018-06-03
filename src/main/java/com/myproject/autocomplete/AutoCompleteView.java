package com.myproject.autocomplete;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

@Named
@Stateless
public class AutoCompleteView {

    private String selectedThemes;
    private String stringValue;
     
    @Inject
    private ThemeService service;
     
    public List<Theme> completeTheme(String query) {
        List<Theme> allThemes = service.getThemes();
        List<Theme> filteredThemes = new ArrayList<Theme>();
         
        for (int i = 0; i < allThemes.size(); i++) {
            Theme skin = allThemes.get(i);
            if(skin.getName().toLowerCase().startsWith(query)) {
                filteredThemes.add(skin);
            }
        }
         
        return filteredThemes;
    }

    public String getSelectedThemes() {
        return selectedThemes;
    }
 
    public void setSelectedThemes(String selectedThemes) {
        stringValue = selectedThemes;
    }
    
}

