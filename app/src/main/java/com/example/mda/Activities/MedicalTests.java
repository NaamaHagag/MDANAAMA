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

public class MedicalTests extends AppCompatActivity {
    EditText etPupilEquality, etRespirations, etOxygenSaturation, etBloodPressure, etPulse;
    Button btn_next1;
    private DatabaseReference databaseReference;
    Context context=this;

    @SuppressLint("MissingInflatedId")
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
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            @Override
            public void onClick(View v) {
                String pulse, bloodPressure, oxygenSaturation, respirations, pupilEquality;
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());

                pulse = String.valueOf(etPulse.getText());
                bloodPressure = String.valueOf(etBloodPressure.getText());
                oxygenSaturation = String.valueOf(etOxygenSaturation.getText());
                respirations = String.valueOf(etRespirations.getText());
                pupilEquality = String.valueOf(etPupilEquality.getText());

                if (pulse.isEmpty() || bloodPressure.isEmpty() || oxygenSaturation.isEmpty() || respirations.isEmpty() || pupilEquality.isEmpty()) {
                    adb.setMessage("ERROR: Please fill all fields!");
                } else {
                    savedetails(pulse, bloodPressure, oxygenSaturation, respirations, pupilEquality);
                }
            }
        });
    }

    private void savedetails(String pulse, String bloodPressure, String oxygenSaturation, String respirations, String pupilEquality) {
        // אתחול רפרנס ל-Firebase (Patients node)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

        Intent intent = getIntent();
        String id = intent.getStringExtra("patientId");

        // יצירת אובייקט למטופל
        Tests tests = new Tests(pulse, bloodPressure, oxygenSaturation, respirations, pupilEquality);

        anamnesis.setMedical_tests(tests);

        databaseReference.child(id).setValue(anamnesis);
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//        });

        intent = new Intent(context, Arrival.class);
        intent.putExtra("patientId", id);
        startActivity(intent);
    }
}