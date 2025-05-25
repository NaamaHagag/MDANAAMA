package com.example.mda.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * @file SignUp.java
 * @brief Activity for new user registration.
 *
 * This activity provides a user interface for new users to sign up for the
 * application. Users are required to provide their ID number (which also serves
 * as their password for initial setup) and their email address.
 *
 * Upon clicking the register button:
 * 1. The input fields (ID and email) are validated to ensure they are not empty.
 * 2. If valid, the activity uses Firebase Authentication to create a new user
 *    account with the provided email and the ID number as the password.
 * 3. If user creation is successful:
 *    a. The user's ID and email are stored in the Firebase Realtime Database
 *       under a "users" node, keyed by the Firebase Authentication User ID (UID).
 *    b. A success message is displayed.
 *    c. The activity navigates the user to the {@link com.example.mda.Activities.Patient_details}
 *       activity and finishes itself to prevent going back to the sign-up screen.
 * 4. If any step in the registration or data saving process fails, an appropriate
 *    error message is displayed to the user via a Toast.
 */
// package com.example.mda.Activities; // Usually excluded from file-level Javadoc content

// import ... // Imports are typically not part of the file-level Javadoc content itself

public class SignUp extends AppCompatActivity {

    private EditText etId, etEmail;
    private Button btn_register; // Variable name corrected to match usage
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // התחברות לרכיבים (Component binding)
        etId = findViewById(R.id.et_id);
        etEmail = findViewById(R.id.et_email);
        btn_register = findViewById(R.id.btn_register); // Ensure ID matches XML

        // התחברות ל-Firebase (Firebase connection)
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        // כפתור ההרשמה (Registration button)
        btn_register.setOnClickListener(v -> registerUser());
    }

    /**
     * Handles the user registration process.
     * Retrieves user input, validates it, creates a new Firebase Authentication user,
     * and saves user details to the Firebase Realtime Database.
     */
    public void registerUser() {
        String id = etId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = id; // ת"ז כסיסמה (ID as password)

        if (id.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show(); // "Please fill all fields"
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();

                        // שמירת פרטי המשתמש (Saving user details)
                        HashMap<String, String> userData = new HashMap<>();
                        userData.put("id", id);
                        userData.put("email", email);

                        dbRef.child(uid).setValue(userData)
                                .addOnCompleteListener(saveTask -> {
                                    if (saveTask.isSuccessful()) {
                                        Toast.makeText(this, "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show(); // "Registered successfully!"
                                        Intent intent = new Intent(SignUp.this, Patient_details.class);
                                        startActivity(intent);
                                        finish(); // Finish SignUp activity
                                    } else {
                                        Toast.makeText(this, "שגיאה בשמירת הנתונים", Toast.LENGTH_SHORT).show(); // "Error saving data"
                                    }
                                });
                    } else {
                        // Display more specific error if possible from task.getException()
                        Toast.makeText(this, "שגיאה בהרשמה: " + task.getException().getMessage(), Toast.LENGTH_LONG).show(); // "Registration error: "
                    }
                });
    }
}