package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.*;

public class DialogueSystem {
    private static final int VIRTUAL_WIDTH = Gdx.graphics.getWidth();;
    private static final int VIRTUAL_HEIGHT = Gdx.graphics.getHeight();;

    private Stage stage;
    private Skin skin;
    private Table rootTable;
    private Label dialogueLabel;
    private Image speakerImage;
    private Image dialogueBoxImage;
    private boolean showDialogue;
    private final List<Dialogue> dialogues;
    private Iterator<Dialogue> dialogueIterator;
    private String fullText = "";  // Full dialogue text to reveal
    private int visibleChars = 0;  // How many characters are currently visible
    private float timePerChar = 0.03f; // Time delay per character in seconds
    private float timeSinceLastChar = 0f; // Timer accumulator
    private boolean isTextFullyRevealed = false;

    private OrthographicCamera camera;
    private Viewport viewport;

    // Constructor
    public DialogueSystem(Skin skin) {
        this.skin = skin;
        this.dialogues = new ArrayList<>();

        // Get actual screen size
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // Calculate scale factor based on a 1280x720 design baseline
        float baseWidth = 1280f;
        float baseHeight = 720f;
        float scaleX = screenWidth / baseWidth;
        float scaleY = screenHeight / baseHeight;
        float scale = Math.min(scaleX, scaleY); // maintain aspect ratio

        // Use ExtendViewport so UI scales with resolution
        ScreenViewport viewport = new ScreenViewport();
        stage = new Stage(viewport);

        rootTable = new Table();
        rootTable.setFillParent(true);

        dialogueBoxImage = new Image(new Texture(Gdx.files.internal("dialogue_box.png")));
        dialogueLabel = new Label("", skin);
        dialogueLabel.setWrap(true);
        dialogueLabel.setColor(Color.WHITE);
        dialogueLabel.setAlignment(Align.bottomLeft);

        // Optional: scale font size dynamically (if needed)
        dialogueLabel.setFontScale(scale);

        speakerImage = new Image(new Texture(Gdx.files.internal("defaultportrait.png")));

        Stack dialogueStack = new Stack();

        Table contentTable = new Table();
        contentTable.add(speakerImage)
                .size(200 * scale, 200 * scale)         // Scaled portrait size
                .align(Align.bottomLeft)
                .padLeft(10 * scale)
                .padRight(10 * scale);


        contentTable.add(dialogueLabel)
                .expand()
                .fill()
                .pad(10 * scale);

        dialogueStack.add(dialogueBoxImage);
        dialogueStack.add(contentTable);

        rootTable.add(dialogueStack)
                .expand()
                .bottom()
                .fillX();


        stage.addActor(rootTable);

        showDialogue = false;
        resetDialogueIterator();
    }

    public void resetDialogueIterator() {
        dialogueIterator = dialogues.iterator();
    }

    public void addDialogue(Dialogue dialogue) {
        synchronized (dialogues) {
            dialogues.add(dialogue);
            resetDialogueIterator();
        }
    }



    public void addDialogues(List<Dialogue> dialoguesList) {
        synchronized (dialogues) {
            dialogues.addAll(dialoguesList);
            resetDialogueIterator();
        }
    }

    public boolean isDialogueActive() {
        return showDialogue;
    }

    public void update(float delta) {
        if (!showDialogue || isTextFullyRevealed) return;

        timeSinceLastChar += delta;

        while (timeSinceLastChar >= timePerChar && visibleChars < fullText.length()) {
            visibleChars++;
            timeSinceLastChar -= timePerChar;
        }

        dialogueLabel.setText(fullText.substring(0, visibleChars));

        if (visibleChars >= fullText.length()) {
            isTextFullyRevealed = true;
        }
    }

    public void render() {
        if (showDialogue) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    public void nextDialogue() {
        if (dialogueIterator.hasNext()) {
            Dialogue currentDialogue = dialogueIterator.next();
            fullText = currentDialogue.getSpeaker() + ": " + currentDialogue.getText();
            visibleChars = 0;
            timeSinceLastChar = 0f;
            isTextFullyRevealed = false;

            Texture imageTexture = currentDialogue.getSpeakerImage();
            if (imageTexture != null) {
                speakerImage.setDrawable(new TextureRegionDrawable(imageTexture));
                speakerImage.setVisible(true);
            } else {
                speakerImage.setVisible(false);
            }
        } else {
            showDialogue = false;
            speakerImage.setVisible(false);
        }
    }

    public boolean isTextFullyRevealed() {
        return isTextFullyRevealed;
    }

    public void skipToFullText() {
        if (!showDialogue || isTextFullyRevealed) return;

        visibleChars = fullText.length();
        dialogueLabel.setText(fullText);
        isTextFullyRevealed = true;
    }

    public void toggleDialogue() {
        if (dialogues.isEmpty()) return;
        if (isDialogueActive()) return;

        showDialogue = true;
        resetDialogueIterator();
        nextDialogue(); // <---- This must be here!
    }

    public void clear() {
        synchronized (dialogues) {
            dialogues.clear();
            resetDialogueIterator();
            showDialogue = false;
            dialogueLabel.setText("");
            speakerImage.setVisible(false);
        }
    }

    public void dispose() {
        stage.dispose();
    }

    // NEW: Resize handler to be called from your game class
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }
}
