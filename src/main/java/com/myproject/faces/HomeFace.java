/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myproject.faces;

import com.mongodb.MongoTimeoutException;
import com.mongodb.client.FindIterable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import org.bson.Document;
import com.myproject.classes.Single;
import com.myproject.db.Home;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import javax.ejb.Stateless;
import org.bson.types.ObjectId;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named
@Stateless
public class HomeFace implements Serializable {

    //Add Sction
    private String uid;
    private String username;
    private String texto;
    private String departament;
    private double state;
    private Date calendarIn;
    private Date calendarOut;

    private List<String> selectedTags;
    private List<String> tags = new ArrayList();;

    //Lists
    private List homeL = new ArrayList();
    private FindIterable<Document> docs;

    private List<Single> selectedElements;
    private Single selectedElement;

    private List manyPids;
    private boolean deleteMany;
    private boolean archiveMany;

    //Search
    private boolean filtered = false;
    private String textFilter;
    private String textFilterType = "userName";
    private Date searchEnd;
    private Date searchStart;

    //Pagination
    private int nelements = 20;
    private int skiper = 0;

    private long results = 0;
    private List<Integer> paginationTable;

    private Single ediElement;

    public void init() {
        
        fillTags();
        setHomeListElements();
        selectedElements = null;
    }
 
    public List getHomeList() {
        if (filtered) {
            setHomeListElements();
        }
        return homeL;
    }

    public void setNewPage(int page) {
        if (page > 1) {
            skiper = (page * 20) - 20;
        } else {
            skiper = 0;
        }
        filtered = true;
        setHomeListElements();
    }

    public void setHomeListElements() {
        this.homeL.clear();
        try {

            Home h = new Home();
            docs = h.homeList(filtered, textFilter, textFilterType, searchStart, searchEnd, skiper, nelements);
            results = h.getTotalresults();

            for (Document doc : docs) {
                Document userinfo = h.getUserName(doc.getString("userId"));
                homeL.add(new Single(
                        doc.getObjectId("_id"),
                        doc.getString("userId"),
                        doc.getString("departament"),
                        userinfo.getString("name"),
                        userinfo.getDouble("avatar"),
                        doc.getString("description"),
                        doc.getDouble("state"),
                        doc.getDate("dateIn"),
                        doc.getDate("dateOut"),
                        doc.get("temas")
                ));
            }

            h.closeMongo();

        } catch (MongoTimeoutException e) {
            System.err.println("**VDEx**" + e);
        }

        filtered = false;
    }

    public void setTableState() {
        setHomeListElements();
    }

    public void setResetList(boolean bol) {
        resetAddForm();
        filtered = false;
        init();
    }

    public Home newHome() {
        return new Home();
    }

    public Date getSearchEnd() {
        return searchEnd;
    }

    public void setSearchEnd(Date searchEnd) {
        this.searchEnd = searchEnd;
    }

    public long getResults() {
        return results;
    }

    public int getResultsPagination() {
        double a = Math.ceil((double) results / 20);
        return (int) a;
    }

    public List getPaginationTable() {
        List<Integer> li = new ArrayList();
        int page = 10;
        if (getResultsPagination() > 10) {
            for (int i = 1; i <= page; i++) {
                li.add(i);
            }
        } else {
            for (int i = 1; i <= getResultsPagination(); i++) {
                li.add(i);
            }
        }

        return li;
    }

    public void setPaginationTable(List<Integer> paginationTable) {
        this.paginationTable = paginationTable;
    }

    public void setResults(long r) {
        this.results = r;
    }

    public void setFiltered(boolean state) {
        this.skiper = 0;
        this.filtered = state;
    }

    public boolean getFiltered() {
        return filtered;
    }

    public String getTextFilter() {
        return textFilter;
    }

    public void setTextFilter(String textfilter) {
        this.textFilter = textfilter;
    }

    public String getTextFilterType() {
        return textFilterType;
    }

    public void setTextFilterType(String textfiltertype) {
        this.textFilterType = textfiltertype;
    }

    public Single getSelectedElement() {
        return selectedElement;
    }

