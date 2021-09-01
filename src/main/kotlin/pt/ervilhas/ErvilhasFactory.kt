package pt.ervilhas


import javafx.geometry.Rectangle2D
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle

import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.components.RandomMoveComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns

import pt.ervilhas.ErvilhasEntityType.*


class ErvilhasFactory : EntityFactory {

    @Spawns("player")
    fun newPlayer(data: SpawnData): Entity {
        return entityBuilder(data)
            .type(PLAYER)
            .viewWithBBox(Rectangle(30.0, 30.0, Color.BLUE))
            .collidable()
            .build()
    }


    @Spawns("bullet")
    fun newBullet(data: SpawnData): Entity {
        val player = getGameWorld().getSingleton(PLAYER)
        val direction = getInput().mousePositionWorld.subtract(player.center)

        return entityBuilder(data)
            .type(BULLET)
            .viewWithBBox(Rectangle(10.0, 2.0, Color.BLACK))
            .collidable()
            .with(ProjectileComponent(direction, 1000.0))
            .with(OffscreenCleanComponent())
            .build()
    }


    @Spawns("enemy")
    fun newEnemy(data: SpawnData): Entity {
        val bbox = Circle(20.0, 20.0, 20.0, Color.RED).also {
            it.stroke = Color.BROWN
            it.strokeWidth = 2.0
        }
        val movBounds = Rectangle2D(0.0, 0.0, getAppWidth().toDouble(), getAppHeight().toDouble())

        return entityBuilder(data)
            .type(ENEMY)
            .viewWithBBox(bbox)
            .collidable()
            .with(RandomMoveComponent(movBounds, 50.0))
            .build()
    }

}
