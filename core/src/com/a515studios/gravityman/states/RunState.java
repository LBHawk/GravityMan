package com.a515studios.gravityman.states;

import com.a515studios.gravityman.AssetLoader.AssetLoader;
import com.a515studios.gravityman.sprites.Box;
import com.a515studios.gravityman.sprites.Dot;
import com.a515studios.gravityman.sprites.Man;
import com.a515studios.gravityman.states.GameStateManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

import sun.rmi.runtime.Log;

/**
 * Created by Luke on 6/12/2016.
 */
public class RunState extends State {
    private int state;
    private GameStateManager gsm;
    private SpriteBatch batch;
    private AssetLoader assetLoader;

    // ------------MENU---------------
    private static final float SCREENWIDTH = Gdx.graphics.getWidth();
    private static final float SCREENHEIGHT = Gdx.graphics.getHeight();
    private Man man;
    private Texture logo;
    private Texture ground;
    private TextureAtlas tapAtlas;
    private TextureRegion currentFrame;
    private Animation tap;
    private float timePassed;
    private int highScore;
    private BitmapFont font;
    //private AssetLoader assetLoader;
    //-------------MENU----------------

    //-------------PLAY----------------
    private static final int TUBE_COUNT = 8;
    //private static final float SCREENWIDTH = Gdx.graphics.getWidth();
    //private static final float SCREENHEIGHT = Gdx.graphics.getHeight();
    private static final float[] YOFFSETS = {SCREENHEIGHT*11/12 - 150,SCREENHEIGHT*10/12 - 150, SCREENHEIGHT*9/12 - 150, SCREENHEIGHT*8/12 - 150, -500 + 150, -566 + 150, -633 + 150, -700 + 150};
    //private Man man;
    //private float timePassed;
    //private Texture ground;
    private Random rand;
    private int score;
    //private int highScore;

    private Box[] boxes;
    private int lastBox;
    private Dot[] dots;

    private boolean ready; // for restart
    private long deadTime;
    private float logoX;
    private float logoY;
    //-------------PLAY---------------

    private Sound grav;
    private Sound death;
    private Music music;

