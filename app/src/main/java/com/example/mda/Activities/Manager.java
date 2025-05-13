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

public class Manager extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText id_input;
    Spinner spinner;
    Button search_button;
    ArrayList<String> events;
    ArrayList<Anamnesis> anamnesis;
    ArrayAdapter<String> adp;

    @SuppressLint("MissingInflatedId")
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

    public static String db2Dsiplay(String str){
        return str.substring(6,8)+"-"+str.substring(4,6)+"-"+str.substring(0,4)
                + " " + str.substring(8,10)+":"+str.substring(10,12)+":"+str.substring(12);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            Intent intent = new Intent(this, Report.class);
            intent.putExtra("patientId", anamnesis.get(position-1).getDetails().getId());
            intent.putExtra("eventId", anamnesis.get(position-1).getKeyID());
            intent.putExtra("exist", true);
            startActivity(intent);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
