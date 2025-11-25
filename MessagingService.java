package com.volunteerapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessagingService implements ICommunicator {

    private final List<Message> messages = new ArrayList<>();
    private int nextId = 1;

    @Override
    public synchronized void sendMessage(User from, User to, String messageText) throws AppException {
        if (messageText == null || messageText.trim().isEmpty()) {
            throw new AppException("Message cannot be empty");
        }
        Message message = new Message(nextId++, from.getId(), to.getId(), messageText, LocalDateTime.now());
        messages.add(message);
    }

    public synchronized List<Message> getMessagesForUser(int userId) {
        List<Message> result = new ArrayList<>();
        for (Message m : messages) {
            if (m.getReceiverId() == userId) {
                result.add(m);
            }
        }
        return result;
    }
}
