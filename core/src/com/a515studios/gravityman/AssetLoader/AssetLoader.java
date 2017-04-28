package com.a515studios.gravityman.AssetLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


/**
 * Created by Luke on 6/12/2016.
 */
public class AssetLoader {
    public static Preferences prefs;

    public void load(){
        prefs = Gdx.app.getPreferences("GravityMan");

        if(!prefs.contains("highScore")){
            prefs.putInteger("highScore", 0);
        }

    }

    public void resetHigh(){
        prefs.putInteger("highScore", 0);
        prefs.flush();
    }

    public static void setHighScore(int v){
        prefs.putInteger("highScore", v);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }
}
