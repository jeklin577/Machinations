package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Event {
    private final String name;
    private final Skills.Skill skill;
    private final Consumer<PlayerCharacter> successEffect;
    private final Consumer<PlayerCharacter> failEffect;
    private final GameMap gameMap;
    private final DialogueSystem dialogueSystem;
    private final Log log;
    private final boolean skillCheckRequired;  // <-- new flag

    private static final Texture SUCCESS_PORTRAIT = new Texture(Gdx.files.internal("Portraits/success.png"));
    private static final Texture FAILURE_PORTRAIT = new Texture(Gdx.files.internal("Portraits/failure.png"));

    public Event(String name, Skills.Skill skill,
                 Consumer<PlayerCharacter> successEffect,
                 Consumer<PlayerCharacter> failEffect,
                 GameMap gameMap,
                 DialogueSystem dialogueSystem,
                 Log log,
                 boolean skillCheckRequired) {  // <-- constructor updated
        this.name = name;
        this.skill = skill;
        this.successEffect = successEffect;
        this.failEffect = failEffect;
        this.gameMap = gameMap;
        this.dialogueSystem = dialogueSystem;
        this.log = log;
        this.skillCheckRequired = skillCheckRequired;
    }

    public void trigger(PlayerCharacter playerCharacter) {
        boolean success;
        if (skillCheckRequired) {
            success = playerCharacter.getSkills().performSkillCheck(playerCharacter, skill);
        } else {
            // If skill check not required, automatically succeed (or change logic here)
            success = true;
        }

        if (success) {
            successEffect.accept(playerCharacter);
            triggerDialogue(true);
        } else {
            failEffect.accept(playerCharacter);
            triggerDialogue(false);
        }

        System.out.println("Event triggered for " + playerCharacter.getName());
    }

    private void triggerDialogue(boolean success) {
        if (dialogueSystem == null) return;

        List<Dialogue> dialogues = new ArrayList<>();
        if (success) {
            dialogues.add(new Dialogue("Success Speaker", "You have succeeded!", SUCCESS_PORTRAIT));
        } else {
            dialogues.add(new Dialogue("Failure Speaker", "You have failed!", FAILURE_PORTRAIT));
        }

        dialogueSystem.clear();
        dialogueSystem.addDialogues(dialogues);
        dialogueSystem.toggleDialogue();

        System.out.println("Triggering " + (success ? "success" : "failure") + " dialogue");
    }

    public String getName() {
        return name;
    }

    public Skills.Skill getSkill() {
        return skill;
    }

    public Consumer<PlayerCharacter> getSuccessEffect() {
        return successEffect;
    }

    public Consumer<PlayerCharacter> getFailEffect() {
        return failEffect;
    }

    public boolean isSkillCheckRequired() {
        return skillCheckRequired;
    }

    // Predefined event factory methods updated to include skillCheckRequired parameter

    public static Event createClimbingEvent(GameMap gameMap, DialogueSystem dialogueSystem, Log log) {
        return new Event(
                "Climbing the Wall",
                Skills.Skill.CLIMB,
                pc -> {
                    String msg = pc.getName() + " successfully climbed the wall.";
                    System.out.println(msg);
                    if (log != null) log.addEntry(msg);

                    if (dialogueSystem != null) {
                        List<Dialogue> dialogues = List.of(
                                new Dialogue("Guide", "Congratulations! You managed to scale the wall.", SUCCESS_PORTRAIT)
                        );
                        dialogueSystem.clear();
                        dialogueSystem.addDialogues(dialogues);
                        dialogueSystem.toggleDialogue();
                    }
                },
                pc -> {
                    String msg = pc.getName() + " failed to climb the wall and fell.";
                    System.out.println(msg);
                    if (log != null) log.addEntry(msg);

                    if (dialogueSystem != null) {
                        List<Dialogue> dialogues = List.of(
                                new Dialogue("Guide", "Oh no! You slipped and fell. Better luck next time.", FAILURE_PORTRAIT)
                        );
                        dialogueSystem.clear();
                        dialogueSystem.addDialogues(dialogues);
                        dialogueSystem.toggleDialogue();
                    }
                },
                gameMap,
                dialogueSystem,
                log,
                true // Climbing requires skill check
        );
    }

    public static Event createHackingEvent(GameMap gameMap, DialogueSystem dialogueSystem, Log log) {
        return new Event(
                "Hacking the Terminal",
                Skills.Skill.HACKER,
                pc -> {
                    String msg = pc.getName() + " successfully hacked the terminal.";
                    System.out.println(msg);
                    if (log != null) log.addEntry(msg);
                    // Additional success logic here
                },
                pc -> {
                    String msg = pc.getName() + " failed to hack the terminal, taking 5 Energy Damage from an overload!";
                    System.out.println(msg);
                    if (log != null) log.addEntry(msg);

                    pc.receiveDamage(5, DamageType.ENERGY);
                    // Additional failure logic here
                },
                gameMap,
                dialogueSystem,
                log,
                true // Hacking requires skill check
        );
    }

    public static Event createMapIndex4TransportEvent(GameMap gameMap, DialogueSystem dialogueSystem, Log log) {
        return new Event(
                "Transport To Map Index 4",
                Skills.Skill.HACKER,
                pc -> {
                    String msg = pc.getName() + " is being transported to Map Index 4.";
                    System.out.println(msg);
                    if (log != null) log.addEntry(msg);

                    gameMap.transportPlayer(gameMap.getPlayer(), 4, 0, 0);

                    String postMsg = pc.getName() + " has been transported to Map Index 4 at position (0, 0).";
                    System.out.println(postMsg);
                    if (log != null) log.addEntry(postMsg);
                },
                pc -> {
                    String msg = pc.getName() + " failed to transport.";
                    System.out.println(msg);
                    if (log != null) log.addEntry(msg);

                    gameMap.transportPlayer(gameMap.getPlayer(), 2, 0, 0);
                },
                gameMap,
                dialogueSystem,
                log,
                false // Transport event does NOT require skill check
        );
    }

    public static Event createLookOutWindowEvent(GameMap gameMap, DialogueSystem dialogueSystem, Log log) {
        return new Event(
                "Look Out The Window",
                Skills.Skill.HACKER,  // or a neutral skill if needed
                pc -> {
                    String msg = pc.getName() + " looks out the window and sees the galaxy outside.";
                    System.out.println(msg);
                    if (log != null) log.addEntry(msg);

                    if (dialogueSystem != null) {
                        List<Dialogue> dialogues = List.of(
                                new Dialogue("Narrator", "You gaze out the window, observing the worlds beyond.", null)
                        );
                        dialogueSystem.clear();
                        dialogueSystem.addDialogues(dialogues);
                        dialogueSystem.toggleDialogue();
                    }
                },
                pc -> {
                    // Maybe failure is not possible here, so just do success effect or do nothing
                },
                gameMap,
                dialogueSystem,
                log,
                false  // No skill check needed
        );
    }
}