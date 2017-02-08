package ua.kyiv.kpi.fpm.kogut.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 07.02.2017.
 */
public class Model {

    private static final int FIELD_WIDTH = 4;

    private Tile[][] gameTiles;
    private int score;
    private int maxTile;

    public Model() {
        resetGameTiles();
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        Tile randomEmptyTile = emptyTiles.get((int) (emptyTiles.size() * Math.random()));
        int newValue = Math.random() < 0.9 ? 2 : 4;
        randomEmptyTile.value = newValue;
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> tiles = new ArrayList<>();

        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                if (gameTiles[i][j].isEmpty()) {
                    tiles.add(gameTiles[i][j]);
                }
            }
        }

        return tiles;
    }

    public void resetGameTiles() {
        this.gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        this.score = 0;
        this.maxTile = 2;

        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }

        addTile();
        addTile();
    }

    public void left() {
        boolean needAddTile = false;
        for (Tile[] tiles : gameTiles) {
            boolean isChangedByCompress = compressTiles(tiles);
            boolean isChangedByMerge = mergeTiles(tiles);

            if ((isChangedByCompress && isChangedByMerge) & !needAddTile) {
                needAddTile = true;
            }
        }

        if (needAddTile) {
            addTile();
        }
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean changedPlayingField = false;

        for (int i = 0; i < tiles.length - 1; i++) {
            if (tiles[i].isEmpty()) {
                for (int j = i + 1; j < tiles.length; j++) {
                    if (!tiles[j].isEmpty()) {
                        changedPlayingField = true;
                        tiles[i].value = tiles[j].value;
                        tiles[j].value = 0;
                        break;
                    }
                }
            }
        }

        return changedPlayingField;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean changedPlayingField = false;

        for (int i = 0; i < tiles.length - 1 && !tiles[i].isEmpty(); i++) {
            if (tiles[i].value == tiles[i+1].value) {
                changedPlayingField = true;

                int valueTile = tiles[i].value * 2;
                tiles[i].value = valueTile;
                tiles[i+1].value = 0;

                if (maxTile < valueTile) {
                    maxTile = valueTile;
                }

                score += valueTile;
                compressTiles(tiles);
            }
        }

        return changedPlayingField;
    }
}