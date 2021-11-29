package com.example.userapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.userapplication.Classes.UserApp;

public class EditProfileActivity extends AppCompatActivity {

    UserApp loggedIn;
    Button btnUpdate;
    EditText profileName, profileEmail, profilePhone, confPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btnUpdate = findViewById(R.id.btn_updateprofile);
        profileName = findViewById(R.id.ed_profilename);
        profileEmail = findViewById(R.id.ed_profileemail);
        profilePhone = findViewById(R.id.ed_profilephone);
        confPass = findViewById(R.id.ed_confirmPassword);


        Intent par = getIntent();
        if(par.hasExtra("loggedIn")){
            loggedIn = par.getParcelableExtra("loggedIn");
            profileName.setText(loggedIn.getName());
            profileEmail.setText(loggedIn.getEmail());
            profilePhone.setText(loggedIn.getTelp());
        }
        btnUpdate.setOnClickListener(this::doUpdate);
    }


    private void doUpdate(View view){
        if(profileName.getText().length()>0 && profileEmail.getText().length()>0 && profilePhone.getText().length()>0 && confPass.getText().length()>0){
            if(loggedIn.getPassword().equals(confPass.getText().toString())){
                //do Update
                loggedIn.setName(profileName.getText().toString());
                loggedIn.setEmail(profileEmail.getText().toString());
                loggedIn.setTelp(profilePhone.getText().toString());

                Intent i = new Intent();
                i.putExtra("loggedIn",loggedIn);
                setResult(110, i);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Password mismatch!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Empty field!", Toast.LENGTH_SHORT).show();
        }
    }
}