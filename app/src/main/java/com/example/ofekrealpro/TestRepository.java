package com.example.ofekrealpro;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TestRepository {
    private static final String COLLECTION_TESTS = "tests";
    private static TestRepository instance;
    private final FirebaseFirestore db;

    // Private constructor to enforce singleton pattern
    private TestRepository() {
        // Get instance of FirebaseFirestore
        db = FirebaseFirestore.getInstance();
    }

    // Singleton pattern implementation
    public static synchronized TestRepository getInstance() {
        if (instance == null) {
            instance = new TestRepository();
        }
        return instance;
    }

    // Save a test to Firestore
    public void saveTest(Test test, SaveTestCallback callback) {
        CollectionReference testsCollection = db.collection(COLLECTION_TESTS);

        // Add a new document with a generated ID
        testsCollection.add(test)
                .addOnSuccessListener(documentReference -> {
                    // Update the test with the ID
                    String id = documentReference.getId();
                    updateTestId(documentReference, id, callback);
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }

    // Get all tests for a specific user
    public void getUserTests(String userId, GetTestsCallback callback) {
        CollectionReference testsCollection = db.collection(COLLECTION_TESTS);

        testsCollection.whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Test> tests = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Test test = document.toObject(Test.class);
                        test.setId(document.getId()); // Set the document ID
                        tests.add(test);
                    }

                    if (callback != null) {
                        callback.onSuccess(tests);
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }

    // Update the test with its document ID
    private void updateTestId(DocumentReference docRef, String id, SaveTestCallback callback) {
        docRef.update("id", id)
                .addOnSuccessListener(aVoid -> {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }

    // Callback interface for saving tests
    public interface SaveTestCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    // Callback interface for getting tests
    public interface GetTestsCallback {
        void onSuccess(List<Test> tests);
        void onFailure(String errorMessage);
    }
}