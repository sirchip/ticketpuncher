package com.gamejam.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL10
import com.badlogic.gdx.utils.TimeUtils
import com.gamejam.controller.TerminalController
import com.gamejam.game.GameJam
import com.gamejam.model.Passenger
import com.gamejam.model.Terminal
import com.gamejam.view.TerminalRenderer

/**
 * Created by 
 * Matthew Fitzpatrick 
 * on 13-Dec-2013
 * at 8:47 PM
 */
class GameScreen implements Screen, InputProcessor {

    final GameJam game
    TerminalController terminalController
    TerminalRenderer terminalRenderer
    Terminal terminal
    Random random

    GameScreen(GameJam game) {
        this.game = game
        random = new Random()
        terminal = new Terminal()
        terminalController = new TerminalController(terminal)
        terminalRenderer = new TerminalRenderer(terminal)
        terminalController.spawnPassenger()
    }

    @Override
    void show() {
        Gdx.input.setInputProcessor(this)
        game.stopMusic()
//        game.setMusic(Gdx.audio.newMusic(Gdx.files.internal("Maths_Deadmau5.mp3")))
        game.loopMusic()
        game.playMusic()
        game.setVolume(0.3)
    }

    @Override
    void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1)
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

        if (!terminalController.update()) {
            game.profileManager.retrieveProfile().notifyScore(terminal.bob.score)
            game.profileManager.persist()
            game.setScreen(new GameOverScreen(game))
        }
        terminalRenderer.render()
    }

    @Override
    boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                terminalController.moveBobLeft()
                break;
            case Input.Keys.RIGHT:
                terminalController.moveBobRight()
                break;
            default:
                terminalController.bob.combo.add(keycode)
                terminalController.checkIfComboIsCorrect()
                break;
        }
        return true;
    }

    @Override
    boolean keyUp(int keycode) {
        terminalController.stopMovingBob()
        return true
    }


    @Override
    void resize(int width, int height) {
    }


    @Override
    void hide() {

    }

    @Override
    void pause() {

    }

    @Override
    void resume() {

    }

    @Override
    void dispose() {

    }

    /*
    UNUSED CONTROLS IGNORE
    */

    @Override
    boolean keyTyped(char character) {
        return false
    }

    @Override
    boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false
    }

    @Override
    boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false
    }

    @Override
    boolean touchDragged(int screenX, int screenY, int pointer) {
        return false
    }

    @Override
    boolean mouseMoved(int screenX, int screenY) {
        return false
    }

    @Override
    boolean scrolled(int amount) {
        return false
    }
    /*
    END OF UNUSED CONTROLS
     */
}
