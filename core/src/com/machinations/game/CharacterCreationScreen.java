package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;

public class CharacterCreationScreen extends com.badlogic.gdx.ScreenAdapter {

    private Stage stage;
    private Skin skin;

    private Texture[] portraits;
    private int currentPortraitIndex = 0;
    private Image portraitDisplay;

    private final String[] ABILITIES = { "Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma", "Comeliness" };
    private Label[] abilityLabels = new Label[ABILITIES.length];
    private TextField[] abilityFields = new TextField[ABILITIES.length];

    private Label rollModeValueLabel;
    private String[] rollModes = { "Pussy Mode", "Easy Mode", "Normal Mode", "Hardcore Mode" };
    private int currentRollModeIndex = 0;
    private boolean uiSetupComplete = false;
    private Integer firstSelectedIndex = null;
    private final TextButton[] swapButtons = new TextButton[7];
    private boolean statsrolled = false;
    private ArrayList<Class<? extends RaceTrait>> selectedTraits = new ArrayList<>();
    private final int MAX_TRAITS = 3;

    private static final Class<? extends RaceTrait>[] ALL_TRAITS = new Class[]{
            RaceTrait.Adaptable.class, RaceTrait.Fecund.class, RaceTrait.Gasbag.class,
            RaceTrait.LimitedShapeshifting.class, RaceTrait.Resilient.class, RaceTrait.Enduring.class,
            RaceTrait.Fearful.class, RaceTrait.StrongWilled.class, RaceTrait.Tough.class,
            RaceTrait.Strong.class, RaceTrait.RapidReaction.class, RaceTrait.VeryStrong.class,
            RaceTrait.VeryTough.class, RaceTrait.HiGAdapted.class, RaceTrait.Survival.class,
            RaceTrait.Stalker.class, RaceTrait.Flight.class, RaceTrait.Mindful.class,
            RaceTrait.Thoughtful.class, RaceTrait.Beautiful.class, RaceTrait.Charming.class,
            RaceTrait.Gregarious.class, RaceTrait.Sexy.class, RaceTrait.Flexible.class,
            RaceTrait.Boiling.class, RaceTrait.DeadFlesh.class, RaceTrait.Graceful.class,
            RaceTrait.Large.class, RaceTrait.Warrior.class, RaceTrait.Weapon.class,
            RaceTrait.Chitin.class, RaceTrait.Flowering.class, RaceTrait.Regeneration.class,
            RaceTrait.Foothands.class, RaceTrait.Tail.class, RaceTrait.PsiPower.class,

            // Add more here as you implement them

            RaceTrait.SlowMetabolism.class, RaceTrait.NoVitals.class, RaceTrait.Ascetic.class,
            RaceTrait.Swimming.class, RaceTrait.KeenSight.class, RaceTrait.LightBody.class,
            RaceTrait.NaturalWeapons.class, RaceTrait.RadResist.class, RaceTrait.Rocky.class,
            RaceTrait.Consort.class, RaceTrait.Monarch.class, RaceTrait.Soldier.class,
            RaceTrait.Worker.class, RaceTrait.Camoflague.class, RaceTrait.Ambush.class,
            RaceTrait.Intrusive.class, RaceTrait.Sneaky.class, RaceTrait.TechSavant.class,
            RaceTrait.Wary.class, RaceTrait.Confident.class, RaceTrait.Excess.class,
            RaceTrait.Fashionable.class, RaceTrait.Shell.class, RaceTrait.Spines.class,
            RaceTrait.Tourist.class, RaceTrait.Linguist.class, RaceTrait.Cunning.class,
            RaceTrait.Air.class, RaceTrait.Fire.class
    };

    private ArrayList<Integer> rolledScores = new ArrayList<>();

    public CharacterCreationScreen(Skin skin) {
        this.skin = skin;
        stage = new Stage(new ScreenViewport());

        setupUI();
        Gdx.input.setInputProcessor(stage);
    }

    private void loadPortraits() {
        FileHandle portraitsDir = Gdx.files.internal("Portraits");
        FileHandle[] files = portraitsDir.list("png"); // load all PNG portraits

        portraits = new Texture[files.length];
        for (int i = 0; i < files.length; i++) {
            portraits[i] = new Texture(files[i]);
        }
    }

    private Drawable createColoredDrawable(Color color) {
        return skin.newDrawable("white", color);
    }

