package com.sasaj.todoapp.ui.common;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sasaj.todoapp.R;
import com.sasaj.todoapp.entity.User;
import com.sasaj.todoapp.ui.list.ToDoListActivity;

import java.util.Arrays;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build());

            // Create and launch sign-in intent
            Intent intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setAlwaysShowSignInMethodScreen(true)
                    .build();
            startActivityForResult(
                    intent,
                    RC_SIGN_IN);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);


            if (resultCode == RESULT_OK) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    writeNewUser(user.getUid(), usernameFromEmail(user.getEmail()), user.getEmail() );
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            showDialog("Sign Out", "Do you want to sign out?");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showDialog(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                AuthUI.getInstance()
                                        .signOut(BaseActivity.this)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            public void onComplete(@NonNull Task<Void> task) {
                                                startActivity(new Intent (BaseActivity.this, ToDoListActivity.class)
                                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

                                            }
                                        });

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(userId).setValue(user);
    }


    private String usernameFromEmail(String email) {
        if(email != null){
            if (email.contains("@")) {
                return email.split("@")[0];
            } else {
                return email;
            }
        } else {
            return "";
        }
    }

    protected void setContent(){}

}
