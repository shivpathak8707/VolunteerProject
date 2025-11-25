package com.volunteerapp;

import java.util.List;
import javax.swing.*;

// Multithreading + Synchronization
public class StatsRefresherThread extends Thread {

    private final List<ParticipationRecord> participationRecords;
    private final JLabel statsLabel;
    private volatile boolean running = true;

    public StatsRefresherThread(List<ParticipationRecord> participationRecords, JLabel statsLabel) {
        this.participationRecords = participationRecords;
        this.statsLabel = statsLabel;
    }

    @Override
    public void run() {
        while (running) {
            int totalHours = 0;
            int totalRecords;
            synchronized (participationRecords) {
                totalRecords = participationRecords.size();
                for (ParticipationRecord pr : participationRecords) {
                    totalHours += pr.getHours();
                }
            }
            final int hours = totalHours;
            final int records = totalRecords;

            SwingUtilities.invokeLater(() ->
                    statsLabel.setText("Total Participation Records: " + records +
                            " | Total Hours Logged: " + hours)
            );

            try {
                Thread.sleep(3000); // refresh every 3 seconds
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void stopRunning() {
        running = false;
    }
}
