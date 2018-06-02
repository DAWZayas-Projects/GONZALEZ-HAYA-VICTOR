package com.myproject.db;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.myproject.faces.HomeFace;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Home extends MongoConnection {

    public FindIterable<Document> homeList(boolean defaultt, String filter, String filtertype, Date searchstart, Date searchend, int skiper, int limiter) throws MongoException {
        FindIterable<Document> docs = null;

        if (!defaultt) {
            setTotalresults(getHomeCol().count());
            docs = getHomeCol().find().skip(skiper).limit(limiter).sort(Sorts.ascending("dateOut"));
        } else {
            docs = homeListFiltered(filter, filtertype, searchstart, searchend, skiper, limiter);
        }

        return docs;
    }

    public FindIterable<Document> homeListFiltered(String nameFilter, String filtertype, Date searchstart, Date searchend, int skiper, int limiter) {

        Document doc = null;
        FindIterable<Document> f;

        if (searchend != null) {

            if (searchstart != null) {
                doc = new Document("dateOut", new Document("$lte", searchend).append("$gte", searchstart));
            } else {
                doc = new Document("dateOut", searchend);
            }

            return ifTextEmpty(doc, filtertype, nameFilter, skiper,limiter);

        } else if (searchend == null && searchstart != null) {

            return ifTextEmpty(new Document("dateOut", searchstart), filtertype, nameFilter, skiper, limiter);

        } else {
            setTotalresults(getHomeCol().count(Filters.regex(filtertype, nameFilter, "i")));
            return getHomeCol().find(Filters.regex(filtertype, nameFilter, "i")).skip(skiper).limit(limiter).sort(Sorts.ascending("dateOut"));
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
        return getHomeCol().find(doc).skip(skip).limit(limit).sort(Sorts.ascending("dateOut"));
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

    public Document getUserName(String userid) {
        Document userInfo = database.getCollection("users").find(eq("_id", new ObjectId(userid))).first();
        return userInfo;
    }
    
    public FindIterable<Document> getUsers() {
        FindIterable<Document> users = database.getCollection("users").find();
        return users;
    }

}
