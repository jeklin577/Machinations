
package com.machinations.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class MapLoader {
    private static final String MAPS_FILE = "maps/maps.json";
    private static Map<Integer, int[][]> loadedMaps = new HashMap<>();

    public static int[][] loadMap(int mapIndex) {
        if (loadedMaps.containsKey(mapIndex)) {
            return loadedMaps.get(mapIndex);
        }

        Json json = new Json();
        JsonValue root = json.fromJson(null, Gdx.files.internal(MAPS_FILE));

        for (JsonValue mapEntry : root.get("maps")) {
            int index = mapEntry.getInt("index");
            if (index == mapIndex) {
                JsonValue tilesJson = mapEntry.get("tiles");
                int[][] tiles = new int[tilesJson.size][tilesJson.get(0).size];

                for (int i = 0; i < tilesJson.size; i++) {
                    JsonValue row = tilesJson.get(i);
                    for (int j = 0; j < row.size; j++) {
                        tiles[i][j] = row.getInt(j);
                    }
                }

                loadedMaps.put(mapIndex, tiles);
                return tiles;
            }
        }

        throw new IllegalArgumentException("Map index " + mapIndex + " not found in JSON.");
    }
}
