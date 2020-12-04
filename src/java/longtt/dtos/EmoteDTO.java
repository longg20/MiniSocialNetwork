/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.dtos;

import java.io.Serializable;

/**
 *
 * @author Long
 */
public class EmoteDTO implements Serializable {
    String id, articleId, email, date;
    boolean like, dislike;
    int status;

    public EmoteDTO() {
    }

    public EmoteDTO(String id, String articleId, String email, boolean like, boolean dislike) {
        this.id = id;
        this.articleId = articleId;
        this.email = email;
        this.like = like;
        this.dislike = dislike;
        this.status = status;
    }
    
    public EmoteDTO(String id, String articleId, String email, boolean like, boolean dislike, String date, int status) {
        this.id = id;
        this.articleId = articleId;
        this.email = email;
        this.like = like;
        this.dislike = dislike;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean isDislike() {
        return dislike;
    }

    public void setDislike(boolean dislike) {
        this.dislike = dislike;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
