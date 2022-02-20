package com.views.redsocial.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.views.redsocial.R;
import com.views.redsocial.activities.PostDetailActivity;
import com.views.redsocial.models.Coment;
import com.views.redsocial.models.Post;
import com.views.redsocial.providers.UsersProvider;

import org.w3c.dom.Comment;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends FirestoreRecyclerAdapter<Coment, CommentAdapter.ViewHolder> {


    Context context;
    UsersProvider mUserProvider;

    public CommentAdapter(FirestoreRecyclerOptions<Coment> option, Context context) {
        super(option);
        this.context = context;
        mUserProvider = new UsersProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Coment coment) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String commentId = document.getId();
        String idUser = document.getString("idUser");

        holder.textViewComment.setText(coment.getComent());
        getUserInfo(idUser,holder);

    }

    private void getUserInfo(String idUser, ViewHolder holder) {

        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        holder.textViewUsername.setText(username.toUpperCase());

                    }if (documentSnapshot.contains("image_profile")){
                        String image_profile = documentSnapshot.getString("image_profile");
                        if (image_profile != null){
                            if (!image_profile.isEmpty()){
                                Picasso.with(context).load(image_profile).into(holder.circleImageViewComment);

                            }
                        }

                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comment, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername;
        TextView textViewComment;
        CircleImageView circleImageViewComment;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewUsername = view.findViewById(R.id.textViewUsernameC);
            textViewComment = view.findViewById(R.id.textViewComment);
            circleImageViewComment = view.findViewById(R.id.circleImageComment);
            viewHolder = view;
        }
    }
}
