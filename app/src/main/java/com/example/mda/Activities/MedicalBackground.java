package com.example.mda.Activities;

import static com.example.mda.Activities.Patient_details.anamnesis;

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

import com.example.mda.Obj.Background;
import com.example.mda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @file MedicalBackground.java
 * @brief Activity for collecting and saving a patient's medical background information.
 *
 * This activity allows the user (likely medical personnel) to input details
 * about a patient's pre-existing diseases, known allergies, and current medications.
 *
 * The information is entered into three EditText fields.
 * Upon clicking the "Next" button:
 * 1. The input from each field is retrieved.
 * 2. Default text is applied if the "diseases" field is empty ("ידוע על מטופל בריא בדרך כלל").
 * 3. Prefixes are added to the "allergies" and "medications" strings if they are not empty
 *    ("אלרגיות ידועות:" and "תרופות ידועות:" respectively).
 * 4. A {@link com.example.mda.Obj.Background} object is created using this information.
 * 5. This {@link com.example.mda.Obj.Background} object is set within the static
 *    {@link com.example.mda.Activities.Patient_details#anamnesis} object.
 * 6. The entire updated {@link com.example.mda.Obj.Anamnesis} object is then saved
 *    (or overwritten) to the Firebase Realtime Database under the "Patients" node,
 *    using the patient's ID (received from the previous activity via an Intent extra)
 *    as the key.
 * 7. Finally, the activity navigates to the {@link com.example.mda.Activities.MedicalTests}
 *    activity, passing the patient's ID to it.
 *
 * Note: This activity relies on a static {@link com.example.mda.Obj.Anamnesis} object
 * (`Patient_details.anamnesis`) being initialized and available from the previous
 * activity in the flow.
 */
// package com.example.mda.Activities; // Usually excluded from file-level Javadoc content

// import static com.example.mda.Activities.Patient_details.anamnesis; // Usually excluded

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class MedicalBackground extends AppCompatActivity {
    EditText et_diseases, et_allergies, et_medications;
    Button btn_next;
    private DatabaseReference databaseReference; // This specific instance is initialized but then shadowed in savedetails
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medical_background);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_diseases=findViewById(R.id.et_diseases);
        et_allergies=findViewById(R.id.et_allergies);
        et_medications=findViewById(R.id.et_medications);
        btn_next=findViewById(R.id.btn_next);
        databaseReference = FirebaseDatabase.getInstance().getReference("Patient"); // Initialized to "Patient"

        btn_next.setOnClickListener(new View.OnClickListener() {
            // AlertDialog.Builder adb = new AlertDialog.Builder(context); // This line is shadowed
            @Override
            public void onClick(View v) {
                String diseases, allergies, medications;
                // AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext()); // This adb is unused

                diseases = String.valueOf(et_diseases.getText());
                allergies = String.valueOf(et_allergies.getText());
                medications = String.valueOf(et_medications.getText());

                if (diseases.isEmpty()){ // No need for == true with boolean
                    diseases="ידוע על מטופל בריא בדרך כלל";
                }
                if (!allergies.isEmpty()){ // More concise to check !isEmpty()
                    allergies = "אלרגיות ידועות:"+allergies;
                }
                if (!medications.isEmpty()){ // More concise
                    medications = "תרופות ידועות:"+medications;
                }

                savedetails(diseases, allergies, medications);
            }
        });
    }

    /**
     * Saves the collected medical background details to Firebase and navigates to the next screen.
     *
     * This method creates a {@link com.example.mda.Obj.Background} object, updates the
     * static {@code Patient_details.anamnesis} object, saves it to the "Patients" node
     * in Firebase using the patient's ID, and then starts the
     * {@link com.example.mda.Activities.MedicalTests} activity.
     *
     * @param diseases A string describing the patient's pre-existing diseases.
     * @param allergies A string describing the patient's known allergies.
     * @param medications A string describing the patient's current medications.
     */
    private void savedetails(String diseases, String allergies, String medications) {
        // אתחול רפרנס ל-Firebase (Patients node)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients"); // Shadows class member

        Intent intent = getIntent();
        String id = intent.getStringExtra("patientId");

        // יצירת אובייקט למטופל
        Background background= new Background(diseases, allergies, medications);

        anamnesis.setBackground(background); // Modifies the static Anamnesis object

        databaseReference.child(id).setValue(anamnesis);
//        databaseReference.child(anamnesis.getKeyID()).setValue(anamnesis) // Commented out logic
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//        });


        intent = new Intent(context, MedicalTests.class); // Reuses the intent variable
        intent.putExtra("patientId", id);
        startActivity(intent);
    }
}