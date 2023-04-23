package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Game {

    public Game() {
        JFrame frame = new JFrame("Waterpipes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 900);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        GameLogic logic = new GameLogic(frame);
        frame.addKeyListener(logic);

        JPanel sideMenu = new JPanel();
        JPanel buttonMenu = new JPanel();
        sideMenu.setBackground(Color.LIGHT_GRAY);
        buttonMenu.setBackground(Color.LIGHT_GRAY);
        JButton buttonRestart = new JButton("RESTART");
        JButton buttonCheck = new JButton("Check path");
        buttonCheck.addActionListener(logic);
        buttonCheck.setFocusable(false);
        buttonRestart.addActionListener(logic);
        buttonRestart.setFocusable(false);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 8, 10, 8);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(1);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(logic);

        sideMenu.setLayout(new GridLayout(2, 2));
        buttonMenu.setLayout(new GridLayout(1, 2));
        sideMenu.add(logic.getLabel());
        buttonMenu.add(buttonRestart);
        buttonMenu.add(buttonCheck);
        sideMenu.add(buttonMenu);
        sideMenu.add(logic.getBoardSizeLabel());
        sideMenu.add(slider);
        frame.add(sideMenu, BorderLayout.PAGE_START);

        frame.setVisible(true);
    }
}
