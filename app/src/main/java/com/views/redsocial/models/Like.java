package com.views.redsocial.models;

public class Like {
    private String idPost;
    private String idUser;
    private String id;
    private long timestap;

    public Like(String idPost, String idUser, String id, long timestap) {
        this.idPost = idPost;
        this.idUser = idUser;
        this.id = id;
        this.timestap = timestap;
    }

    public Like() {
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestap() {
        return timestap;
    }

    public void setTimestap(long timestap) {
        this.timestap = timestap;
    }
}
