package com.example.socialcompass.Friendmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {FriendListItem.class},version = 1)
public abstract class FriendDatabase extends RoomDatabase {

    private static FriendDatabase singleton = null;
    public abstract FriendListItemDao friendListItemDao();

    public synchronized static FriendDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = FriendDatabase.makeDatabase(context);

        }
        return singleton;
    }

    private static FriendDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, FriendDatabase.class,"socialcompass.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    /**
                     * Called when the database is created for the first time. This is called after all the
                     * tables are created.
                     *
                     * @param db The database.
                     */
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(()->{
                            List<FriendListItem> friends = FriendListItem
                                    .loadJSON(context,"demo_friends.json");
                            getSingleton(context).friendListItemDao().insertAll(friends);
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void inject(FriendDatabase testDatabase){
        if(singleton != null){
            singleton.close();
        }
        singleton = testDatabase;
    }
}
