/**
 *
 * @author vicdata
 */
package com.myproject.db;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import org.bson.types.ObjectId;


public class Login extends MongoConnection{

    public boolean Authentication(String user,String pass) {
        
        MongoCollection<Document> collection = database.getCollection("users");
        return collection.count(new Document("name",user).append("email", pass)) == 1;
    }
    
    public ObjectId getUserId(String username){
        Document userInfo = database.getCollection("users").find(eq("name", username)).first();
        return userInfo.getObjectId("_id");
    }
}
