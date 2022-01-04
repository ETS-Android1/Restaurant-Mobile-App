package com.example.restaurant_mobile_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddInquiry extends AppCompatActivity {
    private EditText name, email, subject, content;
    private Button add;
    private DatabaseHandler dbHandler;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inquiry);
        setTitle("Add Inquiry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.add_name);
        email = findViewById(R.id.add_email);
        subject = findViewById(R.id.add_subject);
        content = findViewById(R.id.add_content);
        add = findViewById(R.id.add_button);
        context = this;

        dbHandler = new DatabaseHandler(context);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText())){
                    name.setError("Name Can't be Empty !");
                    name.requestFocus();
                }
                else if (TextUtils.isEmpty(email.getText())){
                    email.setError("Email Can't be Empty !");
                    email.requestFocus();
                }


                else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){

                    email.setError("Invalid Email Address !");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(subject.getText())){


                    subject.setError("Subject Can't be Empty !");
                    subject.requestFocus();
                }
                else if (TextUtils.isEmpty(content.getText())){
                    content.setError("Content Can't be Empty !");
                    content.requestFocus();
                }
                else{
                    String userName = name.getText().toString();
                    String userEmail = email.getText().toString();
                    String userSubject = subject.getText().toString();
                    String userContent = content.getText().toString();

                    Inquiry inquiry = new Inquiry(userName, userEmail, userSubject, userContent);
                    Toast.makeText(getApplicationContext(), "Inquiry added successfully", Toast.LENGTH_LONG).show();
                    dbHandler.addInquiry(inquiry);

                    startActivity(new Intent(context, AllInquiry.class));
                }
            }
        });

    }
}