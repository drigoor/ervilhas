package pt.ervilhas


import com.almasb.fxgl.app.ApplicationMode
import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity

import javafx.geometry.Rectangle2D
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import javafx.util.Duration

import kotlin.properties.Delegates

import pt.ervilhas.ErvilhasEntityType.*


// FROM: https://github.com/FDelporte/FXGLFirstTest


class ErvilhasApp : GameApplication() {

    private var player: Entity by Delegates.notNull()


    override fun initSettings(settings: GameSettings) {
        with(settings) {
            width = 1024
            setHeightFromRatio(16 / 9.0)
            title = "Ervilhas Game"
            version = "vdev"
            applicationMode = ApplicationMode.DEVELOPER
            stageStyle = StageStyle.DECORATED
        }
    }


    override fun initInput() {
        onKey(KeyCode.W) { player.translateY(-5.0) }
        onKey(KeyCode.S) { player.translateY(5.0) }
        onKey(KeyCode.A) { player.translateX(-5.0) }
        onKey(KeyCode.D) { player.translateX(5.0)
            inc("pixelsMoved", +5)
        }

        onBtnDown(MouseButton.PRIMARY) { spawn("bullet", player.getCenter()) }
    }


    override fun initGameVars(vars: MutableMap<String, Any>) {
        vars["pixelsMoved"] = 0
    }


    override fun initGame() {
        getGameWorld().addEntityFactory(ErvilhasFactory())
        player = spawn("player", (getAppWidth() / 2 - 15).toDouble(), (getAppHeight() / 2 - 15).toDouble())

        val movBounds = Rectangle2D(0.0, 0.0, getAppWidth().toDouble(), getAppHeight().toDouble())
        run({ spawn("enemy", FXGLMath.randomPoint(movBounds)) }, Duration.seconds(0.7))
    }


    override fun initPhysics() {
        onCollisionBegin(BULLET, ENEMY) { bullet, enemy ->
            bullet.removeFromWorld()
            enemy.removeFromWorld()
        }
        onCollisionBegin(PLAYER, ENEMY) { player, enemy ->
            showMessage("You Died!") {
                getGameController().startNewGame()
            }
        }
    }


    override fun initUI() {
        val text = getUIFactoryService().newText("", Color.BLACK, 22.0)
        with(text) {
            translateX = 50.0
            translateY = 100.0
            textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString())
        }
        getGameScene().addUINode(text)
    }

}
