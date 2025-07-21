package com.machinations.game;

import com.badlogic.gdx.math.MathUtils;

public class Dice {
    private int upperBound;

    // Constructor to set the upper bound
    public Dice(int upperBound) {
        this.upperBound = upperBound;
    }

    // Method to roll the dice and return a random number between 1 and upperBound
    public int roll() {
        return MathUtils.random(1, upperBound);
    }

    // Getter for upperBound
    public int getUpperBound() {
        return upperBound;
    }

    // Setter for upperBound
    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    /**
     * Rolls multiple dice and returns the sum of the results.
     *
     * @param numberOfDice The number of dice to roll.
     * @param diceSize The number of sides on each die.
     * @return The total sum of all dice rolls.
     */
    public static int rollDice(int numberOfDice, int diceSize) {
        int total = 0;
        Dice dice = new Dice(diceSize);

        for (int i = 0; i < numberOfDice; i++) {
            total += dice.roll();
        }

        return total;
    }
}
