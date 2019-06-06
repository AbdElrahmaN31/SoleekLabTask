package com.abdelrahman.soleeklabtask.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.abdelrahman.soleeklabtask.R;
import com.abdelrahman.soleeklabtask.adapter.CountryAdapter;
import com.abdelrahman.soleeklabtask.model.Country;
import com.abdelrahman.soleeklabtask.network.APIClient;
import com.abdelrahman.soleeklabtask.network.ApiInterface;
import com.abdelrahman.soleeklabtask.utils.IntentUtil;
import com.abdelrahman.soleeklabtask.utils.SnackBarUtil;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    CoordinatorLayout mHomeLayout;
    Button mLogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        getData();

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void initViews() {
        mHomeLayout = findViewById(R.id.layout_home);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mLogoutBtn = findViewById(R.id.btn_logout);
        mRecyclerView = findViewById(R.id.recycler_view_countries);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void getData() {

        ApiInterface apiService =
                APIClient.getClient().create(ApiInterface.class);

        Call<List<Country>> call = apiService.getAllCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                List<Country> countries = response.body();
                mRecyclerView.setAdapter(new CountryAdapter(countries, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                SnackBarUtil.makeSnackBar(HomeActivity.this, mHomeLayout, t.getMessage(), Snackbar.LENGTH_SHORT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        //Facebook sign out
        LoginManager.getInstance().logOut();

        // Google sign out
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(HomeActivity.this, gso);
        mGoogleSignInClient.signOut();

        Toast.makeText(getApplicationContext(), "Logged Out!", Toast.LENGTH_SHORT).show();
        IntentUtil.makeIntent(this, LoginActivity.class);
    }
}
