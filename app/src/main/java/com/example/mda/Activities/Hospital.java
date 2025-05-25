package com.example.mda.Activities;

import static com.example.mda.Activities.Patient_details.anamnesis;
import static com.example.mda.FBRef.refPatients;

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

import com.example.mda.Obj.Hospitald;
import com.example.mda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @file Hospital.java
 * @brief Activity for recording hospital and staff details for patient handover.
 *
 * This activity allows users to input details about the hospital, department,
 * and receiving staff member to which a patient is being transferred or whose
 * care is being documented. It serves as one of the final steps in the patient
 * data collection process.
 *
 * It includes input fields for:
 * <ul>
 *     <li>Hospital Name (שם בית החולים)</li>
 *     <li>Hospital Department (מחלקה בבית החולים)</li>
 *     <li>Receiving Staff Member's Name (שם איש הצוות)</li>
 * </ul>
 *
 * The activity provides two main actions triggered by buttons:
 * 1.  <b>Save Button (שמור):</b>
 *     <ul>
 *         <li>Retrieves the text from all input fields.</li>
 *         <li>Validates that all fields are filled. If not, an error message
 *             (using an AlertDialog) is displayed.</li>
 *         <li>If valid, it calls the {@link #savedetails(String, String, String, boolean)}
 *             method with {@code camera = false}.</li>
 *         <li>This method creates a {@link com.example.mda.Obj.Hospitald} object,
 *             updates the static {@link com.example.mda.Activities.Patient_details#anamnesis}
 *             object with these hospital details.</li>
 *         <li>The complete {@link com.example.mda.Obj.Anamnesis} record is then moved
 *             from the "Patients" node to the "Closed" node in Firebase Realtime Database,
 *             using the patient's ID and the anamnesis key ID
 *             (effectively closing the active case).</li>
 *         <li>Finally, it navigates to the {@link com.example.mda.Activities.Report} activity.</li>
 *     </ul>
 * 2.  <b>Camera Button (כפתור מצלמה):</b>
 *     <ul>
 *         <li>Performs the same input retrieval and validation as the Save button.</li>
 *         <li>If valid, it calls the {@link #savedetails(String, String, String, boolean)}
 *             method with {@code camera = true}.</li>
 *         <li>The data saving logic (creating {@link com.example.mda.Obj.Hospitald},
 *             updating {@code anamnesis}, and moving the record in Firebase) is identical
 *             to the Save button's action.</li>
 *         <li>After saving, it navigates to the {@link com.example.mda.Activities.Camera}
 *             activity, presumably to attach images to the now closed record.</li>
 *     </ul>
 *
 * This activity relies on the static {@code Patient_details.anamnesis} object
 * being available and populated from previous activities in the workflow.
 * The patient's ID is used to identify the correct record in Firebase.
 */
// package com.example.mda.Activities; // Usually excluded from file-level Javadoc content

// import static com.example.mda.Activities.Patient_details.anamnesis; // Usually excluded
// import static com.example.mda.FBRef.refPatients; // Usually excluded

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class Hospital extends AppCompatActivity {
    EditText hospital_name_input, hospital_department_input, staff_name_input;
    Button save_button, camerabtn;
    private DatabaseReference databaseReference; // Field is declared but shadowed in onCreate and savedetails.
    Context context=this;


    // @SuppressLint("MissingInflatedId") // Method annotation, separate from file Javadoc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hospital);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        save_button=findViewById(R.id.save_button);
        hospital_name_input=findViewById(R.id.hospital_name_input);
        hospital_department_input=findViewById(R.id.hospital_department_input);
        staff_name_input=findViewById(R.id.staff_name_input);
        camerabtn = findViewById(R.id.camerabtn);

        // This databaseReference is local to onCreate and not used.
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patient");

        save_button.setOnClickListener(new View.OnClickListener() {
            // This adb is local to the anonymous class and shadows any outer scope adb.
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            @Override
            public void onClick(View v) {
                String name, department, staff;
                // This adb is local to onClick method, preferred over the anonymous class one.
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());

                name = String.valueOf(hospital_name_input.getText());
                department = String.valueOf(hospital_department_input.getText());
                staff = String.valueOf(staff_name_input.getText());

                if (name.isEmpty() || department.isEmpty() || staff.isEmpty()) {
                    adb.setMessage("ERROR: Please fill all fields!");
                    // Consider adding a positive button to dismiss the dialog
                    // adb.setPositiveButton("OK", null);
                    // adb.show();
                } else {
                    savedetails(name, department, staff, false);
                }
            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener() {
            // This adb is local to the anonymous class.
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            @Override
            public void onClick(View v) {
                String name, department, staff;
                // This adb is local to onClick method.
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());

                name = String.valueOf(hospital_name_input.getText());
                department = String.valueOf(hospital_department_input.getText());
                staff = String.valueOf(staff_name_input.getText());

                if (name.isEmpty() || department.isEmpty() || staff.isEmpty()) {
                    adb.setMessage("ERROR: Please fill all fields!");
                    // adb.show(); // Missing show call for error dialog
                } else {
                    savedetails(name, department, staff, true);
                }
            }
        });
    }

    /**
     * Saves the hospital details, moves the patient record to "Closed" in Firebase,
     * and navigates to the next activity (either Report or Camera).
     *
     * This method constructs a {@link com.example.mda.Obj.Hospitald} object with the provided
     * hospital name, department, and staff name. It updates the static
     * {@link com.example.mda.Activities.Patient_details#anamnesis} object with these details.
     *
     * The entire {@code anamnesis} record is then written to the "Closed" node in Firebase,
     * under the path {@code Closed/{patientId}/{anamnesisKeyId}}.
     * After successfully saving to "Closed", the original record under the "Patients" node
     * (path {@code Patients/{patientId}}) is removed.
     *
     * Finally, it starts a new activity based on the {@code camera} parameter:
     * - If {@code camera} is true, it navigates to {@link com.example.mda.Activities.Camera}.
     * - If {@code camera} is false, it navigates to {@link com.example.mda.Activities.Report}.
     *
     * @param name The name of the hospital.
     * @param department The department in the hospital.
     * @param staff The name of the staff member.
     * @param camera A boolean flag indicating whether to proceed to the Camera activity (true)
     *               or the Report activity (false) after saving.
     */
    private void savedetails(String name, String department, String staff, boolean camera) {
        // אתחול רפרנס ל-Firebase (Closed node)
        // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients"); // Original line, changed below
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Closed");

//        Intent intent = getIntent(); // Not needed here, anamnesis object holds patient ID implicitly via getDetails().getId()
//        String id = intent.getStringExtra("patientId");

        // יצירת אובייקט למטופל (Creating an object for hospital details)
        Hospitald hospital= new Hospitald(name, department, staff);

        anamnesis.setHospital(hospital);
        databaseReference.child(anamnesis.getDetails().getId()).child(anamnesis.getKeyID()).setValue(anamnesis);

        refPatients.child(anamnesis.getDetails().getId()).removeValue();


        if (camera) {
            Intent intent = new Intent(context, Camera.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(context, Report.class);
            startActivity(intent);
        }
    }

}