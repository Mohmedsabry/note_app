package com.example.takenote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.takenote.Database.User;
import com.example.takenote.Database.UserDB;
import com.example.takenote.databinding.ActivityLoginBinding;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    ArrayList<User>arrayList;
    UserDB db;
    public static String USERNAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//R.l.login

        arrayList= new ArrayList<>();
        db=new UserDB(this);

        binding.LoginLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=binding.LoginUsername.getText().toString();
                String password=binding.LoginPassword.getText().toString();
                if (!username.equals("")&&!password.equals("")){
                    arrayList=db.getAll();
                    for (User i:arrayList){
                        if (i.getUsername().equals(username)&&i.getPassword().equals(password)){
                            System.out.println("ok");
                            USERNAME=username;
                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                            break;
                        }
                    }
                }else {
                    Toast.makeText(Login.this, "don't let empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.LoginReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),Register.class));
            }
        });
    }
}