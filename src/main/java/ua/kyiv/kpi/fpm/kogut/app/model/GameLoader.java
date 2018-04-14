package ua.kyiv.kpi.fpm.kogut.app.model;

import java.io.*;

class GameLoader {

    private final File recordedGameStateFile;

    private GameLoader(String savedGameFilePath) {
        this.recordedGameStateFile = new File(savedGameFilePath);
        createDirectoryWithSavedGame();
    }

    static GameLoader create(final String savedGameFilePath) {
        return new GameLoader(savedGameFilePath);
    }

    void saveGameData(final GameData gameData) {

        String message;
        try (FileOutputStream fileOutputStream = new FileOutputStream(recordedGameStateFile);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(gameData.getTiles());
            objectOutputStream.writeInt(gameData.getScore());
            objectOutputStream.writeInt(gameData.getMaxTile());
            objectOutputStream.flush();

            message = "Game is saved!";
        } catch (IOException e) {
            message = "Error during saving game...";
        }

        gameData.setMessage(message);
    }

    GameData loadGameData() {

        if (!savedGameExists()) {
            throw new GameDataLoadException("There is not saved game...");
        }

        final GameData gameData = new GameData();

        try (FileInputStream fileInputStream = new FileInputStream(recordedGameStateFile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            gameData.setTiles((Tile[][]) objectInputStream.readObject());
            gameData.setScore(objectInputStream.readInt());
            gameData.setMaxTile(objectInputStream.readInt());
            gameData.setMessage("Game is loaded!");

            return gameData;
        } catch (IOException | ClassNotFoundException e) {
            throw new GameDataLoadException("Error during loading game...");
        }
    }

    private boolean savedGameExists() {
        return recordedGameStateFile.length() != 0;
    }

    private void createDirectoryWithSavedGame() {
        if (!recordedGameStateFile.exists()) {
            try {
                recordedGameStateFile.getParentFile().mkdirs();
                recordedGameStateFile.createNewFile();
            } catch (IOException e) {
                System.exit(0);
            }
        }
    }
}
