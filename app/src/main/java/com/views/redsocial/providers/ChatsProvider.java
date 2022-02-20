package com.views.redsocial.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.views.redsocial.models.Chat;

import java.util.ArrayList;

public class ChatsProvider {
    CollectionReference mCollection;

    public ChatsProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Chats");

    }

    public void create(Chat chat) {
        mCollection.document(chat.getIdUser1() + chat.getIdUser2()).set(chat);
        //mCollection.document(chat.getIdUser1()).collection("Users").document(chat.getIdUser2()).set(chat);
        //mCollection.document(chat.getIdUser2()).collection("Users").document(chat.getIdUser1()).set(chat);
    }

    public Query getChatByUser1AndUser2(String idUser1, String idUser2) {

        ArrayList<String> ids = new ArrayList<>();
        ids.add(idUser1 + idUser2);
        ids.add(idUser2 + idUser1);
        return mCollection.whereIn("id", ids);
    }

    public Query getAll(String idUser) {

        return mCollection.whereArrayContains("ids", idUser);
        //return mCollection.document(idUser).collection("Users");
    }

}
