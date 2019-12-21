package agh.cs.EvolutionSimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation {
    public WorldMap map;
    private Thread threadObject;
    private AtomicBoolean paused;

    public Simulation(WorldMap map){
        this.map=map;
        paused = new AtomicBoolean(false);

    }

    public void startSimulation() {
        JFrame frame = new JFrame();
        frame.setSize(600,700);
        //frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Visualization visualization = new Visualization(map, frame);
        frame.setVisible(true);
        frame.setLayout(null);

        JTextArea statistics = new JTextArea();
        statistics.setBounds(0,0,600,170);

        JButton pauseButton = new JButton();
        pauseButton.setBounds(500,50, 90,60);
        pauseButton.setText("Pauza");
        statistics.add(pauseButton);

        frame.add(statistics);
        frame.add(visualization);

        try {
            while(true) {
                map.nextDay();
                statistics.setText(map.getStatistics());
                visualization.repaint();
                System.out.println(map.toString());
                Thread.sleep(100);
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

}
