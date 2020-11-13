package com.julien;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;

public class Plane {

    private int w = 5;
    private int h = 5;
    private Image image;

    private double x;
    private double y;
    private final ArrayList<Point> history;
    private float angle = 0;
    private int altitude = 0;
    private double velocity = 0;

    private boolean init = false;

    JTextField immat = new JTextField();
    JTextField cap = new JTextField();

    private double futureX;
    private double futureY;


    Object[] infos = {
            "Immatriculation:", immat,
            "Direction:", cap
    };

    public Plane() {

        history = new ArrayList<Point>();

        //loadImage();
    }

    private void loadImage() {

        ImageIcon ii = new ImageIcon("/home/sabine/Documents/AppliJulien/src/com/julien/plane.png");
        image = ii.getImage();

        w = image.getWidth(null);
        h = image.getHeight(null);
        System.out.println("width = "+w);
        System.out.println("height = "+h);
    }

    private void init(){

        int option = JOptionPane.showConfirmDialog(null, infos, "Initialisation", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            System.out.println("Immat : " + immat.getText());
            System.out.println("Cap : " + cap.getText());
            angle = Integer.parseInt(cap.getText())+270;
        }
    }

    public void move() {


        x += (velocity * Math.cos(Math.toRadians(angle)));
        y += (velocity * Math.sin(Math.toRadians(angle)));

        futureX = x + 6*velocity*Math.cos(Math.toRadians(angle));
        futureY = y + 6*velocity*Math.sin(Math.toRadians(angle));

    }

    public void update(){
        if (history.size() >= 5) {
            history.remove(0);
        }
        history.add(new Point((int)x, (int)y));
    }

    public double getX() {

        return x;
    }

    public double getY() {

        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getFutureX() {
        return futureX;
    }

    public double getFutureY() {
        return futureY;
    }

    public ArrayList<Point> getHistory() {
        return history;
    }

    public int getWidth() {

        return w;
    }

    public int getHeight() {

        return h;
    }

    public Image getImage() {

        return image;
    }

    public boolean isInit() {
        return init;
    }

    public int getAltitude() {
        return altitude;
    }

    public double getVelocity() {
        return velocity;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            angle -= 10;

        }

        if (key == KeyEvent.VK_RIGHT) {
            angle += 10;
        }

        if (key == KeyEvent.VK_UP){
            altitude += 100;
        }

        if (key == KeyEvent.VK_DOWN){
            if (altitude >= 100)
                altitude -= 100;
        }

        if (key == KeyEvent.VK_A){
            velocity += 0.5;
        }

        if (key == KeyEvent.VK_Q){
            if (velocity >= 0.5)
                velocity -= 0.5;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (!init){
            init();
            x = e.getX();
            y = e.getY();
            futureX = x;
            futureY = y;
            init = true;
        }
    }
}