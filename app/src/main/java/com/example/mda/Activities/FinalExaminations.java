package com.example.mda.Activities;

import static com.example.mda.Activities.Patient_details.anamnesis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
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
 * @file FinalExaminations.java
 * @brief Activity for recording final examination findings for a patient.
 *
 * This activity presents a series of switches to record observations from
 * a final physical examination of a patient. The observations include:
 * <ul>
 *     <li>Respiratory distress</li>
 *     <li>Paleness and sweating</li>
 *     <li>Nausea or vomiting</li>
 *     <li>Orientation (time and space)</li>
 *     <li>Pupil response</li>
 * </ul>
 *
 * For each observation, a Switch UI element allows the user to indicate
 * the presence or absence of a particular sign or symptom. The text associated
 * with the "checked" and "unchecked" state of each switch provides a descriptive
 * string for that finding.
 *
 * Upon clicking the "Next" button:
 * 1. The state of each Switch is checked.
 * 2. Corresponding descriptive strings are assigned based on whether each switch
 *    is checked or not (e.g., "ללא סימני מצוקה נשימתית" vs. "קוצר נשימה").
 * 3. A {@link com.example.mda.Obj.Tests} object is created using these descriptive strings.
 *    Note: Although the `Tests` class has fields like `pulse`, `bloodPressure`, etc.,
 *    in this context, those fields in the newly created `Tests` object will hold the
 *    string descriptions from the final examination switches.
 * 4. This {@link com.example.mda.Obj.Tests} object is then set as the
 *    `final_examinations` property of the static
 *    {@link com.example.mda.Activities.Patient_details#anamnesis} object.
 * 5. The entire updated {@link com.example.mda.Obj.Anamnesis} object is saved (or
 *    overwritten) to the Firebase Realtime Database under the "Patients" node,
 *    keyed by the patient's ID and the `anamnesis.getKeyID()`.
 *    (Note: The Firebase path is `Patients/{patientId}/{anamnesisKeyId}`).
 * 6. Finally, the activity navigates to the {@link com.example.mda.Activities.Hospital}
 *    activity, passing the patient's ID.
 *
 * This activity is typically one of the final steps in the patient data collection workflow,
 * relying on the static {@code Patient_details.anamnesis} object being progressively
 * updated.
 */
// package com.example.mda.Activities; // Usually excluded from file-level Javadoc content

// import static com.example.mda.Activities.Patient_details.anamnesis; // Usually excluded

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class FinalExaminations extends AppCompatActivity {

    // @SuppressLint("UseSwitchCompatOrMaterialCode") // Field annotation, separate from file Javadoc
    Switch respiratory_distress_switch, paleness_switch, nausea_switch, orientation_switch, pupils_switch;
    Button next_button;
    Context context=this;

    // @SuppressLint("MissingInflatedId") // Method annotation, separate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_final_examinations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        respiratory_distress_switch=findViewById(R.id.respiratory_distress_switch);
        paleness_switch=findViewById(R.id.paleness_switch);
        nausea_switch=findViewById(R.id.nausea_switch);
        orientation_switch=findViewById(R.id.orientation_switch);
        pupils_switch=findViewById(R.id.pupils_switch);
        next_button=findViewById(R.id.next_button);
        // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patient"); // Unused instance in onCreate

        next_button.setOnClickListener(new View.OnClickListener() {
            // AlertDialog.Builder adb = new AlertDialog.Builder(context); // This would shadow the one in onClick
            @Override
            public void onClick(View v) {
                String respiratory_distress, paleness, nausea, orientation, pupils;
                // AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext()); // This adb instance is declared but not used.
                respiratory_distress = respiratory_distress_switch.isChecked() ? "ללא סימני מצוקה נשימתית" : "קוצר נשימה";
                paleness = paleness_switch.isChecked() ? "ללא חיוורון או הזעה" : "קיימים חיוורון והזעה";
                nausea = nausea_switch.isChecked() ? "ללא בחילות או הקאות" : "קיימות בחילות והקאות";
                orientation = orientation_switch.isChecked() ? "המטופל לא מתמצא בזמן ובמרחב" : "המטופל מתמצא בזמן ובמרחב";
                pupils = pupils_switch.isChecked() ? "האישונים שווים ומגיבים לאור" : "תגובת אישונים אינה תקינה";
                savedetails(respiratory_distress, paleness, nausea, orientation, pupils);
            }
        });
    }

    /**
     * Saves the final examination details to Firebase and navigates to the Hospital activity.
     *
     * This method takes the descriptive strings from the final examination switches,
     * creates a {@link com.example.mda.Obj.Tests} object with them (where the fields of
     * the Tests object now hold these descriptive strings), updates the
     * `final_examinations` field of the static {@code Patient_details.anamnesis} object,
     * and then saves the entire {@code anamnesis} object to Firebase under the path
     * "Patients/{patientId}/{anamnesisKeyId}". It then starts the
     * {@link com.example.mda.Activities.Hospital} activity.
     *
     * @param respiratory_distress String describing respiratory status.
     * @param paleness String describing presence/absence of paleness/sweating.
     * @param nausea String describing presence/absence of nausea/vomiting.
     * @param orientation String describing patient's orientation.
     * @param pupils String describing pupil status.
     */
    private void savedetails(String respiratory_distress, String paleness, String nausea, String orientation, String pupils) {
        // אתחול רפרנס ל-Firebase (Patients node)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

        Intent intent = getIntent();
        String id = intent.getStringExtra("patientId");

        // יצירת אובייקט למטופל (Creating an object for the patient - specifically, their final examination results)
        Tests test= new Tests(respiratory_distress, paleness, nausea, orientation, pupils);

        anamnesis.setFinal_examinations(test); // Updating the static Anamnesis object

        // Saving to Patients/{patientId}/{anamnesisKeyId}
        databaseReference.child(id).child(anamnesis.getKeyID()).setValue(anamnesis);
//                .addOnCompleteListener(task -> { // Optional completion listener
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//        });


        intent = new Intent(context, Hospital.class); // Reusing intent variable for next activity
        intent.putExtra("patientId", id);
        startActivity(intent);
    }
}