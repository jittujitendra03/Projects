package com.punisher;
//import java.util.*;
import java.awt.event.*;

import javax.swing.*;

import java.awt.*;

//import javax.swing.*;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener
{
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 96;

    private final Timer timer;

    private int playerX = 310;

    private int ballPositionX = 120;
    private int ballPositionY = 350;
    private int ballXDirection = -1;
    private int ballYDirection = -2;

    private MapGenerator map;

    public Gameplay()
    {
        map = new MapGenerator(8, 12);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        int delay = 0;
        timer=new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics g)
    {
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // drawing map
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // the scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD, 25));
        g.drawString(""+score, 590,30);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballPositionX, ballPositionY, 20, 20);

        // when you won the game
        if(totalBricks <= 0)
        {
            play = false;
            ballXDirection = 0;
            ballYDirection = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("You Won", 260,300);

            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press (Enter) to Restart", 230,350);
        }

        // when you lose the game
        if(ballPositionY > 570)
        {
            play = false;
            ballXDirection = 0;
            ballYDirection = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("Game Over, Scores: "+score, 190,300);

            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press (Enter) to Restart", 230,350);
        }

        g.dispose();
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            if(playerX >= 600)
            {
                playerX = 600;
            }
            else
            {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if(playerX < 10)
            {
                playerX = 10;
            }
            else
            {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if(!play)
            {
                play = true;
                ballPositionX = 120;
                ballPositionY = 350;
                ballXDirection = -1;
                ballYDirection = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public void moveRight()
    {
        play = true;
        playerX+=20;
    }

    public void moveLeft()
    {
        play = true;
        playerX-=20;
    }

    public void actionPerformed(ActionEvent e)
    {
        timer.start();
        if(play)
        {
            if(new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8)))
            {
                ballYDirection = -ballYDirection;
                ballXDirection = -2;
            }
            else if(new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(new Rectangle(playerX + 70, 550, 30, 8)))
            {
                ballYDirection = -ballYDirection;
                ballXDirection = ballXDirection + 1;
            }
            else if(new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(new Rectangle(playerX + 30, 550, 40, 8)))
            {
                ballYDirection = -ballYDirection;
            }

            // check map collision with the ball
            A: for(int i = 0; i<map.map.length; i++)
            {
                for(int j =0; j<map.map[0].length; j++)
                {
                    if(map.map[i][j] > 0)
                    {
                        //scores++;
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);

                        if(ballRect.intersects(rect))
                        {
                            map.setBrickValue(0, i, j);
                            score+=5;
                            totalBricks--;

                            // when ball hit right or left of brick
                            if(ballPositionX + 19 <= rect.x || ballPositionX + 1 >= rect.x + rect.width)
                            {
                                ballXDirection = -ballXDirection;
                            }
                            // when ball hits top or bottom of brick
                            else
                            {
                                ballYDirection = -ballYDirection;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPositionX += ballXDirection;
            ballPositionY += ballYDirection;

            if(ballPositionX < 0)
            {
                ballXDirection = -ballXDirection;
            }
            if(ballPositionY < 0)
            {
                ballYDirection = -ballYDirection;
            }
            if(ballPositionX > 670)
            {
                ballXDirection = -ballXDirection;
            }

            repaint();
        }
    }
}