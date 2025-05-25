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

public class Report extends AppCompatActivity {
    TextView title1, finalAnamnesis;
    ImageView imageViewReport;

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
            showAnamnesis(anamnesis);
        }
    }

    private void showAnamnesis(Anamnesis anamne) {
        Toast.makeText(this, "" + anamne.getDetails().getFullName(), Toast.LENGTH_LONG).show();
        if (anamne.getDetails().getGender().equals("Male")) {
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

        imageViewReport = findViewById(R.id.imageViewReport);
        String imageDocId = anamne.getDetails().getId() + anamne.getKeyID();

        FirebaseFirestore.getInstance()
                .collection("images") // ← כאן התיקון
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
                    Toast.makeText(this, "שגיאה בטעינת תמונה", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Image load failed", e);
                });
    }
}
