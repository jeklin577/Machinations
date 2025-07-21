package com.machinations.game;

import com.badlogic.gdx.graphics.Texture;

public class Dialogue {
    private String speaker;
    private String text;
    private Texture speakerImage;

    public Dialogue(String speaker, String text, Texture speakerImage) {
        this.speaker = speaker;
        this.text = text;
        this.speakerImage = speakerImage;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getText() {
        return text;
    }

    public Texture getSpeakerImage() {
        return speakerImage;
    }
}
