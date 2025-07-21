package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseMenu {
    private Stage stage;
    private boolean isVisible;
    private final TextButton exitButton;
    private final TextButton partyButton;
    private final TextButton inventoryButton;
    private final TextButton galacticGuideButton;

    private final TextButton optionsButton;
    private int selectedIndex;

    public PauseMenu() {
        stage = new Stage(new ScreenViewport());

        // Create a font for the buttons
        BitmapFont font = new BitmapFont(Gdx.files.internal("mainfont.fnt")); // Default font
        font.getData().setScale(1); // Scale the font size as needed

        // Create a TextButtonStyle manually with a simple background color
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        // Optional: Use a plain color as background if no textures
        Drawable background = new TextureRegionDrawable(new Texture(Gdx.files.internal("white.png")));
        buttonStyle.up = ((TextureRegionDrawable) background).tint(Color.DARK_GRAY);
        buttonStyle.down = ((TextureRegionDrawable) background).tint(Color.GRAY);

        // Create buttons with the manual style
        exitButton = new TextButton("Exit", buttonStyle);
        partyButton = new TextButton("Party", buttonStyle);
        inventoryButton = new TextButton("Inventory", buttonStyle);
        galacticGuideButton = new TextButton("Galactic Guide", buttonStyle);
        optionsButton = new TextButton("Options", buttonStyle);

        // Set minimum size for buttons to make them larger
        float buttonWidth = 200f;
        float buttonHeight = 80f;
        exitButton.setSize(buttonWidth, buttonHeight);
        partyButton.setSize(buttonWidth, buttonHeight);
        inventoryButton.setSize(buttonWidth, buttonHeight);
        galacticGuideButton.setSize(buttonWidth, buttonHeight);
        optionsButton.setSize(buttonWidth, buttonHeight);

        // Create a table to organize the buttons
        Table table = new Table();
        table.top().left(); // Align table to the top-left corner
        table.setFillParent(true); // Make the table fill the stage

        // Add buttons to the table with smaller padding and alignment
        float padding = 5f; // Smaller padding value
        table.add(exitButton).pad(padding).minSize(buttonWidth, buttonHeight).align(Align.left).row();
        table.add(partyButton).pad(padding).minSize(buttonWidth, buttonHeight).align(Align.left).row();
        table.add(inventoryButton).pad(padding).minSize(buttonWidth, buttonHeight).align(Align.left).row();
        table.add(galacticGuideButton).pad(padding).minSize(buttonWidth, buttonHeight).align(Align.left).row();
        table.add(optionsButton).pad(padding).minSize(buttonWidth, buttonHeight).align(Align.left);

        // Add the table to the stage
        stage.addActor(table);

        isVisible = false;
        selectedIndex = 0; // Initialize the selected index to the first button
        updateButtonSelection(); // Highlight the initial button
    }

    public void render() {
        if (isVisible) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    public void handlePauseInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            if (selectedIndex < 0) {
                selectedIndex = 4; // Wrap around to the last button
            }
            updateButtonSelection();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            if (selectedIndex > 4) {
                selectedIndex = 0; // Wrap around to the first button
            }
            updateButtonSelection();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            // Trigger the action of the selected button
            switch (selectedIndex) {
                case 0:
                    // Exit button action
                    handleExit();
                    break;
                case 1:
                    // Party button action
                    handleParty();
                    break;
                case 2:
                    // Inventory button action
                    handleInventory();
                    break;
                case 3:
                    // Galactic Guide button action
                    handleGalacticGuide();
                    break;

                case 4:
                    handleOptions();
                    break;
            }
        }
    }

    private void updateButtonSelection() {
        // Reset all button colors
        exitButton.setColor(Color.WHITE);
        partyButton.setColor(Color.WHITE);
        inventoryButton.setColor(Color.WHITE);
        galacticGuideButton.setColor(Color.WHITE);
        optionsButton.setColor(Color.WHITE);

        // Highlight the selected button
        switch (selectedIndex) {
            case 0:
                exitButton.setColor(Color.YELLOW);
                break;
            case 1:
                partyButton.setColor(Color.YELLOW);
                break;
            case 2:
                inventoryButton.setColor(Color.YELLOW);
                break;
            case 3:
                galacticGuideButton.setColor(Color.YELLOW);
                break;

            case 4:
                optionsButton.setColor(Color.YELLOW);
                break;
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void toggleVisibility() {
        isVisible = !isVisible;
        Gdx.input.setInputProcessor(isVisible ? stage : null); // Set input processor to stage when menu is visible
    }

    public boolean isExitButtonPressed() {
        return exitButton.isPressed();
    }

    public boolean isPartyButtonPressed() {
        return partyButton.isPressed();
    }

    public boolean isInventoryButtonPressed() {
        return inventoryButton.isPressed();
    }

    public boolean isGalacticGuideButtonPressed() {
        return galacticGuideButton.isPressed();
    }

    public void dispose() {
        stage.dispose();
        exitButton.getStyle().font.dispose();
    }

    private void handleExit() {
        // Implement the exit button action here
        System.out.println("Exit button pressed");
        this.isVisible = false;
    }

    private void handleParty() {
        // Implement the party button action here
        System.out.println("Party button pressed");
    }

    private void handleInventory() {
        // Implement the inventory button action here
        System.out.println("Inventory button pressed");
    }

    private void handleGalacticGuide() {
        // Implement the galactic guide button action here
        System.out.println("Galactic Guide button pressed");
    }

    private void handleOptions() {
        // Implement the galactic guide button action here
        System.out.println("Options button pressed");
    }
}
