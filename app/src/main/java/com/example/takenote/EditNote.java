package com.example.takenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.example.takenote.Database.Database;
import com.example.takenote.databinding.ActivityEditNoteBinding;

public class EditNote extends AppCompatActivity {
    Intent intent;
    Database database;
    ActivityEditNoteBinding binding;
    Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        intent =getIntent();
        note=new Note();
        database = new Database(this);
        note= (Note) intent.getSerializableExtra(MainActivity.IntentValue);
        if (note!=null){
            binding.modEdTitle.setText(note.getTitle());
            binding.modMulty.setText(note.getDecscrption());
            binding.modResult.setText(note.getType());
        }

        binding.modBtnWork.setOnClickListener(view -> {
            note.setType("work") ;
            binding.modResult.setText("work");
        });
        binding.modBtnLife.setOnClickListener(view -> {
             note.setType("life");
            binding.modResult.setText("life");
        });
        binding.modBtnEntermant.setOnClickListener(view -> {
            note.setType("entermant");
            binding.modResult.setText("entermant");
        });
        binding.modBtnFamily.setOnClickListener(view -> {
            note.setType("family");
            binding.modResult.setText("family");
        });

        binding.modToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.menu_delete){
                    System.out.println(database.DeleteById(note.getId()));
                    doNotification(note.getTitle(),"delete");
                    finish();
                } else if (item.getItemId()==R.id.modifiy) {
                    note.setTitle(binding.modEdTitle.getText().toString());
                    note.setDecscrption(binding.modMulty.getText().toString());
                    System.out.println(database.ModifyById(note));
                    doNotification(note.getTitle(),"update");
                    finish();
                }
                return false;
            }
        });
        binding.modToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
    @SuppressLint("MissingPermission")
    public  void doNotification(String name,String type){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel ch = new NotificationChannel("Notify","configure change", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(ch);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(),"Notify");
        builder.setSmallIcon(R.drawable.baseline_notifications_24).setContentTitle(type).setContentText("this task "+name+" is "+type+"ed").setPriority(NotificationCompat.PRIORITY_HIGH)
        ;
        builder.setTimeoutAfter(7000);
        NotificationManagerCompat compat = NotificationManagerCompat.from(this);
        compat.notify(1,builder.build());
    }
}