package com.views.redsocial.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.views.redsocial.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UsersProvider {

    private CollectionReference kCollection;

    public UsersProvider() {
        kCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id) {
        return kCollection.document(id).get();
    }

    // llamar en tiempo real
    public DocumentReference getUserRealTime(String id) {
        return kCollection.document(id);
    }

    public Task<Void> create(User user) {
        return kCollection.document(user.getId()).set(user);
    }
    public Task<Void> update(User user){
        Map<String,Object> map = new HashMap<>();
        map.put("username",user.getUsername());
        map.put("phone",user.getPhone());
        map.put("timestamp",new Date().getTime());
        map.put("image_profile",user.getImageProfile());
        map.put("image_cover",user.getImageCover());

        return kCollection.document(user.getId()).update(map);
    }
    public Task<Void> updateOnline(String idUser,boolean status){
        Map<String,Object> map = new HashMap<>();
        map.put("online",status);
        map.put("lastConnect", new Date().getTime());
        return kCollection.document(idUser).update(map);
    }
}
