package com.views.redsocial.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.views.redsocial.R;
import com.views.redsocial.activities.PostDetailActivity;
import com.views.redsocial.models.Like;
import com.views.redsocial.models.Post;
import com.views.redsocial.providers.AuthProvider;
import com.views.redsocial.providers.LikesProvider;
import com.views.redsocial.providers.PostProvider;
import com.views.redsocial.providers.UsersProvider;

import java.util.Date;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder> {


    Context context;
    UsersProvider mUserProvider;
    LikesProvider mLikesProvider;
    AuthProvider mAutProvider;
    TextView mTextViewNUmberFilter;
    ListenerRegistration mListener;

    public PostsAdapter(FirestoreRecyclerOptions<Post> option, Context context) {
        super(option);
        this.context = context;
        mUserProvider = new UsersProvider();
        mLikesProvider = new LikesProvider();
        mAutProvider = new AuthProvider();
    }


    public PostsAdapter(FirestoreRecyclerOptions<Post> option, Context context,TextView textView) {
        super(option);
        this.context = context;
        mUserProvider = new UsersProvider();
        mLikesProvider = new LikesProvider();
        mAutProvider = new AuthProvider();
        mTextViewNUmberFilter = textView;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);

        String postid = document.getId();

        if (mTextViewNUmberFilter != null){
            int numberFilter = getSnapshots().size();
            mTextViewNUmberFilter.setText(String.valueOf(numberFilter));
        }

        holder.textViewTitle.setText(post.getTitle().toUpperCase());
        holder.textViewDescription.setText(post.getDescription());
        if (post.getImage1() != null) {
            if (!post.getImage1().isEmpty()) {
                Picasso.with(context).load(post.getImage1()).into(holder.imageViewPost);

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
        holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Like like = new Like();
                like.setIdUser(mAutProvider.getUid());
                like.setIdPost(postid);
                like.setTimestap(new Date().getTime());
                like(like, holder);
            }
        });


        getUserInfo(post.getIdUser(), holder);
        getNumberLikesByPost(postid, holder);
        checkIfExistLike(postid, mAutProvider.getUid(), holder);

    }// fin del onBindViewHolder

    private void checkIfExistLike(String isPost, String isUser, ViewHolder holder) {
        mLikesProvider.getLikeByPostAndUser(isPost, isUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocument = queryDocumentSnapshots.size();
                if (numberDocument > 0) {
                    holder.imageViewLike.setImageResource(R.drawable.icon_like_blue);

                } else {
                    holder.imageViewLike.setImageResource(R.drawable.icon_like_gray);

                }
            }
        });
    }

    private void getNumberLikesByPost(String idPost, ViewHolder holder) {

        mListener = mLikesProvider.getLikeByPost(idPost).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (queryDocumentSnapshots != null){
                    int numberLikes = queryDocumentSnapshots.size();
                    holder.texViewLikes.setText(String.valueOf(numberLikes) + " Me gustas");
                }

            }
        });
    }

    private void like(Like like, ViewHolder holder) {
        mLikesProvider.getLikeByPostAndUser(like.getIdPost(), mAutProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocument = queryDocumentSnapshots.size();
                if (numberDocument > 0) {
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    holder.imageViewLike.setImageResource(R.drawable.icon_like_gray);
                    mLikesProvider.delete(idLike);
                } else {
                    holder.imageViewLike.setImageResource(R.drawable.icon_like_blue);

                    mLikesProvider.cretate(like);
                }
            }
        });
    }

    private void getUserInfo(String idUser, ViewHolder holder) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("username")) {
                        String username = documentSnapshot.getString("username");
                        holder.textUsername.setText("By: " + username);
                    }
                }
            }
        });
    }

    public  ListenerRegistration getListener(){
        return mListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textUsername;
        TextView texViewLikes;
        ImageView imageViewPost;
        ImageView imageViewLike;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitlePostCard);
            textViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            textUsername = view.findViewById(R.id.textViewUsernamePostCard);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            imageViewLike = view.findViewById(R.id.imageViewLike);
            texViewLikes = view.findViewById(R.id.textViewLikes);
            viewHolder = view;
        }
    }
}
