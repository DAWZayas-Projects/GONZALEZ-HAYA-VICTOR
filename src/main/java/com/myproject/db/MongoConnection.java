package com.myproject.db;

import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

public class MongoConnection {

    MongoClientOptions mongoClientOptions = new MongoClientOptions
            .Builder()
            .serverSelectionTimeout(5000)
            //.maxConnectionLifeTime(3000)
            .connectionsPerHost(10)
            .build();
    MongoClient mongo = new MongoClient(new ServerAddress("localhost", 27017), mongoClientOptions);
    MongoDatabase database = mongo.getDatabase("newdb");

}
