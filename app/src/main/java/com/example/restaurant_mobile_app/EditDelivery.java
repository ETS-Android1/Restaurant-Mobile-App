package com.example.restaurant_mobile_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDelivery extends AppCompatActivity {

    EditText name,phone,address1,address2,address3,email;
    Button btnUpdate;
    Delivery delivery;
    DatabaseReference dbRef;
    long maxID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delivery);
        setTitle("Edit Delivery Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name=findViewById(R.id.edtxt_name2);
        phone=findViewById(R.id.edtxt_phone2);
        address1=findViewById(R.id.edtxt_address_1);
        address2=findViewById(R.id.edtxt_address_2);
        address3=findViewById(R.id.edtxt_address3_3);
        email=findViewById(R.id.edtxt_email2);

        btnUpdate=findViewById(R.id.btn_editdelivery);

        delivery=new Delivery();

        final String id = getIntent().getStringExtra("id");

        DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("Delivery").child(id);
        readRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChildren()){
                    name.setText(snapshot.child("name").getValue().toString());
                    phone.setText(snapshot.child("phone").getValue().toString());
                    address1.setText(snapshot.child("address1").getValue().toString());
                    address2.setText(snapshot.child("address2").getValue().toString());
                    address3.setText(snapshot.child("address3").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                }
                else
                    Toast.makeText(getApplicationContext(),"No sourse to Display",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        dbRef= FirebaseDatabase.getInstance().getReference().child("Delivery");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    maxID=(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference upRef= FirebaseDatabase.getInstance().getReference().child("Delivery");
                upRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(id)) {
                            try {
                                if (TextUtils.isEmpty(name.getText().toString())){
                                    name.setError("Name Can't be Empty !");
                                    name.requestFocus();
                                }
                                else if(TextUtils.isEmpty(phone.getText().toString()))
                                {
                                    phone.setError("Phone Can't be Empty !");
                                    phone.requestFocus();
                                }
                                else if (TextUtils.getTrimmedLength(phone.getText()) != 10){
                                    phone.setError("Mobile number should have 10 digits !");
                                    phone.requestFocus();
                                }
                                else if(TextUtils.isEmpty(address1.getText().toString()))
                                {
                                    address1.setError("Address Can't be Empty !");
                                    address1.requestFocus();
                                }
                                else if(TextUtils.isEmpty(email.getText().toString()))
                                {
                                    email.setError("Email Can't be Empty !");
                                    email.requestFocus();
                                }

                                else {
                                    delivery.setName(name.getText().toString().trim());
                                    delivery.setPhone(Long.parseLong(phone.getText().toString().trim()));
                                    delivery.setAddress1(address1.getText().toString().trim());
                                    delivery.setAddress2(address2.getText().toString().trim());
                                    delivery.setAddress3(address3.getText().toString().trim());
                                    delivery.setEmail(email.getText().toString().trim());

                                    dbRef = FirebaseDatabase.getInstance().getReference().child("Delivery").child(id);
                                    dbRef.setValue(delivery);
                                    Toast.makeText(getApplicationContext(), "SuccessFully Update", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EditDelivery.this, ViewOrder.class);
                                    intent.putExtra("id",id);
                                    startActivity(intent);

                                }
                            }

                            catch (NumberFormatException e) {
                                Toast.makeText(getApplicationContext(), "Invalide No", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),"No sourse to Update",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

}