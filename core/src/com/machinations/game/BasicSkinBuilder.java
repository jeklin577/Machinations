package com.machinations.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class BasicSkinBuilder {

    /**
     * Create a very basic skin with minimal styling and no animations.
     */
    public static Skin createSkin() {
        Skin skin = new Skin();

        // 1x1 white texture for backgrounds
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture whiteTexture = new Texture(pixmap);
        pixmap.dispose();
        skin.add("white", whiteTexture);

        // Default bitmap font
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // TextButton style - solid color backgrounds, no animations
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = createColoredDrawable(skin, Color.DARK_GRAY);
        textButtonStyle.down = createColoredDrawable(skin, Color.DARK_GRAY.cpy().mul(0.8f));
        textButtonStyle.over = createColoredDrawable(skin, Color.LIGHT_GRAY);
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;
        skin.add("default", textButtonStyle);

        // TextField style - simple background, cursor, and selection
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = createColoredDrawable(skin, Color.DARK_GRAY);
        textFieldStyle.cursor = createColoredDrawable(skin, Color.WHITE);
        textFieldStyle.selection = createColoredDrawable(skin, Color.LIGHT_GRAY);
        skin.add("default", textFieldStyle);

        // SelectBox style - no fade animations, simple backgrounds and selection
        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = font;
        selectBoxStyle.fontColor = Color.WHITE;
        selectBoxStyle.background = createColoredDrawable(skin, Color.DARK_GRAY);

        // ScrollPaneStyle for dropdown scroll area (no fade)
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = createColoredDrawable(skin, Color.DARK_GRAY);
        selectBoxStyle.scrollStyle = scrollPaneStyle;

        // ListStyle for dropdown list
        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = Color.BLACK;
        listStyle.fontColorUnselected = Color.WHITE;
        listStyle.selection = createColoredDrawable(skin, Color.LIGHT_GRAY);
        listStyle.background = createColoredDrawable(skin, Color.DARK_GRAY); // ✅ Added background
        selectBoxStyle.listStyle = listStyle;

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.background = createColoredDrawable(skin, Color.DARK_GRAY);
        windowStyle.titleFont = new BitmapFont(); // or load a font from skin/fonts
        windowStyle.titleFontColor = Color.WHITE;

        skin.add("default", windowStyle);

        skin.add("default", selectBoxStyle);

        return skin;
    }

    private static TextureRegionDrawable createColoredDrawable(Skin skin, Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        skin.add("colorTex" + color.toString(), texture);
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
}