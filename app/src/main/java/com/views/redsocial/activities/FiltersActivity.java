package com.views.redsocial.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.views.redsocial.R;
import com.views.redsocial.adapters.PostsAdapter;
import com.views.redsocial.models.Post;
import com.views.redsocial.providers.AuthProvider;
import com.views.redsocial.providers.PostProvider;
import com.views.redsocial.utils.ViewedMessageHelper;

public class FiltersActivity extends AppCompatActivity {
    String mExtraCategory;
    AuthProvider mAuthProvider;
    RecyclerView mRecycleView;
    PostProvider mPostProvider;
    PostsAdapter mPostAdapter;

    TextView mtextVewNumberFilter;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        mRecycleView = findViewById(R.id.recyclerViewFilter);
        mToolbar = findViewById(R.id.toolbar);
        mtextVewNumberFilter = findViewById(R.id.textViewNumberFilter);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Filtros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FiltersActivity.this);
        mRecycleView.setLayoutManager(new GridLayoutManager(FiltersActivity.this,2));

        mExtraCategory = getIntent().getStringExtra("category");

        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByCategoryAndTimestamp(mExtraCategory);
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        mPostAdapter = new PostsAdapter(options, FiltersActivity.this,mtextVewNumberFilter);
        mRecycleView.setAdapter(mPostAdapter);
        mPostAdapter.startListening();
        ViewedMessageHelper.updateOnline(true, FiltersActivity.this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mPostAdapter.stopListening();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}