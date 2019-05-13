package com.qi.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.qi.mariobros.Scenes.Hud;
import com.qi.mariobros.SupMario;

public class Brick extends  InteractiveTileObject{
    public Brick(World world, TiledMap map, Rectangle bounds){

        super(world, map, bounds);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Bricks", "Collision");
        setCategoryFilter(SupMario.DESTROYED_BIT);

        getCell().setTile(null);
        Hud.addScore(200);

        SupMario.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
