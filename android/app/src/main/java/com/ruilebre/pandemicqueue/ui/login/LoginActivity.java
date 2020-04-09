package com.ruilebre.pandemicqueue.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ruilebre.pandemicqueue.R;
import com.ruilebre.pandemicqueue.data.models.LoggedInUser;
import com.ruilebre.pandemicqueue.data.models.SessionToken;
import com.ruilebre.pandemicqueue.services.ApiUtils;
import com.ruilebre.pandemicqueue.services.UserService;

public class LoginActivity extends AppCompatActivity {
    private UserService userService;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        userService = ApiUtils.getUserService();

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance())
                    .commitNow();
        }*/

        loginViewModel = new LoginViewModel(userService);

        String token = this.getSessionKey();
        if (token != null) {
            SessionToken sessionToken = new SessionToken();
            sessionToken.setToken(token);
            loginViewModel.offlineLogin(sessionToken);
        }

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        });

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }

            loginButton.setEnabled(loginFormState.isDataValid());

            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }

            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() == true) {
                showLoginFailed(loginResult.getMessage());
            }
            if (loginResult.getError() == false) {
                setSessionKey();
                updateUiWithUser(loginResult.getMessage());
            }

            setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful
            //finish();
        });
    }

    private boolean validateLogin(String username, String password) {
        if (username == null || username.trim().length() == 0) {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateUiWithUser(String model) {
        String welcome = getString(R.string.welcome) + model;
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void setSessionKey() {
        SharedPreferences pref = getSharedPreferences("SessionToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("token", LoggedInUser.getInstance().getToken().getToken());
        edit.commit();
    }

    private String getSessionKey() {
        SharedPreferences pref = getSharedPreferences("SessionToken", Context.MODE_PRIVATE);
        return pref.getString("token", null);
    }
}
