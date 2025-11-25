package com.volunteerapp;

public interface ICommunicator {
    void sendMessage(User from, User to, String messageText) throws AppException;
}