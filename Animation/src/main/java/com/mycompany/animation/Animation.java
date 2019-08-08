package com.mycompany.animation;
import javax.swing.*;  
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class Animation extends JFrame {
public Animation()
{
    this.setTitle("Animation");
    this.setBounds(100, 100, 700, 500);
    animationPanel.setBackground(Color.gray);
    JButton bStart = (JButton)buttonPanel.add(new JButton("Start"));
    
    bStart.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            startAnimation();
        }
    });
    
    JButton bStop = (JButton)buttonPanel.add(new JButton("Stop"));
    bStop.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            stopAnimation();
        }
    });
    
    JButton bDodaj = (JButton)buttonPanel.add(new JButton("Add"));
    bDodaj.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            addAnimation();
        }
    });
    
    this.getContentPane().add(animationPanel);
    this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
public void startAnimation()
{
    animationPanel.startAnimation();
}
public void stopAnimation()
{
    animationPanel.stop();
}
public void addAnimation()
{
    animationPanel.addKropelka();
}
private JPanel buttonPanel = new JPanel();
private AnimationPanel animationPanel = new AnimationPanel();
    public static void main(String[] args) {
        new Animation().setVisible(true);
    }
    private volatile boolean stopped = false;
    private Object lock = new Object();
    class AnimationPanel extends JPanel
    {
        public void addKropelka()
        {
            list.add(new animationFigure());
            thread = new Thread(threadGroup, new FigureRunnable
               ((animationFigure)list.get(list.size()-1)));
            thread.start();   
            threadGroup.list();
        }     
        public void stop()
        {
            stopped = true;
        }
        public void startAnimation() 
        {
            if(stopped)
            {
                stopped = false;
                synchronized(lock)
                {
                    lock.notifyAll(); 
                }
            }
        }
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            for(int i = 0; i < list.size();i++)
            {
    g.drawImage(animationFigure.getImg(),((animationFigure)list.get(i)).x,
            ((animationFigure)list.get(i)).y, null);
            }
        }
    ArrayList list = new ArrayList();
    JPanel ten = this;
    Thread thread;   
    ThreadGroup threadGroup = new ThreadGroup("Figure of animation");
    public class FigureRunnable implements Runnable
        {
        public FigureRunnable(animationFigure figure)
        {
            this.figure = figure;
        }

        @Override
        public void run() 
            {                  
                    while(true)  
                    {
                        synchronized(lock)
                        {
                            while(stopped)
                            {
                                try 
                                {
                                    lock.wait();
                                } 
                                catch (InterruptedException ex) 
                                {
                                    ex.printStackTrace();
                                }
                            }   
                        }
                            this.figure.goFigure(ten);
                            repaint();
                        try 
                        {
                            Thread.sleep(5);   
                        } 
                        catch (InterruptedException ex) 
                        {
                            ex.printStackTrace();
                        }
                    }
              
            }   
        animationFigure figure;
        }
    }        
}

class animationFigure
{
    public static Image getImg()
    {
        return animationFigure.figure;
    }
    public void goFigure(JPanel box)
    {
        Rectangle borders = box.getBounds();
        x += dx;
        y += dy;
        if(y + yAnimationFigure >= borders.getMaxY())
        {
            y = (int)(borders.getMaxY()-yAnimationFigure);
            dy = -dy;
        }
        if(x + xAnimationFigure >= borders.getMaxX())
        {
            x = (int)(borders.getMaxX()-xAnimationFigure);
            dx = -dx;
        }
        if(y < borders.getMinY())
        {
            y = (int)borders.getMinY();
            dy = -dy;
        }
        if(x < borders.getMinX())
        {
            x = (int)borders.getMinX();
            dx = -dx;
        }
    }
    public static Image figure = new ImageIcon("Figure.jpg").getImage();
    
    int x = 0;
    int y = 0; //start position of figure
    int dx = 1;
    int dy = 1; //change position of figure
    int xAnimationFigure = figure.getWidth(null);
    int yAnimationFigure = figure.getHeight(null);
}