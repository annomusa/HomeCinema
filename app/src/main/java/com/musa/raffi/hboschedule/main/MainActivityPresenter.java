package com.musa.raffi.hboschedule.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.musa.raffi.hboschedule.R;

/**
 * Created by maunorafiq on 10/27/16.
 */

public class MainActivityPresenter {
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleSignInResult result;
    private MainActivityInterface mInterface;

    public MainActivityPresenter(Context ctx, FragmentActivity frag, GoogleApiClient.OnConnectionFailedListener GApi, MainActivityInterface mInterface) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        this.mInterface = mInterface;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ctx.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .enableAutoManage(frag, GApi)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public String getDIsplayName(){
        return mFirebaseUser != null ? mFirebaseUser.getDisplayName() : "Not log in yet";
    }

    public String getEmail(){
        return mFirebaseUser != null ? mFirebaseUser.getEmail() : "";
    }

    public void signOut(){
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    public Intent signIn(){
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    public void doLogIn(Intent data){
        result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        GoogleSignInAccount acct = result.getSignInAccount();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    mFirebaseUser = mFirebaseAuth.getCurrentUser();
                    mInterface.onCompleteAuth(task.isSuccessful());
                });
    }
}
