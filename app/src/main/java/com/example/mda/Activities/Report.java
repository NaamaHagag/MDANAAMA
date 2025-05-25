package com.example.mda.Activities;

import static com.example.mda.Activities.Patient_details.anamnesis;
import static com.example.mda.FBRef.refClosed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mda.Obj.Anamnesis;
import com.example.mda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Report Activity for displaying comprehensive medical anamnesis reports.
 *
 * <p>This activity serves as the final step in the medical documentation workflow,
 * presenting a complete formatted report that includes:</p>
 * <ul>
 *   <li>Patient personal details and demographics</li>
 *   <li>Medical background (diseases, allergies, medications)</li>
 *   <li>Incident details and arrival conditions</li>
 *   <li>Medical examinations and vital signs</li>
 *   <li>Hospital transfer information</li>
 *   <li>Associated medical images from Firebase storage</li>
 * </ul>
 *
 * <p>The activity supports two modes of operation:</p>
 * <ul>
 *   <li><strong>New Report Mode:</strong> Displays current anamnesis data from memory</li>
 *   <li><strong>Existing Report Mode:</strong> Retrieves and displays archived reports from Firebase</li>
 * </ul>
 *
 * <p>Reports are automatically formatted with gender-appropriate Hebrew text
 * and include comprehensive medical data presentation suitable for medical
 * documentation and handover purposes.</p>
 *
 * @author Medical Documentation App Team
 * @version 1.0
 * @since API Level 21
 */
public class Report extends AppCompatActivity {

    /** TextView component for displaying the report title */
    TextView title1;

    /** TextView component for displaying the complete formatted anamnesis report */
    TextView finalAnamnesis;

    /** ImageView component for displaying associated medical images */
    ImageView imageViewReport;

