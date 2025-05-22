package com.example.ofekrealpro;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTestActivity extends AppCompatActivity implements AddTestPresenter.AddTestView {

    private EditText etTestName;
    private EditText etTestDate;
    private Spinner spinnerStatus;
    private Button btnAddTest;
    private FloatingActionButton fabSave;
    private ProgressBar progressBar;
    private TextInputLayout tilTestName;
    private TextInputLayout tilTestDate;
    private View rootView;

    private AddTestPresenter presenter;
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_test);

        // Note: Firebase initialization is now handled in MyApplication class
        // We've removed FirebaseApp.initializeApp(this) from here

        // Initialize the presenter
        presenter = new AddTestPresenter(this);

        // Setup views
        initViews();
        setupToolbar();
        setupDatePicker();
        setupStatusSpinner();
        setupListeners();
    }

    private void initViews() {
        rootView = findViewById(R.id.root_layout);
        etTestName = findViewById(R.id.et_test_name);
        etTestDate = findViewById(R.id.et_test_date);
        spinnerStatus = findViewById(R.id.spinner_status);
        btnAddTest = findViewById(R.id.btn_add_test);
        fabSave = findViewById(R.id.fab_save);
        progressBar = findViewById(R.id.progress_bar);
        tilTestName = findViewById(R.id.til_test_name);
        tilTestDate = findViewById(R.id.til_test_date);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Test");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupDatePicker() {
        etTestDate.setFocusable(false);
        etTestDate.setClickable(true);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateField();
            }
        };

        etTestDate.setOnClickListener(v -> {
            new DatePickerDialog(
                    AddTestActivity.this,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void updateDateField() {
        etTestDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void setupStatusSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.test_statuses,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
    }

    private void setupListeners() {
        btnAddTest.setOnClickListener(v -> saveTest());
        fabSave.setOnClickListener(v -> saveTest());

        // Clear errors on focus
        etTestName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilTestName.setError(null);
            }
        });

        etTestDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilTestDate.setError(null);
            }
        });
    }

    private void saveTest() {
        String testName = etTestName.getText().toString().trim();
        String testDate = etTestDate.getText().toString().trim();
        String testStatus = spinnerStatus.getSelectedItem().toString();

        // Clear previous errors
        tilTestName.setError(null);
        tilTestDate.setError(null);

        // Validate fields
        boolean isValid = true;

        if (testName.isEmpty()) {
            tilTestName.setError("Test name cannot be empty");
            isValid = false;
        }

        if (testDate.isEmpty()) {
            tilTestDate.setError("Test date cannot be empty");
            isValid = false;
        }

        if (isValid) {
            presenter.addTest(testName, testDate, testStatus);
        }
    }

    // AddTestView implementation methods
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        btnAddTest.setEnabled(false);
        fabSave.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnAddTest.setEnabled(true);
        fabSave.setEnabled(true);
    }

    @Override
    public void showSuccessMessage(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
                .setAction("Done", v -> finish())
                .show();
    }

    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void clearFields() {
        etTestName.setText("");
        etTestDate.setText("");
        spinnerStatus.setSelection(0);
    }
}