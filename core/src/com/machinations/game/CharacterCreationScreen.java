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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CharacterCreationScreen extends com.badlogic.gdx.ScreenAdapter {

    private Stage stage;
    private Skin skin;

    private Texture[] portraits;
    private int currentPortraitIndex = 0;
    private Image portraitDisplay;

    private final String[] ABILITIES = {"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma", "Comeliness"};
    private Label[] abilityLabels = new Label[ABILITIES.length];
    private TextField[] abilityFields = new TextField[ABILITIES.length];

    private Label rollModeValueLabel;
    private String[] rollModes = {"Pussy Mode", "Easy Mode", "Normal Mode", "Hardcore Mode"};
    private int currentRollModeIndex = 0;
    private boolean uiSetupComplete = false;
    private Integer firstSelectedIndex = null;
    private final TextButton[] swapButtons = new TextButton[7];
    private boolean statsrolled = false;
    private Label selectedTraitsLabel;
    private ArrayList<Class<? extends RaceTrait>> selectedTraits = new ArrayList<>();
    private final int MAX_TRAITS = 3;
    private SelectBox<String> classSelectBox;
    private ClassLevelProgression classLevelProgression;
    private Label.LabelStyle whiteLabelStyle;
    private Label unspentSkillPointsLabel;
    private TextField nameField;
    private TextField speciesField;

    private Machinations game; // store reference


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
    private Map<Integer, Integer> statPenalties = new HashMap<>();

    private ArrayList<Integer> rolledScores = new ArrayList<>();

    private Skills playerSkills = new Skills();
    private int unspentSkillPoints = 0;
    private Map<Skills.Skill, Label> skillValueLabels = new HashMap<>();
    private String selectedClass = "Expert";
    private Table skillsTable;

    private Map<Class<? extends RaceTrait>, Map<Integer, Integer>> traitPenalties = new HashMap<>();

    private int getSkillPointsForLevel(String className, int level) {
        LevelProgression progression = classLevelProgression.getProgressionForClassAndLevel(className, level);
        if (progression != null) {
            return progression.getSkillPoints(); // <- you need a getter in LevelProgression
        } else {
            return 0; // fallback
        }
    }

    private PlayerCharacter buildPlayerCharacter() {

        // Gather basic info
        String name = nameField.getText();
        String species = speciesField.getText();
        String playerClass = selectedClass;

        // Gather abilities
        int strength = parseAbilityField(0);
        int dexterity = parseAbilityField(1);
        int constitution = parseAbilityField(2);
        int intelligence = parseAbilityField(3);
        int wisdom = parseAbilityField(4);
        int charisma = parseAbilityField(5);
        int comeliness = parseAbilityField(6);

        // Pass ClassLevelProgression (you already have it in the screen)
        ClassLevelProgression clp = classLevelProgression;

        // Construct the PlayerCharacter with your existing constructor
        PlayerCharacter pc = new PlayerCharacter(
                name,
                species,
                playerClass,
                clp,
                strength,
                dexterity,
                constitution,
                intelligence,
                wisdom,
                charisma,
                comeliness
        );

        // Apply skills
        pc.setSkills(playerSkills); // make sure you have a copy method
        pc.setUnspentSkillPoints(unspentSkillPoints);

        ArrayList<RaceTrait> traits = new ArrayList<>();
        for (Class<? extends RaceTrait> traitClass : selectedTraits) {
            try {
                // Create an instance of the trait
                RaceTrait trait = traitClass.getDeclaredConstructor().newInstance();
                // Apply it immediately (or later in PlayerCharacter constructor)
                trait.applyEffect(pc);
                traits.add(trait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pc.setRaceTraits(traits);
        pc.setPortrait(portraits[currentPortraitIndex]);
        return pc;
    }


    // Helper to safely parse ability fields
    private int parseAbilityField(int index) {
        try {
            return Integer.parseInt(abilityFields[index].getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    private Table selectedTraitsTable = new Table();

    private void updateSelectedTraitsUI() {
        selectedTraitsTable.clear();
        for (Class<? extends RaceTrait> trait : selectedTraits) {
            TextButton traitButton = new TextButton(trait.getSimpleName(), skin);
            traitButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    removeTrait(trait);
                }
            });
            selectedTraitsTable.add(traitButton).pad(5).row();
        }
    }



    public CharacterCreationScreen(Machinations game, Skin skin) {
        this.game = game;
        this.skin = skin;
        stage = new Stage(new ScreenViewport());
        classLevelProgression = new ClassLevelProgression();
        classLevelProgression.initializeClassProgressions();
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

    private boolean isSkillAllowedForClass(Skills.Skill skill, String className) {
        switch (className) {
            case "Expert":
              //  return skill.getType() == Skills.SkillType.GENERAL;
            case "Killer":
                return skill.getType() == Skills.SkillType.COMBAT;
            case "Scholar":
                return skill.getType() == Skills.SkillType.SCHOLAR;
            case "Psion":
                return skill.getType() == Skills.SkillType.PSION;
            default:
                return false;
        }
    }

    private void updateUnspentSkillPointsLabel() {
        if (unspentSkillPointsLabel == null) {
            // First time setup
            unspentSkillPointsLabel = new Label("Unspent Skill Points: " + unspentSkillPoints, skin);
            // Add it somewhere in your UI — probably at the top of the skills table
            // Example:
             skillsTable.add(unspentSkillPointsLabel).colspan(3).row();
        } else {
            unspentSkillPointsLabel.setText("Unspent Skill Points: " + unspentSkillPoints);
        }
    }

    private void updateSkillUI(String selectedClass) {
        skillValueLabels.clear();
        playerSkills = new Skills();
        updateUnspentSkillPointsLabel();

        // ✅ Use the field reference
        if (skillsTable == null) {
            System.out.println("Error: skillsTable not initialized!");
            return;
        }

        skillsTable.clearChildren();

        // Build three columns inside the existing table
        Table leftColumn = new Table();
        Table centerColumn = new Table();
        Table rightColumn = new Table();

        // LEFT: Everyman + General
        for (Skills.Skill skill : Skills.Skill.values()) {
            if (skill.getType() == Skills.SkillType.EVERYMAN || skill.getType() == Skills.SkillType.GENERAL) {
                leftColumn.add(createSkillRow(skill)).row();
            }
        }
        leftColumn.padTop(10);
       /// leftColumn.add(unspentSkillPointsLabel).colspan(2).row();

        // CENTER: Combat
        for (Skills.Skill skill : Skills.Skill.values()) {
            if (skill.getType() == Skills.SkillType.COMBAT) {
                centerColumn.add(createSkillRow(skill)).row();
            }
        }

        // RIGHT: Class-specific skills
        for (Skills.Skill skill : Skills.Skill.values()) {
            if (!selectedClass.equals("Expert") && isSkillAllowedForClass(skill, selectedClass)) {
                rightColumn.add(createSkillRow(skill)).row();
            }
        }

        skillsTable.add(leftColumn).padRight(20).top().left();
        skillsTable.add(centerColumn).padRight(20).top().left();
        skillsTable.add(rightColumn).top().left();
    }

    private Table createSkillRow(Skills.Skill skill) {
        Table row = new Table();
        Label nameLabel = new Label(skill.name(), whiteLabelStyle); // <--- use whiteLabelStyle
        Label valueLabel = new Label(String.valueOf(playerSkills.getSkillValue(skill)), whiteLabelStyle); // <--- also here
        skillValueLabels.put(skill, valueLabel);

        TextButton plus = new TextButton("+", skin);
        plus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unspentSkillPoints > 0) {
                    playerSkills.incrementSkill(skill);
                    valueLabel.setText(String.valueOf(playerSkills.getSkillValue(skill)));
                    unspentSkillPoints--;
                    updateUnspentSkillPointsLabel();
                }
            }
        });

        TextButton minus = new TextButton("-", skin);
        minus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int current = playerSkills.getSkillValue(skill);
                if (current > 0) {
                    playerSkills.setSkillValue(skill, current - 1);
                    valueLabel.setText(String.valueOf(current - 1));
                    unspentSkillPoints++;
                    updateUnspentSkillPointsLabel();
                }
            }
        });

        //debug shit
        System.out.println("Fetching SP for class '" + selectedClass + "', level " + 1);
        LevelProgression lp = classLevelProgression.getProgressionForClassAndLevel(selectedClass, 1);
        System.out.println("LevelProgression: " + lp);

        row.add(nameLabel).padRight(5);
        row.add(valueLabel).padRight(5);
        row.add(plus).padRight(2);
        row.add(minus);
        return row;
    }

    private void removeTrait(Class<? extends RaceTrait> trait) {
        selectedTraits.remove(trait);

        // Revert any penalties applied by this trait
        Map<Integer, Integer> penalties = traitPenalties.getOrDefault(trait, new HashMap<>());
        for (Map.Entry<Integer, Integer> entry : penalties.entrySet()) {
            int statIndex = entry.getKey();
            int amount = entry.getValue();
            statPenalties.put(statIndex, statPenalties.get(statIndex) - amount);

            // Update UI
            int baseScore = 0;
            String text = abilityFields[statIndex].getText();
            if (text != null && !text.isEmpty()) {
                try { baseScore = Integer.parseInt(text); }
                catch (NumberFormatException e) { baseScore = 0; }
            }
            abilityFields[statIndex].setText(String.valueOf(baseScore - amount));
        }
        traitPenalties.remove(trait);

        // Optionally revert any other trait effects here
        // e.g. playerCharacter.removeTraitEffect(trait);

        ///updateSelectedTraitsUI();
    }

    private void updateSelectedTraitsLabel() {
        if (selectedTraits.isEmpty()) {
            selectedTraitsLabel.setText("Selected Traits: (none yet)");
        } else {
            String traitsText = selectedTraits.stream()
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining("\n")); // line break between traits
            selectedTraitsLabel.setText("Selected Traits:\n" + traitsText);
            System.out.println("Selected Traits:\n" + traitsText);
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
        ///table.setFillParent(true);
        table.top().center();
       // table.padTop(10).padRight(10);
// table.debug(); // Uncomment to debug layout
//Scrollpanestyle

        ScrollPane.ScrollPaneStyle defaultScrollStyle = new ScrollPane.ScrollPaneStyle();
        defaultScrollStyle.background = skin.newDrawable("white", new Color(0.1f,0.1f,0.1f,0.5f));
        defaultScrollStyle.vScroll = skin.newDrawable("white", Color.DARK_GRAY);
        defaultScrollStyle.vScrollKnob = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default", defaultScrollStyle, ScrollPane.ScrollPaneStyle.class);

// White label style
        whiteLabelStyle = new Label.LabelStyle();
        whiteLabelStyle.font = skin.getFont("main-font");
        whiteLabelStyle.fontColor = Color.WHITE;

        /// Window style
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = skin.getFont("main-font");
        windowStyle.titleFontColor = Color.WHITE;
        windowStyle.background = skin.newDrawable("white", new Color(0.1f, 0.1f, 0.1f, 0.9f));

        skin.add("default", windowStyle, Window.WindowStyle.class);


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
        Label traitsLabel = new Label("Select Race Traits (max before penalties " + MAX_TRAITS + "):", whiteLabelStyle);
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
                    if (!statsrolled) {
                        // ❌ Block trait selection and reset checkbox
                        checkBox.setChecked(false);
                        Dialog warning = new Dialog("Stats Not Rolled", skin);
                        warning.text("Please roll your stats before selecting race traits.");
                        warning.button("OK");
                        warning.show(stage);
                        return;
                    }

                    if (checkBox.isChecked()) {
                        selectedTraits.add(traitClass);

                        if (selectedTraits.size() > 3) {
                            if (selectedTraits.size() == 4) {
                                // First penalty automatically to Charisma
                                applyPenalty(5, -4,null);
                            } else {
                                // Prompt player to pick a stat for -4 penalty, pass trait for cancel removal
                                promptForPenaltyStat(traitClass);
                            }
                        }
                    } else {
                        selectedTraits.remove(traitClass);
                        // optionally refund any penalties associated with this trait
                    }

                    updateSelectedTraitsLabel();
                }
            });

            traitTable.add(checkBox).left().pad(5);
            traitTable.row();
        }

        scrollPane.setHeight(150);
        table.add(scrollPane).width(300).height(150).padBottom(20).right();
        table.row();

        classSelectBox = new SelectBox<>(skin);
        classSelectBox.setItems("Expert", "Killer", "Scholar", "Psion");
        classSelectBox.setSelected("Expert"); // Default selection



        classSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedClass = classSelectBox.getSelected(); // store new class

                // Reset skills
                playerSkills = new Skills();

                // Reset unspent skill points
                unspentSkillPoints = classLevelProgression.getSkillPointsForClassAndLevel(selectedClass, 1);

                // Rebuild skill UI
                updateSkillUI(selectedClass);

                // Refresh unspent skill points label
                updateUnspentSkillPointsLabel();
            }
        });

        table.add(new Label("Class:", skin)).left().pad(5);
        table.add(classSelectBox).fillX().pad(5);
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

        nameField = new TextField("", textFieldStyle2);
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
        speciesField = new TextField("", textFieldStyle2);
        speciesField.setMessageText("Enter species name");
        speciesField.setAlignment(Align.left);

        Table speciesTable = new Table();
        speciesTable.add(speciesLabel).padRight(8);
        speciesTable.add(speciesField).width(200);
        speciesTable.align(Align.right);

        table.add(speciesTable).expandX().fillX().right().padBottom(20);
        table.row();

        //Race trait label


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("main-font"); // use the font you know works
        labelStyle.fontColor = Color.WHITE;

        selectedTraitsLabel = new Label("Selected Traits: None", labelStyle);
        table.row();
        table.add(selectedTraitsLabel).left().pad(10);

        updateSelectedTraitsLabel();

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
                statsrolled = true; // ✅ mark that stats are now rolled
                System.out.println("Rolled stats updated.");

            }
        });

        TextButton createCharacterButton = new TextButton("Create Character", skin);
        createCharacterButton.setColor(Color.GREEN);
        createCharacterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                // ✅ Validate prerequisites
                if (!statsrolled) {
                    Dialog warning = new Dialog("Cannot Create Character", skin);
                    warning.text("You must roll your stats first.");
                    warning.button("OK");
                    warning.show(stage);
                    return;
                }
                if (selectedTraits.size() > MAX_TRAITS) {
                    Dialog warning = new Dialog("Cannot Create Character", skin);
                    warning.text("Too many traits selected. Resolve penalties first.");
                    warning.button("OK");
                    warning.show(stage);
                    return;
                }

                PlayerCharacter pc = buildPlayerCharacter();

                // Example: print to console
                System.out.println("Character Created:\n" + pc);

                // TODO: send 'pc' to game manager / main game state

                game.setPlayerCharacter(pc);  // store in main game
                game.startGame();
            }
        });

