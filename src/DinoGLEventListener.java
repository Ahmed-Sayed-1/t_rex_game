/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package project;

import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import javax.media.opengl.*;

import java.util.BitSet;
import java.util.Random;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class DinoGLEventListener extends AnimListener {

    TextRenderer tr = new TextRenderer(Font.decode("PLAIN"));
    int frames = 0;
    private LocalTime startTime;
    private long elapsedSeconds;
    private int elapsedNanos;
    int score = 0;
    double scoree = 0;
    int animationIndex = 0;
    int animationInde = 0;
    final float maxWidth = 1000;
    final float maxHeight = 650;
    final float borderSize = 100;
    float speed = 25;
    float x = -425;
    float y = -125;
    float xenemy = 525;
    float yenemy = -125;
    float xcloud = 500;
    float ycloud = -25;
    float scrollBackground;
    boolean isJumping = false;
    boolean inair = false;
    boolean down = false;
    boolean pause = false;
    boolean sound = false;
    boolean M = false;
    boolean lol = false;
    static String newname;
//String highscore_name; 
    int highscore;

    boolean OnBackground = false;
    int cal = 5;
    String textureNames[]
            = {"Dino-below-right-up.png",
                "Dino-below-left-up.png",
                "Dino-stand.png",
                "Dino-right-up.png",
                "Dino-left-up.png",
                "Cactus-1.png",
                "Cactus-2.png",
                "Cactus-3.png",
                "Cactus-4.png",
                "Cactus-5.png",
                "Cactus-6.png",
                "Cactus-7.png",
                "cloud.png",
                "Picture1.png",
                "Picture12.png",
                "Picture13.png",
                "Picture115.png",
                "Picture133.png",
                "Back.png"};
    TextureReader.Texture textures[] = new TextureReader.Texture[textureNames.length];
    int textureIndecies[] = new int[textureNames.length];

    /*
     5 means gun in array pos
     x and y coordinate for gun 
     */

    public void init(GLAutoDrawable gld) {
        startTime = LocalTime.now();

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // We need to generate some numbers to associate textures with
        // and place it into textures array
        gl.glGenTextures(textureNames.length, textureIndecies, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                textures[i] = TextureReader.readTexture(assetsFolderName + "\\" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndecies[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, textures[i].getWidth(), textures[i].getHeight(),
                        GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, textures[i].getPixels());
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }

        }

        gl.glLoadIdentity();
        gl.glOrtho(-maxWidth / 2, maxWidth / 2, -maxHeight / 2, maxHeight / 2, -1, 1);
    }

    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        if (Math.abs(x - xenemy) < 50 && Math.abs(y - yenemy) < 50 && (animationIndex == 0 || animationIndex == 1)) {
            sound();
            handleKeyPress();
            //DrawSprite(gl, 450, -125, 0);
            DrawBackground(gl, scrollBackground);
            DrawCactus(gl, xenemy, yenemy, cal);
            DrawSprite(gl, x, y, 2);
            DrawCactus(gl, 25, 0, 17);

        } else if (Math.abs(x - xenemy) < 75 && Math.abs(y - yenemy) < 100 && (animationIndex == 2 || animationIndex == 3 || animationIndex == 4)) {
            sound();
            handleKeyPress();
            //   System.out.println("game over");
            //DrawSprite(gl, 450, -125, 0);
            DrawBackground(gl, scrollBackground);
            DrawCactus(gl, xenemy, yenemy, cal);
            DrawSprite(gl, x, y, 2);
            DrawCactus(gl, 25, 0, 17);

        } else if (pause) {

            DrawBackground(gl, scrollBackground);
            DrawCactus(gl, xenemy, yenemy, cal);
            DrawCactus(gl, x, y, animationIndex);

            DrawCactus(gl, 25, 100, 14);
            DrawCactus(gl, 25, -100, 15);
            handleKeyPress();
        } else {

            float deltaTime = getDeltaTime();
            if (isJumping == true) {
                y = y + 300 * deltaTime;
                animationIndex = 0;
            }
            if (y > 125) {
                isJumping = false;
                inair = true;

            }
            if (inair == true) {
                y = y - 300 * deltaTime;
                animationIndex = 0;
            }
            if (y < -125) {
                inair = false;

            }

            gl.glClear(GL.GL_COLOR_BUFFER_BIT);

//        System.out.println(scrollBackground);
            DrawBackground(gl, scrollBackground -= 20);
            if (scrollBackground < -3 * maxWidth / 2) {
                scrollBackground += 4 * maxWidth / 2;
            }

            handleKeyPress();

            if (down) {
                //animationIndex+=3;
                animationIndex = ((++animationIndex) % 2);

                DrawSprite(gl, x, y, animationIndex);
                down = false;
            } else if (!down && !isJumping && !inair) {
                animationIndex = ((++animationInde) % 2) + 3;

                DrawSprite(gl, x, y, animationIndex);
            } else {
                animationIndex = 2;

                DrawSprite(gl, x, y, animationIndex);
            }

            xenemy = xenemy - 300 * deltaTime - score / 40;
            if (cal == 10 || cal == 11) {
                cal = ((++animationInde) % 2) + 10;
                xenemy = xenemy - 300 * deltaTime;

            }
            scoree += .125;
            score = (int) scoree;
            if (scoree % 10 == 0 && score != 0) {
                sound = true;
            }
            sound1();
            highscore = Math.max(highscore, score);
            //  highscore_name = newname; 
            //     System.out.println("score"+score);
            if (xenemy < -500) {
                if (score < 20) {
                    cal = 5 + ((int) (Math.random() * 5));
                    yenemy = -125;
                } else if ((5 + ((int) (Math.random() * 6))) == 10) {

                    if ((((int) (Math.random() * 2))) == 1) {
                        cal = ((++animationInde) % 2) + 10;
                        yenemy = yenemy + 75;
                    } else {
                        yenemy = yenemy + 10;
                        cal = ((++animationInde) % 2) + 10;
                    }
                } else {
                    cal = 5 + ((int) (Math.random() * 5));
                    yenemy = -125;
                }
                xenemy = 450;
            }

            DrawCactus(gl, xenemy, yenemy, cal);
        }

        DrawScore();

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl, float x, float y, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndecies[index]);

        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-50.0f, -50.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(50.0f, -50.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(50.0f, 50.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-50.0f, 50.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawCactus(GL gl, float x, float y, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndecies[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-50.0f, -50.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(50.0f, -50.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(50.0f, 50.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-50.0f, 50.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl, float x) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndecies[textureIndecies.length - 1]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x, 0, 0);
        gl.glScalef(4 * maxWidth / 2, maxHeight / 2, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        //  gl.glTranslated(.5, .5, 1);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    /*
     * KeyListener
     */
    public float getDeltaTime() {
        Duration timeSinceStart = Duration.between(startTime, LocalTime.now());
        int deltaTime = timeSinceStart.minusSeconds(elapsedSeconds).getNano() - elapsedNanos;
        if (timeSinceStart.getSeconds() > elapsedSeconds) {
            // System.out.println(frames + " " + elapsedSeconds);
            frames = 0;
            elapsedSeconds = timeSinceStart.getSeconds();
        } else {
            frames++;
        }
        elapsedNanos = elapsedNanos + deltaTime;
        //  System.out.println(elapsedNanos + " - " + deltaTime);

        return deltaTime < 0 ? 0 : deltaTime / 1000000000f;
    }

    public void handleKeyPress() {
        if ((isKeyPressed(KeyEvent.VK_SPACE) || isKeyPressed(KeyEvent.VK_UP)) && Math.abs(x - xenemy) < 75 && (Math.abs(y - yenemy) < 75 || Math.abs(y - yenemy) < 125)) {
            cal = 5;
            xenemy = 450;
            yenemy = - 125;
            score = 0;
            scoree = 0;
            lol = false;

        }
        if (!inair) {
            if (isKeyPressed(KeyEvent.VK_SPACE)) {
                if (y < 100) {
                    isJumping = true;

                }

            }
            if (isKeyPressed(KeyEvent.VK_UP)) {
                if (y < 100) {
                    isJumping = true;

                }

            }

        }
        if (isKeyPressed(KeyEvent.VK_DOWN)) {

            {
                down = true;

            }
        }
        if (isKeyPressed(KeyEvent.VK_ESCAPE)) {

            pause = true;

        }
        if (pause == true) {
            if (isKeyPressed(KeyEvent.VK_R)) {

                pause = false;

            }
            if (isKeyPressed(KeyEvent.VK_E)) {

                System.exit(0);

            }
            /*        if (isKeyPressed(KeyEvent.VK_M)){
         
       M = true;
Bunny.setvis(false);       
new NewJFrame1().setVisible(true);
        
        
          }*/

        }
    }

    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care 
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    public void DrawScore() {
        tr.beginRendering(500, 300);
        tr.setColor(Color.black);
        tr.draw("Player : " + newname, 8, 280);
        tr.draw("Score : " + score, 8, 260);
        tr.draw("High score : " + highscore, 400, 270);
        //tr.draw("High score player name : " + highscore_name, 300, 280);
        tr.setColor(Color.WHITE);
        tr.endRendering();
    }

    public void sound() {

        if (!lol) {
            try {
                FileInputStream music = new FileInputStream(new File("GameOver.wav"));
                AudioStream audios = new AudioStream(music);
                AudioPlayer.player.start(audios);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }

        }
        lol = true;
    }

    public void sound1() {
        if (sound) {
            try {
                FileInputStream music = new FileInputStream(new File("Score%100.wav"));
                AudioStream audios = new AudioStream(music);
                AudioPlayer.player.start(audios);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
        }
        sound = false;
    }
}
