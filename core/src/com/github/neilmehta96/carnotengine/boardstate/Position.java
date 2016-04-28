package com.github.neilmehta96.carnotengine.boardstate;

import com.github.neilmehta96.carnotengine.utils.MoveTuple;

import java.util.LinkedList;
import java.util.List;

import static com.github.neilmehta96.carnotengine.boardstate.Position.ChessPieces.*;


public class Position implements Comparable<Position> {



    public enum ChessPieces {
        brook,
        bknight,
        bbishop,
        bqueen,
        bking,
        bpawn,
        wrook,
        wknight,
        wbishop,
        wqueen,
        wking,
        wpawn,
        empty
    }

    private static int[] bpawnPST = new int[64];
    private static int[] brookPST = new int[64];
    private static int[] bknightPST = new int[64];
    private static int[] bbishopPST = new int[64];
    private static int[] bqueenPST = new int[64];
    private static int[] bkingMidPST = new int[64];
    private static int[] bkingEndPST = new int[64];
    private static int[] wpawnPST = new int[64];
    private static int[] wrookPST = new int[64];
    private static int[] wknightPST = new int[64];
    private static int[] wbishopPST = new int[64];
    private static int[] wqueenPST = new int[64];
    private static int[] wkingMidPST = new int[64];
    private static int[] wkingEndPST = new int[64];


    private ChessPieces[] allPieces = new ChessPieces[64];
    private ChessPieces[] whitePieces = new ChessPieces[64];
    private ChessPieces[] blackPieces = new ChessPieces[64];
    private boolean whiteCanCastle = true;
    private boolean blackCanCastle = true;
    private int enPassant;
    private boolean doublePawnMove = false;
    private boolean whitesMove = true;
    private int blackKingPos = 60;
    private int whiteKingPos = 4;
    private MoveTuple lastMove;
    private int score = 0;
    private int halfMoveNumber = 0;
    private List<MoveTuple> moveList = new LinkedList<MoveTuple>(); // last in list is most recent move.


    public List<MoveTuple> recentMoves = new LinkedList<MoveTuple>();


    public Position(Position position){
        this.allPieces = copyBoard(position.allPieces);
        this.whitePieces = copyBoard(position.whitePieces);
        this.blackPieces = copyBoard(position.blackPieces);
        this.whiteCanCastle = position.whiteCanCastle;
        this.blackCanCastle = position.blackCanCastle;
        this.enPassant = position.enPassant;
        this.whitesMove = position.whitesMove;
        this.doublePawnMove = position.doublePawnMove;
        this.blackKingPos = position.blackKingPos;
        this.whiteKingPos = position.whiteKingPos;
        if (position.lastMove != null) {
            this.lastMove = position.lastMove;
        }
        this.score = position.score;
        this.halfMoveNumber = position.halfMoveNumber;
        this.moveList = copyList(position.moveList);
        this.recentMoves = copyList(position.recentMoves);
    }

    public Position(Position position, boolean setWhitesMoveTo){
        this(position);
        this.whitesMove = setWhitesMoveTo;
    }

    public Position(){
        initAll(this.allPieces);
        initWhite(this.whitePieces);
        initBlack(this.blackPieces);
        initPST2();
    }

    @Override
    public int compareTo(Position o) {return ((Integer)score).compareTo(o.score);}




    /*
    public Position movePieceNew(MoveTuple move){
        ChessPieces piece;
        int[] scoreTable;
        lastMove = move;
        ChessPieces[] friendlyPieces;
        ChessPieces[] enemyPieces;
        boolean friendlyCastling;
        int friendlyKingPos;
        int scoreColor;
        int castlingRank;
        if (whitesMove) {
            friendlyPieces = whitePieces;
            enemyPieces = blackPieces;
            friendlyKingPos = whiteKingPos;
            friendlyCastling = whiteCanCastle;
            castlingRank = 0;
            scoreColor = -1;
        }
        else {
            friendlyPieces = blackPieces;
            enemyPieces = whitePieces;
            friendlyKingPos = blackKingPos;
            friendlyCastling = blackCanCastle;
            castlingRank = 7;
            scoreColor = 1;
        }

        return movePieceGeneric(friendlyPieces,enemyPieces,friendlyKingPos,friendlyCastling,move,scoreColor,castlingRank);
    }

    private Position movePieceGeneric(ChessPieces[] friendlyPieces,ChessPieces[] enemyPieces,int friendlyKingPos,
                                      boolean friendlyCastling,MoveTuple move, int scoreColor, int castlingRank){
        int from = move.from;
        int to = move.to;
        ChessPieces piece = friendlyPieces[from];
        int[] scoreTable = getTable(piece);
        score += scoreColor*(scoreTable[to]-scoreTable[from]);
        if (piece==wking){
            friendlyKingPos = to;
            if (friendlyCastling&&from/8==castlingRank&&(to%8==2||to%8==6)){
                if (to%8==2){

                }
            }
        }
    }
    */


