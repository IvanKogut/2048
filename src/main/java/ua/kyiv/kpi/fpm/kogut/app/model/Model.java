package ua.kyiv.kpi.fpm.kogut.app.model;

import ua.kyiv.kpi.fpm.kogut.app.controller.EventListener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Model {

    public static final int TILE_LENGTH = 50;
    private static final int FIELD_WIDTH = 4;

    private static File defaultPathToRecordedGameState = new File("D:\\Game2048State\\data.gd");

    static {
        if (!defaultPathToRecordedGameState.exists()) {
            try {
                defaultPathToRecordedGameState.getParentFile().mkdirs();
                defaultPathToRecordedGameState.createNewFile();
            } catch (IOException e) {
                System.exit(0);
            }
        }
    }

    private final EventListener eventListener;

    private boolean isNotWin = true;

    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    private int score;
    private int maxTile;

    private String message = "";

    private Model(EventListener eventListener) {
        this.eventListener = eventListener;
        resetGameTiles();
    }

    public static Model init(EventListener eventListener) {
        return new Model(eventListener);
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

    public String getMessage() {
        return message;
    }

    public void left() {
        message = "";

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

        if (maxTile == 2048 && isNotWin) {
            eventListener.onWin();
            isNotWin = false;
        }

        if (getEmptyTiles().size() == 0) {
            checkCompletion();
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

    public void saveTiles() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(defaultPathToRecordedGameState);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(gameTiles);
            objectOutputStream.writeInt(score);
            objectOutputStream.writeInt(maxTile);
            objectOutputStream.flush();

            message = "Game is saved!";

        } catch (IOException e) {

            message = "Error during saving game...";
        }
    }

    public void loadTiles() {

        if (isEmpty()) {
            message = "There is not saved game...";
            return;
        }

        try (FileInputStream fileInputStream = new FileInputStream(defaultPathToRecordedGameState);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            gameTiles = (Tile[][]) objectInputStream.readObject();
            score = objectInputStream.readInt();
            maxTile = objectInputStream.readInt();

            message = "Game is loaded!";

        } catch (IOException | ClassNotFoundException e) {

            message = "Error during loading game...";
        }
    }

    public void restart() {
        resetGameTiles();
        message = "Game is restarted!";
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
        score = 0;
        maxTile = 2;

        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }

        addTile();
        addTile();
    }

    private void checkCompletion() {
            // (1,3) - UP; (2,2) - RIGHT; (3, 1) - DOWN; (0,0) - LEFT
        if (!checkMoveSide(1, 3) && !checkMoveSide(2, 2) && !checkMoveSide(3, 1) && !checkMoveSide(0, 0)) {
            eventListener.onLose();
        }
    }

    private boolean checkMoveSide(int timesFirst, int timesSecond) {
        rotateBy90Anticlockwise(timesFirst);
        boolean value = tryMergeTiles();
        rotateBy90Anticlockwise(timesSecond);
        return value;
    }

    private boolean tryMergeTiles() {
        boolean value = false;
        for (Tile[] tiles : getCopyGameTiles()) {
            value |= mergeTiles(tiles);
        }
        return value;
    }

    private Tile[][] getCopyGameTiles() {
        Tile[][] tiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for(int i = 0; i < gameTiles.length; i++) {
            tiles[i] = new Tile[FIELD_WIDTH];
            System.arraycopy(gameTiles[i], 0, tiles[i], 0, FIELD_WIDTH);
        }

        return tiles;
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

    private boolean isEmpty() {
        return defaultPathToRecordedGameState.length() == 0;
    }
}