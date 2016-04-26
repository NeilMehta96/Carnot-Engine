package com.nmehta.carnotengine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nmehta.carnotengine.screens.PlayScreen;

public class CarnotEngine extends Game {
	public SpriteBatch batch;
	public static final boolean playerMovesBlack = true;
	public static final int depthOfSearch =4;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

}
