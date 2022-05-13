package com.example.bcawhatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bcawhatsapp.Adapters.ChatAdapter;
import com.example.bcawhatsapp.Models.MessageModel;
import com.example.bcawhatsapp.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {                   //functioning of back arrow
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
//                ChatDetailActivity.this.finish();                                //to finish activity**********************
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatAdapter adapter = new ChatAdapter(messageModels, this);
        binding.chatRecylerView.setAdapter(adapter);

        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.userName.setText("FRIENDS GROUP");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecylerView.setLayoutManager(layoutManager);

        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();                           //use to clear list i.e send message once
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                    messageModels.add(model);
                    Log.d("TAG", "Value is: " + model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {                     //getting data to set on realtime database later
            @Override
            public void onClick(View view) {

                if (binding.etMessage.getText().toString().isEmpty()){           //if user doesn't type anything then it will not sended***
                    return;
                }

                String message = binding.etMessage.getText().toString();
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimestamp(new Date().getTime());                   //storing timestamp to firebase database
                binding.etMessage.setText("");                           //setting text null after sending a message *********88

                //setting data to our realtime database***********************************************************************************************************
                database.getReference().child("Group Chat").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {}
                });
                //*************************************************************************************************************************************************
            }
        });
    }
}