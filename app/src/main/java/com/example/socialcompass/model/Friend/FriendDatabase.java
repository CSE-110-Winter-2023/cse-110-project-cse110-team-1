package com.example.socialcompass.model.Friend;


import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Friend.class}, version = 2, exportSchema = false)
public abstract class FriendDatabase extends RoomDatabase {
    private volatile static FriendDatabase instance = null;

    public abstract FriendDao getDao();


    public synchronized static FriendDatabase provide(Context context) {
        if (instance == null) {
            instance = FriendDatabase.make(context);
        }
        return instance;
    }


    private static FriendDatabase make(Context context) {
        return Room.databaseBuilder(context, FriendDatabase.class, "user_public_info.db")
                .allowMainThreadQueries()
//                .addMigrations(MIGRATION_1_2)
                .build();
    }

//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase db) {
//            db.execSQL("CREATE TABLE user_table_new (public_code TEXT PRIMARY KEY NOT NULL, " +
//                    "label TEXT, latitude LONG, longtitude LONG, updated_at LONG)");
//            db.execSQL("INSERT INTO user_table_new (public_code, label, latitude, longtitude, updated_at) " +
//                    "SELECT public_code, label, latitude, longtitude, updated_at FROM user_public_info");
//            db.execSQL("DROP TABLE user_public_info");
//            db.execSQL("ALTER TABLE user_table_new RENAME TO user_public_info");
//        }
//    };


    @VisibleForTesting
    public static void inject(FriendDatabase testDatabase) {
        if (instance != null ) {
            instance.close();
        }
        instance = testDatabase;
    }



}


