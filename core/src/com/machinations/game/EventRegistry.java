

package com.machinations.game;

import java.util.Map;

public class EventRegistry {
    private final Map<String, Event> registry;

    public EventRegistry(GameMap gameMap, DialogueSystem dialogueSystem, Log log) {
        registry = Map.of(
                "CLIMB_WALL", Event.createClimbingEvent(gameMap, dialogueSystem, log),
                "HACK_TERMINAL", Event.createHackingEvent(gameMap, dialogueSystem, log),
                "LOOK_OUT_WINDOW", Event.createLookOutWindowEvent(gameMap, dialogueSystem, log),
                "MAP4_TRANSPORT", Event.createMapIndex4TransportEvent(gameMap, dialogueSystem, log)
        );
    }

    public Event getEvent(String name) {
        return registry.get(name);
    }
}