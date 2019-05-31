package com.abdelrahman.soleeklabtask;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.abdelrahman.soleeklabtask.Utils.IntentUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {


    private static final String EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";
    @Pattern(regex = EMAIL_ADDRESS_PATTERN, message = "Input does not match email pattern")
    private TextInputEditText mEditTextEmail;
    @Password(min = 8, message = "Password minimum length is 8")
    private TextInputEditText mEditTextPassword;
    private Button mButtonLogin;
    private LinearLayout mLayoutRegister;
    private ScrollView mLayoutLogin;
    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextEmail = findViewById(R.id.edit_login_email);
        mEditTextPassword = findViewById(R.id.edit_login_password);

        mButtonLogin = findViewById(R.id.btn_login);
        mLayoutRegister = findViewById(R.id.layout_login_register);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mButtonLogin.setOnClickListener(this);
        mLayoutRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mValidator.validate(true);
                break;
            case R.id.layout_login_register:
                IntentUtil.makeIntent(this, RegisterActivity.class);
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
