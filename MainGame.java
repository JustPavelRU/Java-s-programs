package FreePractice.Tetris;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class MainGame {

    // Поле.
    private static int height = 20;
    private static char[][] field = new char[height][12];
    private static boolean[][] droppedFiguresCoordinates = new boolean[height][12];

    // Фигуры.
    private static Figure figure, figureNext, figureCopy;
    private static String figuresStore = "OISZLJT";

    // Разное.
    private static int score = 0;
    private static Random random = new Random();

    public static void main(String[] args) throws IOException {
        System.in.read();
        figureNext = new Figure(figuresStore.charAt(random.nextInt(7)));
        figure = new Figure(figureNext);

        while (!isCollision()) {

            // Создание случайной следующей фигуры.
            figureNext = new Figure(figuresStore.charAt(random.nextInt(7)));

            // Удаление заполенных линий(путем переноса всех линий на одну клетку вниз).
            for (int p = 1; p < field.length - 1; p++) {
                if (isLineFull(p)) {
                    score += 100;
                    int pCopy = p;
                    for (; p > 0; p--) {
                        for (int q = 1; q < 11; q++) {
                            droppedFiguresCoordinates[p][q] = droppedFiguresCoordinates[p - 1][q];
                        }
                    }
                    p = pCopy;
                }
            }

            char cmd = 'N'; char ignore;

            // Движение фигуры.
            while (figure.isBottomClean(droppedFiguresCoordinates)) {

                // Движение фигуры до падения.
                if (figure.isBottomClean(droppedFiguresCoordinates)) {
                    showField();
                    Figure figureCopy = new Figure(figure);
                    cmd = (char) System.in.read();
                    figure.move(cmd);
                    do {
                        ignore = (char) System.in.read();
                    } while (ignore != '\n');
                    if (isCollision()) {
                        figure = figureCopy;
                        figure.move('S');
                    }
                }

                // Движение фигуры после падения.
                if (!figure.isBottomClean(droppedFiguresCoordinates)) {
                    showField();
                    figureCopy = new Figure(figure);
                    if (cmd != 'W') {
                        figure.move((char) System.in.read());
                        do {
                            ignore = (char) System.in.read();
                        } while (ignore != '\n' && ignore != 'S');
                    }
                    if (isCollision()) {
                        figure = figureCopy;
                    }
                }
            }

            for (int j = 0; j < figure.getCoordinates().length; j++) {
                droppedFiguresCoordinates[figure.getCoordinates()[j][0]][figure.getCoordinates()[j][1]] = true;
            }
            figure = new Figure(figureNext);
        }
    }

    // Отрисовка и вывод графики.
    private static void showField() {

        // Отрисовка пустого поля.
        for (int p = 0; p < field.length; p++) {
            Arrays.fill(field[p], ' ');
        }

        // Отрисовка границ поля.
        Arrays.fill(field[0], '\u25A1');
        Arrays.fill(field[height - 1], '\u25A1');
        for (int p = 0; p < height; p++) {
            field[p][0] = '\u25A1';
            field[p][11] = '\u25A1';
        }

        // Отрисовка фигуры.
        for (int n = 0; n < figure.getCoordinates().length; n++) {
            field[figure.getCoordinates()[n][0]][figure.getCoordinates()[n][1]] = '\u25A0';
        }

        // Отрисовка тени
        for (int n = 0; n < figure.getShadowCoordinates().length; n++) {
            field[figure.getShadowCoordinates()[n][0]][figure.getShadowCoordinates()[n][1]] = '\u0000';
        }

        // Отрисовка упавших фигур.
        for (int p = 0; p < droppedFiguresCoordinates.length; p++) {
            for (int q = 0; q < droppedFiguresCoordinates[p].length; q++) {
                if (droppedFiguresCoordinates[p][q]) {
                    field[p][q] = '\u25A0';
                }
            }
        }

        // Вывод конечной картинки. Switch-case конструкция нужна для отображения счета и следующей фигуры.
        for (int p = 0; p < field.length; p++) {
            for (int q = 0; q < field[p].length; q++) {
                System.out.print(field[p][q] + "  ");
            }
            switch (p) {
                case 2:
                    System.out.print("\tScore: " + score);
                    break;
                case 5:
                    System.out.print("\tNext figure: ");
                    break;
                case 7:
                    System.out.print("\t  ");
                    if (figureNext.getType() == 'I') {
                        System.out.print("   ");
                    } else if (figureNext.getType() == 'O') {
                        System.out.print(" ");
                    }
                    if (figureNext.getType() != 'S' && figureNext.getType() != 'L') {
                        System.out.print("\u25A0  ");
                    }
                    if (figureNext.getType() != 'I' && figureNext.getType() != 'L' && figureNext.getType() != 'J') {
                        if (figureNext.getType() == 'S') {
                            System.out.print("   ");
                        }
                        System.out.print("\u25A0  ");
                    }
                    if (figureNext.getType() == 'S' || figureNext.getType() == 'T' || figureNext.getType() == 'L') {
                        if (figureNext.getType() == 'L') {
                            System.out.print("      ");
                        }
                        System.out.print("\u25A0  ");
                    }
                    break;
                case 8:
                    System.out.print("\t  ");
                    if (figureNext.getType() == 'I') {
                        System.out.print("   ");
                    } else if (figureNext.getType() == 'O') {
                        System.out.print(" ");
                    }
                    if (figureNext.getType() != 'Z' && figureNext.getType() != 'T') {
                        System.out.print("\u25A0  ");
                    }
                    if (figureNext.getType() != 'I') {
                        if (figureNext.getType() == 'Z' || figureNext.getType() == 'T') {
                            System.out.print("   ");
                        }
                        System.out.print("\u25A0  ");
                    }
                    if (figureNext.getType() == 'Z' || figureNext.getType() == 'L' || figureNext.getType() == 'J') {
                        System.out.print("\u25A0  ");
                    }
                    break;
                case 9:
                    System.out.print("\t  ");
                    if (figureNext.getType() == 'I') {
                        System.out.print("   ");
                    }
                    if (figureNext.getType() == 'I') {
                        System.out.print("\u25A0  ");
                    }
                    break;
                case 10:
                    System.out.print("\t  ");
                    if ('I' == figureNext.getType()) {
                        System.out.print("   ");
                    }
                    if ('I' == figureNext.getType()) {
                        System.out.print("\u25A0  ");
                    }
                    break;
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean isLineFull(int p) {
        for (int q = 1; q < 11; q++) {
            if (!droppedFiguresCoordinates[p][q]) return false;
        }
        return true;
    }

    private static boolean isCollision() {
        if (figure == null) return false;
        for (int n = 0; n < figure.getCoordinates().length; n++) {
            if (droppedFiguresCoordinates[figure.getCoordinates()[n][0]][figure.getCoordinates()[n][1]]) {
                return true;
            }
            if (figure.getCoordinates()[n][1] == 0 || figure.getCoordinates()[n][1] == 11) {
                return true;
            }
        }
        return false;
    }

    public static boolean[][] getDroppedFiguresCoordinates() {
        return droppedFiguresCoordinates;
    }
}