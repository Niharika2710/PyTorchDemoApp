package org.pytorch.demo.vision.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.pytorch.demo.R;

import java.util.ArrayList;
import java.util.List;


public class CovidSafeActivity extends Activity {
    private EditText editTextName;
    private ListView textFromFirestore;
    private FirebaseFirestore db;
    private List<Object> namesList;
    private ArrayAdapter adapter;
    private ArrayList<String> slist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_safe);

        editTextName = findViewById(R.id.editText_enter_name);
        textFromFirestore = findViewById(R.id.list_from_Firestore);

        // Initializing firestore object
        db = FirebaseFirestore.getInstance();

        //Adding listener to the save button
        findViewById(R.id.button_save_to_Firestore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();

                //Setting reference to collection in Firestore
                CollectionReference dbNames = db.collection("Name Directory");

                NameDirectory nameDirectory = new NameDirectory(name);
                dbNames.add(nameDirectory)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(CovidSafeActivity.this, "Name Added!", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CovidSafeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


        // To read the data from Firestore
        findViewById(R.id.button_to_retrieve_from_Firestore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namesList = new ArrayList<>();
                adapter = new ArrayAdapter<String>(CovidSafeActivity.this, android.R.layout.simple_list_item_1, slist);

                db.collection("Name Directory").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list){
                                NameDirectory name = d.toObject(NameDirectory.class);
                                namesList.add(name);
                                slist.add(name.getName());
                                adapter.notifyDataSetChanged();
                            }
                            textFromFirestore.setAdapter(adapter);
                        }
                    }
                });
            }
        });

    }

}
