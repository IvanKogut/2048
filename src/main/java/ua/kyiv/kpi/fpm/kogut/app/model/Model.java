package ua.kyiv.kpi.fpm.kogut.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 07.02.2017.
 */
public class Model {

    public static final int TILE_LENGTH = 50;
    private static final int FIELD_WIDTH = 4;

    private Tile[][] gameTiles;
    private int score;
    private int maxTile;

    public Model() {
        resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public int getScore() {
        return score;
    }

    public int getMaxTile() {
        return maxTile;
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        Tile randomEmptyTile = emptyTiles.get((int) (emptyTiles.size() * Math.random()));
        randomEmptyTile.value = Math.random() < 0.9 ? 2 : 4;
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> tiles = new ArrayList<>();

        for (Tile[] tilesRow : gameTiles) {
            for (Tile tile : tilesRow) {
                if (tile.isEmpty()) {
                    tiles.add(tile);
                }
            }
        }

        return tiles;
    }

    private void resetGameTiles() {
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
//
//        this.gameTiles = new Tile[][] {
//                {new Tile(2), new Tile(), new Tile(2), new Tile(4)},
//                {new Tile(), new Tile(2), new Tile(), new Tile()},
//                {new Tile(), new Tile(), new Tile(), new Tile()},
//                {new Tile(), new Tile(), new Tile(16), new Tile()}
//        };

    }

    public void left() {
        boolean needAddTile = false;
        for (Tile[] tiles : gameTiles) {
            boolean isChangedByCompress = compressTiles(tiles);
            boolean isChangedByMerge = mergeTiles(tiles);

            if ((isChangedByCompress || isChangedByMerge) & !needAddTile) {
                needAddTile = true;
            }
        }

        if (needAddTile) {
            addTile();
        }
    }

    public void up() {
        rotateBy90Anticlockwise(1);
        left();
        rotateBy90Anticlockwise(3);
    }

    public void right() {
        rotateBy90Anticlockwise(2);
        left();
        rotateBy90Anticlockwise(2);
    }

    public void down() {
        rotateBy90Anticlockwise(3);
        left();
        rotateBy90Anticlockwise(1);
    }

    private void rotateBy90Anticlockwise(int times) {
        for (int i = 0; i < times; i++) {
            transposeTiles();
            reverseRows();
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

    private void transposeTiles() {
        for(int i = 0; i < gameTiles.length; i++) {
            for(int j = i+1; j < gameTiles[i].length; j++) {
                Tile temp = gameTiles[i][j];
                gameTiles[i][j] = gameTiles[j][i];
                gameTiles[j][i] = temp;
            }
        }
    }

    private void reverseRows() {
        for(int i = 0; i < gameTiles.length / 2; i++) {
            Tile[] temp = gameTiles[i];
            gameTiles[i] = gameTiles[gameTiles.length - i - 1];
            gameTiles[gameTiles.length - i - 1] = temp;
        }
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