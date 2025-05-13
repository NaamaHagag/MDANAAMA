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

public class MedicalBackground extends AppCompatActivity {
    EditText et_diseases, et_allergies, et_medications;
    Button btn_next;
    private DatabaseReference databaseReference;
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Patient");

        btn_next.setOnClickListener(new View.OnClickListener() {
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            @Override
            public void onClick(View v) {
                String diseases, allergies, medications;
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
                diseases = String.valueOf(et_diseases.getText());
                allergies = String.valueOf(et_allergies.getText());
                medications = String.valueOf(et_medications.getText());

                if (diseases.isEmpty()==true){
                    diseases="ידוע על מטופל בריא בדרך כלל";
                }
                if (allergies.isEmpty()==false){
                    allergies = "אלרגיות ידועות:"+allergies;
                }
                if (medications.isEmpty()==false){
                    medications = "תרופות ידועות:"+medications;
                }

                savedetails(diseases, allergies, medications);
            }
        });
    }

    private void savedetails(String diseases, String allergies, String medications) {
        // אתחול רפרנס ל-Firebase (Patients node)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

        Intent intent = getIntent();
        String id = intent.getStringExtra("patientId");

        // יצירת אובייקט למטופל
        Background background= new Background(diseases, allergies, medications);

        anamnesis.setBackground(background);

        databaseReference.child(id).setValue(anamnesis);
//        databaseReference.child(anamnesis.getKeyID()).setValue(anamnesis)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//        });


        intent = new Intent(context, MedicalTests.class);
        intent.putExtra("patientId", id);
        startActivity(intent);
    }
}