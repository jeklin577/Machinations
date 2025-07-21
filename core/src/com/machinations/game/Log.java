package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Align;

public class Log {
    private BitmapFont font;
    private SpriteBatch batch;
    private Array<String> entries;
    private float x;      // right edge position of log box
    private float y;      // top edge position of log box
    private float width;  // width of log box
    private float height; // height of log box
    private float scrollOffset = 0f;

    public Log(BitmapFont font, SpriteBatch batch, float marginRight, float marginTop, float width, float height, int maxEntries) {
        this.font = font;
        this.batch = batch;
        this.entries = new Array<>();

        this.x = Gdx.graphics.getWidth() - marginRight;
        this.y = Gdx.graphics.getHeight() - marginTop;

        this.width = width;
        this.height = height;
    }

    public void addEntry(String entry) {
        entries.insert(0, entry);  // Add to the start (top) of the log

        // Auto-scroll to top (show newest) after adding new entry
        scrollOffset = 0f;
    }

    public void render() {
        float wrapWidth = width;
        Array<GlyphLayout> layouts = new Array<>();
        float totalContentHeight = 0f;

        for (String entry : entries) {
            GlyphLayout layout = new GlyphLayout(font, entry, Color.WHITE, wrapWidth, Align.left, true);
            layouts.add(layout);
            totalContentHeight += layout.height + 2;
        }

        scrollOffset = MathUtils.clamp(scrollOffset, 0, Math.max(0, totalContentHeight - height));

        // Start drawing at top (y) minus scrollOffset (scroll down moves content up)
        float currentY = y - scrollOffset;

        for (GlyphLayout layout : layouts) {
            font.draw(batch, layout, x - width, currentY);
            currentY -= layout.height + 2;
            if (currentY < y - height) break;
        }
    }

    private float calculateTotalContentHeight() {
        float totalHeight = 0;
        float wrapWidth = width;

        for (String entry : entries) {
            GlyphLayout layout = new GlyphLayout(font, entry, Color.WHITE, wrapWidth, Align.left, true);
            totalHeight += layout.height + 2;
        }

        return totalHeight;
    }

    public void handleScroll(float amountY) {
        float scrollAmount = amountY * 20f;

        float totalHeight = calculateTotalContentHeight();
        float maxScroll = Math.max(0, totalHeight - height);

        // Scroll down (positive amountY) should increase scrollOffset to move view down
        scrollOffset = MathUtils.clamp(scrollOffset + scrollAmount, 0, maxScroll);
    }

    public void clear() {
        entries.clear();
        scrollOffset = 0f;
    }
}