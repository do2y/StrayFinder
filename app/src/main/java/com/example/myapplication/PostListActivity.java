package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostListActivity extends AppCompatActivity {

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


        loadPostsFromFirestore();
    }

    private void loadPostsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pets")
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
                        Log.w("PostList", "Error getting documents.", task.getException());
                    }
                });
    }
}
