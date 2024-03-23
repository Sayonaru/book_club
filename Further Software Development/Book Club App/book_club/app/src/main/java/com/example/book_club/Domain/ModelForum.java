package com.example.book_club.Domain;

public class ModelForum {

    String id, bookId, timestamp, message, uid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ModelForum(String id, String bookId, String timestamp, String comment, String uid) {
        this.id = id;
        this.bookId = bookId;
        this.timestamp = timestamp;
        this.message = comment;
        this.uid = uid;
    }

    public ModelForum() {
    }
}
