package com.a515studios.gravityman.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Luke on 6/12/2016.
 */
public class Dot {

    private Texture dot;

    private static final int GRAVITY = -35;
    private Vector3 position;
    private Vector3 velocity;
    private int gravityDir;

    private int maxH;

    public Dot(int gravityDir){
        position = new Vector3(0, 0, 0);
        velocity = new Vector3(0, 0, 0);
        this.gravityDir = gravityDir;
        maxH = Gdx.graphics.getHeight() - 160;
        dot = new Texture("Dot.png");
    }

    public Texture getDot() {
        return dot;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        position.set(x, y, 0);
    }

    public void setVelocity(float x, float y) {
        velocity.set(x, y, 0);
    }

    public void setGravityDir(int gravityDir) {
        this.gravityDir = gravityDir;
    }

    public void dispose(){
        dot.dispose();
    }

    public void update(float dt){
        // Only add gravity if not on bottom of screen
        if(position.y > 0) {
            velocity.add(0, GRAVITY * gravityDir, 0);
        }

        if(velocity.x > 0 && (position.y == 150 || position.y == maxH)){
            velocity.add(GRAVITY, 0, 0);
        }else if(velocity.x < 0){
            velocity.x = 0;
        }
        velocity.scl(dt);

        position.add(velocity.x, velocity.y, 0);

        velocity.scl(1/dt);

        if(position.y < 150){
            velocity.y = 0;
            position.y = 150;

            // Fixes getting stuck upside down
            /*if(gravityDir == -1){
                position.y += 1;
            }*/
        }

        if(position.y > maxH) {
            velocity.y = 0;
            position.y = maxH;

            // Fixes getting stuck upside down
            /*if (gravityDir == 1) {
                position.y -= 1;
            }*/
        }
    }
}