    public RunState(GameStateManager gsm){
        super(gsm);
        grav = Gdx.audio.newSound(Gdx.files.internal("gravity2.ogg"));
        death = Gdx.audio.newSound(Gdx.files.internal("PUNCH.ogg"));
        music = Gdx.audio.newMusic(Gdx.files.internal("Platformer2.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
        state = 0;
        assetLoader = new AssetLoader();
        assetLoader.load();
        man = new Man(150,500);
        rand = new Random();
        cam.setToOrtho(false);

        boxes = new Box[8];
        populateBoxes();
        ready = false;

        dots = new Dot[15];
        for(int i = 0; i < dots.length; i++){
            dots[i] = new Dot(1);
        }

        logo = new Texture("Logo.png");
        logoX = cam.position.x - (logo.getWidth() / 2);
        logoY = cam.position.y - (logo.getHeight() / 2);
        ground = new Texture("Ground.png");
        tapAtlas = new TextureAtlas(Gdx.files.internal("Pointer.atlas"));
        tap = new Animation(1/2f, tapAtlas.getRegions());
        timePassed = 0;
        score = 0;
        //assetLoader.resetHigh();
        highScore = this.assetLoader.getHighScore();
        font = new BitmapFont();
        font.getData().setScale(5,5);
        deadTime = 0;
    }

    public void populateBoxes(){
        for(int i = 0 ; i < boxes.length; i++){
            boxes[i] = new Box(man.getPosition().x + SCREENWIDTH + ((i+1)*1800), YOFFSETS[rand.nextInt(8)]);
        }
        lastBox = boxes.length - 1;
    }

    public void goToPlay(){
        state = 1;
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            switch(state) {
                case 0:
                    populateBoxes();
                    state = 1;
                    man.changeGrav();
                    grav.play();
                    //System.out.println("0 TO 1");
                    break;

               case 1:
                   grav.play(0.7f);
                   man.changeGrav();

                    //System.out.println("change grav");
                    break;
               case 2:
                   //System.out.println("restart :" + System.currentTimeMillis() + " - " + deadTime);
                   restartGame();
                   break;
            }
        }
    }

    public void restartGame(){
        if(ready){
            music.play();
            man.restart(150,500);
            score = 0;
            state = 0;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        switch(state){
            case 0:
                man.update(dt);
                logoX = cam.position.x - (logo.getWidth() / 2);
                logoY = cam.position.y - (logo.getHeight() / 2);
                break;
            case 1:
                ready = false;
                man.update(dt);
                //xpos = man.getPosition().x;
                //xvol = man.getVelocity().x;

                for(int i = 0; i < boxes.length; i++){
                    Box box = boxes[i];
                    if(cam.position.x - (cam.viewportWidth/2) > box.getPosition().x + box.getBox().getWidth()){
                        box.reposition(boxes[lastBox].getPosition().x + 350, YOFFSETS[rand.nextInt(8)], rand.nextInt(551));
                        man.incrementVel();
                        score++;
                        lastBox = i;
                    }

                    if(box.collides(man.getBounds())){
                        collision();

                        //gsm.pop();

                    }
                }
                break;
            case 2:
                for(Dot d:dots){
                    d.update(dt);
                }
                if(System.currentTimeMillis() - deadTime > 1000){
                    ready = true;// FILL IN
                }
                break;
        }

        cam.position.x = man.getPosition().x + (SCREENWIDTH / 3);
        cam.update();
    }

    public void collision(){
        music.stop();
        death.play(0.5f);
        float xvel = man.getVelocity().x;
        float yvel = man.getVelocity().y;
        float xpos = man.getPosition().x;
        float ypos = man.getPosition().y;
        for(int i = 0; i < dots.length; i++){
            dots[i].setPosition(xpos + rand.nextInt(100), ypos + rand.nextInt(100));
            dots[i].setVelocity(xvel + rand.nextInt(100), yvel + rand.nextInt(100));
            dots[i].setGravityDir(man.getGravityDir());
        }
        man.setVelocity(0,0);
        deadTime = System.currentTimeMillis();
        if(score > highScore){
            highScore = score;
            assetLoader.setHighScore(score);
        }
        state = 2;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        timePassed += Gdx.graphics.getDeltaTime();

        switch (state){
            case 0:
                font.setColor(Color.WHITE);
                sb.draw(tap.getKeyFrame(timePassed, true), cam.position.x - 100, 175);
                sb.draw(ground, cam.position.x - (cam.viewportWidth / 2), 0);
                sb.draw(ground, cam.position.x - (cam.viewportWidth / 2), SCREENHEIGHT - 150);
                sb.draw(man.getCurrentFrame(), man.getPosition().x, man.getPosition().y, 100, 100);
                sb.draw(logo, logoX, logoY);
                font.draw(sb, "High Score: " + highScore, cam.position.x - (SCREENWIDTH / 2) + 150, SCREENHEIGHT - 50);
                font.draw(sb, "Score: " + score, cam.position.x + (SCREENWIDTH / 4), SCREENHEIGHT - 50);
                break;
            case 1:
                boolean flip = (man.getGravityDir() == -1);
                drawBoxes(sb);
                sb.draw(ground, cam.position.x - (cam.viewportWidth / 2), 0);
                sb.draw(ground, cam.position.x - (cam.viewportWidth / 2), SCREENHEIGHT - 150);
                sb.draw(man.getCurrentFrame(), man.getPosition().x, flip ? (100 + man.getPosition().y) : man.getPosition().y, 100, flip ? -100 : 100);
                sb.draw(logo, logoX, logoY);
                font.draw(sb, "High Score: " + highScore, cam.position.x - (SCREENWIDTH / 2) + 150, SCREENHEIGHT - 50);
                font.draw(sb, "Score: " + score, cam.position.x + (SCREENWIDTH / 4), SCREENHEIGHT - 50);
                break;
            case 2:
                drawBoxes(sb);
                sb.draw(ground, cam.position.x - (cam.viewportWidth / 2), 0);
                sb.draw(ground, cam.position.x - (cam.viewportWidth / 2), SCREENHEIGHT - 150);
                drawDots(sb);
                font.setColor(Color.BLACK);
                if(ready) {
                font.draw(sb, "      Game Over\n        Score: " + score + "\n  Tap to Continue", cam.position.x - (SCREENWIDTH / 6), cam.position.y + (SCREENHEIGHT / 6));
                    sb.draw(tap.getKeyFrame(timePassed, true), cam.position.x - 100, 175);
                }
                break;

        }

        sb.end();

    }

    public void drawDots(SpriteBatch sb){
        for(int i = 0; i < dots.length; i++){
            sb.draw(dots[i].getDot(), dots[i].getPosition().x, dots[i].getPosition().y);
        }
    }

    public void drawBoxes(SpriteBatch sb){
        for(int i = 0; i < boxes.length; i++){
            sb.draw(boxes[i].getBox(), boxes[i].getPosition().x, boxes[i].getPosition().y);
        }
    }

    @Override
    public void dispose() {
        ground.dispose();
        man.dispose();
        for(Box b : boxes){
            b.dispose();
        }

        for(Dot d : dots){
            d.dispose();
        }

        grav.dispose();
        death.dispose();

    }
}
