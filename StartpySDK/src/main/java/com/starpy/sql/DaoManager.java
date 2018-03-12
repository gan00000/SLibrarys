package com.starpy.sql;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * Created by gan on 2018/3/10.
 */

public class DaoManager {

    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;

    private static  DaoManager daoManager;

    private DaoSession daoSession;

    private DaoManager(Context context) {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, ENCRYPTED ? "starpy-persion-db-encrypted" : "starpy-persion-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoManager getDaoManager(Context context){

        if (daoManager == null){
            daoManager = new DaoManager(context.getApplicationContext());
        }
        return daoManager;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