    /**
     * Initializes the Report activity and determines display mode.
     *
     * <p>This method performs the following initialization steps:</p>
     * <ul>
     *   <li>Sets up edge-to-edge display and window insets</li>
     *   <li>Initializes UI components</li>
     *   <li>Analyzes intent extras to determine operation mode</li>
     *   <li>Loads anamnesis data from Firebase (existing) or memory (new)</li>
     * </ul>
     *
     * <p>The activity supports two operational modes based on intent extras:</p>
     * <ul>
     *   <li><strong>Existing Report:</strong> When "exist" extra is true, retrieves
     *       archived report using patientId and eventId</li>
     *   <li><strong>New Report:</strong> When "exist" extra is false or missing,
     *       displays current anamnesis from static reference</li>
     * </ul>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                          previously being shut down, this Bundle contains the data
     *                          it most recently supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        title1 = findViewById(R.id.title1);
        finalAnamnesis = findViewById(R.id.finalAnamnesis);

        Intent intent = getIntent();
        boolean exist = intent.getBooleanExtra("exist", false);

        if (exist) {
            // Load existing report from Firebase
            String id = intent.getStringExtra("patientId");
            String eventId = intent.getStringExtra("eventId");
            refClosed.child(id).child(eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DataSnapshot> tsk) {
                    if (tsk.isSuccessful()) {
                        Anamnesis anamn = tsk.getResult().getValue(Anamnesis.class);
                        showAnamnesis(anamn);
                    } else {
                        Log.e("firebase", "Error getting data", tsk.getException());
                    }
                }
            });
        } else {
            // Display current anamnesis from memory
            showAnamnesis(anamnesis);
        }
    }

    /**
     * Displays a comprehensive formatted anamnesis report with associated medical image.
     *
     * <p>This method performs the complete report generation process:</p>
     * <ul>
     *   <li>Formats patient information with gender-appropriate Hebrew text</li>
     *   <li>Structures medical data into readable report format</li>
     *   <li>Populates TextView with comprehensive medical documentation</li>
     *   <li>Retrieves and displays associated medical images from Firestore</li>
     *   <li>Handles image loading errors gracefully with user feedback</li>
     * </ul>
     *
     * <p>The report format includes:</p>
     * <ul>
     *   <li><strong>Personal Details:</strong> Full name, ID, phone, age</li>
     *   <li><strong>Medical Background:</strong> Diseases, allergies, medications</li>
     *   <li><strong>Incident Details:</strong> Location, condition, consciousness level</li>
     *   <li><strong>Informant Information:</strong> Witness details and statements</li>
     *   <li><strong>Medical Examinations:</strong> Vital signs and physical findings</li>
     *   <li><strong>Hospital Transfer:</strong> Destination, department, receiving staff</li>
     *   <li><strong>Detailed Metrics:</strong> Comprehensive vital signs breakdown</li>
     * </ul>
     *
     * <p>Gender-specific formatting ensures appropriate Hebrew grammar and
     * medical terminology for both male and female patients.</p>
     *
     * @param anamne The Anamnesis object containing all medical data to be displayed
     *
     * @throws NullPointerException if anamne or its nested objects are null
     *
     * @see #loadAssociatedImage(Anamnesis) for image retrieval details
     */
    private void showAnamnesis(Anamnesis anamne) {
        Toast.makeText(this, "" + anamne.getDetails().getFullName(), Toast.LENGTH_LONG).show();

        if (anamne.getDetails().getGender().equals("Male")) {
            // Format report for male patients with appropriate Hebrew grammar
            finalAnamnesis.setText("שם מלא: " + anamne.getDetails().getFullName() + "\n" +
                    "תעודת זהות: " + anamne.getDetails().getId() + "\n" +
                    "מספר טלפון: " + anamne.getDetails().getPhoneNumber() + "\n" +
                    "בן כ" + anamne.getDetails().getAge() + ", " + anamne.getBackground().getDiseases() + ", " + anamne.getBackground().getAllergies() + ", " + anamne.getBackground().getMedications() + "." + "\n" +
                    "בהגיענו ל" + anamne.getArrival().getLocation() + ", נמצא המטופל " + anamne.getArrival().getCondition() + ", ב" + anamne.getArrival().getConsciousness() + "." + "\n" +
                    "לדברי " + anamne.getArrival().getInformantName() + ", " + anamne.getArrival().getInformantDetails() + "." + "\n" +
                    "בבדיקה, " + anamne.getFinal_examinations().getRespirations() + ", " + anamne.getFinal_examinations().getBloodPressure() + ", " + anamne.getFinal_examinations().getPulse() + ", " + anamne.getFinal_examinations().getOxygenSaturation() + ", " + anamne.getFinal_examinations().getPupilEquality() + "." + "\n" +
                    "פונה לבית החולים " + anamne.getHospital().getName() + ", למחלקה " + anamne.getHospital().getDepartment() + ". איש צוות רפואי מקבל- " + anamne.getHospital().getStaff() + "." + "\n" +
                    "פירוט מדדים-" + "\n" +
                    "שיוויון אישונים: " + anamne.getMedical_tests().getPupilEquality() + "\n" +
                    "דופק: " + anamne.getMedical_tests().getPulse() + "\n" +
                    "לחץ דם: " + anamne.getMedical_tests().getBloodPressure() + "\n" +
                    "קצב נשימה: " + anamne.getMedical_tests().getRespirations() + "\n" +
                    "אחוזי סיטורציה: " + anamne.getMedical_tests().getOxygenSaturation());
        } else {
            // Format report for female patients with appropriate Hebrew grammar
            finalAnamnesis.setText("שם מלא: " + anamne.getDetails().getFullName() + "\n" +
                    "תעודת זהות: " + anamne.getDetails().getId() + "\n" +
                    "מספר טלפון: " + anamne.getDetails().getPhoneNumber() + "\n" +
                    "בת כ" + anamne.getDetails().getAge() + ", " + anamne.getBackground().getDiseases() + ", " + anamne.getBackground().getAllergies() + ", " + anamne.getBackground().getMedications() + "." + "\n" +
                    "בהגיענו ל" + anamne.getArrival().getLocation() + ", נמצאה המטופלת " + anamne.getArrival().getCondition() + ", ב" + anamne.getArrival().getConsciousness() + "." + "\n" +
                    "לדברי " + anamne.getArrival().getInformantName() + ", " + anamne.getArrival().getInformantDetails() + "." + "\n" +
                    "בבדיקה, " + anamne.getFinal_examinations().getRespirations() + ", " + anamne.getFinal_examinations().getBloodPressure() + ", " + anamne.getFinal_examinations().getPulse() + ", " + anamne.getFinal_examinations().getOxygenSaturation() + ", " + anamne.getFinal_examinations().getPupilEquality() + "." + "\n" +
                    "פונתה לבית החולים " + anamne.getHospital().getName() + ", למחלקה " + anamne.getHospital().getDepartment() + ". איש צוות רפואי מקבל- " + anamne.getHospital().getStaff() + "." + "\n" +
                    "פירוט מדדים-" + "\n" +
                    "שיוויון אישונים: " + anamne.getMedical_tests().getPupilEquality() + "\n" +
                    "דופק: " + anamne.getMedical_tests().getPulse() + "\n" +
                    "לחץ דם: " + anamne.getMedical_tests().getBloodPressure() + "\n" +
                    "קצב נשימה: " + anamne.getMedical_tests().getRespirations() + "\n" +
                    "אחוזי סיטורציה: " + anamne.getMedical_tests().getOxygenSaturation());
        }

        // Load and display associated medical image
        loadAssociatedImage(anamne);
    }

