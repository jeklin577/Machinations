package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CharacterCreationScreen extends com.badlogic.gdx.ScreenAdapter {

    private Stage stage;
    private Skin skin;
    private Machinations game;

    private Texture[] portraits;
    private int currentPortraitIndex = 0;
    private Image portraitDisplay;

    private static final String[] ABILITIES = {
            "Strength", "Dexterity", "Constitution",
            "Intelligence", "Wisdom", "Charisma", "Comeliness"
    };

    private final Label[] abilityLabels = new Label[ABILITIES.length];
    private final TextField[] abilityFields = new TextField[ABILITIES.length];
    private final TextButton[] swapButtons = new TextButton[ABILITIES.length];

    private Label rollModeValueLabel;
    private final String[] rollModes = {"Pussy Mode", "Easy Mode", "Normal Mode", "Hardcore Mode"};
    private int currentRollModeIndex = 0;

    private boolean uiSetupComplete = false;
    private boolean statsRolled = false;
    private Integer firstSelectedIndex = null;

    private Label selectedTraitsLabel;
    private Label unspentSkillPointsLabel;
    private Label hpPreviewLabel;

    private TextField nameField;
    private TextField speciesField;

    private SelectBox<String> classSelectBox;
    private String selectedClass = "Expert";

    private ClassLevelProgression classLevelProgression;
    private Label.LabelStyle whiteLabelStyle;

    private final ArrayList<Class<? extends RaceTrait>> selectedTraits = new ArrayList<>();
    private static final int MAX_TRAITS = 3;

    private final Map<Class<? extends RaceTrait>, Label> traitCountLabels = new HashMap<>();
    private final ArrayList<Integer> extraTraitPenaltyAssignments = new ArrayList<>();
    private final Map<Integer, Integer> statPenalties = new HashMap<>();
    private final int[] rolledBaseStats = new int[ABILITIES.length];

    private Skills playerSkills = new Skills();
    private int unspentSkillPoints = 0;
    private final Map<Skills.Skill, Label> skillValueLabels = new HashMap<>();
    private Table skillsTable;

    private PlayerCharacter previewCharacter;

    // This is the key addition:
    // roll the class HP die once per character roll, then reuse it for preview rebuilds.
    private Integer rolledStartingHpDieResult = null;

    @SuppressWarnings("unchecked")
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

    public CharacterCreationScreen(Machinations game, Skin skin) {
        this.game = game;
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());

        this.classLevelProgression = new ClassLevelProgression();
        this.classLevelProgression.initializeClassProgressions();

        setupUI();
        Gdx.input.setInputProcessor(stage);
    }

    private void loadPortraits() {
        FileHandle portraitsDir = Gdx.files.internal("Portraits");
        FileHandle[] files = portraitsDir.list("png");

        portraits = new Texture[files.length];
        for (int i = 0; i < files.length; i++) {
            portraits[i] = new Texture(files[i]);
        }
    }

    private int getPreviewBaseStat(int index) {
        return rolledBaseStats[index] + statPenalties.getOrDefault(index, 0);
    }

    private int getBaseClassSkillPoints() {
        return classLevelProgression.getSkillPointsForClassAndLevel(selectedClass, 1);
    }

    private int getSpentManualSkillPoints() {
        int spent = 0;
        for (Skills.Skill skill : Skills.Skill.values()) {
            spent += playerSkills.getSkillValue(skill);
        }
        return spent;
    }

    private int rollStartingHpDieForClass(String className) {
        switch (className) {
            case "Killer":
                return new Dice(8).roll();
            case "Expert":
            case "Scholar":
            case "Psion":
            default:
                return new Dice(6).roll();
        }
    }

    private void applyPreviewHpFromRolledDie(PlayerCharacter pc) {
        if (rolledStartingHpDieResult == null) return;

        int hp = rolledStartingHpDieResult + pc.getConMod();
        if (hp < 1) hp = 1;

        pc.setMaxHP(hp);
        pc.setCurrentHp(hp);
    }

    private ArrayList<RaceTrait> instantiateSelectedTraits() {
        ArrayList<RaceTrait> traits = new ArrayList<>();
        for (Class<? extends RaceTrait> traitClass : selectedTraits) {
            try {
                traits.add(traitClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return traits;
    }

    private PlayerCharacter createCharacterFromCurrentSelections() {
        int strength = getPreviewBaseStat(0);
        int dexterity = getPreviewBaseStat(1);
        int constitution = getPreviewBaseStat(2);
        int intelligence = getPreviewBaseStat(3);
        int wisdom = getPreviewBaseStat(4);
        int charisma = getPreviewBaseStat(5);
        int comeliness = getPreviewBaseStat(6);

        PlayerCharacter pc = new PlayerCharacter(
                nameField.getText(),
                speciesField.getText(),
                selectedClass,
                classLevelProgression,
                strength,
                dexterity,
                constitution,
                intelligence,
                wisdom,
                charisma,
                comeliness
        );

        // Override constructor-rolled HP with our fixed preview HP roll
        applyPreviewHpFromRolledDie(pc);

        pc.setSkills(playerSkills.copy());
        pc.setUnspentSkillPoints(Math.max(0, getBaseClassSkillPoints() - getSpentManualSkillPoints()));

        pc.captureBaseState();
        pc.setRaceTraits(instantiateSelectedTraits());
        pc.recalculateFromTraits();

        // Re-apply HP using the same original die roll, but with any Con changes from traits
        applyPreviewHpFromRolledDie(pc);

        pc.setPortrait(portraits[currentPortraitIndex]);
        return pc;
    }

    private PlayerCharacter buildPlayerCharacter() {
        return createCharacterFromCurrentSelections();
    }

    private void syncUiFromPreview() {
        if (previewCharacter == null) {
            for (int i = 0; i < ABILITIES.length; i++) {
                abilityFields[i].setText(String.valueOf(getPreviewBaseStat(i)));
            }
            unspentSkillPoints = Math.max(0, getBaseClassSkillPoints() - getSpentManualSkillPoints());
            hpPreviewLabel.setText("HP: -");
        } else {
            abilityFields[0].setText(String.valueOf(previewCharacter.getStrScore()));
            abilityFields[1].setText(String.valueOf(previewCharacter.getDexScore()));
            abilityFields[2].setText(String.valueOf(previewCharacter.getConScore()));
            abilityFields[3].setText(String.valueOf(previewCharacter.getIntScore()));
            abilityFields[4].setText(String.valueOf(previewCharacter.getWisScore()));
            abilityFields[5].setText(String.valueOf(previewCharacter.getChaScore()));
            abilityFields[6].setText(String.valueOf(previewCharacter.getComScore()));

            unspentSkillPoints = previewCharacter.getUnspentSkillPoints();
            hpPreviewLabel.setText("HP: " + previewCharacter.getCurrentHp() + "/" + previewCharacter.getMaxHP());
        }

        updateUnspentSkillPointsLabel();

        for (Map.Entry<Skills.Skill, Label> entry : skillValueLabels.entrySet()) {
            Skills.Skill skill = entry.getKey();
            int value = (previewCharacter != null)
                    ? previewCharacter.getSkills().getSkillValue(skill)
                    : playerSkills.getSkillValue(skill);
            entry.getValue().setText(String.valueOf(value));
        }
    }

    private void rebuildPreviewCharacter() {
        if (!statsRolled) {
            previewCharacter = null;
            syncUiFromPreview();
            return;
        }

        previewCharacter = createCharacterFromCurrentSelections();
        syncUiFromPreview();
    }

    private void recomputeTraitPenalties() {
        statPenalties.clear();

        int extraTraits = Math.max(0, selectedTraits.size() - MAX_TRAITS);
        while (extraTraitPenaltyAssignments.size() > extraTraits) {
            extraTraitPenaltyAssignments.remove(extraTraitPenaltyAssignments.size() - 1);
        }

        for (Integer statIndex : extraTraitPenaltyAssignments) {
            int currentPenalty = statPenalties.getOrDefault(statIndex, 0);
            int nextPenalty = currentPenalty - 4;
            if (nextPenalty < -8) {
                nextPenalty = -8;
            }
            statPenalties.put(statIndex, nextPenalty);
        }
    }

    private boolean canApplyAdditionalPenalty(int statIndex) {
        int currentPenalty = statPenalties.getOrDefault(statIndex, 0);
        return currentPenalty - 4 >= -8;
    }

    private void updateTraitCountLabels() {
        for (Class<? extends RaceTrait> traitClass : ALL_TRAITS) {
            Label countLabel = traitCountLabels.get(traitClass);
            if (countLabel != null) {
                countLabel.setText(String.valueOf(Collections.frequency(selectedTraits, traitClass)));
            }
        }
    }

    private void updateSelectedTraitsLabel() {
        if (selectedTraits.isEmpty()) {
            selectedTraitsLabel.setText("Selected Traits: (none)");
            return;
        }

        StringBuilder sb = new StringBuilder("Selected Traits:\n");
        boolean first = true;

        for (Class<? extends RaceTrait> traitClass : ALL_TRAITS) {
            int count = Collections.frequency(selectedTraits, traitClass);
            if (count > 0) {
                if (!first) sb.append("\n");
                sb.append(traitClass.getSimpleName());
                if (count > 1) {
                    sb.append(" x").append(count);
                }
                first = false;
            }
        }

        selectedTraitsLabel.setText(sb.toString());
    }

    private void promptForExtraPenaltyStat(final Class<? extends RaceTrait> justAddedTrait) {
        final Dialog dialog = new Dialog("Choose a Stat to Reduce", skin);
        dialog.text("Pick a stat to take -4 for this extra trait.");

        for (int i = 0; i < ABILITIES.length; i++) {
            final int statIndex = i;
            TextButton button = new TextButton(ABILITIES[i], skin);

            if (!canApplyAdditionalPenalty(statIndex)) {
                button.setDisabled(true);
            }

            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!canApplyAdditionalPenalty(statIndex)) return;

                    extraTraitPenaltyAssignments.add(statIndex);
                    recomputeTraitPenalties();
                    updateTraitCountLabels();
                    updateSelectedTraitsLabel();
                    rebuildPreviewCharacter();
                    dialog.hide();
                }
            });

            dialog.getContentTable().add(button).pad(4).row();
        }

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedTraits.remove(justAddedTrait);
                recomputeTraitPenalties();
                updateTraitCountLabels();
                updateSelectedTraitsLabel();
                rebuildPreviewCharacter();
                dialog.hide();
            }
        });

        dialog.button(cancelButton);
        dialog.show(stage);
    }

    private Table createTraitRow(final Class<? extends RaceTrait> traitClass) {
        Table row = new Table();

        Label nameLabel = new Label(traitClass.getSimpleName(), whiteLabelStyle);
        Label countLabel = new Label("0", whiteLabelStyle);
        traitCountLabels.put(traitClass, countLabel);

        TextButton minusButton = new TextButton("-", skin);
        TextButton plusButton = new TextButton("+", skin);

        minusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedTraits.remove(traitClass)) {
                    int extraTraitsNeeded = Math.max(0, selectedTraits.size() - MAX_TRAITS);
                    while (extraTraitPenaltyAssignments.size() > extraTraitsNeeded) {
                        extraTraitPenaltyAssignments.remove(extraTraitPenaltyAssignments.size() - 1);
                    }

                    recomputeTraitPenalties();
                    updateTraitCountLabels();
                    updateSelectedTraitsLabel();
                    rebuildPreviewCharacter();
                }
            }
        });

        plusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!statsRolled) {
                    Dialog warning = new Dialog("Stats Not Rolled", skin);
                    warning.text("Please roll your stats before selecting race traits.");
                    warning.button("OK");
                    warning.show(stage);
                    return;
                }

                selectedTraits.add(traitClass);

                int extraTraitsNeeded = Math.max(0, selectedTraits.size() - MAX_TRAITS);

                if (extraTraitsNeeded > extraTraitPenaltyAssignments.size()) {
                    if (extraTraitsNeeded == 1) {
                        extraTraitPenaltyAssignments.add(5);
                        recomputeTraitPenalties();
                        updateTraitCountLabels();
                        updateSelectedTraitsLabel();
                        rebuildPreviewCharacter();
                    } else {
                        updateTraitCountLabels();
                        updateSelectedTraitsLabel();
                        promptForExtraPenaltyStat(traitClass);
                    }
                } else {
                    recomputeTraitPenalties();
                    updateTraitCountLabels();
                    updateSelectedTraitsLabel();
                    rebuildPreviewCharacter();
                }
            }
        });

        row.add(nameLabel).width(180).left().padRight(10);
        row.add(minusButton).width(35).padRight(5);
        row.add(countLabel).width(30).center().padRight(5);
        row.add(plusButton).width(35).left();

        return row;
    }

    private boolean isSkillAllowedForClass(Skills.Skill skill, String className) {
        switch (className) {
            case "Killer":
                return skill.getType() == Skills.SkillType.COMBAT;
            case "Scholar":
                return skill.getType() == Skills.SkillType.SCHOLAR;
            case "Psion":
                return skill.getType() == Skills.SkillType.PSION;
            case "Expert":
            default:
                return false;
        }
    }

    private void updateUnspentSkillPointsLabel() {
        if (unspentSkillPointsLabel != null) {
            unspentSkillPointsLabel.setText("Unspent Skill Points: " + unspentSkillPoints);
        }
    }

    private Table createSkillRow(final Skills.Skill skill) {
        Table row = new Table();

        Label nameLabel = new Label(skill.name(), whiteLabelStyle);
        Label valueLabel = new Label("0", whiteLabelStyle);
        skillValueLabels.put(skill, valueLabel);

        TextButton plus = new TextButton("+", skin);
        plus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int availablePoints = (previewCharacter != null)
                        ? previewCharacter.getUnspentSkillPoints()
                        : unspentSkillPoints;

                if (availablePoints > 0) {
                    playerSkills.incrementSkill(skill);
                    rebuildPreviewCharacter();
                }
            }
        });

        TextButton minus = new TextButton("-", skin);
        minus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int currentManual = playerSkills.getSkillValue(skill);
                if (currentManual > 0) {
                    playerSkills.setSkillValue(skill, currentManual - 1);
                    rebuildPreviewCharacter();
                }
            }
        });

        row.add(nameLabel).padRight(5);
        row.add(valueLabel).padRight(5);
        row.add(plus).padRight(2);
        row.add(minus);

        return row;
    }

    private void updateSkillUI(String selectedClass) {
        skillValueLabels.clear();

        if (skillsTable == null) {
            return;
        }

        skillsTable.clearChildren();

        Table leftColumn = new Table();
        Table centerColumn = new Table();
        Table rightColumn = new Table();

        for (Skills.Skill skill : Skills.Skill.values()) {
            if (skill.getType() == Skills.SkillType.EVERYMAN || skill.getType() == Skills.SkillType.GENERAL) {
                leftColumn.add(createSkillRow(skill)).row();
            }
        }

        for (Skills.Skill skill : Skills.Skill.values()) {
            if (skill.getType() == Skills.SkillType.COMBAT) {
                centerColumn.add(createSkillRow(skill)).row();
            }
        }

        for (Skills.Skill skill : Skills.Skill.values()) {
            if (!selectedClass.equals("Expert") && isSkillAllowedForClass(skill, selectedClass)) {
                rightColumn.add(createSkillRow(skill)).row();
            }
        }

        skillsTable.add(leftColumn).padRight(20).top().left();
        skillsTable.add(centerColumn).padRight(20).top().left();
        skillsTable.add(rightColumn).top().left();

        syncUiFromPreview();
    }

    private void setupUI() {
        if (uiSetupComplete) return;
        uiSetupComplete = true;

        loadPortraits();

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.background = skin.newDrawable("white", new Color(0.1f, 0.1f, 0.1f, 0.5f));
        scrollStyle.vScroll = skin.newDrawable("white", Color.DARK_GRAY);
        scrollStyle.vScrollKnob = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default", scrollStyle, ScrollPane.ScrollPaneStyle.class);

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = skin.getFont("main-font");
        windowStyle.titleFontColor = Color.WHITE;
        windowStyle.background = skin.newDrawable("white", new Color(0.1f, 0.1f, 0.1f, 0.9f));
        skin.add("default", windowStyle, Window.WindowStyle.class);

        whiteLabelStyle = new Label.LabelStyle();
        whiteLabelStyle.font = skin.getFont("main-font");
        whiteLabelStyle.fontColor = Color.WHITE;

        TextField.TextFieldStyle baseTextFieldStyle = skin.get(TextField.TextFieldStyle.class);
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(baseTextFieldStyle);
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.font = skin.getFont("main-font");
        textFieldStyle.cursor = skin.newDrawable("white");
        textFieldStyle.selection = skin.newDrawable("white", 0.3f, 0.3f, 1f, 0.5f);
        textFieldStyle.background = skin.newDrawable("white", 0.05f, 0.05f, 0.05f, 0.5f);

        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(10);
        stage.addActor(root);

        portraitDisplay = new Image(portraits[currentPortraitIndex]);
        portraitDisplay.setScaling(Scaling.fit);

        TextButton prevButton = new TextButton("<", skin);
        TextButton nextButton = new TextButton(">", skin);

        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentPortraitIndex = (currentPortraitIndex - 1 + portraits.length) % portraits.length;
                portraitDisplay.setDrawable(new TextureRegionDrawable(new TextureRegion(portraits[currentPortraitIndex])));
                rebuildPreviewCharacter();
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentPortraitIndex = (currentPortraitIndex + 1) % portraits.length;
                portraitDisplay.setDrawable(new TextureRegionDrawable(new TextureRegion(portraits[currentPortraitIndex])));
                rebuildPreviewCharacter();
            }
        });

        Table portraitControlTable = new Table();
        portraitControlTable.add(prevButton).padRight(5);
        portraitControlTable.add(portraitDisplay).size(150, 150);
        portraitControlTable.add(nextButton).padLeft(5);

        nameField = new TextField("", textFieldStyle);
        nameField.setMessageText("Enter your character's name");
        nameField.setAlignment(Align.left);

        speciesField = new TextField("", textFieldStyle);
        speciesField.setMessageText("Enter species name");
        speciesField.setAlignment(Align.left);

        Table nameTable = new Table();
        nameTable.add(new Label("Name:", whiteLabelStyle)).padRight(8);
        nameTable.add(nameField).width(220);

        Table speciesTable = new Table();
        speciesTable.add(new Label("Species:", whiteLabelStyle)).padRight(8);
        speciesTable.add(speciesField).width(220);

        rollModeValueLabel = new Label(rollModes[currentRollModeIndex], whiteLabelStyle);
        TextButton changeRollModeButton = new TextButton("Change", skin);
        changeRollModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentRollModeIndex = (currentRollModeIndex + 1) % rollModes.length;
                rollModeValueLabel.setText(rollModes[currentRollModeIndex]);
            }
        });

        Table rollModeTable = new Table();
        rollModeTable.add(new Label("Roll Mode:", whiteLabelStyle)).padRight(8);
        rollModeTable.add(rollModeValueLabel).padRight(10);
        rollModeTable.add(changeRollModeButton);

        classSelectBox = new SelectBox<>(skin);
        classSelectBox.setItems("Expert", "Killer", "Scholar", "Psion");
        classSelectBox.setSelected("Expert");
        selectedClass = classSelectBox.getSelected();

        Table abilitiesTable = new Table();

        for (int i = 0; i < ABILITIES.length; i++) {
            abilityLabels[i] = new Label(ABILITIES[i] + ":", whiteLabelStyle);

            abilityFields[i] = new TextField("", textFieldStyle);
            abilityFields[i].setDisabled(true);
            abilityFields[i].setAlignment(Align.center);

            final int statIndex = i;
            TextButton swapButton = new TextButton("Swap", skin);
            swapButtons[i] = swapButton;

            swapButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String mode = rollModes[currentRollModeIndex];
                    if (!statsRolled) return;
                    if (!mode.equals("Pussy Mode") && !mode.equals("Normal Mode")) return;

                    if (firstSelectedIndex == null) {
                        firstSelectedIndex = statIndex;
                        swapButtons[statIndex].setColor(Color.YELLOW);
                    } else if (firstSelectedIndex == statIndex) {
                        swapButtons[statIndex].setColor(Color.WHITE);
                        firstSelectedIndex = null;
                    } else {
                        int temp = rolledBaseStats[firstSelectedIndex];
                        rolledBaseStats[firstSelectedIndex] = rolledBaseStats[statIndex];
                        rolledBaseStats[statIndex] = temp;

                        rebuildPreviewCharacter();

                        swapButtons[firstSelectedIndex].setColor(Color.WHITE);
                        swapButtons[statIndex].setColor(Color.WHITE);
                        firstSelectedIndex = null;
                    }
                }
            });

            Table singleStatTable = new Table();
            singleStatTable.add(abilityLabels[i]).padRight(8);
            singleStatTable.add(abilityFields[i]).width(60).padRight(8);
            singleStatTable.add(swapButton);

            abilitiesTable.add(singleStatTable).right();
            abilitiesTable.row();
        }

        hpPreviewLabel = new Label("HP: -", whiteLabelStyle);

        Label traitsLabel = new Label("Select Race Traits:", whiteLabelStyle);

        Table traitTable = new Table();
        for (Class<? extends RaceTrait> traitClass : ALL_TRAITS) {
            traitTable.add(createTraitRow(traitClass)).left().pad(4).row();
        }

        ScrollPane traitScrollPane = new ScrollPane(traitTable, skin);
        traitScrollPane.setFadeScrollBars(false);
        traitScrollPane.setScrollingDisabled(true, false);
        traitScrollPane.setScrollbarsOnTop(true);
        traitScrollPane.setSmoothScrolling(true);

        selectedTraitsLabel = new Label("Selected Traits: (none)", whiteLabelStyle);
        selectedTraitsLabel.setWrap(true);

        TextButton rollButton = new TextButton("Roll Stats", skin);
        rollButton.setColor(Color.RED);
        rollButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (rollModes[currentRollModeIndex]) {
                    case "Pussy Mode":
                        ArrayList<Integer> pussyRolls = new ArrayList<>();
                        for (int i = 0; i < ABILITIES.length; i++) {
                            pussyRolls.add(roll4d6DropLowest());
                        }
                        Collections.sort(pussyRolls, Collections.reverseOrder());
                        for (int i = 0; i < ABILITIES.length; i++) {
                            rolledBaseStats[i] = pussyRolls.get(i);
                        }
                        break;

                    case "Easy Mode":
                        for (int i = 0; i < ABILITIES.length; i++) {
                            rolledBaseStats[i] = roll4d6DropLowest();
                        }
                        break;

                    case "Normal Mode":
                        ArrayList<Integer> normalRolls = new ArrayList<>();
                        for (int i = 0; i < ABILITIES.length; i++) {
                            normalRolls.add(roll3d6());
                        }
                        Collections.sort(normalRolls, Collections.reverseOrder());
                        for (int i = 0; i < ABILITIES.length; i++) {
                            rolledBaseStats[i] = normalRolls.get(i);
                        }
                        break;

                    case "Hardcore Mode":
                        for (int i = 0; i < ABILITIES.length; i++) {
                            rolledBaseStats[i] = roll3d6();
                        }
                        break;
                }

                // Roll starting HP die ONCE for this rolled character
                rolledStartingHpDieResult = rollStartingHpDieForClass(selectedClass);

                statsRolled = true;
                recomputeTraitPenalties();
                updateTraitCountLabels();
                updateSelectedTraitsLabel();
                rebuildPreviewCharacter();

                if (firstSelectedIndex != null) {
                    swapButtons[firstSelectedIndex].setColor(Color.WHITE);
                    firstSelectedIndex = null;
                }
            }
        });

        TextButton createCharacterButton = new TextButton("Create Character", skin);
        createCharacterButton.setColor(Color.GREEN);
        createCharacterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!statsRolled) {
                    Dialog warning = new Dialog("Cannot Create Character", skin);
                    warning.text("You must roll your stats first.");
                    warning.button("OK");
                    warning.show(stage);
                    return;
                }

                PlayerCharacter pc = buildPlayerCharacter();
                game.setPlayerCharacter(pc);
                game.startGame();
            }
        });

        skillsTable = new Table();
        ScrollPane skillsScroll = new ScrollPane(skillsTable, skin);
        skillsScroll.setFadeScrollBars(false);
        skillsScroll.setScrollingDisabled(false, false);
        skillsScroll.setScrollbarsOnTop(true);

        unspentSkillPoints = getBaseClassSkillPoints();
        unspentSkillPointsLabel = new Label("Unspent Skill Points: " + unspentSkillPoints, whiteLabelStyle);

        classSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedClass = classSelectBox.getSelected();
                playerSkills = new Skills();

                if (statsRolled) {
                    rolledStartingHpDieResult = rollStartingHpDieForClass(selectedClass);
                }

                updateSkillUI(selectedClass);
                rebuildPreviewCharacter();
            }
        });

        Table left = new Table();
        left.top().left();
        left.add(portraitControlTable).left().row();
        left.add(nameTable).left().padTop(10).row();
        left.add(speciesTable).left().padTop(10).row();

        Table middle = new Table();
        middle.top().left();
        middle.add(new Label("Class:", whiteLabelStyle)).left().row();
        middle.add(classSelectBox).width(220).left().padBottom(15).row();
        middle.add(traitsLabel).left().padBottom(8).row();
        middle.add(traitScrollPane).width(300).height(180).left().row();
        middle.add(selectedTraitsLabel).width(300).left().padTop(10).row();

        Table right = new Table();
        right.top().right();
        right.add(rollModeTable).right().padBottom(12).row();
        right.add(abilitiesTable).right().padBottom(8).row();
        right.add(hpPreviewLabel).right().padBottom(8).row();
        right.add(rollButton).right().padBottom(10).row();
        right.add(createCharacterButton).right().row();

        Table topRow = new Table();
        topRow.add(left).top().left().padRight(30);
        topRow.add(middle).top().left().padRight(30);
        topRow.add(right).top().right();

        root.add(topRow).expandX().fillX().row();
        root.add(unspentSkillPointsLabel).left().expandX().fillX().padTop(15).padBottom(5).row();
        root.add(skillsScroll).expand().fillX().height(220).row();

        updateTraitCountLabels();
        updateSelectedTraitsLabel();
        updateSkillUI(selectedClass);
        rebuildPreviewCharacter();
    }

    private int roll4d6DropLowest() {
        int[] rolls = new int[4];
        for (int i = 0; i < 4; i++) {
            rolls[i] = (int) (Math.random() * 6) + 1;
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
            sum += (int) (Math.random() * 6) + 1;
        }
        return sum;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
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