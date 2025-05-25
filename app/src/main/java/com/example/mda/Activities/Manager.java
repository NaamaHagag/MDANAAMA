package com.example.mda.Activities;

import static com.example.mda.FBRef.refClosed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mda.Obj.Anamnesis;
import com.example.mda.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * @file Manager.java
 * @brief Activity for managers to search for and view patient event history.
 *
 * This activity allows users identified as managers to search for patients
 * within the Firebase Realtime Database using the patient's ID.
 *
 * Upon entering a patient ID and initiating a search:
 * 1. The activity queries the "Closed" node in Firebase for the given ID.
 * 2. If the patient ID exists, a Toast message confirms its presence. The activity
 *    then populates a Spinner with a list of event dates/times associated with
 *    that patient. These event keys are formatted for display.
 * 3. {@link com.example.mda.Obj.Anamnesis} objects for each event are retrieved
 *    and stored.
 * 4. If the patient ID is not found, a Toast message indicates this.
 * 5. Selecting an event from the Spinner navigates the manager to the
 *    {@link com.example.mda.Activities.Report} activity, passing the patient ID
 *    and the selected event ID to display the detailed report for that specific event.
 *
 * The activity implements {@link android.widget.AdapterView.OnItemSelectedListener}
 * to handle selections from the event Spinner.
 */
// package com.example.mda.Activities; // Usually excluded from file-level Javadoc content

// import static com.example.mda.FBRef.refClosed; // Usually excluded

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class Manager extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText id_input;
    Spinner spinner;
    Button search_button;
    ArrayList<String> events;
    ArrayList<Anamnesis> anamnesis;
    ArrayAdapter<String> adp;

    // @SuppressLint("MissingInflatedId") // Method/field annotations are separate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager);
        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);
        events = new ArrayList<>();
        events.add("Please choose an event:");
        anamnesis = new ArrayList<>();
        adp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, events);
        spinner.setAdapter(adp);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        id_input = findViewById(R.id.id_input);
        search_button = findViewById(R.id.search_button);

        // Set button click listener
        search_button.setOnClickListener(v -> checkIfPatientExists());
    }

    private void checkIfPatientExists() {
        String uid = id_input.getText().toString().trim();

        if (uid.isEmpty()) {
            Toast.makeText(this, "אנא הזן תעודת זהות", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming refClosed is a valid DatabaseReference from FBRef class
        refClosed.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dS) {
                if (dS.exists()) {
                    Toast.makeText(Manager.this, "תעודת זהות קיימת במערכת!", Toast.LENGTH_LONG).show();
                    events.clear();
                    events.add("Please choose an event:");
                    anamnesis.clear();
                    for (DataSnapshot event : dS.getChildren()) {
                        events.add(db2Dsiplay(event.getKey()));
                        anamnesis.add(event.getValue(Anamnesis.class));
                    }
                    adp.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(Manager.this, "תעודת זהות לא נמצאה!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Manager.this, "שגיאה בחיפוש הנתונים", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Formats a database timestamp string into a more readable date and time display format.
     * The input string is expected to be in "yyyyMMddHHmmss" format.
     *
     * @param str The raw timestamp string from the database (e.g., "20230115143055").
     * @return A formatted string (e.g., "15-01-2023 14:30:55").
     */
    public static String db2Dsiplay(String str){
        return str.substring(6,8)+"-"+str.substring(4,6)+"-"+str.substring(0,4)
                + " " + str.substring(8,10)+":"+str.substring(10,12)+":"+str.substring(12);
    }

    /**
     * Callback method to be invoked when an item in the event Spinner has been selected.
     * If a valid event (not the initial prompt) is selected, it navigates to the
     * {@link Report} activity with the patient's ID and the selected event's ID.
     *
     * @param parent The AdapterView where the selection happened.
     * @param view The view within the AdapterView that was clicked.
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that is selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) { // Position 0 is "Please choose an event:"
            Intent intent = new Intent(this, Report.class);
            // Pass patient ID and the specific event key (Anamnesis keyID)
            intent.putExtra("patientId", anamnesis.get(position-1).getDetails().getId());
            intent.putExtra("eventId", anamnesis.get(position-1).getKeyID());
            intent.putExtra("exist", true); // Indicates that the event data already exists
            startActivity(intent);
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from the view.
     * This may happen when the item is out of sight.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Not used in this implementation, but required by OnItemSelectedListener.
    }
}