    public Position movePiece(MoveTuple move){
        Position position = new Position(this);
        int from = move.from;
        int to = move.to;
        ChessPieces piece;
        int[] scoreTable;
        position.lastMove = move;
        if (position.whitesMove) {
            piece = position.whitePieces[from];
            scoreTable = getTable(piece);
            position.score -= scoreTable[to]-scoreTable[from];

            if (position.whitePieces[from] == wking) {
                position.whiteKingPos = to;

                if (from/8==0&&(to%8==2||to%8==6)&&position.whiteCanCastle){
                    if (to%8==2) {
                        position.allPieces[0] = empty;
                        position.whitePieces[0] = empty;
                        position.allPieces[3] = wrook;
                        position.whitePieces[3] = wrook;
                        position.score -= wrookPST[3]-wrookPST[0];
                    }
                    else {
                        position.allPieces[7] = empty;
                        position.whitePieces[7] = empty;
                        position.allPieces[5] = wrook;
                        position.whitePieces[5] = wrook;
                        position.score -= wrookPST[5]-wrookPST[7];
                    }
                }
                position.whiteCanCastle = false;


            }
            if (position.blackPieces[to] != empty){
                position.score -= getTable(position.blackPieces[to])[to];
                position.blackPieces[to] = empty;
            }


            if (to/8==7&&position.whitePieces[from]==wpawn){
                position.score -= wqueenPST[to]-wpawnPST[from];
                position.whitePieces[to] = wqueen;
                position.allPieces[to] = wqueen;
            }
            else {
                position.whitePieces[to] = position.whitePieces[from];
                position.allPieces[to] = position.whitePieces[to];
            }
            position.whitePieces[from] = empty;
            position.allPieces[from] = empty;



        }
        else {
            piece = position.blackPieces[from];
            scoreTable = getTable(piece);
            position.score += scoreTable[to]-scoreTable[from];

            if (position.blackPieces[from] == bking) {
                position.blackKingPos = to;

                if (from/8==7&&(to%8==2||to%8==6)&&position.blackCanCastle){
                    if (to%8==2) {
                        position.allPieces[56] = empty;
                        position.blackPieces[56] = empty;
                        position.allPieces[59] = brook;
                        position.blackPieces[59] = brook;
                        position.score += brookPST[59]-brookPST[56];
                    }
                    else {
                        position.allPieces[63] = empty;
                        position.blackPieces[63] = empty;
                        position.allPieces[61] = brook;
                        position.blackPieces[61] = brook;
                        position.score += wrookPST[61]-wrookPST[63];
                    }
                }
                position.blackCanCastle = false;


            }
            if (position.whitePieces[to] != empty){
                position.score += getTable(position.whitePieces[to])[to];
                position.whitePieces[to] = empty;
            }


            if (to/8==0&&position.blackPieces[from]==bpawn){
                position.score += bqueenPST[to];
                position.blackPieces[to] = bqueen;
                position.allPieces[to] = bqueen;
            }
            else {
                position.blackPieces[to] = position.blackPieces[from];
                position.allPieces[to] = position.blackPieces[to];
            }
            position.blackPieces[from] = empty;
            position.allPieces[from] = empty;
        }
        position.whitesMove = !position.whitesMove;
        position.halfMoveNumber++;
        position.moveList.add(move);
        position.recentMoves.add(move);
        position.lastMove = new MoveTuple(move);
        return position;
    }

