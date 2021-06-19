package scripts;

import javax.swing.*;
import java.awt.Color;

public class Main {
    public static void main(String[] args){
        JFrame window = new JFrame("Jumpy Bird");
        gamePlay GamePlay = new gamePlay();


        window.setBounds(200, 100, 300, 400);
        window.setBackground(Color.cyan);
        window.setResizable(true);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(GamePlay);
    }
}
