package com.example.mda.Activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mda.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowDetails extends AppCompatActivity {

    private TextView dataTextView;
    private DatabaseReference databaseReference;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI
        dataTextView = findViewById(R.id.data_text);

        // Get UID from Intent
        uid = getIntent().getStringExtra("uid");

        // Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Patients").child(uid);

        // Fetch all data under the UID
        fetchPatientData();
    }

    private void fetchPatientData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    StringBuilder data = new StringBuilder();

                    for (DataSnapshot record : snapshot.getChildren()) {
                        data.append("🔹 רשומה חדשה:\n");
                        for (DataSnapshot field : record.getChildren()) {
                            String key = field.getKey();
                            String value = field.getValue(String.class);
                            data.append("▪️ ").append(key).append(": ").append(value).append("\n");
                        }
                        data.append("\n");
                    }

                    dataTextView.setText(data.toString());
                } else {
                    Toast.makeText(ShowDetails.this, "אין נתונים למספר זה", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowDetails.this, "שגיאה בטעינת הנתונים", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
