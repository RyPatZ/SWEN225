package tests;

import Game.*;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardLoadingTests {

    // tests to see whether it can load all the chars and display the board correctly.
    @Test
    void LoadTest1() {
        Board board = new Board("test maps/test1/", 3,3);
        String[] boardText = board.toString().split("\n");
        assertEquals(3, boardText.length);
        assertEquals("MS  WW", boardText[0]);
        assertEquals("DDWWWW", boardText[1]);

        assertEquals("WW", boardText[2].substring(0,2));
        assertNotEquals("EE", boardText[2].substring(2,4));
        assertEquals("WW", boardText[2].substring(4,6));
    }

    // tests to see whether it puts all the cells into the right type
    @Test
    void LoadTest2() {
        Board board = new Board("test maps/test1/", 3,3);
        assertTrue(board.getCell(0,0) instanceof FreeCell);
        assertTrue(board.getCell(1,0) instanceof FreeCell);
        assertTrue(board.getCell(2,0) instanceof WallCell);

        assertTrue(board.getCell(0,1) instanceof RoomEntranceCell);
        assertTrue(board.getCell(1,1) instanceof WallCell);
        assertTrue(board.getCell(2,1) instanceof WallCell);

        assertTrue(board.getCell(0,2) instanceof WallCell);
        assertTrue(board.getCell(1,2) instanceof RoomEntityCell);
        assertTrue(board.getCell(2,2) instanceof WallCell);
    }

    // check if it correctly identifies free spaces
    @Test
    void LoadTest3() {
        Board board = new Board("test maps/test1/", 3,3);
        assertFalse(board.getCell(0,0).isFree());
        assertTrue(board.getCell(1,0).isFree());
        assertFalse(board.getCell(2,0).isFree());

        assertTrue(board.getCell(0,1).isFree());
        assertFalse(board.getCell(1,1).isFree());
        assertFalse(board.getCell(2,1).isFree());

        assertFalse(board.getCell(0,2).isFree());
        assertFalse(board.getCell(1,2).isFree());
        assertFalse(board.getCell(2,2).isFree());
    }

    // check if movement is reporting correctly
    @Test
    void LoadTest4() {
        Board board = new Board("test maps/test1/", 3,3);
        Set<Cell.Direction> directions =  board.getAvailableNeighbours(board.getCell(0,0), new HashSet<>());
        Set<Cell.Direction> expectedDirections = new HashSet<Cell.Direction>();
        expectedDirections.add(Cell.Direction.DOWN);
        expectedDirections.add(Cell.Direction.RIGHT);
        assertEquals(expectedDirections, directions);
    }
}
