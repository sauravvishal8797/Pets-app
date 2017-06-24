package android.example.com.pets.data;

import java.lang.reflect.Method;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by saurav on 25/6/17.
 */

public class PetDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PetDbHelper.class.getSimpleName();

    /**
     * Constant representing the name of the database
     */
    private static final String DATABASE_NAME = "Pets.db";

    /**
     * Constant representing the database version
     */
    private static final int DATABASE_VERSION = 1;

    public PetDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );

    }

    /**
     *  Method for creating the database
     */
    @Override public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_ENTRIES = "CREATE" + PetContract.PetEntry.TABLE_NAME + "("
                +PetContract.PetEntry.PET_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +PetContract.PetEntry.PET_NAME_COL + " TEXT NOT NULL, "
                +PetContract.PetEntry.PET_BREED_COL + " TEXT NOT NULL, "
                +PetContract.PetEntry.PET_WEIGHT_COLOUMN + " INTEGER NOT NULL, "
                +PetContract.PetEntry.PET_GENDER_COL + " INTEGER NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);


    }

    /**
     * Method for upgrading the database
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {





    }
}
