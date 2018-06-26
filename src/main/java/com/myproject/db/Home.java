package com.myproject.db;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.myproject.classes.Single;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Home extends MongoConnection {

    public FindIterable<Document> homeList(Document docfilter) throws MongoException {
        FindIterable<Document> docs = null;

        if (!docfilter.getBoolean("isFiltered")) {
            setTotalresults(getHomeCol().count());
            docs = getHomeCol().find().skip(docfilter.getInteger("skiper")).limit(docfilter.getInteger("limiter")).sort(Sorts.ascending("dateOut"));
        } else {
            docs = homeListFiltered(docfilter);
        }

        return docs;
    }
    
    public Document getUserName(String userid) {
        Document userInfo = database.getCollection("users").find(eq("_id", new ObjectId(userid))).first();
        return userInfo;
    }
    
    public FindIterable<Document> getUsers() {
        FindIterable<Document> users = database.getCollection("users").find();
        return users;
    }

    public FindIterable<Document> homeListFiltered(Document searchfilter) {

        Document doc = null;
        FindIterable<Document> f;

        if (searchfilter.getDate("dateTo") != null) {

            if (searchfilter.getDate("dateFrom") != null) {
                doc = new Document("dateOut", new Document("$lte", searchfilter.getDate("dateTo")).append("$gte", searchfilter.getDate("dateFrom")));
            } else {
                doc = new Document("dateOut", searchfilter.getDate("dateTo"));
            }

            return ifTextEmpty(doc, searchfilter.getString("filterKey"), searchfilter.getString("textFilter"), searchfilter.getInteger("skiper"),searchfilter.getInteger("limiter"));

        } else if (searchfilter.getDate("dateTo") == null && searchfilter.getDate("dateFrom") != null) {

            return ifTextEmpty(new Document("dateOut", searchfilter.getDate("dateFrom")), searchfilter.getString("filterKey"), searchfilter.getString("textFilter"), searchfilter.getInteger("skiper"), searchfilter.getInteger("limiter"));

        } else {
            System.out.println("DEFAULTEMPTYQUERY");
            setTotalresults(getHomeCol().count(Filters.regex(searchfilter.getString("filterKey"), searchfilter.getString("textFilter"), "i")));
            return getHomeCol()
                    .find(Filters.regex(searchfilter.getString("filterKey"), searchfilter.getString("textFilter"), "i"))
                    .skip(searchfilter.getInteger("skiper"))
                    .limit(searchfilter.getInteger("limiter"))
                    .sort(Sorts.ascending("dateOut"));
                    //.modifiers(new Document("$hint","textIndex"));
            
        }
    }

    public FindIterable<Document> ifTextEmpty(Document doc, String filtertype, String nameFilter,int skiper,int limiter) {
        if (nameFilter.equals("")) {
            return returnDateSimple(doc,skiper,limiter);
        } else {
            return returnDatePlus(doc, filtertype, nameFilter,skiper,limiter);
        }
    }

    public FindIterable<Document> returnDatePlus(Document doc, String filtertype, String nameFilter,int skiper,int limiter) {
        setTotalresults(getHomeCol().count(doc.append(filtertype, nameFilter)));
        return getHomeCol().find(doc.append(filtertype, nameFilter)).skip(skiper).limit(limiter).sort(Sorts.ascending("dateOut"));
    }

    public FindIterable<Document> returnDateSimple(Document doc,int skip,int limit) {
        setTotalresults(getHomeCol().count(doc));
        return getHomeCol().find(doc).skip(skip).limit(limit).sort(Sorts.ascending("dateOut")).modifiers(new Document("$hint","datIndex"));
    }

    public MongoCollection<Document> getHomeCol() {
        return database.getCollection("homelist");
    }

    public long totalresults;

    public long getTotalresults() {
        return totalresults;
    }

    public void setTotalresults(long totalresults) {
        this.totalresults = totalresults;
    }

    public void insertElement(Document doc) {
        getHomeCol().insertOne(doc);
    }

    public void closeMongo() {
        mongo.close();
    }

    public boolean deleteSingle(ObjectId obj) {
        try {
            DeleteResult result = getHomeCol().deleteOne(eq("_id", obj));
            return result.getDeletedCount() == 1;

        } catch (Exception e) {
            System.out.println("//////" + e);
            return false;
        }

    }
    
    public boolean updateSingle(Single doc){
        
        Document bsonDoc = doc.getSingleBSON();
        
        ObjectId objpid = doc.getPid();
        bsonDoc.remove("postId");
        System.out.println("CONTROL" + bsonDoc);
        try {
            UpdateResult result = getHomeCol().replaceOne(new Document("_id", objpid), bsonDoc);
            return result.getModifiedCount() == 1;

        } catch (Exception e) {
            System.out.println("//////" + e);
            return false;
        }
    }

    public boolean updateState(ObjectId obj, double newstate) {
        
        try {
            UpdateResult result = getHomeCol().updateOne(eq("_id", obj), new Document("$set", new Document("state", newstate)));
            return result.getMatchedCount() == 1;
            
        } catch (Exception e) {
            System.out.println("//////" + e);
            return false;
        }
    }

    public boolean deleteMany(List<ObjectId> manyElements) {
        boolean deletemany = false;
        long c = 0;
        try {
            for (ObjectId doc : manyElements) {
                DeleteResult result = getHomeCol().deleteOne(eq("_id", doc));
                c += result.getDeletedCount();
            }
            if (c == manyElements.size()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean archiveMany(List<Single> selectedElements,List<ObjectId> manyElements){
        System.out.println("go" + selectedElements);
        List<Document> documents = new ArrayList<Document>();
        
        for(Single doc : selectedElements){
            documents.add(doc.getSingleBSON());
        }
        database.getCollection("archive").insertMany(documents);
        deleteMany(manyElements);
        
        return true;
    } 
}