    private void setupUI() {
        if (uiSetupComplete) return; // prevents double-setup
        uiSetupComplete = true;
        System.out.println("Setting up UI");

        loadPortraits();

        Table table = new Table();
        table.setFillParent(true);
        table.top().right();
       // table.padTop(10).padRight(10);
// table.debug(); // Uncomment to debug layout


// White label style
        Label.LabelStyle whiteLabelStyle = new Label.LabelStyle();
        whiteLabelStyle.font = skin.getFont("main-font");
        whiteLabelStyle.fontColor = Color.WHITE;




// Roll Mode Selector
        Table rollModeTable = new Table();
        rollModeTable.align(Align.left);
        rollModeTable.padBottom(20);

        Label rollModeLabel = new Label("Roll Mode:", whiteLabelStyle);
        rollModeValueLabel = new Label(rollModes[currentRollModeIndex], whiteLabelStyle);
        rollModeValueLabel.setColor(Color.WHITE);

        TextButton changeRollModeButton = new TextButton("Change", skin);
        changeRollModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rollModeValueLabel.setText(""); // optional clear

                currentRollModeIndex = (currentRollModeIndex + 1) % rollModes.length;
                rollModeValueLabel.setText(rollModes[currentRollModeIndex]);

                System.out.println("Roll mode changed to: " + rollModes[currentRollModeIndex]);
            }
        });

        rollModeTable.add(rollModeLabel).padRight(5);
        rollModeTable.add(rollModeValueLabel).padRight(10).left();
        rollModeTable.add(changeRollModeButton);

        table.add(rollModeTable).expandX().fillX().right().padBottom(10);
        table.row();


// CheckBox style with default font (no drawables for minimal example)
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font = new BitmapFont();

// Trait selection label
        Label traitsLabel = new Label("Select Race Traits (max " + MAX_TRAITS + "):", whiteLabelStyle);
        table.add(traitsLabel).right().padTop(20).row();

        Table traitTable = new Table();
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.background = skin.newDrawable("white", new Color(0.1f, 0.1f, 0.1f, 0.5f));
        scrollStyle.vScroll = skin.newDrawable("white", Color.DARK_GRAY);
        scrollStyle.vScrollKnob = skin.newDrawable("white", Color.LIGHT_GRAY);

        ScrollPane scrollPane = new ScrollPane(traitTable, scrollStyle);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setSmoothScrolling(true);

// Add checkboxes for each trait
        for (Class<? extends RaceTrait> traitClass : ALL_TRAITS) {
            String traitName = traitClass.getSimpleName();
            CheckBox checkBox = new CheckBox(traitName, style);
            checkBox.getLabel().setColor(Color.WHITE);

            checkBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (checkBox.isChecked()) {
                        if (selectedTraits.size() >= MAX_TRAITS) {
                            checkBox.setChecked(false); // reject selection if max reached
                        } else {
                            selectedTraits.add(traitClass);
                        }
                    } else {
                        selectedTraits.remove(traitClass);
                    }
                }
            });

            traitTable.add(checkBox).left().pad(5);
            traitTable.row();
        }

        scrollPane.setHeight(150);
        table.add(scrollPane).width(300).height(150).padBottom(20).right();
        table.row();


// Portrait display
        portraitDisplay = new Image(portraits[currentPortraitIndex]);
        portraitDisplay.setScaling(Scaling.fit);
// Will be added inside portraitControlTable later

// Roll stats button



// Title label
        Label title = new Label("Character Creation - Stat Rolling", skin);
        title.setColor(Color.WHITE);
        table.add(title).padBottom(20).right();
        table.row();

// Portrait controls: prev/portrait/next buttons
        Table portraitControlTable = new Table();
        TextButton prevButton = new TextButton("<", skin);
        TextButton nextButton = new TextButton(">", skin);

        portraitControlTable.add(prevButton).padRight(5);
        portraitControlTable.add(portraitDisplay).size(150, 150);
        portraitControlTable.add(nextButton).padLeft(5);
        portraitControlTable.align(Align.right);

        table.add(portraitControlTable).right().padBottom(20);
        table.row();

        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentPortraitIndex = (currentPortraitIndex - 1 + portraits.length) % portraits.length;
                portraitDisplay.setDrawable(new TextureRegionDrawable(new TextureRegion(portraits[currentPortraitIndex])));
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentPortraitIndex = (currentPortraitIndex + 1) % portraits.length;
                portraitDisplay.setDrawable(new TextureRegionDrawable(new TextureRegion(portraits[currentPortraitIndex])));
            }
        });


// Name input
        Label nameLabel = new Label("Name:", whiteLabelStyle);
        TextField.TextFieldStyle baseStyleTF2 = skin.get(TextField.TextFieldStyle.class);
        TextField.TextFieldStyle textFieldStyle2 = new TextField.TextFieldStyle(baseStyleTF2);
        textFieldStyle2.fontColor = Color.WHITE;
        textFieldStyle2.font = skin.getFont("main-font");
        textFieldStyle2.cursor = skin.newDrawable("white");
        textFieldStyle2.selection = skin.newDrawable("white", 0.3f, 0.3f, 1f, 0.5f);
        textFieldStyle2.background = skin.newDrawable("white", 0.05f, 0.05f, 0.05f, 0.5f);

        TextField nameField = new TextField("", textFieldStyle2);
        nameField.setMessageText("Enter your character's name");
        nameField.setAlignment(Align.left);

        Table nameTable = new Table();
        nameTable.add(nameLabel).padRight(8);
        nameTable.add(nameField).width(200);
        nameTable.align(Align.right);

        table.add(nameTable).expandX().fillX().right().padBottom(10);
        table.row();


