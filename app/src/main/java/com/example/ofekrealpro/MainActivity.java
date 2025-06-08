package com.example.ofekrealpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout subjectCardLayout;
    private MaterialCardView subjectCard;
    private ProgressBar progressBar;
    private TestRepository testRepository;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize repository
        testRepository = TestRepository.getInstance();

        // Initialize views
        initViews();
        setupBottomNavigation();

        // Load user tests
        loadUserTests();
    }

    private void initViews() {
        subjectCard = findViewById(R.id.subjectCard);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Get the LinearLayout inside the subject card
        subjectCardLayout = subjectCard.findViewById(R.id.subjectCardLayout);
        if (subjectCardLayout == null) {
            // If the layout doesn't have an ID, we'll find it by type
            subjectCardLayout = (LinearLayout) ((LinearLayout) subjectCard.getChildAt(0));
        }

        // Create and add progress bar
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);
        subjectCardLayout.addView(progressBar);

        // Add header to subject card
        TextView headerText = new TextView(this);
        headerText.setText("Your Tests");
        headerText.setTextSize(18);
        headerText.setTextColor(getResources().getColor(android.R.color.black));
        headerText.setPadding(0, 0, 0, 16);
        headerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        subjectCardLayout.addView(headerText, 0); // Add at the beginning
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_create) {
                startActivity(new Intent(this, AddTestActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadUserTests() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        clearTestViews();

        testRepository.getUserTests(userId, new TestRepository.GetTestsCallback() {
            @Override
            public void onSuccess(List<Test> tests) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    displayTests(tests);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    showError("Failed to load tests: " + errorMessage);
                });
            }
        });
    }

    private void displayTests(List<Test> tests) {
        clearTestViews();

        if (tests.isEmpty()) {
            showEmptyState();
            return;
        }

        for (Test test : tests) {
            View testItemView = createTestItemView(test);
            subjectCardLayout.addView(testItemView);
        }
    }

    private View createTestItemView(Test test) {
        // Create a card for each test
        MaterialCardView testCard = new MaterialCardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 8, 0, 8);
        testCard.setLayoutParams(cardParams);
        testCard.setCardElevation(4f);
        testCard.setRadius(8f);
        testCard.setClickable(true);
        testCard.setFocusable(true);

        // Create content layout
        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding(16, 16, 16, 16);

        // Test name
        TextView testName = new TextView(this);
        testName.setText(test.getName());
        testName.setTextSize(16);
        testName.setTextColor(getResources().getColor(android.R.color.black));


        // Test date
        TextView testDate = new TextView(this);
        testDate.setText("Date: " + test.getDate());
        testDate.setTextSize(14);
        testDate.setTextColor(getResources().getColor(android.R.color.darker_gray));
        testDate.setPadding(0, 4, 0, 0);

        // Test status
        TextView testStatus = new TextView(this);
        testStatus.setText("Status: " + test.getStatus());
        testStatus.setTextSize(14);
        testStatus.setPadding(0, 4, 0, 0);

        // Set status color based on status
        int statusColor = getStatusColor(test.getStatus());
        testStatus.setTextColor(statusColor);

        // Add views to content layout
        contentLayout.addView(testName);
        contentLayout.addView(testDate);
        contentLayout.addView(testStatus);

        // Add content to card
        testCard.addView(contentLayout);

        // Set click listener for the test item
        testCard.setOnClickListener(v -> {
            // Handle test item click - you can add navigation to test details here
            Toast.makeText(this, "Clicked on: " + test.getName(), Toast.LENGTH_SHORT).show();
            // Ex ample: startActivity(new Intent(this, TestDetailsActivity.class));
        });

        return testCard;
    }

    private int getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "completed":
                return getResources().getColor(android.R.color.holo_green_dark);
            case "in progress":
                return getResources().getColor(android.R.color.holo_orange_dark);
            case "pending":
                return getResources().getColor(android.R.color.holo_red_dark);
            default:
                return getResources().getColor(android.R.color.darker_gray);
        }
    }

    private void showEmptyState() {
        TextView emptyText = new TextView(this);
        emptyText.setText("No tests found.\nTap the + button to add your first test!");
        emptyText.setTextSize(16);
        emptyText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        emptyText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        emptyText.setPadding(0, 32, 0, 32);
        subjectCardLayout.addView(emptyText);
    }

    private void clearTestViews() {
        // Remove all views except the header (first view) and progress bar
        int childCount = subjectCardLayout.getChildCount();
        for (int i = childCount - 1; i >= 1; i--) {
            View child = subjectCardLayout.getChildAt(i);
            if (child != progressBar) {
                subjectCardLayout.removeViewAt(i);
            }
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        TextView errorText = new TextView(this);
        errorText.setText(message);
        errorText.setTextSize(14);
        errorText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        errorText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        errorText.setPadding(0, 16, 0, 16);
        subjectCardLayout.addView(errorText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh tests when returning to this activity
        loadUserTests();
    }
}