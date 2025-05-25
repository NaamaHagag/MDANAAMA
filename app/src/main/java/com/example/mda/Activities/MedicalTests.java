package com.example.mda.Activities;

import static com.example.mda.Activities.Patient_details.anamnesis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mda.R;
import com.example.mda.Obj.Tests;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @file MedicalTests.java
 * @brief Activity for recording initial medical test results for a patient.
 *
 * This activity presents a form to input results from a set of initial medical
 * tests or observations. These include:
 * <ul>
 *     <li>Pulse rate</li>
 *     <li>Blood pressure</li>
 *     <li>Oxygen saturation</li>
 *     <li>Respirations (breaths per minute)</li>
 *     <li>Pupil equality/status</li>
 * </ul>
 *
 * Upon submission via the "Next" button:
 * 1. The input from each EditText field is retrieved.
 * 2. It checks if any of the fields are empty. If so, an error message
 *    is displayed in an AlertDialog.
 * 3. If all fields are filled, a {@link com.example.mda.Obj.Tests} object is created
 *    with the collected data.
 * 4. This {@link com.example.mda.Obj.Tests} object is then set as the
 *    `medical_tests` property of the static
 *    {@link com.example.mda.Activities.Patient_details#anamnesis} object.
 * 5. The entire updated {@link com.example.mda.Obj.Anamnesis} object is saved (or
 *    overwritten) to the Firebase Realtime Database under the "Patients" node,
 *    keyed by the patient's ID (received from the previous activity).
 * 6. Finally, the activity navigates to the {@link com.example.mda.Activities.Arrival}
 *    activity, passing the patient's ID.
 *
 * Note: This activity, like others in the flow, relies on the static
 * {@code Patient_details.anamnesis} object being available and progressively updated.
 */
// package com.example.mda.Activities; // Usually excluded from file-level Javadoc content

// import static com.example.mda.Activities.Patient_details.anamnesis; // Usually excluded

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class MedicalTests extends AppCompatActivity {
    EditText etPupilEquality, etRespirations, etOxygenSaturation, etBloodPressure, etPulse;
    Button btn_next1;
    // private DatabaseReference databaseReference; // This field is declared but not used. A local one is used in savedetails.
    Context context=this;

    // @SuppressLint("MissingInflatedId") // Method/field annotations are separate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medical_tests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etPulse = findViewById(R.id.et_pulse);
        etBloodPressure = findViewById(R.id.et_blood_pressure);
        etOxygenSaturation = findViewById(R.id.et_oxygen_saturation);
        etRespirations = findViewById(R.id.et_respirations);
        etPupilEquality = findViewById(R.id.et_pupil_equality);
        btn_next1 = findViewById(R.id.btn_next1);

        btn_next1.setOnClickListener(new View.OnClickListener() {
            // AlertDialog.Builder adb = new AlertDialog.Builder(context); // This shadows the one inside onClick if uncommented
            @Override
            public void onClick(View v) {
                String pulse, bloodPressure, oxygenSaturation, respirations, pupilEquality;
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext()); // Correctly scoped to onClick

                pulse = String.valueOf(etPulse.getText());
                bloodPressure = String.valueOf(etBloodPressure.getText());
                oxygenSaturation = String.valueOf(etOxygenSaturation.getText());
                respirations = String.valueOf(etRespirations.getText());
                pupilEquality = String.valueOf(etPupilEquality.getText());

                if (pulse.isEmpty() || bloodPressure.isEmpty() || oxygenSaturation.isEmpty() || respirations.isEmpty() || pupilEquality.isEmpty()) {
                    adb.setMessage("ERROR: Please fill all fields!");
                    // It's good practice to set positive/negative buttons for AlertDialog
                    adb.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                    adb.create().show();
                } else {
                    savedetails(pulse, bloodPressure, oxygenSaturation, respirations, pupilEquality);
                }
            }
        });
    }

    /**
     * Saves the collected medical test results to Firebase and navigates to the next screen.
     *
     * This method creates a {@link com.example.mda.Obj.Tests} object with the provided
     * test results, updates the static {@code Patient_details.anamnesis} object,
     * saves the entire anamnesis to the "Patients" node in Firebase using the patient's ID,
     * and then starts the {@link com.example.mda.Activities.Arrival} activity.
     *
     * @param pulse The patient's pulse rate.
     * @param bloodPressure The patient's blood pressure reading.
     * @param oxygenSaturation The patient's oxygen saturation level.
     * @param respirations The patient's respiration rate.
     * @param pupilEquality A string describing the state of the patient's pupils.
     */
    private void savedetails(String pulse, String bloodPressure, String oxygenSaturation, String respirations, String pupilEquality) {
        // אתחול רפרנס ל-Firebase (Patients node)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

        Intent intent = getIntent();
        String id = intent.getStringExtra("patientId");

        // יצירת אובייקט למטופל (Creating an object for the patient - specifically, for their tests)
        Tests tests = new Tests(pulse, bloodPressure, oxygenSaturation, respirations, pupilEquality);

        anamnesis.setMedical_tests(tests); // Updating the static Anamnesis object

        databaseReference.child(id).setValue(anamnesis);
//                .addOnCompleteListener(task -> { // Optional: Add completion listener
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//        });

        intent = new Intent(context, Arrival.class); // Reusing intent variable for next activity
        intent.putExtra("patientId", id);
        startActivity(intent);
    }
}