// Species input
        Label speciesLabel = new Label("Species:", whiteLabelStyle);
        TextField speciesField = new TextField("", textFieldStyle2);
        speciesField.setMessageText("Enter species name");
        speciesField.setAlignment(Align.left);

        Table speciesTable = new Table();
        speciesTable.add(speciesLabel).padRight(8);
        speciesTable.add(speciesField).width(200);
        speciesTable.align(Align.right);

        table.add(speciesTable).expandX().fillX().right().padBottom(20);
        table.row();


// Ability Fields Table
        Table abilitiesTable = new Table();

        for (int i = 0; i < ABILITIES.length; i++) {
            abilityLabels[i] = new Label(ABILITIES[i] + ":", whiteLabelStyle);

            TextField.TextFieldStyle baseStyleTF = skin.get(TextField.TextFieldStyle.class);
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(baseStyleTF);
            textFieldStyle.fontColor = Color.WHITE;
            textFieldStyle.font = skin.getFont("main-font");
            textFieldStyle.cursor = skin.newDrawable("white");
            textFieldStyle.selection = skin.newDrawable("white", 0.3f, 0.3f, 1f, 0.5f);
            textFieldStyle.background = skin.newDrawable("white", 0.05f, 0.05f, 0.05f, 0.5f);

            abilityFields[i] = new TextField("", textFieldStyle);
            abilityFields[i].setDisabled(true);
            abilityFields[i].setAlignment(Align.center);

            Table singleStatTable = new Table();
            singleStatTable.add(abilityLabels[i]).padRight(8);
            singleStatTable.add(abilityFields[i]).width(60);
            singleStatTable.align(Align.right);

            final int statIndex = i;
            TextButton swapButton = new TextButton("Swap", skin);
            swapButtons[i] = swapButton;

            swapButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String mode = rollModes[currentRollModeIndex];
                    if (!mode.equals("Pussy Mode") && !mode.equals("Normal Mode")) {
                        return; // Disable swap in Easy/Hardcore modes
                    }

                    if (firstSelectedIndex == null) {
                        firstSelectedIndex = statIndex;
                        swapButtons[statIndex].setColor(Color.YELLOW); // Highlight first selection
                    } else if (firstSelectedIndex == statIndex) {
                        swapButtons[statIndex].setColor(Color.WHITE); // Deselect
                        firstSelectedIndex = null;
                    } else {
                        // Swap text values
                        String temp = abilityFields[firstSelectedIndex].getText();
                        abilityFields[firstSelectedIndex].setText(abilityFields[statIndex].getText());
                        abilityFields[statIndex].setText(temp);

                        // Reset highlights
                        swapButtons[firstSelectedIndex].setColor(Color.WHITE);
                        swapButtons[statIndex].setColor(Color.WHITE);
                        firstSelectedIndex = null;
                    }
                }
            });

            singleStatTable.add(swapButton);
            abilitiesTable.add(singleStatTable).right();
            abilitiesTable.row();
        }

        table.add(abilitiesTable).right();
        table.row();

        TextButton rollButton = new TextButton("Roll Stats", skin);
        rollButton.setColor(Color.RED);

        rollButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rolledScores.clear();

                switch (rollModes[currentRollModeIndex]) {
                    case "Pussy Mode":
                        for (int i = 0; i < ABILITIES.length; i++) {
                            rolledScores.add(roll4d6DropLowest());
                        }
                        Collections.sort(rolledScores, Collections.reverseOrder());
                        for (int i = 0; i < ABILITIES.length; i++) {
                            abilityFields[i].setText(rolledScores.get(i).toString());
                        }
                        break;
                    case "Easy Mode":
                        for (int i = 0; i < ABILITIES.length; i++) {
                            int val = roll4d6DropLowest();
                            abilityFields[i].setText(Integer.toString(val));
                        }
                        break;
                    case "Normal Mode":
                        for (int i = 0; i < ABILITIES.length; i++) {
                            rolledScores.add(roll3d6());
                        }
                        Collections.sort(rolledScores, Collections.reverseOrder());
                        for (int i = 0; i < ABILITIES.length; i++) {
                            abilityFields[i].setText(rolledScores.get(i).toString());
                        }
                        break;
                    case "Hardcore Mode":
                        for (int i = 0; i < ABILITIES.length; i++) {
                            int val = roll3d6();
                            abilityFields[i].setText(Integer.toString(val));
                        }
                        break;
                }
                System.out.println("Rolled stats updated.");
            }
        });

        Table rollButtonTable = new Table();
        rollButtonTable.add(rollButton);
        rollButtonTable.debug();
        table.add(rollButtonTable).expandX().right().padBottom(20);
        table.row();


// Add the fully constructed table to the stage
        stage.addActor(table);


    }


    private int roll4d6DropLowest() {
        int[] rolls = new int[4];
        for (int i = 0; i < 4; i++) {
            rolls[i] = (int)(Math.random() * 6) + 1;
        }
        int min = rolls[0];
        int sum = 0;
        for (int roll : rolls) {
            if (roll < min) min = roll;
            sum += roll;
        }
        return sum - min;
    }

    private int roll3d6() {
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += (int)(Math.random() * 6) + 1;
        }
        return sum;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);  // or your background color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        for (Texture t : portraits) {
            t.dispose();
        }
        stage.dispose();

    }
}