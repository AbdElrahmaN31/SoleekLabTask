package com.abdelrahman.soleeklabtask;

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

import com.abdelrahman.soleeklabtask.Utils.IntentUtil;
import com.abdelrahman.soleeklabtask.Utils.SnackBarUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final String EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";
    @Pattern(regex = EMAIL_ADDRESS_PATTERN, message = "Input does not match email pattern")
    private TextInputEditText mEditTextNewEmail;
    @Password(min = 8, message = "Password minimum length is 8")
    private TextInputEditText mEditTextNewPassword;
    @ConfirmPassword
    private TextInputEditText mEditTextConfirmPassword;
    private Button mButtonRegister;
    private LinearLayout mLayoutLogin;
    private ScrollView mLayoutRegister;
    private Validator mValidator;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            IntentUtil.makeIntent(this, HomeActivity.class);
        }
        setContentView(R.layout.activity_register);

        mLayoutRegister = findViewById(R.id.layout_register);
        mEditTextNewEmail = findViewById(R.id.edit_login_email);
        mEditTextNewPassword = findViewById(R.id.edit_login_password);
        mEditTextConfirmPassword = findViewById(R.id.edit_register_confirm_password);

        mButtonRegister = findViewById(R.id.btn_register);
        mLayoutLogin = findViewById(R.id.layout_register_login);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mButtonRegister.setOnClickListener(this);
        mLayoutLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                mValidator.validate(true);
                break;
            case R.id.layout_register_login:
                IntentUtil.makeIntent(this, LoginActivity.class);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        String email = mEditTextNewEmail.getText().toString();
        String password = mEditTextNewPassword.getText().toString();
        register(email, password);
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            IntentUtil.makeIntent(RegisterActivity.this, HomeActivity.class);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            SnackBarUtil.makeSnackBar(getApplicationContext(), mLayoutRegister, "Authentication failed !", Snackbar.LENGTH_SHORT);
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
