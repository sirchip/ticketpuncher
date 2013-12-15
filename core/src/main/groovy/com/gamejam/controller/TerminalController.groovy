package com.gamejam.controller

import com.badlogic.gdx.utils.TimeUtils
import com.gamejam.model.Bob
import com.gamejam.model.LinePosHelper
import com.gamejam.model.Passenger
import com.gamejam.model.Terminal
import com.gamejam.screens.GameOverScreen

/**
 * Created by 
 * Matthew Fitzpatrick 
 * on 14-Dec-2013
 * at 3:01 PM
 */
class TerminalController {
    private static final int END_OF_LEFT = 0
    private static final int END_OF_RIGHT = 4
    def input = [left: false, right: false]
    Bob bob
    Terminal terminal
    def movementProcessed = true
    long lastPassengerTime
    long timeBetweenPassengers = 2.0e+9
    long increaseSpawnRateTime = 0.25e+9
    long gameStartTime
    def everyXTicketsAddButton = 5
    def everyXTicketsIncreaseSpawnRate = 15
    def everyXTicketsIncreaseMaxCombo = 10
    Random random


    TerminalController(Terminal terminal) {
        this.terminal = terminal
        this.bob = terminal.bob
        random = new Random()
        gameStartTime = TimeUtils.nanoTime()
    }

    def update() {
        def gameOngoing = true
        if (terminal.bob.ticketsPunched / everyXTicketsAddButton >= 1){
            Passenger.increasePossibleButtons()
            everyXTicketsAddButton += 5
        }

        //Logic to Spawn Passengers
        if (TimeUtils.nanoTime() - lastPassengerTime > timeBetweenPassengers)
            gameOngoing = spawnPassenger()

        //Bob Stuff...
        if (input.left && !movementProcessed)
            if (bob.currentLineNumber != END_OF_LEFT) {
                bob.currentLineNumber -= 1
                movementProcessed = true
            }

        if (input.right && !movementProcessed)
            if (bob.currentLineNumber != END_OF_RIGHT) {
                bob.currentLineNumber += 1
                movementProcessed = true
            }

        //Update all Passengers in Open Lines
        terminal.linesMap.values().each { line ->
            line.passengers.eachWithIndex { passenger, idx ->
                switch (line.lineNumber) {
                    case 0:
                        passenger.position = LinePosHelper.LINE_0.getLinePosition(idx)
                        break;
                    case 1:
                        passenger.position = LinePosHelper.LINE_1.getLinePosition(idx)
                        break;
                    case 2:
                        passenger.position = LinePosHelper.LINE_2.getLinePosition(idx)
                        break;
                    case 3:
                        passenger.position = LinePosHelper.LINE_3.getLinePosition(idx)
                        break;
                    case 4:
                        passenger.position = LinePosHelper.LINE_4.getLinePosition(idx)
                        break;
                }
            }
        }

        terminal.updateCurrentPassenger()
        gameOngoing
    }

    def spawnPassenger() {
        int makeBob = random.nextInt(20 - 5) + 5
        boolean evilBob = false
        if (makeBob == 15) {
            evilBob = true
        }
        int passengerNumber = random.nextInt(6 - 1) + 1
        Passenger passenger = new Passenger(random.nextInt(9 - 1) + 1, "passenger$passengerNumber", evilBob)
        lastPassengerTime = TimeUtils.nanoTime()
        terminal.addPerson(passenger)
    }

//arrows handle user direction
//USER BUTTONS
//x
//z
//l-shift
//space
//enter
//l-ctrl
//esc = escape exit
//ARCADE BUTTONS
//1 //2 player start and join
//5 //6 player coin buttons
    public void moveBobLeft() {
        movementProcessed = false
        input.right = false
        input.left = true
    }

    public void moveBobRight() {
        movementProcessed = false
        input.left = false
        input.right = true
    }

    def stopMovingBob() {
        movementProcessed = true
        input.left = false
        input.right = false
    }

    public void checkIfComboIsCorrect() {
        //check currentPassenger ticket
        ArrayList<Integer> passengerCombo = terminal.currentPassenger.combo
        if (passengerCombo.size() > 0) {
            for (int f = 0; f < terminal.bob.combo.size(); f++) {
                if (terminal.currentPassenger.evilBob && !terminal.bob.combo.get(f).equals(terminal.currentPassenger.combo.get(f))) {
                    terminal.bob.combo = new ArrayList<>()
                    this.terminal.bob.updateScore(terminal.currentPassenger.points);
                    this.terminal.bob.updateTicketsPunched();
                    popCurrentPassenger()
                } else if (terminal.bob.combo.size() - 1 >= f &&
                        terminal.currentPassenger.combo.size() - 1 >= f &&
                        terminal.bob.combo.get(f).equals(terminal.currentPassenger.combo.get(f))) {
                    if (f == (passengerCombo.size() - 1)) {
                        if (terminal.currentPassenger.evilBob) {
                            terminal.currentPassenger.points = 30
                        }
                        terminal.bob.combo = new ArrayList<Integer>();
                        this.terminal.bob.updateScore(terminal.currentPassenger.points);
                        this.terminal.bob.updateTicketsPunched();
                        popCurrentPassenger()
                    }
                }

            }

        }
    }

    def popCurrentPassenger() {
        def currentLine = terminal.bob.currentLineNumber
        terminal.linesMap[currentLine.toString()].passengers.remove(0)
    }


}
