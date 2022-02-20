package com.views.redsocial.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.views.redsocial.R;
import com.views.redsocial.activities.ChatActivity;
import com.views.redsocial.models.Message;
import com.views.redsocial.providers.AuthProvider;
import com.views.redsocial.providers.UsersProvider;
import com.views.redsocial.utils.RelativeTime;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {


    Context context;
    UsersProvider mUserProvider;
    AuthProvider mAuthProvider;

    public MessagesAdapter(FirestoreRecyclerOptions<Message> option, Context context) {
        super(option);
        this.context = context;
        mUserProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Message message) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String menssageId = document.getId();

        holder.textViewMessage.setText(message.getMessage());
        //String relativeTime = RelativeTime.getTimeAgo(message.getTimestamp(),context);
        String relativeTime = RelativeTime.timeFormatAMPM(message.getTimestamp(),context);
        holder.textViewDate.setText(relativeTime);

        if (message.getIdSender().equals(mAuthProvider.getUid())){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(150,0,0,0);
            holder.linearLayout.setLayoutParams(params);
            holder.linearLayout.setPadding(30,20,0,20);
            holder.linearLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_linear_layout));
            holder.imageViewViewed.setVisibility(View.VISIBLE);
            holder.textViewMessage.setTextColor(Color.WHITE);
            holder.textViewDate.setTextColor(Color.LTGRAY   );
        }
        else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins(0,0,150,0);
            holder.linearLayout.setLayoutParams(params);
            holder.linearLayout.setPadding(30,20,30,20);
            holder.linearLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_linear_layout_grey));
            holder.imageViewViewed.setVisibility(View.GONE);
            holder.textViewMessage.setTextColor(Color.DKGRAY);
            holder.textViewDate.setTextColor(Color.LTGRAY   );

        }
        if (message.isViewed()){
            holder.imageViewViewed.setImageResource(R.drawable.icon_check_blue);
        }
        else {
            holder.imageViewViewed.setImageResource(R.drawable.icon_check_grey);

        }

    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView textViewDate;
        ImageView imageViewViewed;
        View viewHolder;
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            textViewMessage = view.findViewById(R.id.textViewMessage);
            textViewDate = view.findViewById(R.id.textViewDateMessage);
            imageViewViewed = view.findViewById(R.id.imageViewViewedMessage);
            linearLayout = view.findViewById(R.id.linearLayoutMessage);
            viewHolder = view;
        }
    }
}
