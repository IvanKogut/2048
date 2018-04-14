package ua.kyiv.kpi.fpm.kogut.app.model;

import ua.kyiv.kpi.fpm.kogut.app.controller.EventListener;

import java.util.ArrayList;
import java.util.List;

public class Model {

    public static final int TILE_LENGTH = 50;
    public static final int FIELD_WIDTH = 4;

    private final EventListener eventListener;
    private final GameLoader gameLoader;
    private final GameData gameData;

    private boolean isNotWin = true;

    private Model(EventListener eventListener) {
        this.eventListener = eventListener;
        this.gameLoader = GameLoader.create();
        this.gameData = new GameData();

        resetGameTiles();
    }

    public static Model init(EventListener eventListener) {
        return new Model(eventListener);
    }

    public void left() {
        gameData.setMessage("");

        boolean needAddTile = false;

        for (Tile[] tiles : gameData.getTiles()) {
            boolean isChangedByCompress = compressTiles(tiles);
            boolean isChangedByMerge = mergeTiles(tiles);

            if ((isChangedByCompress || isChangedByMerge) & !needAddTile) {
                needAddTile = true;
            }
        }

        if (needAddTile) {
            addTile();
        }

        if (gameData.getMaxTile() == 2048 && isNotWin) {
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

    public void saveGame() {
        gameLoader.saveGameData(gameData);
    }

    public void loadGame() {
        try {
            final GameData loadedGame = gameLoader.loadGameData();

            gameData.setTiles(loadedGame.getTiles());
            gameData.setScore(loadedGame.getScore());
            gameData.setMaxTile(loadedGame.getMaxTile());
            gameData.setMessage(loadedGame.getMessage());

        } catch (Exception e) {
            gameData.setMessage(e.getMessage());
        }
    }

    public void restart() {
        resetGameTiles();
        gameData.setMessage("Game is restarted!");
    }

    public GameData getGameData() {
        final GameData copiedGameData = new GameData(gameData);
        copiedGameData.setTiles(getCopyGameTiles());

        return this.gameData;
    }

    private void addTile() {
        final List<Tile> emptyTiles = getEmptyTiles();

        final Tile randomEmptyTile = emptyTiles.get((int) (emptyTiles.size() * Math.random()));
        randomEmptyTile.setValue(Math.random() < 0.9 ? 2 : 4);
    }

    private List<Tile> getEmptyTiles() {
        final List<Tile> tiles = new ArrayList<>();

        for (Tile[] tilesRow : gameData.getTiles()) {
            for (Tile tile : tilesRow) {
                if (tile.isEmpty()) {
                    tiles.add(tile);
                }
            }
        }

        return tiles;
    }

    private void resetGameTiles() {
        gameData.setScore(0);
        gameData.setMaxTile(2);

        final Tile[][] gameTiles = gameData.getTiles();

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
        final boolean value = tryMergeTiles();
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
        final Tile[][] copiedTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        final Tile[][] gameTiles = gameData.getTiles();

        for(int i = 0; i < gameTiles.length; i++) {
            copiedTiles[i] = new Tile[FIELD_WIDTH];
            System.arraycopy(gameTiles[i], 0, copiedTiles[i], 0, FIELD_WIDTH);
        }

        return copiedTiles;
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
                        tiles[i].setValue(tiles[j].getValue());
                        tiles[j].setValue(0);
                        break;
                    }
                }
            }
        }

        return changedPlayingField;
    }

    private void transposeTiles() {
        final Tile[][] gameTiles = gameData.getTiles();

        for(int i = 0; i < gameTiles.length; i++) {
            for(int j = i+1; j < gameTiles[i].length; j++) {
                Tile temp = gameTiles[i][j];
                gameTiles[i][j] = gameTiles[j][i];
                gameTiles[j][i] = temp;
            }
        }
    }

    private void reverseRows() {
        final Tile[][] gameTiles = gameData.getTiles();

        for(int i = 0; i < gameTiles.length / 2; i++) {
            Tile[] temp = gameTiles[i];
            gameTiles[i] = gameTiles[gameTiles.length - i - 1];
            gameTiles[gameTiles.length - i - 1] = temp;
        }
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean changedPlayingField = false;

        for (int i = 0; i < tiles.length - 1 && !tiles[i].isEmpty(); i++) {
            if (tiles[i].getValue() == tiles[i + 1].getValue()) {
                changedPlayingField = true;

                int valueTile = tiles[i].getValue() * 2;
                tiles[i].setValue(valueTile);
                tiles[i + 1].setValue(0);

                if (gameData.getMaxTile() < valueTile) {
                    gameData.setMaxTile(valueTile);
                }

                gameData.setScore(gameData.getScore() + valueTile);
                compressTiles(tiles);
            }
        }

        return changedPlayingField;
    }
}