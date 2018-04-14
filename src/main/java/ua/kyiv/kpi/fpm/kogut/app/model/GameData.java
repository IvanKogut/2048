package ua.kyiv.kpi.fpm.kogut.app.model;

import lombok.Data;

import static ua.kyiv.kpi.fpm.kogut.app.model.Model.FIELD_WIDTH;

@Data
public class GameData {

    private Tile[][] tiles;
    private int score;
    private int maxTile;
    private String message;

    GameData() {
        this.tiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        this.message = "";
    }

    GameData(GameData from) {
        this.tiles = from.tiles;
        this.score = from.score;
        this.maxTile = from.maxTile;
        this.message = from.message;
    }
}
