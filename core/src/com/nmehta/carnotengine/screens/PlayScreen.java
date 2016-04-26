package com.nmehta.carnotengine.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.nmehta.carnotengine.CarnotEngine;

import com.nmehta.carnotengine.ai.AI;
import com.nmehta.carnotengine.boardstate.Position;
import com.nmehta.carnotengine.boardstate.Position.ChessPieces;
import static com.nmehta.carnotengine.boardstate.Position.ChessPieces.*;
import com.nmehta.carnotengine.rules.Rules;
import com.nmehta.carnotengine.utils.MoveTuple;
import com.nmehta.carnotengine.utils.PointTuple;
import com.nmehta.carnotengine.utils.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Neil on 4/18/2016.
 */
public class PlayScreen implements Screen{
    private CarnotEngine game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private final int worldWidth = 8;
    private final int worldHeight = 8;

    private Texture bRookImage;
    private Texture bKnightImage;
    private Texture bBishopImage;
    private Texture bQueenImage;
    private Texture bKingImage;
    private Texture bPawnImage;
    private Texture wRookImage;
    private Texture wKnightImage;
    private Texture wBishopImage;
    private Texture wQueenImage;
    private Texture wKingImage;
    private Texture wPawnImage;

    private Texture blackSquare;
    private Texture whiteSquare;
    private Texture selectedWhiteSquare;
    private Texture selectedBlackSquare;
    private Texture previousWhiteSquare;
    private Texture previousBlackSquare;



    private Position currentPosition;

    private boolean isTouched = false;
    private PointTuple lastTouch;
    private MoveTuple prevMove;


    public static HashMap<Position,ArrayList<Position>> positionHash = new HashMap<Position,ArrayList<Position>>();