    /**
     * Retrieves and displays the medical image associated with the anamnesis report.
     *
     * <p>This method handles the complete image retrieval process from Firestore:</p>
     * <ul>
     *   <li>Constructs unique image document ID from patient ID and event key</li>
     *   <li>Queries Firestore "images" collection for the associated image</li>
     *   <li>Converts Firebase Blob data to displayable Bitmap</li>
     *   <li>Updates ImageView with retrieved medical image</li>
     *   <li>Provides comprehensive error handling with Hebrew user feedback</li>
     * </ul>
     *
     * <p>Error scenarios handled:</p>
     * <ul>
     *   <li><strong>Document not found:</strong> Displays "לא נמצאה תמונה במסמך"</li>
     *   <li><strong>Empty image field:</strong> Displays "שדה התמונה ריק"</li>
     *   <li><strong>Network/Firestore errors:</strong> Displays "שגיאה בטעינת תמונה"</li>
     * </ul>
     *
     * <p>The image loading process is asynchronous and non-blocking,
     * allowing the report text to display immediately while the image loads.</p>
     *
     * @param anamne The Anamnesis object containing patient and event identifiers
     *               used to construct the image document ID
     *
     * @implNote Uses Firebase Firestore SDK for cloud image retrieval
     * @implNote Image data is stored as Blob in Firestore documents
     *
     * @see FirebaseFirestore#getInstance() for Firestore instance creation
     * @see Blob#toBytes() for binary data extraction
     * @see BitmapFactory#decodeByteArray(byte[], int, int) for image decoding
     */
    private void loadAssociatedImage(Anamnesis anamne) {
        imageViewReport = findViewById(R.id.imageViewReport);
        String imageDocId = anamne.getDetails().getId() + anamne.getKeyID();

        FirebaseFirestore.getInstance()
                .collection("images") // Access images collection in Firestore
                .document(imageDocId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("imageData")) {
                        Blob blob = documentSnapshot.getBlob("imageData");
                        if (blob != null) {
                            byte[] imageBytes = blob.toBytes();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            imageViewReport.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(this, "שדה התמונה ריק", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "לא נמצאה תמונה במסמך", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "שגיאה בטעינתתמונה", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Image load failed", e);
                });
    }
}