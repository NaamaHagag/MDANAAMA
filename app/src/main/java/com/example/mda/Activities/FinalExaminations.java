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

public class FinalExaminations extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch respiratory_distress_switch, paleness_switch, nausea_switch, orientation_switch, pupils_switch;
    Button next_button;
    Context context=this;

    @SuppressLint("MissingInflatedId")
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patient");

        next_button.setOnClickListener(new View.OnClickListener() {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            @Override
            public void onClick(View v) {
                String respiratory_distress, paleness, nausea, orientation, pupils;
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
                respiratory_distress = respiratory_distress_switch.isChecked() ? "ללא סימני מצוקה נשימתית" : "קוצר נשימה";
                paleness = paleness_switch.isChecked() ? "ללא חיוורון או הזעה" : "קיימים חיוורון והזעה";
                nausea = nausea_switch.isChecked() ? "ללא בחילות או הקאות" : "קיימות בחילות והקאות";
                orientation = orientation_switch.isChecked() ? "המטופל לא מתמצא בזמן ובמרחב" : "המטופל מתמצא בזמן ובמרחב";
                pupils = pupils_switch.isChecked() ? "האישונים שווים ומגיבים לאור" : "תגובת אישונים אינה תקינה";
                savedetails(respiratory_distress, paleness, nausea, orientation, pupils);
            }
        });
    }
    private void savedetails(String respiratory_distress, String paleness, String nausea, String orientation, String pupils) {
        // אתחול רפרנס ל-Firebase (Patients node)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

        Intent intent = getIntent();
        String id = intent.getStringExtra("patientId");

        // יצירת אובייקט למטופל
        Tests test= new Tests(respiratory_distress, paleness, nausea, orientation, pupils);

        anamnesis.setFinal_examinations(test);

        databaseReference.child(id).setValue(anamnesis);
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//        });


        intent = new Intent(context, Hospital.class);
        intent.putExtra("patientId", id);
        startActivity(intent);
    }

}