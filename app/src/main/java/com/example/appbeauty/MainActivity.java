package com.example.appbeauty;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbeauty.database.DatabaseController;
import com.example.appbeauty.dialogs.DatePickerFragment;
import com.example.appbeauty.models.Professional;
import com.example.appbeauty.models.Schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private DatabaseController dbController;
    private int activatedScreen = 0;
    private Professional user = null;

    private final int[] screens = new int[] {
            R.id.activityToday,
            R.id.activitySchedule,
            R.id.activityCalendar,
            R.id.activityNewUser,
            R.id.activityEditUser,
            R.id.activityDeleteUser
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user != null && user.getUsername().equals("admin")) {
            getMenuInflater().inflate(R.menu.admin_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.btnNewUser:
                changeActivatedScreen(3);
                return true;
            case R.id.btnEditUser:
                changeActivatedScreen(4);
                return true;
            case R.id.btnDeleteUser:
                changeActivatedScreen(5);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbController = new DatabaseController(getBaseContext());
        final long userId = getIntent().getLongExtra("userId", 0);
        user = dbController.getProfessionalById(userId);

        final View activityToday = findViewById(R.id.activityToday);
        final View activitySchedule = findViewById(R.id.activitySchedule);
        final View activityCalendar = findViewById(R.id.activityCalendar);
        final View activityNewUser = findViewById(R.id.activityNewUser);
        final View activityEditUser = findViewById(R.id.activityEditUser);
        final View activityDeleteUser = findViewById(R.id.activityDeleteUser);
        activityToday.setVisibility(View.GONE);
        activitySchedule.setVisibility(View.GONE);
        activityCalendar.setVisibility(View.GONE);
        activityNewUser.setVisibility(View.GONE);
        activityEditUser.setVisibility(View.GONE);
        activityDeleteUser.setVisibility(View.GONE);

        final Button btnToday = findViewById(R.id.btnToday);
        final Button btnSchedule = findViewById(R.id.btnSchedule);
        final Button btnCalendar = findViewById(R.id.btnCalendar);

        final Button btnCreateUser = activityNewUser.findViewById(R.id.btnCreateUser);
        final EditText newUserName = activityNewUser.findViewById(R.id.txtName);
        final EditText newUserUsername = activityNewUser.findViewById(R.id.txtUsername);
        final EditText newUserPassword = activityNewUser.findViewById(R.id.txtPassword);
        final Button btnSaveUser = activityEditUser.findViewById(R.id.btnSaveUser);
        final Spinner spinner = activityEditUser.findViewById(R.id.spinner);
        final TextView txtEditId = activityEditUser.findViewById(R.id.txtEditId);
        final EditText txtEditName = activityEditUser.findViewById(R.id.txtEditName);
        final EditText txtEditUsername = activityEditUser.findViewById(R.id.txtEditUsername);
        final EditText txtEditPassword = activityEditUser.findViewById(R.id.txtEditPassword);
        final Button btnRemoveUser = activityDeleteUser.findViewById(R.id.btnRemoveUser);
        final Spinner deleteSpinner = activityDeleteUser.findViewById(R.id.deleteSpinner);
        final TextView txtDeleteId = activityDeleteUser.findViewById(R.id.txtDeleteId);
        final CalendarView calendar = activityCalendar.findViewById(R.id.calendarView);
        final Button btnSelectDate = activitySchedule.findViewById(R.id.btnSelectDate);

        btnToday.setOnClickListener(v -> {
            changeActivatedScreen(0);
        });

        btnSchedule.setOnClickListener(v -> {
            changeActivatedScreen(1);
        });

        btnCalendar.setOnClickListener(v -> {
            changeActivatedScreen(2);
        });

        btnCreateUser.setOnClickListener(v -> {
            final String name = newUserName.getText().toString();
            final String username = newUserUsername.getText().toString();
            final String password = newUserPassword.getText().toString();
            createNewUser(name, username, password);
        });

        btnSaveUser.setOnClickListener(v -> {
            final long id = Long.parseLong(txtEditId.getText().toString());
            final String name = txtEditName.getText().toString();
            final String username = txtEditUsername.getText().toString();
            final String password = txtEditPassword.getText().toString();
            editUser(id, name, username, password);
        });

        btnRemoveUser.setOnClickListener(v -> {
            final long id = Long.parseLong(txtDeleteId.getText().toString());
            dbController.deleteProfessional(id);
            changeActivatedScreen(0);
        });

        btnSelectDate.setOnClickListener(v -> {
            DialogFragment dialog = new DatePickerFragment();
            dialog.show(getSupportFragmentManager(), "datePicker");
        });

        calendar.setDate(new Date().getTime());
        calendar.setOnDateChangeListener((view, year, month, day) -> {
            Calendar calendarTemp = Calendar.getInstance();
            calendarTemp.set(year, month, day, 0, 0, 0);
            Schedule[] result = dbController.getSchedule(userId, calendarTemp.getTime());
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Professional[] result = dbController.getProfessional();
                txtEditId.setText(Long.toString(result[position].getId()));
                txtEditName.setText(result[position].getName());
                txtEditUsername.setText(result[position].getUsername());
                txtEditPassword.setText(result[position].getPassword());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        deleteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Professional[] result = dbController.getProfessional();
                txtDeleteId.setText(Long.toString(result[position].getId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        changeActivatedScreen(activatedScreen);
    }

    private void changeActivatedScreen(int screen) {
        if (activatedScreen != screen) {
            final View oldScreen = findViewById(screens[activatedScreen]);
            oldScreen.setVisibility(View.GONE);
            activatedScreen = screen;
            final View newScreen = findViewById(screens[activatedScreen]);
            newScreen.setVisibility(View.VISIBLE);
            onScreenChanged(screen);
        }
    }

    private void onScreenChanged(int screen) {
        switch (screen) {
            case 0:

            case 2:

                break;
            case 4:
                final View activityEditUser = findViewById(R.id.activityEditUser);
                final Spinner spinner = activityEditUser.findViewById(R.id.spinner);

                Professional[] result = dbController.getProfessional();

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                        activityEditUser.getContext(), android.R.layout.simple_spinner_item, selectProfessionalNames(result));
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);

                final TextView txtId = activityEditUser.findViewById(R.id.txtEditId);
                final EditText txtName = activityEditUser.findViewById(R.id.txtEditName);
                final EditText txtUsername = activityEditUser.findViewById(R.id.txtEditUsername);
                final EditText txtPassword = activityEditUser.findViewById(R.id.txtEditPassword);
                txtId.setText(Long.toString(result[0].getId()));
                txtName.setText(result[0].getName());
                txtUsername.setText(result[0].getUsername());
                txtPassword.setText(result[0].getPassword());
                break;
            case 5:
                final View activityDeleteUser = findViewById(R.id.activityDeleteUser);
                final Spinner deleteSpinner = activityDeleteUser.findViewById(R.id.deleteSpinner);

                Professional[] deleteResult = dbController.getProfessional();

                ArrayAdapter<String> deleteSpinnerAdapter = new ArrayAdapter<String>(
                        activityDeleteUser.getContext(), android.R.layout.simple_spinner_item, selectProfessionalNames(deleteResult));
                deleteSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                deleteSpinner.setAdapter(deleteSpinnerAdapter);

                final TextView txtDeleteId = activityDeleteUser.findViewById(R.id.txtDeleteId);
                txtDeleteId.setText(Long.toString(deleteResult[0].getId()));
                break;
        }
    }

    private String[] selectProfessionalNames(Professional[] values) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; i++) {
            list.add(values[i].getName());
        }
        return list.toArray(new String[0]);
    }

    private void createNewUser(String name, String username, String password) {
        if (HasValue(name)) {
            if (HasValue(username)) {
                if (HasValue(password) && password.length() > 5) {
                    Professional professional = new Professional();
                    professional.setName(name);
                    professional.setUsername(username);
                    professional.setPassword(password);
                    professional = dbController.createProfessional(professional);

                    if (professional.getId() > 0) {
                        changeActivatedScreen(0);
                        Toast.makeText(getApplicationContext(), getString(R.string.success_new_user), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_password), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.invalid_username), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_name), Toast.LENGTH_LONG).show();
        }
    }

    private void editUser(long id, String name, String username, String password) {
        if (HasValue(name)) {
            if (HasValue(username)) {
                if (HasValue(password) && password.length() > 5) {
                    Professional professional = new Professional();
                    professional.setId(id);
                    professional.setName(name);
                    professional.setUsername(username);
                    professional.setPassword(password);
                    professional = dbController.editProfessional(professional);

                    if (professional.getId() > 0) {
                        changeActivatedScreen(0);
                        Toast.makeText(getApplicationContext(), getString(R.string.success_edit_user), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_password), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.invalid_username), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_name), Toast.LENGTH_LONG).show();
        }
    }

    private boolean HasValue(String value) {
        return !value.replaceAll("\\s*", "").equals("");
    }
}