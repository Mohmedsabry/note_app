package com.example.takenote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.takenote.Database.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Adptar adptar;
    Database database;
    Button all,Work,Life,Family,Entermant;
    FloatingActionButton btn;
    ArrayList<Note>arrayList;
    public static String IntentValue="Note";
    SearchView searchView;
    Menu menuMain;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);



        all = findViewById(R.id.btn_home);
        Work = findViewById(R.id.mod_btn_work);
        Entermant = findViewById(R.id.mod_btn_entermant);
        Family = findViewById(R.id.mod_btn_family);
        Life = findViewById(R.id.mod_btn_life);
        btn = findViewById(R.id.ft_btn);


        database = new Database(this);
        arrayList= new ArrayList<>();
        arrayList = database.getAll(Login.USERNAME);

        all.setOnClickListener(view -> {
            arrayList = database.getAll(Login.USERNAME);
            adptar.setArrayList(arrayList);
        });
        Work.setOnClickListener(view -> {
            arrayList = database.getType("work",Login.USERNAME);
            adptar.setArrayList(arrayList);
        });
        Life.setOnClickListener(view -> {
            arrayList=database.getType("life",Login.USERNAME);
            adptar.setArrayList(arrayList);
        });
        Entermant.setOnClickListener(view -> {
            arrayList=database.getType("entermant",Login.USERNAME);
            adptar.setArrayList(arrayList);
        });
        Family.setOnClickListener(x->{
            arrayList=database.getType("family",Login.USERNAME);
            adptar.setArrayList(arrayList);
        });



        adptar=new Adptar(arrayList, new Adptar.Listener() {
            @Override
            public void OnObjectClick(int id) {
                Note n=new Note();
                n=database.SearchById(id,Login.USERNAME);
                Log.d("id",id+"");
                Log.d("note",n.toString());
                Intent intent = new Intent(getBaseContext(), EditNote.class);
                intent.putExtra(IntentValue,n);
                startActivityForResult(intent,5);
            }
        });

        NotesShow n= NotesShow.newInstance(adptar);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.FrameL,n);
        ft.commit();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getBaseContext(),AddNote.class),2);
            }
        });
        System.out.println(arrayList.size()+" new ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        arrayList=database.getAll(Login.USERNAME);
        adptar.setArrayList(arrayList);
        System.out.println(arrayList.size()+" it is back");
        for (int i=0;i<arrayList.size();i++){
            System.out.println(i+" "+arrayList.get(i).toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view,menu);
        final Switch sw = (Switch) menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switch2);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println(b);
                if (b){
                    startService(new Intent(getBaseContext(), MyService.class));
                }else {
                    stopService(new Intent(getBaseContext(), MyService.class));
                }
            }
        });

        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                arrayList=database.SearchByTitle(s,Login.USERNAME);
                adptar.setArrayList(arrayList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayList = database.SearchByTitle(s,Login.USERNAME);
                adptar.setArrayList(arrayList);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                arrayList=database.getAll(Login.USERNAME);
                adptar.setArrayList(arrayList);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("on destroy");
        stopService(new Intent(getBaseContext(), MyService.class));
    }
}