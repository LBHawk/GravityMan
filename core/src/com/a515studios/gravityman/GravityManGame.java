package com.a515studios.gravityman;

import com.a515studios.gravityman.states.GameStateManager;
import com.a515studios.gravityman.states.RunState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GravityManGame extends ApplicationAdapter {
	private GameStateManager gsm;
	private SpriteBatch batch;
	private AdsController adsController;
	//private AssetLoader assetLoader;


	public GravityManGame(AdsController adsController){
		this.adsController = adsController;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		adsController.showBannerAd();
		//assetLoader = new AssetLoader();
		//assetLoader.load();
		gsm.push(new RunState(gsm));
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
}
