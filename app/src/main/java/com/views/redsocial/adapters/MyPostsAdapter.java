package com.views.redsocial.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.views.redsocial.R;
import com.views.redsocial.activities.PostDetailActivity;
import com.views.redsocial.models.Like;
import com.views.redsocial.models.Post;
import com.views.redsocial.providers.AuthProvider;
import com.views.redsocial.providers.CommentsProvider;
import com.views.redsocial.providers.LikesProvider;
import com.views.redsocial.providers.PostProvider;
import com.views.redsocial.providers.UsersProvider;
import com.views.redsocial.utils.RelativeTime;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostsAdapter extends FirestoreRecyclerAdapter<Post, MyPostsAdapter.ViewHolder> {


    Context context;
    UsersProvider mUserProvider;
    LikesProvider mLikesProvider;
    AuthProvider mAutProvider;
    PostProvider mPostProvider;
    CommentsProvider mCommentProvider;

    public MyPostsAdapter(FirestoreRecyclerOptions<Post> option, Context context) {
        super(option);
        this.context = context;
        mUserProvider = new UsersProvider();
        mLikesProvider = new LikesProvider();
        mAutProvider = new AuthProvider();
        mPostProvider = new PostProvider();
        mCommentProvider = new CommentsProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);

        String postid = document.getId();
        String relativetime = RelativeTime.getTimeAgo(post.getTimestamp(), context);

        holder.textViewRelativeTime.setText(relativetime);
        holder.textViewTitle.setText(post.getTitle().toUpperCase());

        if (post.getIdUser().equals(mAutProvider.getUid())){
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }
        else {
            holder.imageViewDelete.setVisibility(View.GONE);

        }

        if (post.getImage1() != null) {
            if (!post.getImage1().isEmpty()) {
                Picasso.with(context).load(post.getImage1()).into(holder.circleImagePost);

            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id", postid);
                context.startActivity(intent);
            }
        });
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDelete(postid);


            }
        });


    }// fin del onBindViewHolder

    private void showConfirmDelete(String postid) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar pulicacion")
                .setMessage("¿Estas seguro de realizar esta accion?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteComment(postid);
                        deleteLike(postid);
                        deletePost(postid);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteComment(String postid) {
        mCommentProvider.getCommentsByPost(postid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocument = queryDocumentSnapshots.size();
                if (numberDocument > 0) {
                    for (int i = 0; i < numberDocument; ++i) {
                        String idComment = queryDocumentSnapshots.getDocuments().get(i).getId();
                        mCommentProvider.delete(idComment);
                    }
                }
            }
        });
    }

    private void deleteLike(String postid) {
        mLikesProvider.getLikeByPost(postid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocument = queryDocumentSnapshots.size();
                if (numberDocument > 0) {
                    for (int i = 0; i < numberDocument; ++i) {
                        String idLike = queryDocumentSnapshots.getDocuments().get(i).getId();
                        mLikesProvider.delete(idLike);
                    }
                }
            }
        });
    }

    private void deletePost(String postid) {
        mPostProvider.delete(postid).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "El post de elimino correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No se puedo eliminar el post", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_post, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewRelativeTime;
        CircleImageView circleImagePost;
        ImageView imageViewDelete;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitleMyPost);
            textViewRelativeTime = view.findViewById(R.id.textViewRelativeTimeMyPost);
            circleImagePost = view.findViewById(R.id.circleImageMyPost);
            imageViewDelete = view.findViewById(R.id.imageViewDeleteMyPost);
            viewHolder = view;
        }
    }
}
