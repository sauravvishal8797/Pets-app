package android.example.com.pets.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by saurav on 25/6/17.
 */

public class petProvider extends ContentProvider {

    private PetDbHelper mPetDbHelper;


    @Override public boolean onCreate() {

        mPetDbHelper = new PetDbHelper(getContext());
        return false;
    }

    @Nullable @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1,
                        @Nullable String s1) {
        return null;
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
