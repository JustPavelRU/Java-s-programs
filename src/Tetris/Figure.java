package Tetris;

public class Figure {

    private int[][] coordinates; private char type = 'N';
    private int step = 1;

    public Figure(char type) {
        switch (type) {
            case 'O':
                this.coordinates = new int[][]{{1, 5}, {1, 6}, {2, 5}, {2, 6}};
                this.type = type;
                break;
            case 'I':
                this.coordinates = new int[][]{{1, 4}, {1, 5}, {1, 6}, {1, 7}};
                this.type = type;
                break;
            case 'S':
                this.coordinates = new int[][]{{2, 5}, {2, 6}, {1, 6}, {1, 7}};
                this.type = type;
                break;
            case 'Z':
                this.coordinates = new int[][]{{1, 5}, {1, 6}, {2, 6}, {2, 7}};
                this.type = type;
                break;
            case 'L':
                this.coordinates = new int[][]{{2, 5}, {2, 6}, {2, 7}, {1, 7}};
                this.type = type;
                break;
            case 'J':
                this.coordinates = new int[][]{{2, 7}, {2, 6}, {2, 5}, {1, 5}};
                this.type = type;
                break;
            case 'T':
                this.coordinates = new int[][]{{2, 5}, {2, 6}, {1, 6}, {2, 7}};
                this.type = type;
                break;
        }
    }

    public Figure(Figure figure) {
        coordinates = new int[4][2];
        type = figure.type;
        for (int p = 0; p < figure.coordinates.length; p++) {
            for (int q = 0; q < figure.coordinates[p].length; q++) {
                coordinates[p][q] = figure.coordinates[p][q];
            }
        }
    }

    // Движение фигуры.
    public void move(char cmd) {
        switch (cmd) {
            case 'W':
                for (int p = 0; p < MainGame.getDroppedFiguresCoordinates().length; p++) {
                    if (!isBottomClean(MainGame.getDroppedFiguresCoordinates())) break;
                    for (int q = 0; q < coordinates.length; q++) {
                        coordinates[q][0]++;
                    }
                }
                break;
            case 'A':
                for (int n = 0; n < coordinates.length; n++) {
                    if (step % 2 == 0) {
                        coordinates[n][0]++;
                    }
                    coordinates[n][1]--;
                }
                step++;
                break;
            case 'S':
                for (int n = 0; n < coordinates.length; n++) {
                    coordinates[n][0]++;
                }
                step = 1;
                break;
            case 'D':
                for (int n = 0; n < coordinates.length; n++) {
                    if (step % 2 == 0) {
                        coordinates[n][0]++;
                    }
                    coordinates[n][1]++;
                }
                step++;
                break;
            case 'R':
                rotate();
                if (step % 2 == 0) {
                    for (int n = 0; n < coordinates.length; n++) {
                        coordinates[n][0]++;
                    }
                }
                step++;
                break;
        }
    }

    // Поворот фигуры(по часовой стрелке).
    private void rotate() {
        if (type == 'O') return;
        int centerP = coordinates[1][0]; int centerQ = coordinates[1][1];
        for (int n = 0; n < 4; n++) {
            if (n == 1) continue;
            if ((centerP - 1 == coordinates[n][0]) && (centerQ == coordinates[n][1])) {     // 12 hours
                coordinates[n][0] += 1;
                coordinates[n][1] += 1;
                continue;
            }
            if ((centerP - 2 == coordinates[n][0]) && (centerQ == coordinates[n][1])) {     // 12 hours / 2 Ring
                coordinates[n][0] += 2;
                coordinates[n][1] += 2;
                continue;
            }
            if ((centerP - 1 == coordinates[n][0]) && (centerQ + 1 == coordinates[n][1])) { // 1.5 hours
                coordinates[n][0] += 2;
                continue;
            }
            if ((centerP == coordinates[n][0]) && (centerQ + 1 == coordinates[n][1])) {     // 3 hours
                coordinates[n][0] += 1;
                coordinates[n][1] -= 1;
                continue;
            }
            if ((centerP == coordinates[n][0]) && (centerQ + 2 == coordinates[n][1])) {     // 3 hours / 2 Ring
                coordinates[n][0] += 2;
                coordinates[n][1] -= 2;
                continue;
            }
            if ((centerP + 1 == coordinates[n][0]) && (centerQ + 1 == coordinates[n][1])) { // 4.5 hours
                coordinates[n][1] -= 2;
                continue;
            }
            if ((centerP + 1 == coordinates[n][0]) && (centerQ == coordinates[n][1])) {     // 6 hours
                coordinates[n][0] -= 1;
                coordinates[n][1] -= 1;
                continue;
            }
            if ((centerP + 2 == coordinates[n][0]) && (centerQ == coordinates[n][1])) {     // 6 hours / 2 Ring
                coordinates[n][0] -= 2;
                coordinates[n][1] -= 2;
                continue;
            }
            if ((centerP + 1 == coordinates[n][0]) && (centerQ - 1 == coordinates[n][1])) { // 7.5 hours
                coordinates[n][0] -= 2;
                continue;
            }
            if ((centerP == coordinates[n][0]) && (centerQ - 1 == coordinates[n][1])) {     // 9 hours
                coordinates[n][0] -= 1;
                coordinates[n][1] += 1;
                continue;
            }
            if ((centerP == coordinates[n][0]) && (centerQ - 2 == coordinates[n][1])) {     // 9 hours / Ring 2
                coordinates[n][0] -= 2;
                coordinates[n][1] += 2;
                continue;
            }
            if ((centerP - 1 == coordinates[n][0]) && (centerQ - 1 == coordinates[n][1])) { // 10.5 hours
                coordinates[n][1] += 2;
            }
        }
    }

    // Проверка пространства под фигурой.
    public boolean isBottomClean(boolean[][] droppedFiguresCoordinates) {
        for (int n = 0; n < coordinates.length; n++) {
            if (coordinates[n][0] == droppedFiguresCoordinates.length - 2) return false;            // Дно поля.
            if (droppedFiguresCoordinates[coordinates[n][0] + 1][coordinates[n][1]]) return false;  // Снизу фигуры.
        }
        return true;
    }

    // Создание и возвращение координат тени.
    public int[][] getShadowCoordinates() {
        Figure figureShadow = new Figure(this);
        figureShadow.move('W');
        return figureShadow.coordinates;
    }

    public int[][] getCoordinates() {
        return this.coordinates;
    }

    public char getType() {
        return type;
    }
}
