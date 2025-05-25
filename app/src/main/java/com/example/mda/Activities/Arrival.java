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

/**
 * @file Arrival.java
 * @brief Activity for recording details about a patient's arrival and the informant.
 *
 * This activity allows the user to input information related to a patient's
 * arrival condition and the person who provided the initial case details.
 * It includes fields for:
 * <ul>
 *     <li>Patient's condition upon arrival</li>
 *     <li>Patient's location (where they were found or incident location)</li>
 *     <li>Patient's level of consciousness</li>
 *     <li>Name of the informant</li>
 *     <li>Details provided by the informant (case story)</li>
 * </ul>
 *
 * The activity also features a speech-to-text functionality to allow users
 * to dictate the informant's details directly into the corresponding EditText field.
 * This is triggered by an ImageButton and uses the RecognizerIntent for speech
 * recognition in Hebrew.
 *
 * Upon clicking the "Save" button:
 * 1. The input from all EditText fields is retrieved.
 * 2. It checks if any of the fields are empty. If so, an error message
 *    is displayed in an AlertDialog.
 * 3. If all fields are filled, an {@link com.example.mda.Obj.ArrivalClass} object is
 *    created with the collected data.
 * 4. This {@link com.example.mda.Obj.ArrivalClass} object is then set as the
 *    `arrival` property of the static
 *    {@link com.example.mda.Activities.Patient_details#anamnesis} object.
 * 5. The entire updated {@link com.example.mda.Obj.Anamnesis} object is saved (or
 *    overwritten) to the Firebase Realtime Database under the "Patients" node,
 *    keyed by the patient's ID (received from the previous activity via an Intent extra).
 * 6. Finally, the activity navigates to the
 *    {@link com.example.mda.Activities.FinalExaminations} activity, passing the
 *    patient's ID to it.
 *
 * Note: This activity is part of a sequence for collecting patient data and relies
 * on the static {@code Patient_details.anamnesis} object being initialized and
 * progressively updated by preceding activities. The patient's ID is also passed
 * between activities using Intent extras.
 */
// package com.example.mda.Activities; // Usually excluded from file-level Javadoc content

// import static com.example.mda.Activities.Patient_details.anamnesis; // Usually excluded

// import ... // Imports are typically not part of the file-level Javadoc content itself

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

    /**
     * Initiates the speech-to-text recognition process.
     * An intent is created to launch the system's speech recognition service,
     * configured for free-form input in Hebrew (he-IL).
     */
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "he-IL"); // שפה עברית
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    /**
     * Handles the result returned from the speech recognition activity.
     * If the recognition was successful (RESULT_OK) and provides a result,
     * the recognized text (the first result from the list) is populated into the
     * `informant_details_input` EditText field.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from. This should be {@link #SPEECH_REQUEST_CODE}.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult(). This should be {@link android.app.Activity#RESULT_OK}
     *                   for a successful speech recognition.
     * @param data An Intent, which can return result data to the caller. For speech
     *               recognition, this contains an ArrayList of strings under the
     *               {@link RecognizerIntent#EXTRA_RESULTS} key.
     */
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

    /**
     * Saves the collected arrival and informant details to Firebase and navigates to the next screen.
     *
     * This method retrieves the patient ID from the intent, creates an
     * {@link com.example.mda.Obj.ArrivalClass} object with the provided
     * details, updates the static {@code Patient_details.anamnesis} object's arrival
     * information, saves the entire updated anamnesis to the "Patients" node in Firebase
     * using the patient's ID, and then starts the
     * {@link com.example.mda.Activities.FinalExaminations} activity, passing the patient ID.
     *
     * @param condition The patient's condition upon arrival.
     * @param location The patient's location (incident location).
     * @param consciousness The patient's level of consciousness.
     * @param name The name of the informant.
     * @param details The details provided by the informant (case story).
     */
    private void savedetails(String condition, String location, String consciousness, String name, String details) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

        Intent intent = getIntent();
        String id = intent.getStringExtra("patientId");

        // יצירת אובייקט למטופל
        ArrivalClass arr = new ArrivalClass(condition, location, consciousness, name, details);

        anamnesis.setArrival(arr);
        databaseReference.child(id).setValue(anamnesis);

        intent = new Intent(context, FinalExaminations.class);
        intent.putExtra("patientId", id);
        startActivity(intent);
    }
}
