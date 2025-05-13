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

public class Hospital extends AppCompatActivity {
    EditText hospital_name_input, hospital_department_input, staff_name_input;
    Button save_button;
    private DatabaseReference databaseReference;
    Context context=this;


    @SuppressLint("MissingInflatedId")
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patient");

        save_button.setOnClickListener(new View.OnClickListener() {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            @Override
            public void onClick(View v) {
                String name, department, staff;
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());

                name = String.valueOf(hospital_name_input.getText());
                department = String.valueOf(hospital_department_input.getText());
                staff = String.valueOf(staff_name_input.getText());

                if (name.isEmpty() || department.isEmpty() || staff.isEmpty()) {
                    adb.setMessage("ERROR: Please fill all fields!");
                } else {
                    savedetails(name, department, staff);
                }
            }
        });
    }

    private void savedetails(String name, String department, String staff) {
        // אתחול רפרנס ל-Firebase (Patients node)
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Closed");

        Intent intent = getIntent();
        String id = intent.getStringExtra("patientId");

        // יצירת אובייקט למטופל
        Hospitald hospital= new Hospitald(name, department, staff);

        anamnesis.setHospital(hospital);
        databaseReference.child(anamnesis.getDetails().getId()).child(anamnesis.getKeyID()).setValue(anamnesis);

        refPatients.child(anamnesis.getDetails().getId()).removeValue();

//        databaseReference.child(anamnesis.getKeyID()).setValue(anamnesis)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//        });


        intent = new Intent(context, Report.class);
        startActivity(intent);
    }

    public void takephoto(View view) {
        Intent intent2 = new Intent(context, Camera.class);
        startActivity(intent2);
    }
}