    private static ChessPieces[] copyBoard(ChessPieces[] toCopy){
        ChessPieces[] output = new ChessPieces[64];
        for (int i=0; i<=63; i++){
            output[i] = toCopy[i];
        }
        return output;
    }

    public static <T> List<T> copyList(List<T> obList){
        LinkedList<T> toReturn = new LinkedList<T>();
        for (T obj : obList){
            toReturn.add(obj);
        }
        return toReturn;
    }

    private static boolean pieceIsWhite(ChessPieces piece){
        return piece==wpawn||piece==wrook||piece==wknight||piece==wbishop||piece==wqueen||piece==wking;
    }

    public static boolean pieceIsBlack(ChessPieces piece){
        return piece!=empty&&!Position.pieceIsWhite(piece);
    }

    private static int[] getTable(Position.ChessPieces piece){
        if (piece==wpawn||piece==bpawn){
            if (piece==bpawn) {
                return bpawnPST;
            }
            else {
                return wpawnPST;
            }
        }
        else if (piece==wrook||piece==brook){
            if (piece==brook) {
                return brookPST;
            }
            else {
                return wrookPST;
            }
        }
        else if (piece==wknight||piece==bknight){
            if (piece==bknight) {
                return bknightPST;
            }
            else {
                return wknightPST;
            }
        }
        else if (piece==wbishop||piece==bbishop){
            if (piece==bbishop) {
                return bbishopPST;
            }
            else {
                return wbishopPST;
            }
        }
        else if (piece==wqueen||piece==bqueen){
            if (piece==bqueen) {
                return bqueenPST;
            }
            else {
                return wqueenPST;
            }
        }
        else if (piece==wking||piece==bking){
            if (piece==bking) {
                return bkingMidPST;
            }
            else {
                return wkingMidPST;
            }
        }
        else {
            return null;
        }
    }


    private static void initAll(ChessPieces[] initChessPieces) {

        initEmptyBoard(initChessPieces);

        initChessPieces[0]=wrook;
        initChessPieces[1]=wknight;
        initChessPieces[2]=wbishop;
        initChessPieces[3]=wqueen;
        initChessPieces[4]=wking;
        initChessPieces[5]=wbishop;
        initChessPieces[6]=wknight;
        initChessPieces[7]=wrook;

        for (int i=8;i<=15;i++){
            initChessPieces[i]=wpawn;
            initChessPieces[63-i]=bpawn;
        }

        initChessPieces[56]=brook;
        initChessPieces[56+1]=bknight;
        initChessPieces[56+2]=bbishop;
        initChessPieces[56+3]=bqueen;
        initChessPieces[56+4]=bking;
        initChessPieces[56+5]=bbishop;
        initChessPieces[56+6]=bknight;
        initChessPieces[56+7]=brook;


    }
    private static void initWhite(ChessPieces[] initChessPieces) {

        initEmptyBoard(initChessPieces);

        initChessPieces[0]=wrook;
        initChessPieces[1]=wknight;
        initChessPieces[2]=wbishop;
        initChessPieces[3]=wqueen;
        initChessPieces[4]=wking;
        initChessPieces[5]=wbishop;
        initChessPieces[6]=wknight;
        initChessPieces[7]=wrook;

        for (int i=8;i<=15;i++){
            initChessPieces[i]=wpawn;
        }

    }

    private static void initBlack(ChessPieces[] initChessPieces) {

        initEmptyBoard(initChessPieces);

        for (int i=8;i<=15;i++){
            initChessPieces[63-i]=bpawn;
        }

        initChessPieces[56]=brook;
        initChessPieces[56+1]=bknight;
        initChessPieces[56+2]=bbishop;
        initChessPieces[56+3]=bqueen;
        initChessPieces[56+4]=bking;
        initChessPieces[56+5]=bbishop;
        initChessPieces[56+6]=bknight;
        initChessPieces[56+7]=brook;
    }

    private static void initEmptyBoard(ChessPieces[] board){
        for (int i=0; i<=63; i++){
            board[i] = empty;
        }
    }