    public void setSelectedElement(Single selectedelement) {
        this.selectedElement = selectedelement;
    }

    public List<Single> getSelectedElements() {
        return selectedElements;
    }

    public String selectedElementsConfirmation() {
        String s = "";

        if (selectedElements.size() > 0) {
            for (Single doc : selectedElements) {
                s += doc.getId() + "\n";
            }
        }

        return s;
    }

    public void setSelectedElements(List<Single> selectedelements) {
        this.selectedElements = selectedelements;
    }

    public int selectedCount() {
        if (this.selectedElements != null) {
            return this.selectedElements.size();
        } else {
            return 0;
        }

    }

    public Single getEdiElement() {
        return ediElement;
    }

    public void setEdiElement(Single ediElement) {
        this.ediElement = ediElement;
    }

    public void deleteSingle(ObjectId obj) {
        boolean deleteconfirm;
        deleteconfirm = newHome().deleteSingle(obj);
        if (deleteconfirm) {
            addMessage("Delete", "Eliminado con éxito.");
            filtered = true;
            setHomeListElements();
        } else {
            addMessage("Delete", "No se ha podido eliminar.");
        }
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void resetAddForm() {
        textFilter = "";
        textFilterType = "userName";
        searchEnd = null;
        searchStart = null;
    }

    public Date getSearchStart() {
        return searchStart;
    }

    public void setSearchStart(Date searchStart) {
        this.searchStart = searchStart;
    }

    public List getManyPids() {
        List<ObjectId> pids = new ArrayList();
        for (Single elem : selectedElements) {
            pids.add(elem.getPid());
        }
        return pids;
    }

    public boolean getDeleteMany() {
        return deleteMany;
    }

    public void setDeleteMany(boolean confmany) {
        this.deleteMany = confmany;
        boolean deletemany;

        deletemany = newHome().deleteMany(getManyPids());
        if (deletemany) {
            addMessage("Delete", "Eliminados n elementos.");
            filtered = true;
            selectedElements.clear();
        } else {
            addMessage("Delete", "No se ha podido eliminar.");
        }
    }

    public void setArchiveMany(boolean confmany) {
        this.archiveMany = confmany;
        boolean archivemany;

        try {
            Home x = new Home();
            archivemany = x.archiveMany(selectedElements, getManyPids());
            x.closeMongo();

            if (archivemany) {
                addMessage("Archive", "Archive succesfull");
                filtered = true;
                selectedElements.clear();
            } else {
                addMessage("Archive", "Archive fail.");
            }
        } catch (MongoTimeoutException e) {
            System.err.println("**VDEx**" + e);
        }

    }

    public void buttonAction(ObjectId pid, int newstate) {

        boolean updateconfirm = new Home().updateState(pid, (double) newstate);

        if (updateconfirm) {
            addMessage("Updated", "Succesfull.");
            filtered = true;
            setHomeListElements();
        } else {
            addMessage("Updated", "Update error.");
        }

    }

    //Add Section
    public void addElement() {
        System.out.println("notadded");
        if (calendarIn != null && calendarOut != null && texto != null && !texto.equals("")) {
            System.out.println("added");
            Home home = new Home();
            Document doc = new Document("userId", "5ab4212db797ad40c44fd145")
                    .append("userName", "maria")
                    .append("departament", departament)
                    .append("description", texto)
                    .append("state", state)
                    .append("temas", selectedTags)
                    .append("dateIn", calendarIn)
                    .append("dateOut", calendarOut);

            home.insertElement(doc);
            filtered = true;
            setHomeListElements();
            System.out.println("ADE" + selectedTags);

        } else {
            addMessage("Formulario", "Necesitas rellenar todos los campos");
        }
    }

    public void resetAdd() {
        texto = null;
        calendarIn = null;
        calendarOut = null;
        departament = null;
        state = 0;
        selectedTags.clear();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String txt) {
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

    public List<String> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<String> selectedTags) {
        this.selectedTags = selectedTags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    
    public void fillTags(){
        tags.clear();
        tags.add("HTML");
        tags.add("Firefox");
        tags.add("Linux");
        tags.add("React");
        tags.add("Vue");
    }
    
    // Add Section

}
