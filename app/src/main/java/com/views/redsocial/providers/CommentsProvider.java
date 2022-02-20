package com.views.redsocial.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.views.redsocial.models.Coment;



public class CommentsProvider {
    CollectionReference mCollection;

    public CommentsProvider() {


        mCollection= FirebaseFirestore.getInstance().collection("Comments");

    }
    public Task<Void> create(Coment coment){
        return mCollection.document().set(coment);
    }
    public Query getCommentsByPost(String idPost) {
        return mCollection.whereEqualTo("idPost", idPost);
    }
    public Task<Void> delete(String id){
        return  mCollection.document(id).delete();
    }
}