// Add it to the right table





// Add the fully constructed table to the stage
      ///  table.setFillParent(true);
      ///  stage.addActor(table);

        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(10);
        stage.addActor(root);

// LEFT: Portraits
        Table left = new Table();
        left.top().left();
        left.add(portraitControlTable).row();
        left.add(nameTable).padTop(10).row();
        left.add(speciesTable).padTop(10).row();

// MIDDLE: Class and Traits
        Table middle = new Table();
        middle.add(classSelectBox).row();
        middle.add(scrollPane).width(250).height(150).row();
        middle.add(selectedTraitsLabel).padTop(5).left();

// RIGHT: Roll Mode, Stats, Roll Button
        Table right = new Table();
        right.add(rollModeTable).padBottom(10).row();
        right.add(abilitiesTable).padBottom(10).row();
        right.add(rollButton).padTop(10);
        right.add(createCharacterButton).padTop(15).row();

// Combine left, middle, right at the top
        Table topRow = new Table();
        topRow.add(left).top().left().padRight(30);
        topRow.add(middle).width(350).top().left().padRight(30);
        topRow.add(right).top().right();
        root.add(topRow).expandX().fillX().row();  // <-- top row done

        skillsTable = new Table();
        skillsTable.setName("skillsTable");

        ScrollPane skillsScroll = new ScrollPane(skillsTable, skin);
        unspentSkillPointsLabel = new Label("Unspent Skill Points: " + unspentSkillPoints, whiteLabelStyle);
        skillsScroll.setFadeScrollBars(false);
        skillsScroll.setScrollingDisabled(false, false);
        skillsScroll.setScrollbarsOnTop(true);

        // 4️⃣ Add the scroll pane to root
        root.add(unspentSkillPointsLabel).expandX().left().padBottom(5).row();

