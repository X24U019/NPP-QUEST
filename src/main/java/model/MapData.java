package model;

public class MapData {

    public static int[][] getMap(String mapId) {
        // 現在は船橋（Village）マップを返す
        return Map_Funabashi.MAP;
    }

    public static boolean isWalkable(int[][] map, int x, int y) {
        if (map == null || y < 0 || y >= map.length || x < 0 || x >= map[0].length) {
            return false;
        }

        int tile = map[y][x];

        // 歩けるマス（草地・土・石畳・ドア下・室内床・橋など）
        return (tile == 0 || tile == 1 || tile == 6 || tile == 11 || tile == 30 || tile == 36);
    }
}