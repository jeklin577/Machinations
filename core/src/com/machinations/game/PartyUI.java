package com.machinations.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class PartyUI {
    private final Party party;
    private final int portraitSize = 200; // Size of each portrait
    private final int spacing = 50;       // Space between portraits

    private final SpriteBatch batch;
    private final BitmapFont font;

    public PartyUI(Party party) {
        this.party = party;
        this.batch = new SpriteBatch();

        this.font = new BitmapFont(); // Default font
        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(1.5f); // Adjust as needed
    }

    public void render(float deltaTime) {
        batch.begin();

        int screenWidth = Gdx.graphics.getWidth();
        int columns = 2;

        for (int i = 0; i < party.getSize(); i++) {
            PlayerCharacter character = party.getMembers().get(i);
            Texture portrait = character.getPortrait();

            int col = i % columns;
            int row = i / columns;

            // Position from bottom-right
            int x = screenWidth - ((columns - col) * (portraitSize + spacing));
            int y = spacing + row * (portraitSize + spacing);

            // Draw portrait
            batch.draw(portrait, x, y, portraitSize, portraitSize);

            // Get name and health info
            String name = character.getName();
            String health = character.getHealth() + " / " + character.getMaxHP();

            // Text positioning
            float nameWidth = font.draw(batch, name, 0, 0).width;
            float nameX = x + (portraitSize - nameWidth) / 2f;
            float nameY = y - 10; // Just below portrait

            float healthX = x + portraitSize + 10; // To the right of portrait
            float healthY = y + portraitSize - 20; // Near the top of the portrait

            // Draw text
            font.draw(batch, name, nameX, nameY);
            font.draw(batch, health, healthX, healthY);
        }

        batch.end();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}