    private static void initPST2() {


        wpawnPST = new int[]{
                0, 0, 0, 0, 0, 0, 0, 0,
                -21, -16, -6, -1, -1, -6, -16, -21,
                -21, -16, -6, 4, 4, -6, -16, -21,
                -21, -16, -1, 9, 9, -1, -16, -21,
                -14, -8, 6, 17, 17, 6, -8, -14,
                -5, 1, 14, 29, 29, 14, 1, -5,
                7, 11, 23, 39, 39, 23, 11, 7,
                0, 0, 0, 0, 0, 0, 0, 0

        };
        /*
        wpawnEndPST = new int[]{
                0, 0, 0, 0, 0, 0, 0, 0,,
                5, -10, -20, -25, -25, -20, -10, 5,
                5, -10, -20, -25, -25, -20, -10, 5,
                10, -5, -15, -20, -20, -15, -5, 10,
                18, 2, -8, -15, -15, -8, 2, 18,
                30, 14, 1, -10, -10, 1, 14, 30,
                45, 30, 16, 5, 5, 16, 30, 45,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        */

        wknightPST = new int[]{
                -69, -19, -24, -14, -14, -24, -19, -69,
                -54, -39, -9, 11, 11, -9, -39, -54,
                -39, 1, 31, 21, 21, 31, 1, -39,
                -39, 11, 41, 36, 36, 41, 11, -39,
                -39, 41, 51, 51, 51, 51, 41, -39,
                -39, 46, 61, 71, 71, 61, 46, -39,
                -39, 21, 41, 41, 41, 41, 21, -39,
                -59, -39, -29, -29, -29, -29, -39, -59

        };
        /*
        wknightEndPST = new int[]{
                -63, -53, -43, -43, -43, -43, -53, -63,
                -53, -43, 18, 28, 28, 18, -43, -53,
                -43, 18, 48, 38, 38, 48, 18, -43,
                -43, 38, 58, 68, 68, 58, 38, -43,
                -43, 38, 73, 78, 78, 73, 38, -43,
                -43, 28, 78, 73, 73, 78, 28, -43,
                -53, -43, 38, 48, 48, 38, -43, -53,
                -63, -53, -43, -43, -43, -43, -53, -63
        };
        */

        wbishopPST = new int[]{
                -30, -25, -20, -20, -20, -20, -25, -30,
                -28, 11, 6, 1, 1, 6, 11, -28,
                -25, 6, 16, 11, 11, 16, 6, -25,
                1, 1, 16, 21, 21, 16, 1, 1,
                1, 21, 21, 26, 26, 21, 21, 1,
                1, 11, 21, 26, 26, 21, 11, 1,
                -10, 11, 1, 1, 1, 1, 11, -10,
                -20, -18, -16, -14, -14, -16, -18, -20

        };
        /*
        wbishopEndPST = new int[]{
                -38, -18, -8, 2, 2, -8, -18, -38,
                -18, -8, 2, 7, 7, 2, -8, -18,
                -8, 2, 10, 12, 12, 10, 2, -8,
                2, 12, 16, 20, 20, 16, 12, 2,
                2, 12, 17, 22, 22, 17, 12, 2,
                -8, 2, 20, 22, 22, 20, 2, -8,
                -18, -8, 0, 12, 12, 0, -8, -18,
                -38, -18, -8, 2, 2, -8, -18, -38
        };
        */

        wrookPST = new int[]{
                -8, -6, 2, 7, 7, 2, -6, -8,
                -8, -6, 2, 7, 7, 2, -6, -8,
                -8, -6, 6, 7, 7, 6, -6, -8,
                -8, -6, 6, 7, 7, 6, -6, -8,
                -8, -6, 6, 8, 8, 6, -6, -8,
                -8, -6, 6, 10, 10, 6, -6, -8,
                2, 2, 7, 12, 12, 7, 2, 2,
                -8, -6, 2, 7, 7, 2, -6, -8

        };
        /*
        wrookEndPST = new int[]{
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        */

        wqueenPST = new int[]{
                -26, -16, -6, 4, 4, -6, -16, -26,
                -16, -11, -1, 4, 4, -1, -11, -16,
                -6, -6, -1, 4, 4, -1, -6, -6,
                4, 4, 4, 4, 4, 4, 4, 4,
                4, 4, 4, 4, 4, 4, 4, 4,
                4, 4, 4, 4, 4, 4, 4, 4,
                4, 4, 4, 4, 4, 4, 4, 4,
                4, 4, 4, 4, 4, 4, 4, 4

        };
        /*
        wqueenEndPST = new int[]{
                -46, -41, -31, -26, -26, -31, -41, -46,
                -31, -26, -16, -6, -6, -16, -26, -31,
                -16, -1, 14, 24, 24, 14, -1, -16,
                -6, 9, 24, 34, 34, 24, 9, -6,
                -6, 9, 24, 34, 34, 24, 9, -6,
                -6, 9, 24, 34, 34, 24, 9, -6,
                -16, 4, 19, 29, 29, 19, 4, -16,
                -26, -6, -1, 4, 4, -1, -6, -26
        };
        */

        wkingMidPST = new int[]{
                -20, 0, 0, -10, -10, 0, 0, -20,
                -30, -30, -30, -35, -35, -30, -30, -30,
                -40, -40, -45, -50, -50, -45, -40, -40,
                -50, -50, -55, -60, -60, -55, -50, -50,
                -55, -55, -60, -70, -70, -60, -55, -55,
                -55, -55, -60, -70, -70, -60, -55, -55,
                -55, -55, -60, -70, -70, -60, -55, -55,
                -55, -55, -60, -70, -70, -60, -55, -55

        };

        wkingEndPST = new int[]{
                -20, 0, 0, -10, -10, 0, 0, -20,
                -30, -30, -30, -35, -35, -30, -30, -30,
                -40, -40, -45, -50, -50, -45, -40, -40,
                -50, -50, -55, -60, -60, -55, -50, -50,
                -55, -55, -60, -70, -70, -60, -55, -55,
                -55, -55, -60, -70, -70, -60, -55, -55,
                -55, -55, -60, -70, -70, -60, -55, -55,
                -55, -55, -60, -70, -70, -60, -55, -55
        };

        for (int i=0; i<=63; i++){
            wpawnPST[i] = wpawnPST[i]+100;
            wrookPST[i] = wrookPST[i]+500;
            wknightPST[i] = wknightPST[i]+320;
            wbishopPST[i] = wbishopPST[i]+330;
            wqueenPST[i] = wqueenPST[i]+900;
            wkingMidPST[i] = wkingMidPST[i]+20000;
            wkingEndPST[i] = wkingEndPST[i]+20000;

        }

        bpawnPST = reverseTable(wpawnPST);
        brookPST = reverseTable(wrookPST);
        bknightPST = reverseTable(wknightPST);
        bbishopPST = reverseTable(wbishopPST);
        bqueenPST = reverseTable(wqueenPST);
        bkingMidPST = reverseTable(wkingMidPST);
        bkingEndPST = reverseTable(wkingEndPST);
    }

    private static int[] reverseTable(int[] whiteTable){
        int[] toReturn = new int[64];
        for (int i=0; i<=63; i++){
            toReturn[i] = whiteTable[63-i];
        }
        return toReturn;
    }


    public int getWhiteKingPos() {
        return whiteKingPos;
    }

    public int getBlackKingPos() {
        return blackKingPos;
    }

    public boolean isWhitesMove() {
        return whitesMove;
    }

    public boolean isDoublePawnMove() {
        return doublePawnMove;
    }

    public int getEnPassant() {
        return enPassant;
    }

    public boolean isBlackCanCastle() {
        return blackCanCastle;
    }

    public boolean isWhiteCanCastle() {
        return whiteCanCastle;
    }

    public ChessPieces[] getWhitePieces() {
        return whitePieces;
    }

    public ChessPieces[] getBlackPieces() {
        return blackPieces;
    }

    public ChessPieces[] getAllPieces() {
        return allPieces;
    }

    public MoveTuple getLastMove() {
        return lastMove;
    }

    public int getScore() {
        return score;
    }

    public int getHalfMoveNumber() {
        return halfMoveNumber;
    }

    public List<MoveTuple> getMoveList() {
        return moveList;
    }

}
