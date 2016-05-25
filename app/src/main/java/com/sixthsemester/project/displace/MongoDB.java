package com.sixthsemester.project.displace;

/**
 * Created by hps on 12/05/2016.
 */
import java.util.ArrayList;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
public class MongoDB {
    private static final String USERNAME = "happyhipster";
    private static final String PASSWORD = "testtest1";
    private static final String DB_ADDRESS = "ds015939.mlab.com";
    private static final int PORT = 15939;
    private static final String DB_NAME = "displace";
    private static final String COLLECTION_IMAGE = "Authentication";
    private static final String COLLECTION_USERS = "users";
    private static final String connectionString = "mongodb://" + USERNAME + ":" + PASSWORD + "@" + DB_ADDRESS + ":"
            + PORT + "/" + DB_NAME;


    private static MongoDB mongoDB;
    private DB db;

    @SuppressWarnings({ "deprecation", "resource" })
    private MongoDB() {
        MongoClientURI mongoClientURI = new MongoClientURI(connectionString);
        MongoClient mongoclient = new MongoClient(mongoClientURI);
        db = mongoclient.getDB(mongoClientURI.getDatabase());
    }

    public static MongoDB getInstance() {
        if (mongoDB == null) {
            mongoDB = new MongoDB();
        }
        return mongoDB;
    }

    public DB getDb() {
        return db;
    }

    public ArrayList<String> retrieveUserList() {

        ArrayList<String> arrayListUsers = new ArrayList<String>();

        DBObject query = new BasicDBObject();
        BasicDBObject fields = new BasicDBObject("_id", 0);
        DBCursor dbCursor = db.getCollection(COLLECTION_USERS).find(query, fields);

        for (DBObject elem : dbCursor) {
            arrayListUsers.add(elem.get("username").toString());
        }
        return arrayListUsers;
    }

    public void insertImage(String base64EncodedData,String Email) {
       // db.getCollection(COLLECTION_IMAGE).insert(new BasicDBObject("Image", base64EncodedData));
        BasicDBObject updateQuery = new BasicDBObject();
        updateQuery.put("email",Email);

        BasicDBObject updateCommand = new BasicDBObject();
        updateCommand.put("$push", new BasicDBObject("Image", base64EncodedData));
        db.getCollection(COLLECTION_IMAGE).update( updateQuery, updateCommand, true, true );
    }

    public Object[] retrieveImages(ArrayList<String> alreadyEncodedData) {

       // BasicDBObject fromFromToTo = new BasicDBObject("from", from).append("to", to);
        //BasicDBObject fromToToFrom = new BasicDBObject("from", to).append("to", from);

            BasicDBList orCondition = new BasicDBList();
       // orCondition.add(fromFromToTo);
        //orCondition.add(fromToToFrom);

        // db.chat.find({$or : [{"from" : from, "to" : to}, {"from" : to, "to" : from}]})
        DBObject query = new BasicDBObject("$or", orCondition);
        BasicDBObject fields = new BasicDBObject("_id", "573b60835acb2edbb6870b3d").append("state", 0);
        DBCursor dbCursor = db.getCollection(COLLECTION_IMAGE).find(query, fields);

        ArrayList<String> arrayListUsernames = new ArrayList<String>();
        ArrayList<Bitmap> arrayListBitmaps = new ArrayList<Bitmap>();
        ArrayList<String> arrayListEncodedData = new ArrayList<String>();

        for (DBObject elem : dbCursor) {
                if (!alreadyEncodedData.contains(elem.get("selectedImageBytes").toString())) {
                    arrayListUsernames.add("Image sent by " + elem.get("from") + " to " + elem.get("to"));
                    arrayListBitmaps.add(returnBitmap(elem.get("selectedImageBytes").toString()));
                    arrayListEncodedData.add(elem.get("selectedImageBytes").toString());
                }
            }


        Object object[] = new Object[3];
        object[0] = arrayListUsernames;
        object[1] = arrayListBitmaps;
        object[2] = arrayListEncodedData;
        return object;
    }

    public void deleteUnSavedImages(String to, String from) {
        DBObject query = new BasicDBObject("from", to).append("to", from).append("state", false);
        db.getCollection(COLLECTION_IMAGE).remove(query);
    }

    private Bitmap returnBitmap(String base64EncodedData) {
        byte[] imageDataInBytes = Base64.decode(base64EncodedData, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageDataInBytes, 0, imageDataInBytes.length);
    }
}
