package com.nmehta.carnotengine.ai;

import com.nmehta.carnotengine.CarnotEngine;
import com.nmehta.carnotengine.boardstate.Position;
import com.nmehta.carnotengine.openingbook.OpeningBook;
import com.nmehta.carnotengine.rules.Rules;
import com.nmehta.carnotengine.utils.MoveTuple;
import com.nmehta.carnotengine.utils.Tree;
import com.nmehta.carnotengine.utils.TreeNodeReturn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static com.nmehta.carnotengine.boardstate.Position.ChessPieces.*;

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
            System.out.println(nodeReturn.score);
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
        for (int depth = CarnotEngine.depthOfSearch; depth <= CarnotEngine.depthOfSearch; depth++) {
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
            //return new TreeNodeReturn(color *tree.root.score, tree.root);
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
            score = position.score;
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
        for (int i = 0; i <= 63; i++) {
            if (!position.whitesMove && position.blackPieces[i] != empty) {
                moves.addAll(generateMovePossibilities(i, position));
            } else if (position.whitesMove && position.whitePieces[i] != empty) {
                moves.addAll(generateMovePossibilities(i, position));
            }
        }
        return moves;
    }

    private static HashSet<MoveTuple> generateMovePossibilities(int from, Position position) {

        Position.ChessPieces piece = position.allPieces[from];
        HashSet<Integer> moves = new HashSet<Integer>();

        if (piece == bpawn) {
            moves.add(from-16);
            moves.add(from-8);
            moves.add(from-7);
            moves.add(from-9);
        }
        if (piece == wpawn) {
            moves.add(from+16);
            moves.add(from+8);
            moves.add(from+7);
            moves.add(from+9);
        } else if (piece == brook || piece == wrook) {
            for (int i = 0; i <= 7; i++) {
                moves.add((from/8)*8+i);
                moves.add(from%8+i*8);
            }
        } else if (piece == bknight || piece == wknight) {
            moves.add(from+6);
            moves.add(from+15);
            moves.add(from+17);
            moves.add(from+10);
            moves.add(from-6);
            moves.add(from-15);
            moves.add(from-17);
            moves.add(from-10);
        } else if (piece == bbishop || piece == wbishop) {
            for (int i = -7; i <= 7; i++) {
                moves.add(from+i*9);
                moves.add(from+i*7);
            }
        } else if (piece == bqueen || piece == wqueen) {
            for (int i = -7; i <= 7; i++) {
                moves.add(from+i*9);
                moves.add(from+i*7);
            }
            for (int i = 0; i <= 7; i++) {
                moves.add((from/8)*8+i);
                moves.add(from%8+i*8);
            }
        } else if (piece == bking || piece == wking) {
            moves.add(from+1);
            moves.add(from+7);
            moves.add(from+8);
            moves.add(from+9);
            moves.add(from-1);
            moves.add(from-7);
            moves.add(from-8);
            moves.add(from-9);
            if ((piece == wking && position.whiteCanCastle) || (piece == bking && position.blackCanCastle)) {
                moves.add(from+2);
                moves.add(from-2);
            }
        }

        return pairLegalMoves(from, moves, position);
    }

    private static HashSet<MoveTuple> pairLegalMoves(int from, HashSet<Integer> moves, Position position) {
        HashSet<MoveTuple> pairedMoves = new HashSet<MoveTuple>();
        MoveTuple toAdd;
        for (Integer tuple : moves) {
            toAdd = new MoveTuple(from,tuple);
            if (tuple>= 0 && tuple <= 63 && Rules.ruleCheck(toAdd, position)) {
                pairedMoves.add(toAdd);
            }
        }
        return pairedMoves;
    }
}
