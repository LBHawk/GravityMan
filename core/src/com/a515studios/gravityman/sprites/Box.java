package com.a515studios.gravityman.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Luke on 6/11/2016.
 */
public class Box {
    private static final int FLUCTUATION = Gdx.graphics.getHeight() / 4;

    private Texture box;
    private Vector2 position;
    private Random rand;
    private int numRepos;
    private Rectangle bounds;


    public Box(float x, float y){
        box = new Texture("Box.png");
        rand = new Random();
        numRepos = 0;

        position = new Vector2(x, y);

        bounds = new Rectangle(position.x, position.y, box.getWidth(), box.getHeight());
    }

    public Texture getBox() {
        return box;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void reposition(float x, float y, float z){
        position.set(x + 550 - z, y);
        System.out.println(300-z);
        if(numRepos < 150){
            numRepos++;
        }
        bounds.setPosition(position.x, position.y);
    }

    public boolean collides(Rectangle player){
        return player.overlaps(bounds);
    }

    public void dispose(){
        box.dispose();
    }

    public void setX(int x){
        position.x = x;
    }
}
