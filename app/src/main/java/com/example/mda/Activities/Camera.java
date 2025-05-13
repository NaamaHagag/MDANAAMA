package com.example.mda.Activities;

import static com.example.mda.Activities.Patient_details.anamnesis;
import static com.example.mda.FBRef.refImageStamp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.CollectionReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Camera extends AppCompatActivity {
    ImageView imageView;
    Button btn_takepic, nextbtn, gallery;
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 102;
    private static final int REQUEST_PICK_IMAGE = 301;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageView=findViewById(R.id.imageView);
        btn_takepic=findViewById(R.id.btn_takepic);
        nextbtn=findViewById(R.id.nextbtn);
        gallery=findViewById(R.id.gallery);

        btn_takepic.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("source","camera");
            activityResultLauncher.launch(intent);
        });
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//        }
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
//        }
//    }
//
//    /**
//     * onRequestPermissionsResult method
//     * <p> Method triggered by other activities returning result of permissions request
//     * </p>
//     *
//     * @param requestCode the request code triggered the activity
//     * @param permissions the array of permissions granted
//     * @param grantResults the array of permissions granted
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CAMERA_PERMISSION) {
//            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION) {
//            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    ActivityResultLauncher<Intent> activityResultLauncher= registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String source=result.getData().getStringExtra("source");
                    Toast.makeText(this, source, Toast.LENGTH_LONG).show();
                    Log.i("Camera","the source is: "+source);
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imageView.setImageBitmap(bitmap);
                    addImage(bitmap, anamnesis.getDetails().getId()+anamnesis.getKeyID());
                }
            }
    );

    public void gallery(View view) {
        Intent si = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(si, REQUEST_PICK_IMAGE);
    }

    public void addImage(Bitmap image, String name) {
        ProgressDialog pd;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imageBytes = baos.toByteArray();
        Toast.makeText(this, ""+imageBytes.length, Toast.LENGTH_SHORT).show();
        if (imageBytes.length > 1040000) {
            pd = ProgressDialog.show(this, "Compress", "Image size is too large\nCompressing...", true);
            int qual = 100;
            // Degradation
            while (imageBytes.length > 1040000) {
                qual -= 5;
                baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, qual, baos);
                imageBytes = baos.toByteArray();
            }
            // Resize
//        while (imageBytes.length > 1048576) {
//            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, (int) (imageBitmap.getWidth() * 0.9), (int) (imageBitmap.getHeight() * 0.9), true);
//            baos = new ByteArrayOutputStream();
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            imageBytes = baos.toByteArray();
//        }
            pd.dismiss();
        }

        Blob blob = Blob.fromBytes(imageBytes);
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("imageName", name);
        imageMap.put("imageData", blob);

        pd=ProgressDialog.show(this,"Upload image","Uploading...",true);
        refImageStamp.document(name).set(imageMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Camera", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Camera", "Error writing document", e);
                    }
                });
        pd.dismiss();
    }
    public void nextbtn(View view){
        Intent intent=new Intent(this, Report.class);
        startActivity(intent);

    }
}