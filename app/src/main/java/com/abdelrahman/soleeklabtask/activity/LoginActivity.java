package com.abdelrahman.soleeklabtask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.abdelrahman.soleeklabtask.R;
import com.abdelrahman.soleeklabtask.utils.IntentUtil;
import com.abdelrahman.soleeklabtask.utils.SnackBarUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        Validator.ValidationListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private static final String EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";
    @Pattern(regex = EMAIL_ADDRESS_PATTERN, message = "Input does not match email pattern")
    private TextInputEditText mEditTextEmail;
    @Password(min = 8, message = "Password minimum length is 8")
    private TextInputEditText mEditTextPassword;
    private Button mButtonLogin;
    private LinearLayout mLayoutRegister;
    private ScrollView mLayoutLogin;
    private Validator mValidator;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient googleApiClient;
    private SignInButton mGoogleSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAuth();
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        initViews();
        initValidation();


        mButtonLogin.setOnClickListener(this);
        mLayoutRegister.setOnClickListener(this);
        mGoogleSignInButton.setOnClickListener(this);


        configureGoogleSignIn();
    }

    private void checkAuth() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            IntentUtil.makeIntent(this, HomeActivity.class);
        }
    }

    private void initViews() {
        mLayoutLogin = findViewById(R.id.layout_login);
        mEditTextEmail = findViewById(R.id.edit_login_email);
        mEditTextPassword = findViewById(R.id.edit_login_password);

        mButtonLogin = findViewById(R.id.btn_login);
        mGoogleSignInButton = findViewById(R.id.button_google_sign_in);
        mLayoutRegister = findViewById(R.id.layout_login_register);
    }

    private void initValidation() {
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(
                this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        SnackBarUtil.makeSnackBar(getApplicationContext(), mLayoutLogin, "Connection failed !", Snackbar.LENGTH_SHORT);
                    }
                }
        ).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mValidator.validate(true);
                break;
            case R.id.button_google_sign_in:
                signIn();
                break;
            case R.id.layout_login_register:
                IntentUtil.makeIntent(this, RegisterActivity.class);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();
        signIn(email, password);
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            IntentUtil.makeIntent(LoginActivity.this, HomeActivity.class);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            SnackBarUtil.makeSnackBar(getApplicationContext(), mLayoutLogin, task.getException().getMessage(), Snackbar.LENGTH_SHORT);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {
                    authWithGoogle(account);
                }
            }
        }
    }

    private void authWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    IntentUtil.makeIntent(LoginActivity.this, HomeActivity.class);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithGoogle:failure", task.getException());
                    SnackBarUtil.makeSnackBar(getApplicationContext(), mLayoutLogin, "Authentication failed !", Snackbar.LENGTH_SHORT);

                }
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            }
        }
    }

}
