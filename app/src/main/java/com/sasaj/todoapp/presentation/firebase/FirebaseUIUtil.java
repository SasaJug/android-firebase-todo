package com.sasaj.todoapp.presentation.firebase;

import android.content.Context;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class FirebaseUIUtil {

    public static Intent getAuthUIIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create sign-in intent
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setAlwaysShowSignInMethodScreen(true)
                .build();
    }

    public static void signOut(Context context, Intent intent) {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(task -> context.startActivity(intent));
    }
}
