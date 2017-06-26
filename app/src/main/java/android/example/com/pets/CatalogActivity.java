package android.example.com.pets;

import static android.support.v7.widget.AppCompatDrawableManager.get;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.pets.data.PetContract;
import android.example.com.pets.data.PetDbHelper;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mTextview;
    private FloatingActionButton mFloatingActionButton;
    private CursorAdapter mCursorAdapter;

    private static final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        mTextview = (TextView) findViewById(R.id.text);

        // Setup FAB to open EditorActivity
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(CatalogActivity.this, EditorActivity.class));
            }
        });

        ListView listView = (ListView) findViewById(R.id.list_id);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri contenturi = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI, l);
                intent.setData(contenturi);
                startActivity(intent);
            }
        });
        View emptyiew = (View) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyiew);
        mCursorAdapter = new PetCursorAdapter(this, null);
        listView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    private void DisplayDatabaseInfo() {

        String projections[] = {PetContract.PetEntry.PET_NAME_COL, PetContract.PetEntry.PET_WEIGHT_COLOUMN,
                PetContract.PetEntry.PET_GENDER_COL, PetContract.PetEntry.PET_BREED_COL};
        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI, projections, null, null, null);

        int Namecoloumnindexx = cursor.getColumnIndex(PetContract.PetEntry.PET_NAME_COL);
        int WeightcoloumnIndeex = cursor.getColumnIndex(PetContract.PetEntry.PET_WEIGHT_COLOUMN);
        int IdcoloumnIndex = cursor.getColumnIndex(PetContract.PetEntry.PET_ID_COL);
        int gendercoloumniindex = cursor.getColumnIndex(PetContract.PetEntry.PET_GENDER_COL);
        int breedcoloumnindex = cursor.getColumnIndex(PetContract.PetEntry.PET_BREED_COL);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            mTextview.setText("Number of rows in pets database table: " + cursor.getCount());
            while (cursor.moveToNext()){
                String name = cursor.getString(Namecoloumnindexx);
                String brred = cursor.getString(breedcoloumnindex);
                String weight = cursor.getString(WeightcoloumnIndeex);
                int gender = cursor.getInt(gendercoloumniindex);
                String gen = String.valueOf(gender);

                mTextview.append("\n" + name + "-" + brred + "-" + weight + "-" + gen);

            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }

    @Override protected void onStart() {
        super.onStart();
        DisplayDatabaseInfo();

    }

    private void insertpet(){

        PetDbHelper petDbHelper = new PetDbHelper(this);

        SQLiteDatabase db = petDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.PET_NAME_COL, "TOMMY");
        contentValues.put(PetContract.PetEntry.PET_BREED_COL, "CHIHUAHUA");
        contentValues.put(PetContract.PetEntry.PET_GENDER_COL, 1);
        contentValues.put(PetContract.PetEntry.TABLE_NAME, 34);

        Uri uri = getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, contentValues);

    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertpet();


                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:

                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getApplicationContext(), PetContract.PetEntry.CONTENT_URI, null, null, null, null);
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);

    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);

    }
}
