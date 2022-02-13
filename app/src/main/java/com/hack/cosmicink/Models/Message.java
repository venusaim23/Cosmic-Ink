package com.hack.cosmicink.Models;

public class Message {
    private String id;
    private String text;
    private String startTime;
    private String endTime;
    private String conversationId;

    public Message() {
    }

    public Message(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public Message(String id, String text, String conversationId) {
        this.id = id;
        this.text = text;
        this.conversationId = conversationId;
    }

    public Message(String id, String text, String startTime, String endTime, String conversationId) {
        this.id = id;
        this.text = text;
        this.startTime = startTime;
        this.endTime = endTime;
        this.conversationId = conversationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
