package android.example.com.pets.data;

import static android.R.attr.id;

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
        int match = uriMatcher.match(uri);
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
        return null;
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


        Long id = db.insert(PetContract.PetEntry.TABLE_NAME, null,contentValues);
        if(id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        else {
            Toast.makeText(getContext(), R.string.Inserted, Toast.LENGTH_SHORT).show();

        }
        Uri inserturi = ContentUris.withAppendedId(uri, id);
        return inserturi;
    }

    @Override public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s,
                                @Nullable String[] strings) {
        return 0;
    }
}
