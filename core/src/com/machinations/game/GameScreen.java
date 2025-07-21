package com.machinations.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.InputAdapter;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;

    private Player player;
    private GameMap map;
    private PartyUI partyUI;
    private DialogueSystem dialogueSystem;
    private static Log log;

    private Texture[] playerTextures;

    private boolean isPaused;

    public GameScreen() {
        batch = new SpriteBatch();

        font = new BitmapFont(Gdx.files.internal("mainfont.fnt"));
        log = new Log(font, batch, 10, 10, 700, 400, 5000);
        log.addEntry("Game started");
        log.addEntry("Testicles");
        log.addEntry("Is cool");

        // Load player textures
        playerTextures = new Texture[4];
        playerTextures[0] = new Texture("player_up.png");
        playerTextures[1] = new Texture("player_down.png");
        playerTextures[2] = new Texture("player_left.png");
        playerTextures[3] = new Texture("player_right.png");

        // Setup map tiles as before...
        Texture tileSheet = new Texture(Gdx.files.internal("Tiles/Spritesheet.png"));
        int tileWidth = 40;
        int tileHeight = 40;
        int tilesPerRow = tileSheet.getWidth() / tileWidth;
        int tilesPerColumn = tileSheet.getHeight() / tileHeight;

        TextureRegion[] tileRegions = new TextureRegion[tilesPerRow * tilesPerColumn];
        for (int row = 0; row < tilesPerColumn; row++) {
            for (int col = 0; col < tilesPerRow; col++) {
                int index = row * tilesPerRow + col;
                tileRegions[index] = new TextureRegion(tileSheet, col * tileWidth, row * tileHeight, tileWidth, tileHeight);
            }
        }

        map = GameMap.getInstance(tileRegions, 0);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        dialogueSystem = new DialogueSystem(skin);

        // Create Player instance
        player = new Player(
                playerTextures[0], playerTextures[1], playerTextures[2], playerTextures[3],
                map,
                0, 0,
                dialogueSystem,
                log
        );

        // Initialize party and UI (add your party setup here)

        // Initialize class level progression
        ClassLevelProgression party1Prog = new ClassLevelProgression();
        party1Prog.initializeClassProgressions();

        // Initialize testDummy with example stats
        PlayerCharacter testDummy = new PlayerCharacter(
                "Dummy the expert",     // Name
                "Human",          // Species
                "Expert",         // Class type
                party1Prog,       // ClassLevelProgression
                10,               // Charisma score
                12,               // Comeliness score
                14,               // Constitution score
                16,               // Dexterity score
                18,               // Strength score
                20,               // Intelligence score
                22                // Wisdom score
        );

        PlayerCharacter Testicles = new PlayerCharacter(
                "Testicles the Psion",     // Name
                "Roman",          // Species
                "Psion",         // Class type
                party1Prog,       // ClassLevelProgression
                10,               // Charisma score
                12,               // Comeliness score
                14,               // Constitution score
                16,               // Dexterity score
                18,               // Strength score
                20,               // Intelligence score
                22                // Wisdom score
        );

        PlayerCharacter secondDummy = new PlayerCharacter(
                "Second Dummy the killer",      // Name
                "Scholar",           // Species
                "Killer",             // Class type
                party1Prog,          // Class progression
                14,                  // Charisma
                15,                  // Comeliness
                12,                  // Constitution
                18,                  // Dexterity
                16,                  // Strength
                19,                  // Intelligence
                21                   // Wisdom
        );

        PlayerCharacter AverageDummy = new PlayerCharacter(
                "Average the Scholar",      // Name
                "Scholar",           // Species
                "Scholar",             // Class type
                party1Prog,          // Class progression
                10,                  // Charisma
                10,                  // Comeliness
                10,                  // Constitution
                10,                  // Dexterity
                10,                  // Strength
                10,                  // Intelligence
                10                   // Wisdom
        );

        Party party = new Party();
        party.addMember(testDummy);
        party.addMember(AverageDummy);
        AverageDummy.setPortrait(new Texture(Gdx.files.internal("Portraits/FlyPortrait.png")));
        party.addMember(Testicles);

        Testicles.setPortrait(new Texture(Gdx.files.internal("Portraits/AlienKingPortrait.png")));
        party.addMember(secondDummy);
        secondDummy.setPortrait(new Texture(Gdx.files.internal("Portraits/SnowfflePortrait.png")));
        this.partyUI = new PartyUI(party);  // Assuming your party instance exists

        // add members etc.
        partyUI = new PartyUI(party);

        // Associate player with map
        map.setPlayer(player);

        isPaused = false;
    }

    //Allows grabbing info for UI setup
    public static Log getLog() {
        return log;
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        map.draw(batch);
        player.handleInput();
        player.draw(batch);
        log.render();
        batch.end();

        partyUI.render(delta);

        if (dialogueSystem.isDialogueActive()) {
            dialogueSystem.update(delta);
            dialogueSystem.render();

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (!dialogueSystem.isTextFullyRevealed()) {
                    dialogueSystem.skipToFullText();
                } else {
                    dialogueSystem.nextDialogue();
                }
            }
        }
    }



    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        dialogueSystem.dispose();

        for (Texture texture : playerTextures) {
            texture.dispose();
        }

        // Dispose other resources as needed
    }

    // Other Screen methods: show, resize, pause, resume, hide can be left empty or used as needed
    @Override public void show() {

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                log.handleScroll(amountY);
                return true;
            }
        });
    }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}