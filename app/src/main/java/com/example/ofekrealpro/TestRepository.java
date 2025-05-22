package com.example.ofekrealpro;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

public class TestRepository {
    private static final String COLLECTION_TESTS = "tests";
    private static TestRepository instance;
    private final FirebaseFirestore db;

    // Private constructor to enforce singleton pattern
    private TestRepository() {
        // Get instance of FirebaseFirestore
        // This line was causing the crash because Firebase wasn't initialized
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

    // Callback interface for test operations
    public interface SaveTestCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
