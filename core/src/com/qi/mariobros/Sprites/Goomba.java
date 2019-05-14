package com.qi.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.qi.mariobros.Screens.PlayScreen;
import com.qi.mariobros.SupMario;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i ++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 1, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16/SupMario.PPM, 16 / SupMario.PPM);
    }

    public void update(float dt){
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion)walkAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / SupMario.PPM,32/ SupMario.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / SupMario.PPM);

        fdef.filter.categoryBits = SupMario.ENEMY_BIT;
        fdef.filter.maskBits = SupMario.Ground_BIT
                | SupMario.BRICK_BIT | SupMario.COIN_BIT | SupMario.OBJECT_BIT | SupMario.ENEMY_BIT | SupMario.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }


}
