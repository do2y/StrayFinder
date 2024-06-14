package com.example.myapplication;

public class Notice {
    private String noticeId;
    private String title;
    private String content;

    public Notice() {

    }

    public Notice(String noticeId, String title, String content) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
