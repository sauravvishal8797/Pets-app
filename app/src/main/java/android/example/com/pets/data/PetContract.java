package android.example.com.pets.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by saurav on 24/6/17.
 */

public final class PetContract {

    public static final String CONTENT_AUTHORITY = "android.example.com.pets";

    public static final Uri BASE_URI = Uri.parse("Content://" + CONTENT_AUTHORITY);

    public static final String PATH_NAME = "Pets";

    /**
     *Keeping the constract constructor private to prevent someone from instantiating the contracts class.
     */
    private PetContract(){

    }

    /**
     * Inner class that defines constant values for the pets database table
     * Each entry in the table represents an indivdual pet
     */
    public static final class PetEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_NAME);

        /**
         * Constant for the table name
         */
        public static final String TABLE_NAME = "Pets";

        /**
         * Constant for the petsname coloumn
         */
        public static final String PET_NAME_COL = "Name";

        /**
         * Constant for the petsbreed coloumn
         */
        public static final String PET_BREED_COL = "Breed";

        /**
         * Constant for the petsgender coloumn
         */
        public static final String PET_GENDER_COL = "Gender";

        /**
         * Constant for the petsweight coloumn
         */
        public static final String PET_WEIGHT_COLOUMN = "Weight";

        /**
         * Constant for the petsid coloumn
         */
        public static final String PET_ID_COL = BaseColumns._ID;

        /**
         * Integer constant for gender male
         */
        public static final int MALE = 1;

        /**
         * Integer constant for gender female
         */
        public static final int FEMALE = 2;

        /**
         * Integer constant for other gender
         */
        public static final int UNKNOWN = 0;


    }


}
