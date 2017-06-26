package android.example.com.pets.data;

import static android.R.attr.id;
import static android.R.attr.thicknessRatio;
import static android.example.com.pets.data.PetContract.PetEntry.CONTENT_LIST_TYPE;
import static android.icu.lang.UCharacter.JoiningGroup.PE;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.pets.R;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by saurav on 25/6/17.
 */

public class petProvider extends ContentProvider {

    public static final String LOG_TAG = petProvider.class.getSimpleName();

    private PetDbHelper mPetDbHelper;

    private static final int PETS = 100;
    private static final int PETS_ID = 101;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_NAME, 100);
        uriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_NAME + "/", 101);
    }


    @Override public boolean onCreate() {

        mPetDbHelper = new PetDbHelper(getContext());
        return false;
    }

    @Nullable @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1,
                        @Nullable String s1) {

        SQLiteDatabase db = mPetDbHelper.getReadableDatabase();
        Cursor cursor = null;
        final int match = uriMatcher.match(uri);
        switch (match){
            case PETS:
                cursor = db.query(PetContract.PetEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;

            case PETS_ID:
                s = PetContract.PetEntry.PET_ID_COL + "=?";
                strings1 = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PetContract.PetEntry.TABLE_NAME, null, s, strings1, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri" + uri);
        }
        return cursor;
    }

    @Nullable @Override public String getType(@NonNull Uri uri) {

        final int match = uriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PETS_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable @Override public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {


        final int match = uriMatcher.match(uri);
        switch (match){
            case PETS:
                return petinsert(uri, contentValues);
            default:
                throw new IllegalArgumentException("Unknown uri" + uri);
        }

    }

    private Uri petinsert(Uri uri, ContentValues contentValues){

        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
        String name = contentValues.getAsString(PetContract.PetEntry.PET_NAME_COL);
        if(name == null){
            throw new IllegalArgumentException("Pet reqquires a name");
        }
        String breed = contentValues.getAsString(PetContract.PetEntry.PET_BREED_COL);
        if(breed == null){
            throw new IllegalArgumentException("Pet requires a breed");
        }
        String weight = contentValues.getAsString(PetContract.PetEntry.PET_WEIGHT_COLOUMN);
        if(weight == null){
            throw new IllegalArgumentException("Pet requires some weight");
        }
        Integer gender = contentValues.getAsInteger(PetContract.PetEntry.PET_GENDER_COL);
        if (gender == null || !PetContract.PetEntry.validgender(gender)) {
            throw new IllegalArgumentException("Gender out of scope");

        }


        Long id = db.insert(PetContract.PetEntry.TABLE_NAME, null,contentValues);
        if(id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        Uri inserturi = ContentUris.withAppendedId(uri, id);
        return inserturi;
    }

    @Override public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        final int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case PETS:
                return deletepet(s, strings);

            case PETS_ID:
                SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
                s = PetContract.PetEntry.PET_ID_COL + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int i = db.delete(PetContract.PetEntry.TABLE_NAME, s, strings);
                return i;
        }
    }
    private int deletepet(String selection, String[] selectionargs) {

        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
        int i = db.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionargs);
        return i;
    }

        @Override public int update (@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s,
                @Nullable String[]strings){


            final int match = uriMatcher.match(uri);
            switch (match) {
                case PETS:
                    return updatepet(contentValues, s, strings);

                case PETS_ID:
                    SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
                    s = PetContract.PetEntry.PET_ID_COL + "=?";
                    strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                    int i = db.update(PetContract.PetEntry.TABLE_NAME, contentValues, s, strings);
                    return i;
            }
            return 0;
        }


    private int updatepet(ContentValues contentValues, String selection, String[] selectionargs){

        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
        if(contentValues.containsKey(PetContract.PetEntry.PET_NAME_COL)){
            String name = contentValues.getAsString(PetContract.PetEntry.PET_NAME_COL);
            if(name == null){
                throw new IllegalArgumentException("Pet requires a name to be updated");
            }
        }
        if(contentValues.containsKey(PetContract.PetEntry.PET_BREED_COL)){
            String breed = contentValues.getAsString(PetContract.PetEntry.PET_BREED_COL);
            if(breed == null){
                throw new IllegalArgumentException("Pet requires a breed to be updated");
            }
        }
        if(contentValues.containsKey(PetContract.PetEntry.PET_WEIGHT_COLOUMN)){
            String weight = contentValues.getAsString(PetContract.PetEntry.PET_WEIGHT_COLOUMN);
            if (weight == null) {
                throw new IllegalArgumentException("Pet requires a weight to be updated");
            }
        }
        if(contentValues.containsKey(PetContract.PetEntry.PET_GENDER_COL)) {
            Integer gender = contentValues.getAsInteger(PetContract.PetEntry
                    .PET_GENDER_COL);
            if (gender != 0 && gender != 1 && gender != 2) {
                throw new IllegalArgumentException("Gender out of scope");

            }
        }
        int i = db.update(PetContract.PetEntry.TABLE_NAME, contentValues, selection, selectionargs);
        return i;


        }


    }
}
