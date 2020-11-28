package test;

import javax.swing.*;
import java.awt.*;

public class PracticeGround {
    public static void main(String args[]) {
        JFrame jFrame = new JFrame();
        jFrame = new JFrame("ClientUI");
        jFrame.setSize(530, 440);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //jFrame.setLayout(null);
        jFrame.setVisible(true);

        JPanel panel1=new JPanel(new FlowLayout());
        jFrame.setContentPane(panel1);
        //panel1.setBounds(10,10,100,100);
        for (int i=0;i<20;i++){
            JButton button=new JButton(String.valueOf(i));
            panel1.add(button);
        }
    }
}

