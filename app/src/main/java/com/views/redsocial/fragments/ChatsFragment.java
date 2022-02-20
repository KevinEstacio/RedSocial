package com.views.redsocial.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.views.redsocial.R;
import com.views.redsocial.adapters.ChatsAdapter;
import com.views.redsocial.adapters.PostsAdapter;
import com.views.redsocial.models.Chat;
import com.views.redsocial.models.Post;
import com.views.redsocial.providers.AuthProvider;
import com.views.redsocial.providers.ChatsProvider;
import com.views.redsocial.providers.UsersProvider;

public class ChatsFragment extends Fragment {


    ChatsAdapter mAdapter;
    RecyclerView mRecyclerView;
    View mView;

    ChatsProvider mChatsProvider;
    AuthProvider mAtuthProvider;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chats, container, false);
        mRecyclerView = mView.findViewById(R.id.recycleViewChats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mChatsProvider = new ChatsProvider();
        mAtuthProvider = new AuthProvider();

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mChatsProvider.getAll(mAtuthProvider.getUid());
            FirestoreRecyclerOptions<Chat> options =
                new FirestoreRecyclerOptions.Builder<Chat>()
                        .setQuery(query, Chat.class)
                        .build();
        mAdapter = new ChatsAdapter(options, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();

    }

    @Override
    public void onStop() {

        super.onStop();
        mAdapter.stopListening();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter.getListener() != null){
            mAdapter.getListener().remove();
        }
    }
}