package com.views.redsocial.models;

public class   Coment {
    private String id;
    private String coment;
    private String idUser;
    private String idPost;
    long timestamo;

    public Coment() {
    }

    public Coment(String id, String coment, String idUser, String idPost, long timestamo) {
        this.id = id;
        this.coment = coment;
        this.idUser = idUser;
        this.idPost = idPost;
        this.timestamo = timestamo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public long getTimestamo() {
        return timestamo;
    }

    public void setTimestamo(long timestamo) {
        this.timestamo = timestamo;
    }
}
