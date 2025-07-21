package com.machinations.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Machinations extends Game {

	private GameScreen gameScreen;
	private Skin skin;

	@Override
	public void create() {
		// Load your texture atlas and skin here
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("atlas/UIAtlas.atlas"));
		skin = new Skin(Gdx.files.internal("atlas/UI.json"), atlas);

		gameScreen = new GameScreen();

		// Pass the skin to your CharacterCreationScreen constructor
		setScreen((Screen) new CharacterCreationScreen(skin));
		// setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		gameScreen.dispose();
		skin.dispose();
	}
}
