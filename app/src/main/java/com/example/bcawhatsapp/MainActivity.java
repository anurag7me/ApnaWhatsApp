package com.example.bcawhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.bcawhatsapp.Adapters.FragmentsAdapter;
import com.example.bcawhatsapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    ActionBar actionBar;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //actionbar color change********************************************************************
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#2f54ab"));
        actionBar.setBackgroundDrawable(colorDrawable);
        //******************************************************************************************

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        //setting fragments into the main page******************************************************
        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.viewPager);
        //******************************************************************************************
    }

    //creates & show only options in main page for settings,logout & different stuffs **************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //**********************************************************************************************

    //working of options above**********************************************************************
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                auth.signOut();
                Intent intent1=new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent1);
                break;
            case R.id.groupChat:
                Intent intent2=new Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(intent2);
                break;
        }
        return true;
    }
    //**********************************************************************************************
}