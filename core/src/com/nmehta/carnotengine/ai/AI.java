package com.nmehta.carnotengine.ai;

import com.nmehta.carnotengine.CarnotEngine;
import com.nmehta.carnotengine.boardstate.Position;
import com.nmehta.carnotengine.openingbook.OpeningBook;
import com.nmehta.carnotengine.rules.Rules;
import com.nmehta.carnotengine.utils.MoveTuple;
import com.nmehta.carnotengine.utils.Tree;
import com.nmehta.carnotengine.utils.PointTuple;
import com.nmehta.carnotengine.utils.TreeNodeReturn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static com.nmehta.carnotengine.boardstate.Position.ChessPieces.*;
import static com.nmehta.carnotengine.boardstate.Position.scorePositionPST;

/**
 * Created by Neil on 4/21/2016.
 */
public final class AI {


    private AI() {
    }

    public static MoveTuple generateBlackBestLegalMoveGivenDepth(Position position) {
        MoveTuple toReturn = OpeningBook.opening(position);


        if (toReturn == null) {

//            Tree testPos = new Tree(position);
//            TreeNodeReturn abReturn = alphabeta(new Tree(position), CarnotEngine.depthOfSearch, -40000, 40000);
//            return abReturn.position.lastMove;

            TreeNodeReturn nodeReturn = IDDFS(position);
            return nodeReturn.position.lastMove;
        } else {
            return toReturn;
        }


        //Tree testPos = new Tree(position);
        //TreeNodeReturn negamaxReturn = negamax(testPos,CarnotEngine.depthOfSearch,1);
        //return negamaxReturn.position.lastMove;


    }

    private static TreeNodeReturn IDDFS(Position position) {
        TreeNodeReturn bestSoFar = new TreeNodeReturn(-40000, null);
        for (int depth = 1; depth <= CarnotEngine.depthOfSearch; depth++) {
//            if (PlayScreen.positionHash.containsKey(position.moveList)) {
//                System.out.println("hashing happens");
//                bestSoFar = alphabeta(new Tree(position, PlayScreen.positionHash.get(position.moveList)), depth, -40000, 40000);
//            }
//            else {
                bestSoFar = alphabeta(new Tree(position), depth, -40000, 40000);
//            }
        }
        return bestSoFar;
    }

    private static TreeNodeReturn alphabeta(Tree tree, int depth, int a, int b) {
        if (depth == 0) {
            //return new TreeNodeReturn(scorePositionPST(tree.root),tree.root);
            return new TreeNodeReturn(tree.root.score, tree.root);
        }
        if (!tree.root.whitesMove) {
            TreeNodeReturn bestNode = new TreeNodeReturn(-40000, null);
            TreeNodeReturn returnNode;
            for (Position position : tree.children) {
                returnNode = alphabeta(new Tree(position), depth - 1, a, b);
                if (returnNode.score > bestNode.score) {
                    bestNode.score = returnNode.score;
                    bestNode.position = position;
                }
                if (bestNode.score > a) {
                    a = bestNode.score;
                }
                if (b <= a) {
                    break;
                }
            }
            return bestNode;
        } else {
            TreeNodeReturn bestNode = new TreeNodeReturn(40000, null);
            TreeNodeReturn returnNode;
            for (Position position : tree.children) {
                returnNode = alphabeta(new Tree(position), depth - 1, a, b);
                if (returnNode.score < bestNode.score) {
                    bestNode.score = returnNode.score;
                    bestNode.position = position;
                }
                if (bestNode.score < b) {
                    b = bestNode.score;
                }
                if (b <= a) {
                    break;
                }
            }
            return bestNode;
        }

    }


    private static TreeNodeReturn negamax(Tree tree, int depth, int color) {
        if (depth == 0) {
            return new TreeNodeReturn(color * scorePositionPST(tree.root), tree.root);
        }

        TreeNodeReturn bestValue = new TreeNodeReturn(-40000, null);
        TreeNodeReturn returnNode;
        for (Position position : tree.children) {
            returnNode = negamax(new Tree(position), depth - 1, -color);
            if (-returnNode.score > bestValue.score) {
                bestValue.score = -returnNode.score;
                bestValue.position = position;
            }
        }
        return bestValue;
    }

    public static MoveTuple generateBlackBestLegalMoveDepth1(Position position) {

        HashSet<MoveTuple> moves = generatePositionPossibilities(position);

        MoveTuple bestMove = null;
        int bestScore = -40000;
        int score;
        for (MoveTuple tuple : moves) {
            score = scorePositionPST(new Position(position).movePiece(tuple));
            if (bestScore < score) {
                bestMove = tuple;
                bestScore = score;
            }
        }

        return bestMove;
    }

    /*
    private static int scorePosition(Position position){
        if (CarnotEngine.usePST){
            return scorePositionPST(position);
        }
        else {
            return scorePositionPiece(position);
        }
    }

    private static int scorePositionPiece(Position position){

        int score = 0;
        for (int i=0; i<=7; i++) {
            for (int j = 0; j <= 7; j++) {
                score += getPieceScore(position.allPieces[j][i]);
            }
        }
        return 0;
    }


    private static int getPieceScore(Position.ChessPieces piece){
        if (piece==wpawn){
            return -100;
        }
        else if (piece==bpawn){
            return 100;
        }
        else if (piece==wrook){
            return -500;
        }
        else if (piece==brook){
            return 500;
        }
        else if (piece==wknight){
            return -320;
        }
        else if (piece==bknight){
            return 320;
        }
        else if (piece==wbishop){
            return -330;
        }
        else if (piece==bbishop){
            return 330;
        }
        else if (piece==wqueen){
            return -900;
        }
        else if (piece==bqueen){
            return 900;
        }
        else if (piece==wking||piece==bking){
            return 0;
        }
        else {
            return 0;
        }
    }
    */

