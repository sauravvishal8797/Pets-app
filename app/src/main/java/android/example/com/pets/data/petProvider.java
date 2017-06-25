package android.example.com.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by saurav on 25/6/17.
 */

public class petProvider extends ContentProvider {

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
        return null;
    }

    @Override public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s,
                                @Nullable String[] strings) {
        return 0;
    }
}