// Then add the scroll pane
        root.add(skillsScroll)
                .width(root.getWidth())
                .height(200)
                .expandX()
                .fillX()
                .row();

// 5️⃣ Populate skills dynamically

        selectedClass = classSelectBox.getSelected(); // "Expert"
        unspentSkillPoints = classLevelProgression.getSkillPointsForClassAndLevel(selectedClass, 1); // Level 1
        playerSkills = new Skills(); // reset skills
        updateSkillUI(selectedClass); // now UI builds with correct unspent SP




    }

    private void applyPenalty(int statIndex, int amount, Class<? extends RaceTrait> trait) {
        int currentPenalty = statPenalties.getOrDefault(statIndex, 0);
        int newPenalty = currentPenalty + amount;

        // Clamp so it can’t go below -8
        if (newPenalty < -8) newPenalty = -8;

        statPenalties.put(statIndex, newPenalty);

        // Record this penalty for the trait
        if (trait != null) {
            traitPenalties.computeIfAbsent(trait, k -> new HashMap<>())
                    .put(statIndex, amount);
        }

        // Safe parse of the current ability field
        int baseScore = 0;
        String text = abilityFields[statIndex].getText();
        if (text != null && !text.isEmpty()) {
            try { baseScore = Integer.parseInt(text); }
            catch (NumberFormatException e) { baseScore = 0; }
        }

        // Update UI
        abilityFields[statIndex].setText(String.valueOf(baseScore + newPenalty));

        System.out.println("Applied penalty of " + amount + " to " + ABILITIES[statIndex] +
                " (total penalty: " + newPenalty + ")");
    }

    private void promptForPenaltyStat(Class<? extends RaceTrait> traitToRemoveOnCancel) {
        Dialog dialog = new Dialog("Choose a Stat to Reduce", skin);

        for (int i = 0; i < ABILITIES.length; i++) {
            final int statIndex = i;
            TextButton button = new TextButton(ABILITIES[i], skin);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (canApplyPenalty(statIndex)) {
                        applyPenalty(statIndex, -4, traitToRemoveOnCancel);
                        dialog.hide();
                    }
                }
            });
            dialog.getContentTable().add(button).pad(5).row();
        }

        // Cancel button removes the trait
        dialog.button("Cancel", true).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Remove the trait from selection
                selectedTraits.remove(traitToRemoveOnCancel);
                updateSelectedTraitsLabel();

                dialog.hide();
            }
        });

        dialog.show(stage);
    }


    private boolean canApplyPenalty(int statIndex) {
        // If no penalties assigned yet, treat as 0

        int currentPenalty = statPenalties.getOrDefault(statIndex, 0);
        int newPenalty = currentPenalty - 4; // what would happen if we applied another penalty
        return newPenalty >= -8;
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