    /*
    private static int scorePositionPST(Position position){
        //assuming no need to instantiate defensive copy of position object

        int score = 0;
        for (int i=0; i<=7; i++){
            for (int j=0; j<=7; j++){
                int[][] blackTable = getTable(position.allPieces[j][i]);
                boolean white = Position.pieceIsWhite(position.allPieces[j][i]);
                if (blackTable!=null&&white){
                    score -= blackTable[7-j][i];
                }
                else if (blackTable!=null&&!white) {
                    score += blackTable[j][i];
                }
            }
        }
        return score;
    }
    */


    public static MoveTuple generateBlackRandomLegalMove(Position position) {

        /*
        for(int i=0; i<=7; i++){
            for(int j=0; j<=7; j++){
                if (position.blackPieces[j][i]!=empty){
                    moves.addAll(generateMovePossibilities(new PointTuple(i,j), position));
                }
            }
        }
        */
        HashSet<MoveTuple> moves = generatePositionPossibilities(position);

        ArrayList<MoveTuple> shuffledMoves = new ArrayList<MoveTuple>(moves);
        Collections.shuffle(shuffledMoves);

        return shuffledMoves.get(0);
    }

    public static HashSet<MoveTuple> generatePositionPossibilities(Position position) {
        HashSet<MoveTuple> moves = new HashSet<MoveTuple>();
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (!position.whitesMove && position.blackPieces[j][i] != empty) {
                    moves.addAll(generateMovePossibilities(new PointTuple(i, j), position));
                } else if (position.whitesMove && position.whitePieces[j][i] != empty) {
                    moves.addAll(generateMovePossibilities(new PointTuple(i, j), position));
                }
            }
        }
        return moves;
    }

    private static HashSet<MoveTuple> generateMovePossibilities(PointTuple from, Position position) {

        Position.ChessPieces piece = position.allPieces[from.y][from.x];
        HashSet<PointTuple> moves = new HashSet<PointTuple>();

        if (piece == bpawn) {
            moves.add(new PointTuple(from.x, from.y - 2));
            moves.add(new PointTuple(from.x, from.y - 1));
            moves.add(new PointTuple(from.x - 1, from.y - 1));
            moves.add(new PointTuple(from.x + 1, from.y - 1));
        }
        if (piece == wpawn) {
            moves.add(new PointTuple(from.x, from.y + 2));
            moves.add(new PointTuple(from.x, from.y + 1));
            moves.add(new PointTuple(from.x - 1, from.y + 1));
            moves.add(new PointTuple(from.x + 1, from.y + 1));
        } else if (piece == brook || piece == wrook) {
            for (int i = 0; i <= 7; i++) {
                moves.add(new PointTuple(from.x, i));
                moves.add(new PointTuple(i, from.y));
            }
        } else if (piece == bknight || piece == wknight) {
            moves.add(new PointTuple(from.x + 1, from.y - 2));
            moves.add(new PointTuple(from.x - 1, from.y - 2));
            moves.add(new PointTuple(from.x + 1, from.y + 2));
            moves.add(new PointTuple(from.x - 1, from.y + 2));
            moves.add(new PointTuple(from.x + 2, from.y - 1));
            moves.add(new PointTuple(from.x - 2, from.y - 1));
            moves.add(new PointTuple(from.x + 2, from.y + 1));
            moves.add(new PointTuple(from.x - 2, from.y + 1));
        } else if (piece == bbishop || piece == wbishop) {
            for (int i = -7; i <= 7; i++) {
                moves.add(new PointTuple(from.x + i, from.y + i));
                moves.add(new PointTuple(from.x + i, from.y - i));
            }
        } else if (piece == bqueen || piece == wqueen) {
            for (int i = -7; i <= 7; i++) {
                moves.add(new PointTuple(from.x + i, from.y + i));
                moves.add(new PointTuple(from.x + i, from.y - i));
            }
            for (int i = 0; i <= 7; i++) {
                moves.add(new PointTuple(from.x, i));
                moves.add(new PointTuple(i, from.y));
            }
        } else if (piece == bking || piece == wking) {
            moves.add(new PointTuple(from.x + 1, from.y));
            moves.add(new PointTuple(from.x + 1, from.y + 1));
            moves.add(new PointTuple(from.x, from.y + 1));
            moves.add(new PointTuple(from.x - 1, from.y + 1));
            moves.add(new PointTuple(from.x - 1, from.y));
            moves.add(new PointTuple(from.x - 1, from.y - 1));
            moves.add(new PointTuple(from.x, from.y - 1));
            moves.add(new PointTuple(from.x + 1, from.y - 1));
            if ((piece == wking && position.whiteCanCastle) || (piece == bking && position.blackCanCastle)) {
                moves.add(new PointTuple(from.x + 2, from.y));
                moves.add(new PointTuple(from.x - 2, from.y));
            }
        }

        return pairLegalMoves(from, moves, position);
    }

    private static HashSet<MoveTuple> pairLegalMoves(PointTuple from, HashSet<PointTuple> moves, Position position) {
        HashSet<MoveTuple> pairedMoves = new HashSet<MoveTuple>();
        MoveTuple toAdd;
        for (PointTuple tuple : moves) {
            toAdd = new MoveTuple(from, tuple);
            if (tuple.x >= 0 && tuple.x <= 7 && tuple.y >= 0 && tuple.y <= 7 && Rules.ruleCheck(toAdd, position)) {
                pairedMoves.add(toAdd);
            }
        }
        return pairedMoves;
    }
}
