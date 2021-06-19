package scripts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class gamePlay extends JPanel implements KeyListener, ActionListener, Serializable {

    public float vel = 5.0f;
    public boolean aga = true;
    public float playerPos = 180;

    public boolean gameOver = true;

    public int pistonYPos = 250;
    public int pistonXPos = 385;

    int cloudX1 = 100;
    int cloudY1 = 20;
    int cloudX2 = 268;
    int cloudY2 = 200;

    public boolean canGetScore = true;

    List<Integer> coliderPistonD = new ArrayList<>();
    List<Integer> coliderPistonU = new ArrayList<>();

    public int playerSpeed = 0;

    public int score = 0;

    public ImageIcon bird;
    public ImageIcon pistonTopD;
    public ImageIcon pistonTopU;
    public ImageIcon pistonBottomD;
    public ImageIcon pistonBottomU;

    public ImageIcon cloud;

    Timer timer = new Timer(10, this);
    Timer timer1 = new Timer(50, this);


    public gamePlay(){
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public void gameOver(){
        gameOver = true;
        playerSpeed = 0;
    }

    public void genCloud(Graphics g, int x, int y) {//todo: generate clouds
        cloud = new ImageIcon(new ImageIcon("src/assets/cloud.png").getImage().getScaledInstance(128, 120, Image.SCALE_DEFAULT));
        cloud.paintIcon(this, g, x, y);
    }

    public void genPiston(Graphics g, int x, int y){ //todo: generate piston
        pistonTopD = new ImageIcon(new ImageIcon("src/assets/piston.png").getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
        pistonBottomD = new ImageIcon(new ImageIcon("src/assets/pistonbottom.png").getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));

        pistonTopU = new ImageIcon(new ImageIcon("src/assets/pistonu.png").getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
        pistonBottomU = new ImageIcon(new ImageIcon("src/assets/pistonbottom.png").getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));

        pistonTopU.paintIcon(this, g, x, y-80);
        pistonTopD.paintIcon(this, g, x, y+80);

        for(int i = 0; i < 12; i++){
            pistonBottomD.paintIcon(this, g, x, y-124-i*64);
            pistonBottomU.paintIcon(this, g, x, y+124+i*64);
        }

        //creating coliders

        int ysd = y + 80;
        int ysu = y - 16;
        coliderPistonD.clear();
        coliderPistonU.clear();

        coliderPistonD.add(x); //x -- x, y -- y
        coliderPistonD.add(x + 64);
        coliderPistonD.add(ysd);
        coliderPistonD.add(401);

        coliderPistonU.add(x); //x -- x, y -- y
        coliderPistonU.add(x + 64);
        coliderPistonU.add(ysu);
        coliderPistonU.add(0);
    }

    @Override
    public void paint(Graphics g) {

        timer.start();

        g.setColor(Color.cyan);
        g.fillRect(0, 0, 385, 401);

        if(cloudX1 <= -150){ //create louds
            cloudY1 = ThreadLocalRandom.current().nextInt(8, 220);
            cloudX1 = 301;
        }

        if(cloudX2 <= -150){
            cloudY2 = ThreadLocalRandom.current().nextInt(8, 220);
            cloudX2 = 301;
        }

        cloudX1 -= 1;
        cloudX2 -= 1;
        genCloud(g, cloudX1, cloudY1);
        genCloud(g, cloudX2, cloudY2);

        g.setColor(Color.GREEN);
        g.fillRect(-1, 345, 302, 21);

        if(pistonXPos <= -62){ //todo: make piston move and teleport to the 'begining'
            int tempYPiston = ThreadLocalRandom.current().nextInt(48, 270 + 1);
            pistonXPos = 300; //385
            pistonYPos = (int) tempYPiston;
            canGetScore = true;
            System.out.println(String.valueOf(tempYPiston) + ";" + String.valueOf(pistonXPos));
        }

        pistonXPos -= playerSpeed;

        /*
        g.setColor(Color.green);
        g.fillRect(pistonXPos, pistonYPos, 32, 32);
        */

        genPiston(g, pistonXPos, pistonYPos);

        //g.setColor(Color.black);
        //g.drawRect(50, (int) playerPos, 32, 20);

        bird = new ImageIcon(new ImageIcon("src/assets/bird.png").getImage().getScaledInstance(32, 20, Image.SCALE_DEFAULT));
        bird.paintIcon(this, g, 50, (int) playerPos);

        //System.out.println((int) playerPos);

        //drawing number (score)
        g.setColor(Color.black);
        g.setFont(new Font("Sans", Font.PLAIN, 30));
        g.drawString(String.valueOf(score), 143, 50);

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        timer1.start();
        try {
            if (50 >= coliderPistonD.get(0) && 50 <= coliderPistonD.get(1) || 50 + 32 >= coliderPistonD.get(0) && 50 + 32 <= coliderPistonD.get(1)) {
                if (playerPos + 20 >= coliderPistonD.get(2) || playerPos <= coliderPistonU.get(2)) {
                    gameOver();
                }
            }
            if(playerPos > 330){
                gameOver();
            }
            if((50 == coliderPistonD.get(1) || 51 == coliderPistonD.get(1)) && canGetScore){
                score++;
                System.out.println("SCORE BOOSTO");
                canGetScore = false;
            }
        }catch (Exception IndexOutOfBoundsException){

        }

        if(!gameOver) {
            if (vel > -4.5f)
                vel -= 0.2f;

            playerPos -= vel;
        }
        //System.out.println(((int) playerPos));

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (aga) {
                    //System.out.println("JUMP");
                    aga = false;

                    vel = 5.0f;
                }
            }
        }else{
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                gameOver = false;

                playerPos = 180;
                vel = 5.0f;
                pistonYPos = 250;
                pistonXPos = 385;

                playerSpeed = 2;

                score = 0;
                canGetScore = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            //System.out.println("REKEASED");
            aga = true;
        }
    }
}
