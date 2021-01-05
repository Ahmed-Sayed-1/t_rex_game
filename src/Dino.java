//package Texture;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package project;

import com.sun.opengl.util.*;
import java.awt.*;
import javafx.embed.swing.JFXPanel;
import javax.media.opengl.*;
import javax.swing.*;
//import javax.swing.JFrame;

public class Dino extends JFrame  {
       

    public static void main(String[] args) {
         try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dino.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        Dino dino = new Dino();
        SwingUtilities.invokeLater(
                new Runnable() {
            @Override
            public void run() {
                dino.setVisible(true);
                JFXPanel j = new JFXPanel();

                
            }
        });
        //new Bunny();
    }


    public Dino() {
        GLCanvas glcanvas;
        Animator animator;
        
        AnimListener listener = new DinoGLEventListener();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(15);
        animator.add(glcanvas);
        animator.start();

        setTitle("T-REX");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
      
    }
    
   
}
