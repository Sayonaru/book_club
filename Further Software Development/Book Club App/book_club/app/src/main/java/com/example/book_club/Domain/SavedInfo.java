package com.example.book_club.Domain;

public class SavedInfo {
    private String title;
    private String publishedDate;
    private String pageCount;
    private String uid;
    private int noOfItems;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public SavedInfo(String title, String publishedDate, String pageCount) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
    }

    public SavedInfo() {
    }
}
