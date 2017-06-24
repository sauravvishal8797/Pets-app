package android.example.com.pets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by saurav on 24/6/17.
 */

public class EditorActivity extends AppCompatActivity{

    private EditText mNameEditText;

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



    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);

        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);

        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);

        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupspinner();


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
                        mGender = 1;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = 2;
                    } else {
                        mGender = 0;
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

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:

                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)

                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
