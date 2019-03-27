package com.sasaj.todoapp.ui.common;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sasaj.todoapp.R;
import com.sasaj.todoapp.data.Repository;
import com.sasaj.todoapp.entity.User;
import com.sasaj.todoapp.ui.list.ToDoListActivity;

import java.util.Arrays;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        repository = Repository.INSTANCE();

        if(repository.getCurrentUser() == null){
            startActivityForResult(
                    repository.getAuthUIIntent(),
                    RC_SIGN_IN);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                    repository.addUserToDatabase();
                    setContent();
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            showSignoutDialog(getString(R.string.sign_out_title), getString(R.string.sign_out_question));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showSignoutDialog(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> AuthUI.getInstance()
                                .signOut(BaseActivity.this)
                                .addOnCompleteListener(task -> startActivity(new Intent (BaseActivity.this, ToDoListActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK))))
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    protected void setContent(){}

}
