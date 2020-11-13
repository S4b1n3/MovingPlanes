package com.julien;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Board extends JPanel implements ActionListener, MouseWheelListener, MouseListener, MouseMotionListener {

    private Timer timer;
    private Plane plane;
    private JLabel lAltitude = new JLabel("");
    private JLabel lVelocity = new JLabel("");
    private JLabel lImmat = new JLabel("");

    private BufferedImage background;
    private final int iWidth;
    private final int iHeight;

    private final int DELAY = 8000; //8 seconds

    private double zoomFactor = 1;
    private double prevZoomFactor = 1;
    private boolean zoomer;
    private boolean dragger;
    private boolean released;
    private double xOffset = 0;
    private double yOffset = 0;
    private int xDiff;
    private int yDiff;
    private Point startPoint;


    public Board() {

        initBoard();
        try {
            background = ImageIO.read(new File("/home/sabine/Documents/AppliJulien/src/com/julien/PTPair.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        iWidth = background.getWidth(null);
        iHeight = background.getHeight(null);
        System.out.println("height : " + iHeight);
        System.out.println("width : " + iWidth);

    }

    private void initBoard() {

        setLayout(new BorderLayout());
        addKeyListener(new TAdapter());
        addMouseListener(new MAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);

        plane = new Plane();

        timer = new Timer(DELAY, this);
        timer.start();

        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        setBounds(0, 0, width, height);
        setBorder(BorderFactory.createLineBorder(Color.black));

    }

    public int getiWidth() {
        return iWidth;
    }

    public int getiHeight() {
        return iHeight;
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        clear(g2);

        /*if (zoomer) {
            AffineTransform at = new AffineTransform();

            double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
            double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

            double zoomDiv = zoomFactor / prevZoomFactor;

            xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
            yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

            at.translate(xOffset, yOffset);
            at.scale(zoomFactor, zoomFactor);
            prevZoomFactor = zoomFactor;
            g2.transform(at);
            zoomer = false;
        }

        if (dragger) {
            AffineTransform at = new AffineTransform();
            at.translate(xOffset + xDiff, yOffset + yDiff);
            at.scale(zoomFactor, zoomFactor);
            g2.transform(at);

            if (released) {
                xOffset += xDiff;
                yOffset += yDiff;
                dragger = false;
            }

        }*/

        g2.drawImage(background, 0, 0, null);

        g2.setColor(Color.cyan);


        if (plane.isInit()){
            for (Point p : plane.getHistory()){
                g2.drawOval(p.x+plane.getWidth()/3, p.y+plane.getHeight()/3, plane.getWidth()/3, plane.getHeight()/3);
            }


            g2.drawOval((int)plane.getX(), (int)plane.getY(), plane.getWidth(), plane.getHeight());

            lImmat.setForeground(Color.cyan);
            lAltitude.setForeground(Color.cyan);
            lVelocity.setForeground(Color.cyan);

            lImmat.setFont(new Font("Serif", Font.PLAIN, 8));
            lAltitude.setFont(new Font("Serif", Font.PLAIN, 8));
            lVelocity.setFont(new Font("Serif", Font.PLAIN, 8));

            lImmat.setBounds((int)plane.getX()+3, (int)plane.getY()+ plane.getHeight()+3, 50, 15);
            lAltitude.setBounds((int)plane.getX()+3, (int)plane.getY()+ plane.getHeight()+13, 50, 15);
            lVelocity.setBounds((int)plane.getX()+3, (int)plane.getY()+ plane.getHeight()+24, 50, 15);


            lImmat.setText(plane.immat.getText());
            lAltitude.setText(Integer.toString(plane.getAltitude()));
            int velocity_temp = (int)(plane.getVelocity()*10/0.5);
            String velocity_printed = Integer.toString(velocity_temp);
            if (velocity_temp < 100)
                velocity_printed = "0"+velocity_printed;
            lVelocity.setText(velocity_printed);

            this.add(lAltitude);
            this.add(lVelocity);
            this.add(lImmat);

            g2.drawLine((int)plane.getX()+ plane.getWidth()/2, (int)plane.getY()+plane.getHeight()/2, (int)plane.getFutureX(), (int)plane.getFutureY());

        }


        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        step();
    }

    private void clear(Graphics g){

        super.paintComponent(g);
        this.removeAll();
        this.updateUI();
    }

    private void step() {

        plane.move();

        repaint((int)plane.getX(), (int)plane.getY(), plane.getWidth(), plane.getHeight());

        plane.update();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            plane.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            plane.keyPressed(e);
        }
    }

    private class MAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) { plane.mousePressed(e);}
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoomer = true;

        //Zoom in
        if (e.getWheelRotation() < 0) {
            zoomFactor *= 1.1;
            repaint();
        }
        //Zoom out
        if (e.getWheelRotation() > 0) {
            zoomFactor /= 1.1;
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point curPoint = e.getLocationOnScreen();
        xDiff = curPoint.x - startPoint.x;
        yDiff = curPoint.y - startPoint.y;

        dragger = true;
        repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        released = false;
        startPoint = MouseInfo.getPointerInfo().getLocation();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        released = true;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}