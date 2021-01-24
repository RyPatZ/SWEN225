package tests;

import Game.Board;
import Game.Player;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerMovementTests {
    /**
     * No Movement - Test the loading of test2 board
     */
    @Test
    void MoveTest1() {
        Board board = new Board("test maps/test2/", 8,8);

        assertEquals(
                "+--------------+\n" +
                        "||MS          ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||    CS  WW  ||\n" +
                        "||  WW        ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||          CM||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * 1 Step to the right
     */
    @Test
    void MoveTest2() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("right\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(0));

        player.move(1);

        assertEquals(
                "+--------------+\n" +
                "||  MS        ||\n" +
                "||  WWWWWWWW  ||\n" +
                "||    CS  WW  ||\n" +
                "||  WW        ||\n" +
                "||  WWWWWWWW  ||\n" +
                "||          CM||\n" +
                "+--------------+\n", board.toString());
    }
    /**
     * 2 Steps upwards, player 2
     */
    @Test
    void MoveTest3() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("up\nup\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(1));

        player.move(2);

        assertEquals(
                "+--------------+\n" +
                        "||MS          ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||    CS  WW  ||\n" +
                        "||  WW      CM||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||            ||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * try to move into wall
     */
    @Test
    void MoveTest4() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("left\ndown\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(0));

        player.move(1);

        assertEquals(
                "+--------------+\n" +
                        "||            ||\n" +
                        "||MSWWWWWWWW  ||\n" +
                        "||    CS  WW  ||\n" +
                        "||  WW        ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||          CM||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * try to move into a cell you've already gone to
     */
    @Test
    void MoveTest5() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("down\nup\ndown\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(0));

        player.move(2);

        assertEquals(
                "+--------------+\n" +
                        "||            ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||MS  CS  WW  ||\n" +
                        "||  WW        ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||          CM||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * Move into a cell you've already gone to, but in separate moves
     */
    @Test
    void MoveTest6() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("down\nup\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(0));

        player.move(1);
        player.move(1);

        assertEquals(
                "+--------------+\n" +
                        "||MS          ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||    CS  WW  ||\n" +
                        "||  WW        ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||          CM||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * try to move into another player
     */
    @Test
    void MoveTest7() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("down\ndown\ndown\ndown\ndown\nright\nright\nright\nright\nright\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(0));

        player.move(10);

        assertEquals(
                "+--------------+\n" +
                        "||            ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||    CS  WW  ||\n" +
                        "||  WW        ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||        MSCM||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * move into a room
     */
    @Test
    void MoveTest8() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("down\ndown\nright\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(0));

        player.move(3);

        assertEquals(
                "+--------------+\n" +
                        "||            ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||    CSMSWW  ||\n" +
                        "||  WW        ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||          CM||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * exit a room #1
     */
    @Test
    void MoveTest9() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("down\ndown\nright\n0\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(0));

        player.move(3);
        player.move(1);

        assertEquals(
                "+--------------+\n" +
                        "||            ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||MS  CS  WW  ||\n" +
                        "||  WW        ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||          CM||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * exit a room #2
     */
    @Test
    void MoveTest10() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("down\ndown\nright\n1\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(0));

        player.move(3);
        player.move(1);

        assertEquals(
                "+--------------+\n" +
                        "||            ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||    CS  WW  ||\n" +
                        "||  WW      MS||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||          CM||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * trying to re-enter a room from the same door
     */
    @Test
    void MoveTest11() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("down\ndown\nright\n1\nleft\nup\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(0));

        player.move(3);
        player.move(2);

        assertEquals(
                "+--------------+\n" +
                        "||            ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||    CS  WWMS||\n" +
                        "||  WW        ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||          CM||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * trying to exit a door while another player is there
     */
    @Test
    void MoveTest12() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner1 = new Scanner("down\ndown\nright\n1\n0\n");
        Scanner scanner2 = new Scanner("up\nup\n");

        Player player1 = new Player(null, scanner1, 1, board.getSuspect(0));
        Player player2 = new Player(null, scanner2, 1, board.getSuspect(1));

        player1.move(3);
        player2.move(2);
        player1.move(1);

        assertEquals(
                "+--------------+\n" +
                        "||            ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||MS  CS  WW  ||\n" +
                        "||  WW      CM||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||            ||\n" +
                        "+--------------+\n", board.toString());
    }
    /**
     * some invalid entries
     */
    @Test
    void MoveTest13() {
        Board board = new Board("test maps/test2/", 8,8);

        Scanner scanner = new Scanner("0\n1\nyes\nrght\nup\nright\n");

        Player player = new Player(null, scanner, 1, board.getSuspect(0));

        player.move(1);

        assertEquals(
                "+--------------+\n" +
                        "||  MS        ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||    CS  WW  ||\n" +
                        "||  WW        ||\n" +
                        "||  WWWWWWWW  ||\n" +
                        "||          CM||\n" +
                        "+--------------+\n", board.toString());
    }









}
