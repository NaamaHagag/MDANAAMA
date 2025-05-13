package com.example.mda.Activities;

import static com.example.mda.Activities.Patient_details.anamnesis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mda.Obj.ArrivalClass;
import com.example.mda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Arrival extends AppCompatActivity {

    EditText patient_condition_input, patient_location_input, consciousness_input, informant_name_input, informant_details_input;
    Button save_button;
    ImageButton btn_record_case;
    Context context = this;

    private static final int SPEECH_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_arrival);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // אתחול שדות ה-EditText והכפתור
        patient_condition_input = findViewById(R.id.patient_condition_input);
        patient_location_input = findViewById(R.id.patient_location_input);
        consciousness_input = findViewById(R.id.consciousness_input);
        informant_name_input = findViewById(R.id.informant_name_input);
        informant_details_input = findViewById(R.id.informant_details_input);
        save_button = findViewById(R.id.save_button);
        btn_record_case = findViewById(R.id.btn_record_case);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patient");

        // כפתור שמירה
        save_button.setOnClickListener(v -> {
            String condition = String.valueOf(patient_condition_input.getText());
            String location = String.valueOf(patient_location_input.getText());
            String consciousness = String.valueOf(consciousness_input.getText());
            String name = String.valueOf(informant_name_input.getText());
            String details = String.valueOf(informant_details_input.getText());

            if (condition.isEmpty() || location.isEmpty() || consciousness.isEmpty() || name.isEmpty() || details.isEmpty()) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("ERROR: Please fill all fields!")
                        .show();
            } else {
                savedetails(condition, location, consciousness, name, details);
            }
        });

        // כפתור הקלטת דיבור
        btn_record_case.setOnClickListener(v -> {
            startSpeechToText();
        });
    }

    // פונקציה שמבצעת את פעולת הדיבור לטקסט
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "he-IL"); // שפה עברית
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // מטפל בתוצאה מהדיבור לטקסט
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                // עדכון ה-EditText של סיפור המקרה עם התוצאה
                informant_details_input.setText(result.get(0));
            }
        }
    }

    // פונקציה לשמירת הנתונים ב-Firebase
    private void savedetails(String condition, String location, String consciousness, String name, String details) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

        Intent intent = getIntent();
        String id = intent.getStringExtra("patientId");

        // יצירת אובייקט למטופל
        ArrivalClass arr = new ArrivalClass(condition, location, consciousness, name, details);

        anamnesis.setArrival(arr);
        databaseReference.child(id).setValue(anamnesis);
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//         });

        intent = new Intent(context, FinalExaminations.class);
        intent.putExtra("patientId", id);
        startActivity(intent);
    }
}
