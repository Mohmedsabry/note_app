package com.example.takenote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.takenote.Database.User;
import com.example.takenote.Database.UserDB;
import com.example.takenote.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {
    ActivityRegisterBinding binding;
    UserDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new UserDB(this);
        binding.RegisterAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=binding.RegisterUsername.getText().toString();
                String password=binding.RegisterPassword.getText().toString();
                if (!username.equals("")&&!password.equals("")){
                    User user = new User(username,password);
                    System.out.println(db.insert(user));
                    finish();
                }else {
                    Toast.makeText(Register.this, "don't let empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}