package com.example.mda.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView username_label, password_label, tv_title;
    EditText et_username, et_password;
    Button login;
    FirebaseAuth mAuth;
    AlertDialog.Builder adb;
    Intent intent;
    Context context= this;
    String manager="no";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_username= findViewById(R.id.et_username);
        et_password= findViewById(R.id.et_password);
        login= findViewById(R.id.login);
        adb=new AlertDialog.Builder(this);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = et_username.getText().toString();
                password = et_password.getText().toString();
                if(email.equals("shimon@gmail.com")){
                    manager="yes";
                }
                if (email.isEmpty() || password.isEmpty()) {
                    adb.setMessage("Incorrect email or password");
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    adb.create().show();
                    return; // מוסיף יציאה מהפעולה כדי שלא ימשיכו לנסות התחברות עם ערכים ריקים
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // המשתמש מחובר
                                    Log.d("Login", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if(manager=="yes"){
                                        Intent intent = new Intent(context, Manager.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(context, Patient_details.class);
                                        startActivity(intent);
                                    }
                                }

                                else {
                                    // המשתמש לא מחובר
                                    Log.w("Login", "signInWithEmail:failure", task.getException());
                                    adb.setMessage("Incorrect email or password");
                                    adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    adb.create().show();
                                }
                            }
                        });

            }
        });

    }
}