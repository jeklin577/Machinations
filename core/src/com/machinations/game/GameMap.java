package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameMap {
    private static final int TILE_SIZE = 96;       // Rendered tile size
    private static final int SPRITE_TILE_SIZE = 40; // Actual tile size in spritesheet
    private static final Set<Integer> IMPASSABLE_TILES = new HashSet<>(Arrays.asList(4, 5, 6, 7, 8));
    public static final int TRIGGER_TILE = 2;
    private static final int TOTAL_MAP_COUNT = 10;

    private int[][] map;              // [x][y]
    private Event[][] events;         // [x][y]
    private boolean[][] passability;  // [x][y]
    private int[] triggerTilePosition = new int[2]; // {x, y}
    private Player player;

    private TextureRegion[] tileRegions; // Tiles from spritesheet
    private int currentMapIndex;
    private EventRegistry eventRegistry;

    private static GameMap instance;

    // Private constructor for singleton
    private GameMap(TextureRegion[] tileRegions, int mapIndex) {
        this.tileRegions = tileRegions;
        this.currentMapIndex = mapIndex;
        loadMapFromJson(mapIndex);
        this.events = new Event[getWidth()][getHeight()];
    }

    // Singleton access
    public static GameMap getInstance(TextureRegion[] tileRegions, int mapIndex) {
        if (instance == null || instance.currentMapIndex != mapIndex) {
            instance = new GameMap(tileRegions, mapIndex);
        }
        return instance;
    }

    // Load map from JSON file
    private void loadMapFromJson(int mapIndex) {
        String filename = "maps/map" + mapIndex + ".json";
        Json json = new Json();

        try {
            String jsonString = Gdx.files.internal(filename).readString();
            MapData mapData = json.fromJson(MapData.class, jsonString);

            // mapData.tiles is [y][x], top row first
            int[][] original = mapData.tiles;
            int height = original.length;
            int width = original[0].length;
            map = new int[width][height]; // [x][y]

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Flip vertically while loading so y=0 is bottom row
                    map[x][y] = original[height - 1 - y][x];
                }
            }

            passability = createPassabilityMap(map);
            triggerTilePosition = findTriggerTilePosition(map);

        } catch (Exception e) {
            throw new RuntimeException("Error loading map JSON: " + filename, e);
        }
    }

    // Generate passability grid
    private boolean[][] createPassabilityMap(int[][] map) {
        int width = map.length;
        int height = map[0].length;

        boolean[][] passMap = new boolean[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                passMap[x][y] = !IMPASSABLE_TILES.contains(map[x][y]);
            }
        }
        return passMap;
    }

    // Find trigger tile position
    private int[] findTriggerTilePosition(int[][] map) {
        int width = map.length;
        int height = map[0].length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] == TRIGGER_TILE) {
                    return new int[]{x, y};
                }
            }
        }
        return new int[]{-1, -1};
    }

    public int getWidth() {
        return map.length;
    }

    public int getHeight() {
        return map[0].length;
    }

    public int[][] getMap() {
        return map;
    }

    public boolean[][] getPassability() {
        return passability;
    }

    public int[] getTriggerTilePosition() {
        return triggerTilePosition;
    }

    public void draw(SpriteBatch batch) {
        int width = getWidth();
        int height = getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int tileId = map[x][y];
                TextureRegion tileRegion = tileRegions[tileId];
                if (tileRegion == null) {
                    System.err.println("Warning: tileRegion for tileId " + tileId + " at (" + x + "," + y + ") is null. Skipping draw.");
                    continue;
                }
                batch.draw(tileRegion, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public void changeTile(int x, int y, int newTileId) {
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            map[x][y] = newTileId;
            passability = createPassabilityMap(map);
            triggerTilePosition = findTriggerTilePosition(map);
        }
    }

    public Event getEventAt(int x, int y) {
        if (events != null && x >= 0 && y >= 0 && x < events.length && y < events[0].length) {
            return events[x][y];
        }
        return null;
    }

    public void transportPlayer(Player player, int mapIndex, int posX, int posY) {
        if (player == null) {
            System.out.println("Error: player is null in transportPlayer!");
            return;
        }

        this.player = player;
        setMapIndex(mapIndex);
        player.setPosition(posX, posY);

        System.out.println("Player transported to map " + mapIndex + " at position (" + posX + ", " + posY + ")");
    }

    public void addEventTile(int x, int y, Event event) {
        if (x >= 0 && y >= 0 && x < events.length && y < events[0].length) {
            events[x][y] = event;
        }
    }

    public void setEventRegistry(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    public void populateEvents(EventRegistry registry) {
        this.events = new Event[getWidth()][getHeight()];
        populateSemanticEvents(registry);
        populateUniqueEvents(registry);
    }

    private void populateSemanticEvents(EventRegistry registry) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                int tileId = map[x][y];

                switch (tileId) {
                    case 6:
                        addEventTile(x, y, registry.getEvent("LOOK_OUT_WINDOW"));
                        break;
                    case 9:
                        addEventTile(x, y, registry.getEvent("HACK_TERMINAL"));
                        break;
                    // case 10:
                    //     addEventTile(x, y, registry.getEvent("SEARCH_SCENERY"));
                    //     break;
                }
            }
        }
    }

    private void populateUniqueEvents(EventRegistry registry) {
        switch (currentMapIndex) {
            case 4:
                addEventTile(6, 6, registry.getEvent("MAP4_TRANSPORT"));
                break;
            default:
                break;
        }
    }

    public void triggerEvent(int x, int y, PlayerCharacter player) {
        if (events != null && x >= 0 && y >= 0 && x < events.length && y < events[0].length) {
            Event event = events[x][y];
            if (event != null) {
                event.trigger(player);
            }
        }
    }

    public boolean isPassable(int x, int y) {
        if (x < 0 || x >= passability.length || y < 0 || y >= passability[0].length) {
            return false;
        }
        return passability[x][y];
    }

    public Event[][] getEvents() {
        return events;
    }

    public int getTriggerMapIndex() {
        return (currentMapIndex + 1) % TOTAL_MAP_COUNT;
    }

    public void setMapIndex(int newIndex) {
        this.currentMapIndex = newIndex;
        loadMapFromJson(newIndex);
        this.events = new Event[getWidth()][getHeight()];

        if (eventRegistry != null) {
            populateEvents(eventRegistry);
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    // Utility method to load and split spritesheet into TextureRegion array
    public static TextureRegion[] loadTileRegions(String spritesheetPath, int tileWidth, int tileHeight) {
        Texture spritesheet = new Texture(Gdx.files.internal("Tiles/Spritesheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(spritesheet, tileWidth, tileHeight);

        int rows = tmp.length;
        int cols = tmp[0].length;
        TextureRegion[] flat = new TextureRegion[rows * cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                flat[row * cols + col] = tmp[row][col];
            }
        }

        return flat;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}