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

/**
 * Camera Activity for capturing and managing patient medical images.
 *
 * <p>This activity provides functionality to:</p>
 * <ul>
 *   <li>Capture photos using device camera</li>
 *   <li>Select images from device gallery</li>
 *   <li>Compress and upload images to Firebase Firestore</li>
 *   <li>Display captured/selected images in preview</li>
 * </ul>
 *
 * <p>The activity handles necessary permissions for camera and storage access,
 * and integrates with Firebase for cloud storage of medical documentation.</p>
 *
 * @author Medical Documentation App Team
 * @version 1.0
 * @since API Level 21
 */
public class Camera extends AppCompatActivity {

    /** ImageView component for displaying captured or selected images */
    ImageView imageView;

    /** Button for triggering camera capture functionality */
    Button btn_takepic;

    /** Button for navigating to the next screen in the workflow */
    Button nextbtn;

    /** Button for accessing device gallery to select existing images */
    Button gallery;

    /** Request code for camera permission requests */
    private static final int REQUEST_CAMERA_PERMISSION = 101;

    /** Request code for external storage read permission requests */
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 102;

    /** Request code for image picker intent results */
    private static final int REQUEST_PICK_IMAGE = 301;

    /**
     * Activity result launcher for handling camera capture results.
     *
     * <p>This launcher processes the result from camera capture intents,
     * extracts the bitmap from the result bundle, displays it in the ImageView,
     * and initiates the upload process to Firebase.</p>
     */
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            imageView.setImageBitmap(bitmap);
                            addImage(bitmap, anamnesis.getDetails().getId() + anamnesis.getKeyID());
                        }
                    }
            );

    /**
     * Initializes the activity and sets up the user interface.
     *
     * <p>This method:</p>
     * <ul>
     *   <li>Enables edge-to-edge display</li>
     *   <li>Sets the activity layout</li>
     *   <li>Configures window insets for proper display</li>
     *   <li>Initializes UI components</li>
     *   <li>Sets up click listeners for camera functionality</li>
     * </ul>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                          previously being shut down, this Bundle contains the data
     *                          it most recently supplied in onSaveInstanceState(Bundle)
     */
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
        imageView = findViewById(R.id.imageView);
        btn_takepic = findViewById(R.id.btn_takepic);
        nextbtn = findViewById(R.id.nextbtn);
        gallery = findViewById(R.id.gallery);

        btn_takepic.setOnClickListener(v -> {
            Intent takePicIntent = new Intent();
            takePicIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            activityResultLauncher.launch(takePicIntent);
        });
    }

    /**
     * Called when the activity will start interacting with the user.
     *
     * <p>This method checks for camera permissions and requests them if not already granted.
     * This ensures the app has necessary permissions before attempting to use camera functionality.</p>
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    /**
     * Handles the result of permission requests.
     *
     * <p>This method processes the user's response to permission requests for camera
     * and external storage access. It displays appropriate toast messages when
     * permissions are denied to inform the user of the consequences.</p>
     *
     * @param requestCode  The request code passed to requestPermissions()
     * @param permissions  The requested permissions (never null)
     * @param grantResults The grant results for the corresponding permissions
     *                    which are either PERMISSION_GRANTED or PERMISSION_DENIED (never null)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Initiates the gallery selection process for choosing existing images.
     *
     * <p>This method creates an intent to open the device's image gallery,
     * allowing users to select existing photos instead of capturing new ones.
     * The selected image will be processed in the onActivityResult method.</p>
     *
     * @param view The view that triggered this method (typically the gallery button)
     */
    public void gallery(View view) {
        Intent si = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(si, REQUEST_PICK_IMAGE);
    }

    /**
     * Handles results from activities started for result.
     *
     * <p>This method specifically processes results from the gallery image picker.
     * When an image is successfully selected, it:</p>
     * <ul>
     *   <li>Converts the selected image URI to a Bitmap</li>
     *   <li>Displays the image in the ImageView</li>
     *   <li>Initiates the upload process to Firebase</li>
     *   <li>Handles any errors during image processing</li>
     * </ul>
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult()
     * @param resultCode  The integer result code returned by the child activity
     * @param data        An Intent that carries the result data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                imageView.setImageBitmap(bitmap);
                addImage(bitmap, anamnesis.getDetails().getId() + anamnesis.getKeyID());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "שגיאה בטעינת התמונה מהגלריה", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Processes and uploads an image to Firebase Firestore.
     *
     * <p>This method handles the complete image upload workflow:</p>
     * <ul>
     *   <li>Converts the bitmap to a byte array with JPEG compression</li>
     *   <li>Checks image size and compresses further if necessary (max 1MB)</li>
     *   <li>Creates a Firebase Blob from the image data</li>
     *   <li>Uploads the image with metadata to Firestore</li>
     *   <li>Provides user feedback through progress dialogs and logging</li>
     * </ul>
     *
     * <p>The method implements automatic image compression to ensure uploaded
     * images don't exceed the 1MB size limit, gradually reducing quality
     * until the size requirement is met.</p>
     *
     * @param image The Bitmap image to be uploaded
     * @param name  The unique identifier/name for the image in Firebase
     */
    public void addImage(Bitmap image, String name) {
        ProgressDialog pd;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        Toast.makeText(this, "" + imageBytes.length, Toast.LENGTH_SHORT).show();

        if (imageBytes.length > 1040000) {
            pd = ProgressDialog.show(this, "Compress", "Image size is too large\nCompressing...", true);
            int qual = 100;
            // Gradual quality degradation until size requirement is met
            while (imageBytes.length > 1040000) {
                qual -= 5;
                baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, qual, baos);
                imageBytes = baos.toByteArray();
            }
            pd.dismiss();
        }

        Blob blob = Blob.fromBytes(imageBytes);
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("imageName", name);
        imageMap.put("imageData", blob);

        pd = ProgressDialog.show(this, "Upload image", "Uploading...", true);
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

    /**
     * Navigates to the Report activity.
     *
     * <p>This method handles the transition to the next step in the medical
     * documentation workflow, moving from image capture to report generation.</p>
     *
     * @param view The view that triggered this navigation (typically the next button)
     */
    public void next11(View view) {
        Intent intent = new Intent(this, Report.class);
        startActivity(intent);
    }
}