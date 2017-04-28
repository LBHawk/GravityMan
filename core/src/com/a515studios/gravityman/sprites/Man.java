package com.a515studios.gravityman.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Luke on 6/11/2016.
 */
public class Man {
    private TextureAtlas runningAtlas;
    private TextureRegion currentFrame;
    private Animation animation;
    public float timePassed = 0;  //MAKE PRIVATE ON BUILD

    private static final int GRAVITY = -40;
    private Vector3 position;
    private Vector3 velocity;
    private int gravityDir;
    private TextureRegion falling;
    private Rectangle bounds;

    private int maxH;

    public Man(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(400, 0, 0);
        gravityDir = 1;
        maxH = Gdx.graphics.getHeight() - 250;

        runningAtlas = new TextureAtlas(Gdx.files.internal("RunningMan.atlas"));
        animation = new Animation(1/24f, runningAtlas.getRegions());
        currentFrame = animation.getKeyFrame(timePassed, true);
        falling = new TextureRegion(new Texture("falling.png"));

        bounds = new Rectangle(x + 25,y+10,65,85);
    }

    public void restart(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(400, 0, 0);
        gravityDir = 1;
        bounds = new Rectangle(x + 25,y+10,65,85);
    }

    public void incrementVel(){
        if(velocity.x < 1490) {
            velocity.x += 10;
        }else{
            velocity.x = 1500;
        }
    }
/*
    public void menuUpdate(float dt){
        timePassed += Gdx.graphics.getDeltaTime();
        if(velocity.y == 0) {
            currentFrame = animation.getKeyFrame(timePassed, true);
        }else{
            currentFrame = falling;
        }
    }
*/
    public void update(float dt){
        timePassed += Gdx.graphics.getDeltaTime();
        if(velocity.y == 0) {
            currentFrame = animation.getKeyFrame(timePassed, true);
        }else{
            currentFrame = falling;
        }
        // Only add gravity if not on bottom of screen
        if(position.y > 0) {
            velocity.add(0, GRAVITY * gravityDir, 0);
        }
        velocity.scl(dt);

        position.add(velocity.x, velocity.y, 0);

        velocity.scl(1/dt);

        if(position.y < 150){
            velocity.y = 0;
            position.y = 150;

            // Fixes getting stuck upside down
            if(gravityDir == -1){
                position.y += 1;
            }
        }

        if(position.y > maxH){
            velocity.y = 0;
            position.y = maxH;

            // Fixes getting stuck upside down
            if(gravityDir == 1){
                position.y -= 1;
            }
        }

        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void changeGrav(){
        position.y += gravityDir;
        velocity.y /= 3;
        gravityDir *= -1;
    }

    public TextureRegion getCurrentFrame(){
        return currentFrame;
    }

    public Vector3 getPosition() {
        return position;
    }

    public int getGravityDir(){
        return gravityDir;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void setVelocity(float x, float y) {
        velocity.set(x,y,0);
    }

    /*public Texture getTexture() {
        return texture;
    }*/

    public void dispose(){

    }
}
