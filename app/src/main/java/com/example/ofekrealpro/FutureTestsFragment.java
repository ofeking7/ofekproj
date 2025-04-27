package com.example.ofekrealpro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FutureTestsFragment extends AppCompatActivity {
    private RecyclerView recyclerView;
    private test_adapter testadapter;
    private List<TestModel> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_tests_fragment);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load test data (Replace with API or Database fetch)
        loadTestData();
    }

    private void loadTestData() {
        testList = new ArrayList<>();
        testList.add(new TestModel("Math Exam", "2025-03-20", "Upcoming"));
        testList.add(new TestModel("Physics Quiz", "2025-02-15", "Completed"));
        testList.add(new TestModel("History Test", "2025-04-10", "Upcoming"));

        testadapter = new test_adapter(testList);
        recyclerView.setAdapter(testadapter);
    }
}
