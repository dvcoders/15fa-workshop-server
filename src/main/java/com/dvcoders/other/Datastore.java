package com.dvcoders.other;

import com.dvcoders.model.User;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * @author Jake Loo (23 September, 2015)
 */
public class Datastore {
    private static Logger LOG = LoggerFactory.getLogger(Datastore.class);

    private static String MONGODB_HOSTNAME = "127.0.0.1";
    private static int MONGODB_PORT = 27017;
    private static String MONGODB_NAME = "dvcoders";

    private static final Datastore INSTANCE = new Datastore();
    private static final Morphia MORPHIA = new Morphia();
    private static org.mongodb.morphia.Datastore DATASTORE;
    private static MongoClient MONGO_CLIENT;

    static {
        try {
            MONGO_CLIENT = new MongoClient(MONGODB_HOSTNAME, MONGODB_PORT);
            DATASTORE = MORPHIA.createDatastore(MONGO_CLIENT, MONGODB_NAME);
        } catch (UnknownHostException e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
        }

        MORPHIA.map(User.class);
    }

    public static Datastore getInstance() {
        return INSTANCE;
    }

    public org.mongodb.morphia.Datastore getDatastore() {
        return DATASTORE;
    }
}
