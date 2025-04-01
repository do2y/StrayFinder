package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Post_Adapter extends RecyclerView.Adapter<Post_Adapter.PostViewHolder> {

    private List<Pet> postList;
    private Context context;

    public Post_Adapter(Context context, List<Pet> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Pet post = postList.get(position);
        holder.title.setText(post.getTitle());
        holder.animalType.setText(post.getAnimalType());
        holder.feature.setText(post.getFeature());
        holder.reportType.setText(post.getReportType());
        holder.reportType.setVisibility(View.VISIBLE);

        // 이미지 URI를 사용하여 이미지 설정
        Glide.with(context).load(post.getImageUrl()).into(holder.imagePost);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("title", post.getTitle());
            intent.putExtra("animalType", post.getAnimalType());
            intent.putExtra("name", post.getName());
            intent.putExtra("gender", post.getGender());
            intent.putExtra("age", post.getAge());
            intent.putExtra("feature", post.getFeature());
            intent.putExtra("reportType", post.getReportType());
            intent.putExtra("imageUrl", post.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title, animalType, feature, reportType;
        ImageView imagePost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            animalType = itemView.findViewById(R.id.tv_animalType);
            feature = itemView.findViewById(R.id.tv_feature);
            reportType = itemView.findViewById(R.id.tv_reportType);
            imagePost = itemView.findViewById(R.id.image_post); // ImageView 추가
        }
    }
}
