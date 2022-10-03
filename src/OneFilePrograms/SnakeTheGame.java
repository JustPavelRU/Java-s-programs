package OneFilePrograms;

import java.util.Arrays;
import java.util.Random;
import java.io.IOException;

public class SnakeTheGame {

    private static int[][] snakeCoord = new int[50][2];    // Coordinates of snake's segments. Starts with head.
    private static int snakeLength = 3;                    // Snake's length.
    private static int lastTailP = 4, lastTailQ = 9;       // Coordinates of last tail.

    private static char[][] field = new char[5][10];       // Graphic field.
    private static int appleP = 3, appleQ = 6;             // Apple coordinates.
    private static boolean isRules = true;

    private static Random random = new Random();
    private static String textEnd = "GAMEOVER";

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < field.length; i++) {
            Arrays.fill(field[i], ' ');
        }
        snakeCoord[0][0] = 0;                              // Head.
        snakeCoord[0][1] = 2;

        snakeCoord[1][0] = 0;                              // Body.
        snakeCoord[1][1] = 1;

        snakeCoord[2][0] = 0;                              // Tail.
        snakeCoord[2][1] = 0;

        do {
            appleP = random.nextInt(5);
            appleQ = random.nextInt(10);
        } while (checkBodyPenetrated());
        showField();
        while (isRules) {                                  // Gaming process.
            goSnake((char) System.in.read());
            showField();
        }
        sayGameOver();
    }

    private static void showField() throws InterruptedException {              // Graphic's writing and some logic.
        Thread.sleep(350);
        field[lastTailP][lastTailQ] = ' ';                 // Deleting of last tail.
        field[appleP][appleQ] = '#';
        checkApplePicked();
        for (int i = 0; i < snakeLength; i++) {
            int p = snakeCoord[i][0]; int q = snakeCoord[i][1];
            if (i == 0) {
                field[p][q] = 'O';
            } else if (i == snakeLength - 1) {             // For tail segment.
                field[p][q] = 'o';
            } else {
                field[p][q] = 'o';
            }
        }
        for (int i = 0; i < field.length; i++) {
            System.out.println(Arrays.toString(field[i])); // Showing a field.
        }
        System.out.println();
    }

    private static void goSnake(char cmd) {
        switch (cmd) {
            case 'w' :
                ChangeCoordinatesBack();
                snakeCoord[0][0] -= 1;
                checkRules();
                break;
            case 'a' :
                ChangeCoordinatesBack();
                snakeCoord[0][1] -= 1;
                checkRules();
                break;
            case 's':
                ChangeCoordinatesBack();
                snakeCoord[0][0] += 1;
                checkRules();
                break;
            case 'd':
                ChangeCoordinatesBack();
                snakeCoord[0][1] += 1;
                checkRules();
                break;
        }
    }

    private static void ChangeCoordinatesBack() {
        lastTailP = snakeCoord[snakeLength - 1][0];        // Seeking of last tail position.
        lastTailQ = snakeCoord[snakeLength - 1][1];
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeCoord[i][0] = snakeCoord[i - 1][0];
            snakeCoord[i][1] = snakeCoord[i - 1][1];
        }
    }

    private static void checkApplePicked() {
        if ((snakeCoord[0][0] == appleP) && (snakeCoord[0][1] == appleQ)) {
            field[appleP][appleQ] = ' ';
            do {
                appleP = random.nextInt(5);
                appleQ = random.nextInt(10);
            } while (checkBodyPenetrated());
            snakeLength += 1;
            snakeCoord[snakeLength - 1][0] = lastTailP; snakeCoord[snakeLength - 1][1] = lastTailQ;
        }
    }

    private static boolean checkBodyPenetrated() {
        for (int i = 0; i < snakeLength; i++) {
            int p = snakeCoord[i][0]; int q = snakeCoord[i][1];
            if ((p == appleP) && (q == appleQ)) {
                return true;
            }
        }
        return false;
    }

    private static void checkRules() {
        for (int i = 1; i < snakeLength; i++) {
            if ((snakeCoord[0][0] == snakeCoord[i][0]) && (snakeCoord[0][1] == snakeCoord[i][1])) {
                isRules = false;
            }
        }
    }

    private static void sayGameOver() {
        for (int i = 0; i < field.length; i++) {
            Arrays.fill(field[i], ' ');
        }
        int n = 0;
        for (int p = 2, q = 1; q < 9; q++) {
            field[p][q] = textEnd.charAt(n++);
        }
        for (int i = 0; i < field.length; i++) {
            System.out.println(Arrays.toString(field[i]));
        }
    }
}
