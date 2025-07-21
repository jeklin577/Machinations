package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private static final int TILE_SIZE = 96; // Adjusted for scaling
    private static final int DRAW_SIZE = 96; // Draw at 96x96

    private Animation<TextureRegion> upAnimation;
    private Animation<TextureRegion> downAnimation;
    private Animation<TextureRegion> leftAnimation;
    private Animation<TextureRegion> rightAnimation;
    private Animation<TextureRegion> currentAnimation;
    private float animationTime;

    private Vector2 position;
    private Vector2 targetPosition;
    private Vector2 initialPosition;
    private boolean moving;
    private boolean bouncing;

    private GameMap map;
    private int x;
    private int y;
    private int facingX;
    private int facingY;

    public PlayerCharacter pc;
    private DialogueSystem dialogueSystem;
    private Log log;
    private PauseMenu pauseMenu;
    private int currentMapIndex;

    Sound failmovesound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/failmove.wav"));

    public Player(Texture upTexture, Texture downTexture, Texture leftTexture, Texture rightTexture, GameMap map, int startX, int startY, DialogueSystem dialogueSystem, Log log) {
        this.upAnimation = createAnimation(upTexture);
        this.downAnimation = createAnimation(downTexture);
        this.leftAnimation = createAnimation(leftTexture);
        this.rightAnimation = createAnimation(rightTexture);
        this.currentAnimation = downAnimation;

        this.map = map;
        this.x = startX;
        this.y = startY;
        this.dialogueSystem = dialogueSystem;
        this.log = log;
        this.facingX = 0;
        this.facingY = -1;

        this.position = new Vector2(x * TILE_SIZE, y * TILE_SIZE);
        this.targetPosition = new Vector2(position);
        this.initialPosition = new Vector2(position);
        this.moving = false;
        this.bouncing = false;

        this.pauseMenu = new PauseMenu();
    }

    private Animation<TextureRegion> createAnimation(Texture texture) {
        TextureRegion[][] frames = TextureRegion.split(texture, 32, 32);
        return new Animation<>(0.1f, frames[0]);
    }

    public void draw(SpriteBatch batch) {
        animationTime += Gdx.graphics.getDeltaTime();
        batch.draw(currentAnimation.getKeyFrame(animationTime, true), position.x, position.y, DRAW_SIZE, DRAW_SIZE);

        if (pauseMenu.isVisible()) {
            pauseMenu.render();
        }
    }

    public void handleInput() {
        if (pauseMenu.isVisible()) {
            pauseMenu.handlePauseInput();
            if (pauseMenu.isExitButtonPressed()) Gdx.app.exit();
            return;
        }

        if (dialogueSystem.isDialogueActive()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                dialogueSystem.toggleDialogue();
            }
            return;
        }

        if (!moving && !bouncing) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                dialogueSystem.clear();
                Texture characterATexture = new Texture(Gdx.files.internal("Portraits/CharacterA.png"));
                Texture characterBTexture = new Texture(Gdx.files.internal("Portraits/CharacterB.png"));
                List<Dialogue> dialogueSequence1 = new ArrayList<>();
                dialogueSequence1.add(new Dialogue("Ayy lmao", "Hello! Welcome to SPACE...", characterATexture));
                dialogueSequence1.add(new Dialogue("Alieum", "Ah cheers mate i love space", characterBTexture));
                dialogueSystem.addDialogues(dialogueSequence1);
                dialogueSystem.toggleDialogue();
                return;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                dialogueSystem.clear();
                dialogueSystem.addDialogue(pc.speak("Hello!"));
                dialogueSystem.addDialogue(pc.speak("I'm the main character! try switching my portrait with I!"));
                dialogueSystem.toggleDialogue();
                return;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                Texture otherGuy = new Texture(Gdx.files.internal("Portraits/GhostmanPortrait.png"));
                pc.setPortrait(otherGuy);
                log.addEntry("Change Player Sprite");
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                attemptMove(x - 1, y, leftAnimation, -1, 0);
                log.addEntry("Move Left");
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                attemptMove(x + 1, y, rightAnimation, 1, 0);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                attemptMove(x, y + 1, upAnimation, 0, 1);    // Changed from y - 1 to y + 1
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                attemptMove(x, y - 1, downAnimation, 0, -1);   // Changed from y + 1 to y - 1
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                dialogueSystem.toggleDialogue();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                map.changeTile(0, 0, GameMap.TRIGGER_TILE);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                map.transportPlayer(this, 3, 0, 0);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                map.addEventTile(7, 7, Event.createClimbingEvent(map, dialogueSystem, GameScreen.getLog()));
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                map.addEventTile(6, 6, Event.createMapIndex4TransportEvent(map, dialogueSystem,GameScreen.getLog()));
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                log.addEntry("Added entry and checked event on tile");
                System.out.println("Added entry to log");
                checkNextTileForEvent();
                System.out.println("Map player: " + map.getPlayer());
                System.out.println("Controlled player: " + this);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                checkNextTileForEvent();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                pauseMenu.toggleVisibility();
                return;
            }
        }

        if (moving) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            position.x = Interpolation.linear.apply(position.x, targetPosition.x, deltaTime * 25f);
            position.y = Interpolation.linear.apply(position.y, targetPosition.y, deltaTime * 25f);
            if (position.epsilonEquals(targetPosition, 0.1f)) {
                position.set(targetPosition);
                moving = false;
            }
        }

        if (bouncing) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            position.x = Interpolation.linear.apply(position.x, initialPosition.x, deltaTime * 20f);
            position.y = Interpolation.linear.apply(position.y, initialPosition.y, deltaTime * 20f);
            if (position.epsilonEquals(initialPosition, 0.1f)) {
                position.set(initialPosition);
                bouncing = false;
            }
        }
    }

    private void attemptMove(int newX, int newY, Animation<TextureRegion> newAnimation, int faceX, int faceY) {
        if (isPositionValid(newX, newY)) {
            currentAnimation = newAnimation;
            x = newX;
            y = newY;
            facingX = faceX;
            facingY = faceY;
            // Changed y-flip to direct positioning
            targetPosition.set(x * TILE_SIZE, y * TILE_SIZE);
            initialPosition.set(position);
            moving = true;

            triggerTileEvent(x, y);

            int[] triggerTilePos = map.getTriggerTilePosition();
            if (x == triggerTilePos[0] && y == triggerTilePos[1]) {
                int newMapIndex = map.getTriggerMapIndex();
                map.setMapIndex(newMapIndex);
                x = 0;
                y = 0;
                // Changed y-flip to direct positioning
                targetPosition.set(x * TILE_SIZE, y * TILE_SIZE);
                position.set(targetPosition);
            }
        } else {
            if (!bouncing) {
                currentAnimation = newAnimation;
                facingX = faceX;
                facingY = faceY;
                initialPosition.set(position);
                targetPosition.set(position.x + (faceX * TILE_SIZE / 6f), position.y + (faceY * TILE_SIZE / 6f));
                moving = false;
                bouncing = true;
                failmovesound.play(1.0f);
            }
        }
    }

    public void triggerTileEvent(int x, int y) {
        Event[][] events = map.getEvents(); // You may need to add a getter in GameMap if `events` is private
        if (events != null && x >= 0 && x < events.length && y >= 0 && y < events[0].length) {
            Event event = events[x][y];
            if (event != null && pc != null) {
                event.trigger(pc);
            }
        }
    }

    private void checkNextTileForEvent() {
        int nextX = x + facingX;
        int nextY = y + facingY;

        int width = map.getWidth();
        int height = map.getHeight();

        if (nextX >= 0 && nextX < width && nextY >= 0 && nextY < height) {
            Event event = map.getEventAt(nextX, nextY);
            if (event != null) {
                event.trigger(pc);
                log.addEntry("Triggered event on tile (" + nextX + ", " + nextY + ")");
            } else {
                log.addEntry("No event on tile (" + nextX + ", " + nextY + ")");
            }
        }
    }

    private boolean isPositionValid(int x, int y) {
        int width = map.getWidth();
        int height = map.getHeight();

        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        return map.isPassable(x, y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.position.set(x * TILE_SIZE, y * TILE_SIZE);
        this.targetPosition.set(position);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public void setPc(PlayerCharacter pc) {
        this.pc = pc;
    }

    public PlayerCharacter getPc() {
        return this.pc;
    }
}
