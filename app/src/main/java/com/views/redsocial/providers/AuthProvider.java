package com.views.redsocial.providers;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthProvider {

    private FirebaseAuth kAuth;

    public AuthProvider() {
        kAuth = FirebaseAuth.getInstance();

    }

    public Task<AuthResult> register(String email, String password) {
        return kAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> login(String email, String password) {

        return kAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> googleLogin(GoogleSignInAccount googleSignInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        return kAuth.signInWithCredential(credential);
    }

    public String getUid() {
        if (kAuth.getCurrentUser() != null) {
            return kAuth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }
    public FirebaseUser getUserSession() {
        if (kAuth.getCurrentUser() != null) {
            return kAuth.getCurrentUser();
        } else {
            return null;
        }
    }

    public String getEmail() {
        if (kAuth.getCurrentUser() != null) {
            return kAuth.getCurrentUser().getEmail();
        } else {
            return null;
        }
    }

    public void logout(){
        if (kAuth != null)
        kAuth.signOut();
    }
}
