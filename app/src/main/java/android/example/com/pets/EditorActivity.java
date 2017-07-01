package android.example.com.pets;


import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.support.v7.widget.AppCompatDrawableManager.get;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.pets.data.PetContract;
import android.example.com.pets.data.PetDbHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.example.com.pets.data.PetContract.PetEntry;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by saurav on 24/6/17.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText mNameEditText;

    private boolean mPethasChanged = false;

    private Uri uri;

    private static final int LOADER_ID = 1;

    /**
     * EditText field to enter the pet's breed
     */
    private EditText mBreedEditText;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText mWeightEditText;

    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mGenderSpinner;

    private int mGender = 0;

    private View.OnTouchListener mTouchlistener = new View.OnTouchListener() {
        @Override public boolean onTouch(View view, MotionEvent motionEvent) {
            mPethasChanged = true;
            return false;
        }
    };



    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        Intent intent = getIntent();
        uri = intent.getData();
        if(uri == null){

            setTitle(R.string.Add_pet);
        }
        else{

            setTitle(R.string.Edit_pet);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);

        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);

        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);

        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        mNameEditText.setOnTouchListener(mTouchlistener);

        mBreedEditText.setOnTouchListener(mTouchlistener);

        mWeightEditText.setOnTouchListener(mTouchlistener);

        mGenderSpinner.setOnTouchListener(mTouchlistener);

        setupspinner();

        getLoaderManager().initLoader(LOADER_ID, null, this);


    }

    @Override public void onBackPressed() {

        if(mPethasChanged == true){
            UnsavedChanges();
        }
        else{
            super.onBackPressed();

        }

    }

    /** Method for setting up the spinner
     *
     */
    private void setupspinner(){

        ArrayAdapter spinneradapter = ArrayAdapter.createFromResource(this, R.array.array_gender_options, android.R
                .layout.simple_spinner_item );

        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mGenderSpinner.setAdapter(spinneradapter);
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetContract.PetEntry.MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetContract.PetEntry.FEMALE;
                    } else {
                        mGender = PetContract.PetEntry.UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
            }
        });
    }

    //To insert a single pet from the editor activity into the database
    private void savepet(){

        String name = mNameEditText.getText().toString().trim();
        String breed = mBreedEditText.getText().toString().trim();
        String weight = mWeightEditText.getText().toString().trim();
        int Weightint = Integer.parseInt(weight);
        if(TextUtils.isEmpty(name) && TextUtils.isEmpty(breed) && TextUtils.isEmpty(weight) && mGender ==
                PetEntry.UNKNOWN){
            finish();

        }
        if(TextUtils.isEmpty(weight)){
            Weightint = 0;
        }

        PetDbHelper petDbHelper = new PetDbHelper(this);
        SQLiteDatabase db = petDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.PET_NAME_COL, name);
        contentValues.put(PetContract.PetEntry.PET_BREED_COL, breed);
        contentValues.put(PetContract.PetEntry.PET_WEIGHT_COLOUMN, weight);
        contentValues.put(PetContract.PetEntry.PET_GENDER_COL, mGender);
        if(this.getTitle() == "Add a pet"){
            Uri newUri = getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, contentValues);
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.Error_inserting),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.Inserted),
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if(this.getTitle() == "Edit a pet"){
            int updated = getContentResolver().update(uri, contentValues, null, null);
            if(updated == 0){
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.error_updating_pet),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.update_pet),
                        Toast.LENGTH_SHORT).show();
            }

            }
        }

        private void UnsavedChanges(){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.dialog_message);
            builder.setPositiveButton(R.string.positive_dialog_button, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialogInterface, int i) {

                    if(dialogInterface != null){

                        finish();

                    }

                }
            });

            builder.setNegativeButton(R.string.negative_dialog_button, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialogInterface, int i) {
                    if(dialogInterface != null){
                        return;
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //To save the pet in the database and then exit the activity
                savepet();
                finish();

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:

                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                if(mPethasChanged == true){

                    UnsavedChanges();
                }
                else{
                    NavUtils.navigateUpFromSameTask(this);
                }


                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int nameindex = data.getColumnIndex(PetContract.PetEntry.PET_NAME_COL);
        int breedindex = data.getColumnIndex(PetContract.PetEntry.PET_BREED_COL);
        int weightindex = data.getColumnIndex(PetContract.PetEntry.PET_WEIGHT_COLOUMN);
        int genderindex = data.getColumnIndex(PetContract.PetEntry.PET_GENDER_COL);

        String name = data.getString(nameindex);
        String breed = data.getString(breedindex);
        String weight = data.getString(weightindex);
        String gender = data.getString(genderindex);
        int gen = Integer.parseInt(gender);

        mNameEditText.setText(name);
        mWeightEditText.setText(weight);
        mGender = gen;
        mBreedEditText.setText(breed);

    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {

        mNameEditText.setText(null);
        mBreedEditText.setText(null);
        mGender = 0;
        mWeightEditText.setText(null);

    }
}
