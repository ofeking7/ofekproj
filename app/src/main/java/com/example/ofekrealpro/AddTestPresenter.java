package com.example.ofekrealpro;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class AddTestPresenter {
    private final AddTestView view;
    private final TestRepository repository;

    public AddTestPresenter(AddTestView view) {
        this.view = view;
        // Get the singleton instance of TestRepository
        this.repository = TestRepository.getInstance();
    }

    public void addTest(String testName, String testDate, String testStatus) {
        // Show loading indicator
        view.showLoading();

        // Create a Test object
        Test test = new Test();
        test.setName(testName);
        test.setDate(testDate);
        test.setStatus(testStatus);
        test.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Save the test to the repository
        repository.saveTest(test, new TestRepository.SaveTestCallback() {
            @Override
            public void onSuccess() {
                view.hideLoading();
                view.clearFields();
                view.showSuccessMessage("Test added successfully!");
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showErrorMessage("Failed to add test: " + errorMessage);
            }
        });
    }

    // Interface for communication with the view
    public interface AddTestView {
        void showLoading();
        void hideLoading();
        void showSuccessMessage(String message);
        void showErrorMessage(String message);
        void clearFields();
    }
}