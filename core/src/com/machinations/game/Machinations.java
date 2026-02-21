package com.machinations.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Machinations extends Game {

	private GameScreen gameScreen;
	private Skin skin;
	private PlayerCharacter playerCharacter; // <-- add this

	public void startGame() {
		if (playerCharacter == null) {
			System.out.println("Cannot start game: no player character set.");
			return;
		}

		// Create GameScreen
		gameScreen = new GameScreen();

		// If your GameScreen needs to know about the PlayerCharacter, you can
		// pass it via a setter or constructor
		gameScreen.setPlayerCharacter(playerCharacter);

		// Set the screen to the game
		setScreen(gameScreen);
	}

	public void setPlayerCharacter(PlayerCharacter pc) {
		this.playerCharacter = pc;
	}

	public PlayerCharacter getPlayerCharacter() {
		return playerCharacter;
	}

	@Override
	public void create() {
		// Load your texture atlas and skin here
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("atlas/UIAtlas.atlas"));
		skin = new Skin(Gdx.files.internal("atlas/UI.json"), atlas);

		gameScreen = new GameScreen();



		// Pass the skin to your CharacterCreationScreen constructor
		setScreen((Screen) new CharacterCreationScreen(this,skin));
		// setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		gameScreen.dispose();
		skin.dispose();
	}
}
