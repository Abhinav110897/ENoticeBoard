package com.example.thedarkknight.enoticeboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String LOGIN_URL = "http://192.168.43.189/MyApi/login.php";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView forgot_password;
    EditText user_name;
    EditText user_password;
    CheckBox checkBox;
    Button login, reg;
    LoginButton loginButton;
    SignInButton SignIn;
    CallbackManager callbackManager;
    GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    int c = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        // to get data from previous activity
        Intent intent = getIntent();
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        forgot_password = findViewById(R.id.forgot);
        checkBox = findViewById(R.id.checkbox);
        SignIn = findViewById(R.id.google_login_button);
        loginButton = findViewById(R.id.fb_login_button);
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        final String mActionBarTitle = intent.getStringExtra("actionBarTitle");
        mActionBarTitle.trim();
        actionBar.setTitle(mActionBarTitle + " Login Page");
        user_name = findViewById(R.id.user_name);
        user_password = findViewById(R.id.user_password);
        reg = findViewById(R.id.reg);
        login = findViewById(R.id.login_button);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                intent.putExtra("CollegeName", mActionBarTitle);
                startActivity(intent);
            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_CODE);
            }
        });
        if (sharedPreferences.getBoolean("remember", false))
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);
        user_name.setText(sharedPreferences.getString("username", ""));
        user_password.setText(sharedPreferences.getString("password", ""));
        user_name.addTextChangedListener(this);
        user_password.addTextChangedListener(this);
        checkBox.setOnCheckedChangeListener(this);
        loginWithFb(mActionBarTitle);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                intent.putExtra("CollegeName", mActionBarTitle);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(user_name.getText().toString(), user_password.getText().toString(), mActionBarTitle);
            }
        });
    }

    private void validate(final String userName, final String userPassword, final String userCollege) {
        StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, NoticeList.class);
                            intent.putExtra("CollegeName", userCollege);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, "Login Not Successful", Toast.LENGTH_SHORT).show();
                            c--;
                            if (c == 0) {
                                Toast.makeText(Login.this, "Login attempt limit exceeded", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Login Error:" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", userName);
                params.put("password", userPassword);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this, new HurlStack());
        requestQueue.add(request).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0; //retry turn off
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

    }

    private void loginWithFb(final String userCollege) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, NoticeList.class);
                intent.putExtra("CollegeName", userCollege);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }

    private void managePrefs() {
        if (checkBox.isChecked()) {
            editor.putString("username", user_name.getText().toString().trim());
            editor.putString("password", user_password.getText().toString().trim());
            editor.putBoolean("remember", true);
            editor.apply();
            ;
        } else {
            editor.putBoolean("remember", false);
            editor.remove("password");
            editor.remove("username");
            editor.apply();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Login Error" + connectionResult.toString(), Toast.LENGTH_SHORT).show();
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Toast.makeText(Login.this, "Google Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, NoticeList.class);
            intent.putExtra("CollegeName", "");
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Login.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
        finish();

    }
}






















