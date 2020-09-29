package com.example.testquesthub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.testquesthub.Models.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.example.testquesthub.Models.Quest;

import java.util.EventListener;

public class TabActivity extends AppCompatActivity {

    FloatingActionButton add_quest;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference quests;
    FirebaseUser user = auth.getInstance().getCurrentUser();
    FirebaseListAdapter mAdapter;

    RelativeLayout root;
    ListView ListUserTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);


        add_quest = findViewById(R.id.addQuest);

        root = findViewById(R.id.root_element);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        quests = db.getReference("Quests");
        //db.getReference().child("Quests").observeSingleEvent
        ValueEventListener questListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Quest quest = snapshot.getValue(Quest.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        quests.addValueEventListener(questListener);


        add_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewQuest();
            }
        });
    }

    private void createNewQuest() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Создать новый квест");
        dialog.setMessage("Введите все данные для создания квеста");

        LayoutInflater inflater = LayoutInflater.from(this);
        View new_record_create = inflater.inflate(R.layout.new_record_create, null);
        dialog.setView(new_record_create);

        final MaterialEditText address = new_record_create.findViewById(R.id.addressField);
        final MaterialEditText info = new_record_create.findViewById(R.id.infoField);
        final MaterialEditText codeword = new_record_create.findViewById(R.id.codewordField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(address.getText().toString())){
                    Snackbar.make(root, "Введите адресс", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(codeword.getText().toString())){
                    Snackbar.make(root, "Введите кодовое слово", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //quests.setValue(quest);
                ValueEventListener val = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final Quest quest = new Quest();
                        quest.setAd(address.getText().toString());
                        quest.setInfo(info.getText().toString());
                        quest.setCodeword(codeword.getText().toString());

                        quests.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(quest)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(root, "Квест добавлен", Snackbar.LENGTH_SHORT).show();
                                    }
                                });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                quests.addValueEventListener(val);
            }
        });

        dialog.show();
    }

    private void openOSM(){
        Intent intent = new Intent(this, com.example.testquesthub.ui.osm.OSM.class);
        startActivity(intent);
    }
}
