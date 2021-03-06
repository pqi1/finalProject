package com.qi.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.qi.mariobros.Screens.PlayScreen;
import com.qi.mariobros.SupMario;

public class Mario extends Sprite {
    public enum State{FALLING,  JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion marioStand;
    private Animation marioRun;
    private Animation marioJump;
    private float stateTimer;
    private boolean runningRight;


    public Mario(PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(getTexture(), i  * 16 + 1, 11, 16, 16));
        }
        marioRun = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 4; i < 6; i++){
            frames.add(new TextureRegion(getTexture(), i  * 16 + 1, 11, 16, 16));
        }

        marioJump = new Animation(0.1f, frames);

        marioStand = new TextureRegion(getTexture(),1, 11, 16,16);
        defineMario();
        setBounds(1,11, 16/SupMario.PPM, 16/SupMario.PPM);
        setRegion(marioStand);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = (TextureRegion) marioJump.getKeyFrame(stateTimer, true);
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
                default:
                    region = marioStand;
                    break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    public  State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        }
        else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }else{
            return State.STANDING;
        }
    }

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / SupMario.PPM,32/ SupMario.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / SupMario.PPM);

        fdef.filter.categoryBits = SupMario.MARIO_BIT;
        fdef.filter.maskBits = SupMario.GROUND_BIT | SupMario.BRICK_BIT |
                SupMario.COIN_BIT | SupMario.ENEMY_BIT | SupMario.OBJECT_BIT | SupMario.ENEMY_HEAD_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/SupMario.PPM, 6 /SupMario.PPM), new Vector2(2/SupMario.PPM, 6 /SupMario.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
    }
}
