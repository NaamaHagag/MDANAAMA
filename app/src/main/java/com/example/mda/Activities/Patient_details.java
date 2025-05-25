package com.example.mda.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ipsec.ike.IkeKeyIdIdentification;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mda.Obj.Anamnesis;
import com.example.mda.Obj.Patient;
import com.example.mda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("UseSwitchCompatOrMaterialCode")
/**
 * @file Patient_details.java
 * @brief Activity for collecting and saving new patient details.
 *
 * This activity presents a form to the user (likely medical personnel)
 * to input essential information about a new patient, including their
 * full name, ID, phone number, gender, and age.
 *
 * Upon submission, the entered details are validated to ensure all fields are
 * filled. If valid, a new {@link com.example.mda.Obj.Patient} object is created,
 * and the information is saved to the Firebase Realtime Database under a "Patients"
 * node, keyed by the patient's ID. An {@link com.example.mda.Obj.Anamnesis}
 * object is also initialized and updated with these patient details and a
 * timestamp-based key.
 *
 * After successfully saving the data, the activity navigates to the
 * {@link com.example.mda.Activities.MedicalBackground} activity, passing the
 * patient's ID to it.
 */
// package com.example.mda.Activities; // Usually excluded from file-level Javadoc content

// import ... // Imports are typically not part of the file-level Javadoc content itself

// @SuppressLint("UseSwitchCompatOrMaterialCode") // Class-level annotations are separate
public class Patient_details extends AppCompatActivity {
    Context context = this;
    EditText et_full_name, et_id, et_phone, et_age;
    Switch switch_gender;
    Button next;
    private DatabaseReference databaseReference;
    // ArrayList<String> ageList; // This field is declared but not used in the provided code.
    // ArrayAdapter<String> adapter; // This field is declared but not used.
    public static Anamnesis anamnesis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        anamnesis = new Anamnesis();
        et_full_name=findViewById(R.id.et_full_name);
        et_id=findViewById(R.id.et_id);
        et_phone=findViewById(R.id.et_phone);
        switch_gender=findViewById(R.id.switch_gender);
        et_age=findViewById(R.id.et_age);
        next=findViewById(R.id.next);
        databaseReference = FirebaseDatabase.getInstance().getReference("Patient"); // Note: This ref seems unused, savedetails uses "Patients"


        next.setOnClickListener(new View.OnClickListener() {
            // AlertDialog.Builder adb = new AlertDialog.Builder(context); // This line is shadowed by the one inside onClick
            @Override
            public void onClick(View v) {
                String fullName, id, gender, phoneNumber, age;
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());

                fullName = String.valueOf(et_full_name.getText());
                id = String.valueOf(et_id.getText());
                phoneNumber = String.valueOf(et_phone.getText());
                gender = switch_gender.isChecked() ? "Female" : "Male";
                age = String.valueOf(et_age.getText());

                if (fullName.isEmpty() || id.isEmpty() || phoneNumber.isEmpty() || gender.isEmpty() || age.isEmpty()) {
                    adb.setMessage("ERROR: Please fill all fields!");
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    adb.create().show();
                } else {
                    savedetails(fullName, id, phoneNumber, gender, age);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Consider if any specific cleanup or navigation logic is needed here.
    }

    private void savedetails(String fullName, String id, String phoneNumber, String gender, String age) {
        // אתחול רפרנס ל-Firebase (Patients node)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

        // יצירת מזהה ייחודי לכל מטופל
        // String keyID = databaseReference.child(id).push().getKey(); // This keyID is generated but not directly used for setting patient details. The anamnesis keyID is different.

        Calendar calNow = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = sdf.format(calNow.getTime());


        // יצירת אובייקט למטופル
        Patient patient = new Patient(fullName, id, phoneNumber, gender, age);
        anamnesis.setDetails(patient);
        anamnesis.setKeyID(date); // This sets the key for the Anamnesis object itself

        // שמירת הפרטים תחת קטגוריית "details"
        // The entire anamnesis object (which includes patient details) is saved under the patient's ID.
        databaseReference.child(id).setValue(anamnesis);

        Intent intent = new Intent(context, MedicalBackground.class);
        intent.putExtra("patientId", id);
        startActivity(intent);
    }
}