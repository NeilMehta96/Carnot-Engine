package com.github.neilmehta96.carnotengine.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.neilmehta96.carnotengine.CarnotEngine;

import com.github.neilmehta96.carnotengine.ai.AI;
import com.github.neilmehta96.carnotengine.boardstate.Position;
import com.github.neilmehta96.carnotengine.boardstate.Position.ChessPieces;
import static com.github.neilmehta96.carnotengine.boardstate.Position.ChessPieces.*;
import com.github.neilmehta96.carnotengine.rules.Rules;
import com.github.neilmehta96.carnotengine.utils.MoveTuple;

import java.util.*;

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
    private int lastTouch;
    private MoveTuple prevMove;



    private static HashMap<List<MoveTuple>,List<Position>> positionHash =
            new HashMap<List<MoveTuple>,List<Position>>();


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

        if (!CarnotEngine.playerMovesBlack&&!currentPosition.isWhitesMove()){

            currentPosition.recentMoves = new LinkedList<MoveTuple>();
            double start = System.currentTimeMillis();
            prevMove = AI.generateBlackBestLegalMoveGivenDepth(currentPosition);
            double end = System.currentTimeMillis();
            positionHash = new HashMap<List<MoveTuple>,List<Position>>();

            System.out.println((end-start)/1000);
//            System.out.println(currentPosition.halfMoveNumber);
//            System.out.println(currentPosition.allPieces[6][2]);
//            System.out.println(prevMove.from.x);
//            System.out.println(prevMove.from.y);
//            System.out.println(prevMove.to.x);
//            System.out.println(prevMove.to.y);

            currentPosition = currentPosition.movePiece(prevMove);
//            for (MoveTuple move : currentPosition.moveList){
//                System.out.println(move.from.x);
//                System.out.println(move.from.y);
//                System.out.println(move.to.x);
//                System.out.println(move.to.y);
//
//            }
        }



        if (Gdx.input.justTouched()){
            int touch = getIdx(camera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0),
                    viewport.getScreenX(),viewport.getScreenY(), viewport.getScreenWidth(),viewport.getScreenHeight()));
            if (touch>=0&& touch<=63) {
                if (((currentPosition.isWhitesMove() && currentPosition.getWhitePieces()[touch] != empty) ||
                        (CarnotEngine.playerMovesBlack && !currentPosition.isWhitesMove() && currentPosition.getBlackPieces()[touch] != empty))) {
                    if (isTouched && lastTouch%8 == touch%8 && lastTouch/8 == touch/8) {
                        isTouched = false;
                    } else {
                        isTouched = true;
                        lastTouch = touch;
                    }

                } else if (isTouched) {
                    MoveTuple move = new MoveTuple(lastTouch,touch);
                    if (Rules.ruleCheck(move, currentPosition, true)) {
                        currentPosition = currentPosition.movePiece(move);
                        prevMove = currentPosition.getLastMove();
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
                if(currentPosition.getAllPieces()[j*8+i]!=empty) {
                    game.batch.draw(findTexture(currentPosition.getAllPieces()[j*8+i]), i - 4, j - 4, 1, 1);
                }
            }
        }

        if (prevMove!=null){
            if (((prevMove.from%8)%2+(prevMove.from/8)%2)%2==0) {
                game.batch.draw(previousBlackSquare, prevMove.from%8 - 4, prevMove.from/8 - 4, 1, 1);
            }
            else {
                game.batch.draw(previousWhiteSquare, prevMove.from%8 - 4, prevMove.from/8 - 4, 1, 1);
            }
            if (((prevMove.to%8)%2+(prevMove.to/8)%2)%2==0) {
                game.batch.draw(previousBlackSquare, prevMove.to%8 - 4, prevMove.to/8 - 4, 1, 1);
            }
            else {
                game.batch.draw(previousWhiteSquare, prevMove.to%8 - 4, prevMove.to/8 - 4, 1, 1);
            }
            game.batch.draw(findTexture(currentPosition.getAllPieces()[prevMove.to]),prevMove.to%8 - 4, prevMove.to/8 - 4, 1, 1);

        }

        if (isTouched){
            if (((lastTouch%8)%2+(lastTouch/8)%2)%2==0){
                game.batch.draw(selectedBlackSquare,lastTouch%8-4,lastTouch/8-4,1,1);
            }
            else {
                game.batch.draw(selectedWhiteSquare,lastTouch%8-4,lastTouch/8-4,1,1);
            }
            game.batch.draw(findTexture(currentPosition.getAllPieces()[lastTouch]),lastTouch%8-4,lastTouch/8-4,1,1);
        }
        game.batch.end();


    }


    private int getIdx(Vector3 vec){
        int x;
        int y;
        if (vec.x>=0){
            x = (int)vec.x;
        }
        else{
            x = ((int)vec.x)-1;
        }
        if (vec.y>=0){
            y = (int)vec.y;
        }
        else{
            y = ((int)vec.y)-1;
        }
        return (y+4)*8+x+4;
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
