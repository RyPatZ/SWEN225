package tests;

import Game.*;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    @Test
    void GetNumberTest1() {
        int num = Game.getNumberInput(new Scanner("1\n"), 0, 100);
        assertEquals(1, num);
    }

    // boundary tests
    @Test
    void GetNumberTest2() {
        int num1 = Game.getNumberInput(new Scanner("100\n"), 0, 100);
        assertEquals(100, num1);

        int num2 = Game.getNumberInput(new Scanner("0\n"), 0, 100);
        assertEquals(0, num2);
    }

    // boundary tests
    @Test
    void GetNumberTest3() {
        // should not except 101
        int num1 = Game.getNumberInput(new Scanner("101\n50\n"), 0, 100);
        assertEquals(50, num1);

        int num2 = Game.getNumberInput(new Scanner("-10\n50\n"), 0, 100);
        assertEquals(50, num2);
    }

    @Test
    void GetNumberTest4() {
        // should not except 101
        int num1 = Game.getNumberInput(new Scanner("asgs\n50\n"), 0, 100);
        assertEquals(50, num1);
    }

    @Test
    void getRoomTest1() {
        Card card = Game.getRoomInput(new Scanner("0"));
        assertEquals(Game.allRooms[0], card.getName());
    }

    @Test
    void getWeaponTest1() {
        Card card = Game.getWeaponInput(new Scanner("0"));
        assertEquals(Game.allWeapons[0], card.getName());
    }

    @Test
    void getSuspectTest1() {
        Card card = Game.getSuspectInput(new Scanner("0"));
        assertEquals(Game.allSuspects[0], card.getName());
    }
}
