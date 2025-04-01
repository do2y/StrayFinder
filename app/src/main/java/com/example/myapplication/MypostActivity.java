package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MypostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Post_Adapter postAdapter;
    private List<Pet> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new Post_Adapter(this, postList);
        recyclerView.setAdapter(postAdapter);

        loadMyPostsFromFirestore();
    }

    private void loadMyPostsFromFirestore() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("pets")
                    .whereEqualTo("userId", userId) // Query only posts created by the current user
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    Pet post = document.toObject(Pet.class);
                                    if (post != null) {
                                        postList.add(post);
                                    }
                                }
                                postAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("Mypost", "Error getting documents.", task.getException());
                        }
                    });
        }
    }
}
