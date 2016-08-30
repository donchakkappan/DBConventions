package com.servexe.dbconventions;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Button insert, update, delete;
    private ListView contactList;
    private SimpleCursorAdapter simpleCursorAdapter;
    private EditText userName,password;


    private String coloumns[] = {UsersProvider.userColoumnTitle,UsersProvider.passwordColoumnTitle};
    private int viewID[] = {android.R.id.text1,android.R.id.text2};
    private static final String[] PROJECTION = new String[]{"_id",UsersProvider.userColoumnTitle,UsersProvider.passwordColoumnTitle};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        contactList = (ListView) findViewById(R.id.showusers);
        insert = (Button) findViewById(R.id.insert);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        simpleCursorAdapter = new SimpleCursorAdapter(HomeActivity.this,android.R.layout.simple_list_item_1, null, coloumns, viewID, 0);
        contactList.setAdapter(simpleCursorAdapter);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = ((SimpleCursorAdapter)contactList.getAdapter()).getCursor();
                c.moveToPosition(position);
                String name=c.getString(1);
                userName.setText(name.toString());
            }
        });

        getSupportLoaderManager().initLoader(1, null, this);
    }

    /**
     * onClick Callback
     *
     * @param view
     */
    public void performDBOperation(View view) {
        int viewID = view.getId();
        String username=userName.getText().toString();
        String passwordEntered=password.getText().toString();
        ContentValues values = new ContentValues();
        switch (viewID) {
            case R.id.insert:
                values.put(UsersProvider.userColoumnTitle,username);
                values.put(UsersProvider.passwordColoumnTitle,passwordEntered);
                Uri uri = getContentResolver().insert(UsersProvider.CONTENT_URI, values);
                break;
            case R.id.update:
                values.put(UsersProvider.passwordColoumnTitle,passwordEntered);
                getContentResolver().update(UsersProvider.CONTENT_URI,values,UsersProvider.userColoumnTitle+"=?",new String[] {username});
                break;
            case R.id.delete:
                getContentResolver().delete(UsersProvider.CONTENT_URI,UsersProvider.userColoumnTitle+"=?",new String[] {username});
                break;
            default:
                break;
        }
        userName.setText("");
        password.setText("");
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(HomeActivity.this, UsersProvider.CONTENT_URI,
                PROJECTION, null, null, null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 1:
                simpleCursorAdapter.swapCursor(data);
                break;
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }
}
