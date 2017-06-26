package android.example.com.pets;

import android.content.Context;
import android.database.Cursor;
import android.example.com.pets.data.PetContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by saurav on 26/6/17.
 */

public class PetCursorAdapter extends CursorAdapter {

    private TextView mTextview1;

    private TextView mTextview2;

    public PetCursorAdapter(Context context, Cursor c){
        super(context, c, 0);


    }
    @Override public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.each_item, viewGroup);
    }

    @Override public void bindView(View view, Context context, Cursor cursor) {

        int nameindex = cursor.getColumnIndex(PetContract.PetEntry.PET_NAME_COL);
        int breedindex = cursor.getColumnIndex(PetContract.PetEntry.PET_BREED_COL);
        String name = cursor.getString(nameindex);
        String breed = cursor.getString(breedindex);

        mTextview1 = (TextView) view.findViewById(R.id.name);
        mTextview1.setText(name);

        mTextview2 = (TextView) view.findViewById(R.id.summary);
        mTextview2.setText(breed);


    }
}
