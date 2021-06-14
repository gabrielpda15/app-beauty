package com.example.appbeauty;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appbeauty.database.DatabaseController;
import com.example.appbeauty.models.Professional;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final TextView loginFailViewText = findViewById(R.id.loginfail);
        final Button loginButton = findViewById(R.id.login);

        final DatabaseController dbController = new DatabaseController(getBaseContext());

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            loginFailViewText.setText("");

            if (!username.replaceAll("\\s*", "").equals("")) {
                if (!password.replaceAll("\\s*", "").equals("") &&
                    password.length() > 5) {
                    Professional professional = dbController.login(username, password);
                    if (professional != null) {
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("userId", professional.getId());
                        startActivity(intent);
                    } else {
                        loginFailViewText.setText(getString(R.string.login_failed));
                    }
                } else {
                    loginFailViewText.setText(getString(R.string.invalid_password));
                }
            } else {
                loginFailViewText.setText(getString(R.string.invalid_username));
            }
        });
    }

}