    public PlayScreen(CarnotEngine game){
        this.game = game;
        camera = new OrthographicCamera(worldWidth,worldHeight);
        viewport = new FitViewport(worldWidth,worldHeight,camera);

        bRookImage = new Texture(Gdx.files.internal("black_rook.png"));
        bKnightImage = new Texture(Gdx.files.internal("black_knight.png"));
        bBishopImage = new Texture(Gdx.files.internal("black_bishop.png"));
        bQueenImage = new Texture(Gdx.files.internal("black_queen.png"));
        bKingImage = new Texture(Gdx.files.internal("black_king.png"));
        bPawnImage = new Texture(Gdx.files.internal("black_pawn.png"));
        wRookImage = new Texture(Gdx.files.internal("white_rook.png"));
        wKnightImage = new Texture(Gdx.files.internal("white_knight.png"));
        wBishopImage = new Texture(Gdx.files.internal("white_bishop.png"));
        wQueenImage = new Texture(Gdx.files.internal("white_queen.png"));
        wKingImage = new Texture(Gdx.files.internal("white_king.png"));
        wPawnImage = new Texture(Gdx.files.internal("white_pawn.png"));

        blackSquare = new Texture(Gdx.files.internal("black_square.png"));
        whiteSquare = new Texture(Gdx.files.internal("white_square.png"));
        selectedWhiteSquare = new Texture(Gdx.files.internal("white_square_selected.png"));
        selectedBlackSquare = new Texture(Gdx.files.internal("black_square_selected.png"));
        previousWhiteSquare = new Texture(Gdx.files.internal("white_square_previous_move.png"));
        previousBlackSquare = new Texture(Gdx.files.internal("black_square_previous_move.png"));


        game.batch.setProjectionMatrix(camera.combined);

        currentPosition = new Position();


    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        System.gc();

        if (!CarnotEngine.playerMovesBlack&&!currentPosition.whitesMove){
            double start = System.currentTimeMillis();
            prevMove = AI.generateBlackBestLegalMoveGivenDepth(currentPosition);
            positionHash = new HashMap<Position,ArrayList<Position>>();
            //prevMove = AI.generateBlackBestLegalMoveDepth1(Position.currentPosition);
            double end = System.currentTimeMillis();

//            System.out.println((end-start)/1000);
//            System.out.println(currentPosition.halfMoveNumber);
//            System.out.println(currentPosition.allPieces[6][2]);
//            System.out.println(prevMove.from.x);
//            System.out.println(prevMove.from.y);
//            System.out.println(prevMove.to.x);
//            System.out.println(prevMove.to.y);

            currentPosition.movePiece(prevMove);
            for (MoveTuple move : currentPosition.moveList){
                System.out.println(move.from.x);
                System.out.println(move.from.y);
                System.out.println(move.to.x);
                System.out.println(move.to.y);

            }
        }



        if (Gdx.input.justTouched()){

            PointTuple touch = new PointTuple(roundVector3(camera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0),
                    viewport.getScreenX(),viewport.getScreenY(), viewport.getScreenWidth(),viewport.getScreenHeight())));
            if (touch.x>=0&& touch.x<=7&& touch.y>=0 && touch.y<=7) {
                if (((currentPosition.whitesMove && currentPosition.whitePieces[touch.y][touch.x] != empty) ||
                        (CarnotEngine.playerMovesBlack && !currentPosition.whitesMove && currentPosition.blackPieces[touch.y][touch.x] != empty))) {
                    if (isTouched && lastTouch.x == touch.x && lastTouch.y == touch.y) {
                        isTouched = false;
                    } else {
                        isTouched = true;
                        lastTouch = touch;
                    }

                } else if (isTouched) {
                    MoveTuple move = new MoveTuple(lastTouch,touch);
                    if (Rules.ruleCheck(move, currentPosition)) {
                        currentPosition.movePiece(move);
                        prevMove = currentPosition.lastMove;
                    }
                    isTouched = false;
                }
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        for (int i=0;i<=7;i++){
            for (int j=0;j<=7;j++){
                if ((i+j)%2 ==0){
                    game.batch.draw(blackSquare,i-4,j-4,1,1);
                }
                else {
                    game.batch.draw(whiteSquare,i-4,j-4,1,1);
                }

                if(currentPosition.allPieces[j][i]!=empty) {
                    game.batch.draw(findTexture(currentPosition.allPieces[j][i]), i - 4, j - 4, 1, 1);
                }
            }
        }

        if (prevMove!=null){
            if ((prevMove.from.x+prevMove.from.y)%2==0) {
                game.batch.draw(previousBlackSquare, prevMove.from.x - 4, prevMove.from.y - 4, 1, 1);
            }
            else {
                game.batch.draw(previousWhiteSquare, prevMove.from.x - 4, prevMove.from.y - 4, 1, 1);
            }
            if ((prevMove.to.x+prevMove.to.y)%2==0) {
                game.batch.draw(previousBlackSquare, prevMove.to.x - 4, prevMove.to.y - 4, 1, 1);
            }
            else {
                game.batch.draw(previousWhiteSquare, prevMove.to.x - 4, prevMove.to.y - 4, 1, 1);
            }
            game.batch.draw(findTexture(currentPosition.allPieces[prevMove.to.y][prevMove.to.x]),prevMove.to.x - 4, prevMove.to.y - 4, 1, 1);

        }

        if (isTouched){
            if ((lastTouch.x+lastTouch.y)%2==0){
                game.batch.draw(selectedBlackSquare,lastTouch.x-4,lastTouch.y-4,1,1);
            }
            else {
                game.batch.draw(selectedWhiteSquare,lastTouch.x-4,lastTouch.y-4,1,1);
            }
            game.batch.draw(findTexture(currentPosition.allPieces[lastTouch.y][lastTouch.x]),lastTouch.x-4,lastTouch.y-4,1,1);
        }
        game.batch.end();


    }


    private Vector3 roundVector3(Vector3 vec){
        if (vec.x>=0){
            vec.x = (int)vec.x;
        }
        else{
            vec.x = ((int)vec.x)-1;
        }
        if (vec.y>=0){
            vec.y = (int)vec.y;
        }
        else{
            vec.y = ((int)vec.y)-1;
        }
        return vec;
    }



    private Texture findTexture(ChessPieces piece){
        switch (piece) {
            case wrook:
                return wRookImage;
            case wknight:
                return wKnightImage;
            case wbishop:
                return wBishopImage;
            case wqueen:
                return wQueenImage;
            case wking:
                return wKingImage;
            case wpawn:
                return wPawnImage;
            case brook:
                return bRookImage;
            case bknight:
                return bKnightImage;
            case bbishop:
                return bBishopImage;
            case bqueen:
                return bQueenImage;
            case bking:
                return bKingImage;
            case bpawn:
                return bPawnImage;
            default:
                return null;

        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.batch.dispose();

    }
}
