package com.abdelrahman.soleeklabtask;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {

    private static final String EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";
    @Pattern(regex = EMAIL_ADDRESS_PATTERN, message = "Input does not match email pattern")
    private TextInputEditText mEditTextNewEmail;
    @NotEmpty
    private TextInputEditText mEditTextNewUser;
    @Password(min = 8, message = "Password minimum length is 8")
    private TextInputEditText mEditTextNewPassword;
    @ConfirmPassword
    private TextInputEditText mEditTextConfirmPassword;
    private Button mButtonSignUp;
    private LinearLayout mLayoutLogin;
    private ScrollView mLayoutSingup;
    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEditTextNewUser = findViewById(R.id.edit_sign_up_name);
        mEditTextNewEmail = findViewById(R.id.edit_sign_up_email);
        mEditTextNewPassword = findViewById(R.id.edit_sign_up_password);
        mEditTextConfirmPassword = findViewById(R.id.edit_sign_up_confirm_password);

        mButtonSignUp = findViewById(R.id.btn_register);
        mLayoutLogin = findViewById(R.id.layout_sign_up_login);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mButtonSignUp.setOnClickListener(this);
        mLayoutLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                mValidator.validate(true);
                break;
            case R.id.layout_sign_up_login:
//                IntentUtil.makeIntent(this, LoginActivity.class);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {

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
