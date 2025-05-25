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

/**
 * @file ShowDetails.java
 * @brief Activity to display comprehensive patient record details retrieved from Firebase.
 *
 * This activity is responsible for fetching and displaying all data associated
 * with a specific patient UID from the Firebase Realtime Database. The data is
 * structured under a "Patients" node, further categorized by the patient's UID,
 * and then by individual records (which seem to be timestamped or uniquely keyed).
 *
 * Key functionalities:
 * <ul>
 *     <li>Receives a patient UID via an Intent extra from the calling activity.</li>
 *     <li>Initializes a Firebase Database reference to the specific patient's data
 *         path (e.g., "Patients/{uid}").</li>
 *     <li>Attaches a single value event listener to fetch all child data under
 *         the specified UID.</li>
 *     <li>If data exists:
 *         <ul>
 *             <li>It iterates through each child snapshot (representing an individual record
 *                 or a sub-category of data for the patient).</li>
 *             <li>For each record, it further iterates through its children (fields within
 *                 that record).</li>
 *             <li>It formats this hierarchical data into a human-readable string using a
 *                 {@link StringBuilder}. Each record is prefixed with "🔹 רשומה חדשה:"
 *                 (New record:), and each field within a record is prefixed with "▪️ ".</li>
 *             <li>The fully formatted string is then displayed in a {@link TextView}
 *                 (R.id.data_text).</li>
 *         </ul>
 *     </li>
 *     <li>If no data exists for the UID, a Toast message "אין נתונים למספר זה"
 *         (No data for this number) is shown.</li>
 *     <li>Handles potential database errors by displaying a Toast message
 *         "שגיאה בטעינת הנתונים" (Error loading data).</li>
 *     <li>Applies window insets to manage system bars and ensure proper padding
 *         for the main layout (R.id.main).</li>
 * </ul>
 *
 * This activity provides a read-only view of the patient's information as stored
 * in Firebase.
 */
// package com.example.mda.Activities; // Usually excluded from file-level Javadoc content

// import ... // Imports are typically not part of the file-level Javadoc content itself

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

    /**
     * Fetches all data for the current patient UID from Firebase Realtime Database.
     *
     * It attaches a {@link ValueEventListener} for a single read operation.
     * If the {@link DataSnapshot} exists (i.e., data is found for the UID):
     * - It iterates through each child of the snapshot (each representing a distinct record or data block).
     * - For each record, it iterates through its fields (key-value pairs).
     * - The data is formatted into a readable string with "🔹 רשומה חדשה:" marking new records
     *   and "▪️ " marking individual fields within a record.
     * - The compiled string is set to the {@code dataTextView}.
     * If the snapshot does not exist, a "No data" Toast is displayed.
     * If there's a database error during fetching, an "Error loading data" Toast is shown.
     */
    private void fetchPatientData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    StringBuilder data = new StringBuilder();

                    for (DataSnapshot record : snapshot.getChildren()) {
                        data.append("🔹 רשומה חדשה:\n"); // New record marker
                        for (DataSnapshot field : record.getChildren()) {
                            String key = field.getKey();
                            String value = field.getValue(String.class);
                            data.append("▪️ ").append(key).append(": ").append(value).append("\n"); // Field marker
                        }
                        data.append("\n"); // Add a newline after each record's fields
                    }

                    dataTextView.setText(data.toString());
                } else {
                    Toast.makeText(ShowDetails.this, "אין נתונים למספר זה", Toast.LENGTH_LONG).show(); // "No data for this number"
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowDetails.this, "שגיאה בטעינת הנתונים", Toast.LENGTH_SHORT).show(); // "Error loading data"
            }
        });
    }
}