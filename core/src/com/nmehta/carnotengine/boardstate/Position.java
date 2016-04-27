package com.nmehta.carnotengine.boardstate;

import com.nmehta.carnotengine.utils.MoveTuple;
import com.nmehta.carnotengine.utils.PointTuple;

import java.util.LinkedList;
import java.util.List;

import static com.nmehta.carnotengine.boardstate.Position.ChessPieces.*;

/**
 * Created by Neil on 4/21/2016.
 */
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

    private static int[][] bpawnPST = new int[8][8];
    private static int[][] brookPST = new int[8][8];
    private static int[][] bknightPST = new int[8][8];
    private static int[][] bbishopPST = new int[8][8];
    private static int[][] bqueenPST = new int[8][8];
    private static int[][] bkingMidPST = new int[8][8];
    private static int[][] bkingEndPST = new int[8][8];


    public ChessPieces[][] allPieces = new ChessPieces[8][8];
    public ChessPieces[][] whitePieces = new ChessPieces[8][8];
    public ChessPieces[][] blackPieces = new ChessPieces[8][8];
    public boolean whiteCanCastle = true;
    public boolean blackCanCastle = true;
    public PointTuple enPassant;
    public boolean doublePawnMove = false;
    public boolean whitesMove = true;
    public PointTuple blackKingPos = new PointTuple(4,7);
    public PointTuple whiteKingPos = new PointTuple(4,0);
    public boolean checkForCheck = true;
    public MoveTuple lastMove;
    public int score = 0;
    public int halfMoveNumber = 0;

    public List<MoveTuple> moveList = new LinkedList<MoveTuple>(); // last in list is most recent move.
    public List<MoveTuple> recentMoves = new LinkedList<MoveTuple>();


    public Position(Position position){
        this.allPieces = copyBoard(position.allPieces);
        this.whitePieces = copyBoard(position.whitePieces);
        this.blackPieces = copyBoard(position.blackPieces);
        this.whiteCanCastle = position.whiteCanCastle;
        this.blackCanCastle = position.blackCanCastle;
        if (position.enPassant != null) {
            this.enPassant = new PointTuple(position.enPassant);
        }
        this.whitesMove = position.whitesMove;
        this.doublePawnMove = position.doublePawnMove;
        this.blackKingPos = new PointTuple(position.blackKingPos);
        this.whiteKingPos = new PointTuple(position.whiteKingPos);
        this.checkForCheck = position.checkForCheck;
        if (position.lastMove != null) {
            this.lastMove = position.lastMove;
        }
        this.score = position.score;
        this.halfMoveNumber = position.halfMoveNumber;
        this.moveList = copyList(position.moveList);
        this.recentMoves = copyList(position.recentMoves);
    }

    public Position(){
        initAll(this.allPieces);
        initWhite(this.whitePieces);
        initBlack(this.blackPieces);
        initPST2();
    }

    @Override
    public int compareTo(Position o) {return ((Integer)score).compareTo(o.score);}


    public static int scorePositionPST(Position position){
        //assuming no need to instantiate defensive copy of position object

        int score = 0;
        for (int i=0; i<=7; i++){
            for (int j=0; j<=7; j++){
                int[][] blackTable = getTable(position.allPieces[j][i]);
                boolean white = Position.pieceIsWhite(position.allPieces[j][i]);
                if (blackTable!=null&&white){
                    score -= blackTable[7-j][i];
                }
                else if (blackTable!=null) {
                    score += blackTable[j][i];
                }
            }
        }
        return score;
    }


    public Position movePiece(MoveTuple move){
        PointTuple from = move.from;
        PointTuple to = move.to;
        ChessPieces piece;
        int[][] scoreTable;
        lastMove = move;
        if (whitesMove) {
            piece = whitePieces[from.y][from.x];
            scoreTable = getTable(piece);
            score -= scoreTable[7-to.y][to.x]-scoreTable[7-from.y][from.x];

            if (whitePieces[from.y][from.x] == wking) {
                whiteKingPos = to;

                if (from.y==0&&(to.x==2||to.x==6)&&whiteCanCastle){
                    if (to.x==2) {
                        allPieces[0][0] = empty;
                        whitePieces[0][0] = empty;
                        allPieces[0][3] = wrook;
                        whitePieces[0][3] = wrook;
                        score -= brookPST[7][3]-brookPST[7][0];
                    }
                    else {
                        allPieces[0][7] = empty;
                        whitePieces[0][7] = empty;
                        allPieces[0][5] = wrook;
                        whitePieces[0][5] = wrook;
                        score -= brookPST[7][5]-brookPST[7][7];
                    }
                }
                whiteCanCastle = false;


            }
            if (blackPieces[to.y][to.x] != empty){
                score -= getTable(blackPieces[to.y][to.x])[to.y][to.x];
                blackPieces[to.y][to.x] = empty;
            }


            if (to.y==7&&whitePieces[from.y][from.x]==wpawn){
                score -= bqueenPST[7-to.y][to.x];
                whitePieces[to.y][to.x] = wqueen;
                allPieces[to.y][to.x] = wqueen;
            }
            else {
                whitePieces[to.y][to.x] = whitePieces[from.y][from.x];
                allPieces[to.y][to.x] = whitePieces[to.y][to.x];
            }
            whitePieces[from.y][from.x] = empty;
            allPieces[from.y][from.x] = empty;



        }
        else {
            piece = blackPieces[from.y][from.x];
            scoreTable = getTable(piece);

            score += scoreTable[to.y][to.x]-scoreTable[from.y][from.x];

            if (blackPieces[from.y][from.x] == bking){
                blackKingPos = move.to;

                if (from.y==7&&(to.x==2||to.x==6)&&blackCanCastle){
                    if (to.x==2) {
                        allPieces[7][0] = empty;
                        blackPieces[7][0] = empty;
                        allPieces[7][3] = brook;
                        blackPieces[7][3] = brook;
                        score += brookPST[0][3]-brookPST[0][0];
                    }
                    else {
                        allPieces[7][7] = empty;
                        blackPieces[7][7] = empty;
                        allPieces[7][5] = brook;
                        blackPieces[7][5] = brook;
                        score += brookPST[0][5]-brookPST[0][7];
                    }
                }
                blackCanCastle = false;
            }
            if (whitePieces[to.y][to.x] != empty) {
                score += getTable(whitePieces[to.y][to.x])[7-to.y][to.x];
                whitePieces[to.y][to.x] = empty;
            }


            if (to.y==0&&blackPieces[from.y][from.x]==wpawn){
                score += bqueenPST[to.y][to.x];
                blackPieces[to.y][to.x] = bqueen;
                allPieces[to.y][to.x] = bqueen;
            }
            else {
                blackPieces[to.y][to.x] = blackPieces[from.y][from.x];
                allPieces[to.y][to.x] = blackPieces[to.y][to.x];
            }
            blackPieces[from.y][from.x] = empty;
            allPieces[from.y][from.x] = empty;
        }
        whitesMove = !whitesMove;
        halfMoveNumber++;
        moveList.add(move);
        recentMoves.add(move);
        return this;
    }

    private static ChessPieces[][] copyBoard(ChessPieces[][] toCopy){
        ChessPieces[][] output = new ChessPieces[8][8];
        for (int i=0; i<=7; i++){
            for (int j=0; j<=7; j++){
                output[j][i] = toCopy[j][i];
            }
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

    private static int[][] getTable(Position.ChessPieces piece){
        if (piece==wpawn||piece==bpawn){
            return bpawnPST;
        }
        else if (piece==wrook||piece==brook){
            return brookPST;
        }
        else if (piece==wknight||piece==bknight){
            return bknightPST;
        }
        else if (piece==wbishop||piece==bbishop){
            return bbishopPST;
        }
        else if (piece==wqueen||piece==bqueen){
            return bqueenPST;
        }
        else if (piece==wking||piece==bking){
            return bkingMidPST;
        }
        else {
            return null;
        }
    }


    private static void initAll(ChessPieces[][] initChessPieces) {

        initEmptyBoard(initChessPieces);

        initChessPieces[0][0]=wrook;
        initChessPieces[0][1]=wknight;
        initChessPieces[0][2]=wbishop;
        initChessPieces[0][3]=wqueen;
        initChessPieces[0][4]=wking;
        initChessPieces[0][5]=wbishop;
        initChessPieces[0][6]=wknight;
        initChessPieces[0][7]=wrook;

        for (int i=0;i<=7;i++){
            initChessPieces[1][i]=wpawn;
            initChessPieces[6][i]=bpawn;
        }

        initChessPieces[7][0]=brook;
        initChessPieces[7][1]=bknight;
        initChessPieces[7][2]=bbishop;
        initChessPieces[7][3]=bqueen;
        initChessPieces[7][4]=bking;
        initChessPieces[7][5]=bbishop;
        initChessPieces[7][6]=bknight;
        initChessPieces[7][7]=brook;


    }
    private static void initWhite(ChessPieces[][] initChessPieces) {

        initEmptyBoard(initChessPieces);

        initChessPieces[0][0]=wrook;
        initChessPieces[0][1]=wknight;
        initChessPieces[0][2]=wbishop;
        initChessPieces[0][3]=wqueen;
        initChessPieces[0][4]=wking;
        initChessPieces[0][5]=wbishop;
        initChessPieces[0][6]=wknight;
        initChessPieces[0][7]=wrook;

        for (int i=0;i<=7;i++){
            initChessPieces[1][i]=wpawn;
        }

    }

    private static void initBlack(ChessPieces[][] initChessPieces) {

        initEmptyBoard(initChessPieces);

        for (int i=0;i<=7;i++){
            initChessPieces[6][i]=bpawn;
        }

        initChessPieces[7][0]=brook;
        initChessPieces[7][1]=bknight;
        initChessPieces[7][2]=bbishop;
        initChessPieces[7][3]=bqueen;
        initChessPieces[7][4]=bking;
        initChessPieces[7][5]=bbishop;
        initChessPieces[7][6]=bknight;
        initChessPieces[7][7]=brook;

    }

    private static void initEmptyBoard(ChessPieces[][] board){
        for (int i=0; i<=7; i++){
            for (int j=0;j<=7;j++){
                board[j][i] = empty;
            }
        }
    }

    public static void initPST1(){

        bpawnPST = new int[][]{
                {0,  0,  0,  0,  0,  0,  0,  0},
                {50, 50, 50, 50, 50, 50, 50, 50},
                {10, 10, 20, 30, 30, 20, 10, 10},
                {5,  5, 10, 25, 25, 10,  5,  5},
                {0,  0,  0, 20, 20,  0,  0,  0},
                {5, -5,-10,  0,  0,-10, -5,  5},
                {5, 10, 10,-20,-20, 10, 10,  5},
                {0,  0,  0,  0,  0,  0,  0,  0}
        };

        bknightPST = new int[][]{
                {-50,-40,-30,-30,-30,-30,-40,-50},
                {-40,-20,  0,  0,  0,  0,-20,-40},
                {-30,  0, 10, 15, 15, 10,  0,-30},
                {-30,  5, 15, 20, 20, 15,  5,-30},
                {-30,  0, 15, 20, 20, 15,  0,-30},
                {-30,  5, 10, 15, 15, 10,  5,-30},
                {-40,-20,  0,  5,  5,  0,-20,-40},
                {-50,-40,-30,-30,-30,-30,-40,-50}
        };

        bbishopPST = new int[][]{

                {-20,-10,-10,-10,-10,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5, 10, 10,  5,  0,-10},
                {-10,  5,  5, 10, 10,  5,  5,-10},
                {-10,  0, 10, 10, 10, 10,  0,-10},
                {-10, 10, 10, 10, 10, 10, 10,-10},
                {-10,  5,  0,  0,  0,  0,  5,-10},
                {-20,-10,-10,-10,-10,-10,-10,-20}
        };

        brookPST = new int[][]{
                {0,  0,  0,  0,  0,  0,  0,  0},
                {5, 10, 10, 10, 10, 10, 10,  5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {0,  0,  0,  5,  5,  0,  0,  0}
        };

        bqueenPST = new int[][]{
                {-20,-10,-10, -5, -5,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5,  5,  5,  5,  0,-10},
                {-5,  0,  5,  5,  5,  5,  0, -5},
                {0,  0,  5,  5,  5,  5,  0, -5},
                {-10,  5,  5,  5,  5,  5,  0,-10},
                {-10,  0,  5,  0,  0,  0,  0,-10},
                {-20,-10,-10, -5, -5,-10,-10,-20}
        };

        bkingMidPST = new int[][]{
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-20,-30,-30,-40,-40,-30,-30,-20},
                {-10,-20,-20,-20,-20,-20,-20,-10},
                {20, 20,  0,  0,  0,  0, 20, 20},
                {20, 30, 10, 0, 0, 10, 30, 20}
        };

        bkingEndPST = new int[][]{
                {-50,-40,-30,-20,-20,-30,-40,-50},
                {-30,-20,-10,  0,  0,-10,-20,-30},
                {-30,-10, 20, 30, 30, 20,-10,-30},
                {-30,-10, 30, 40, 40, 30,-10,-30},
                {-30,-10, 30, 40, 40, 30,-10,-30},
                {-30,-10, 20, 30, 30, 20,-10,-30},
                {-30,-30,  0,  0,  0,  0,-30,-30},
                {-50,-30,-30,-30,-30,-30,-30,-50}
        };
    }

    private static void initPST2(){

        int[][] newbpawnPST = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0},
                {-21, -16, -6, -1, -1, -6, -16, -21},
                {-21, -16, -6, 4, 4, -6, -16, -21},
                {-21, -16, -1, 9, 9, -1, -16, -21},
                {-14, -8, 6, 17, 17, 6, -8, -14},
                {-5, 1, 14, 29, 29, 14, 1, -5},
                {7, 11, 23, 39, 39, 23, 11, 7},
                {0, 0, 0, 0, 0, 0, 0, 0}

        };
        /*
        int[][] newbpawnEndPST = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0,},
                {5, -10, -20, -25, -25, -20, -10, 5},
                {5, -10, -20, -25, -25, -20, -10, 5},
                {10, -5, -15, -20, -20, -15, -5, 10},
                {18, 2, -8, -15, -15, -8, 2, 18},
                {30, 14, 1, -10, -10, 1, 14, 30},
                {45, 30, 16, 5, 5, 16, 30, 45},
                {0, 0, 0, 0, 0, 0, 0, 0}

        };
        */

        int[][] newbknightPST = new int[][]{
                {-69, -19, -24, -14, -14, -24, -19, -69},
                {-54, -39, -9, 11, 11, -9, -39, -54},
                {-39, 1, 31, 21, 21, 31, 1, -39},
                {-39, 11, 41, 36, 36, 41, 11, -39},
                {-39, 41, 51, 51, 51, 51, 41, -39},
                {-39, 46, 61, 71, 71, 61, 46, -39},
                {-39, 21, 41, 41, 41, 41, 21, -39},
                {-59, -39, -29, -29, -29, -29, -39, -59}

        };
        /*
        int[][] newbknightEndPST = new int[][]{
                {-63, -53, -43, -43, -43, -43, -53, -63},
                {-53, -43, 18, 28, 28, 18, -43, -53},
                {-43, 18, 48, 38, 38, 48, 18, -43},
                {-43, 38, 58, 68, 68, 58, 38, -43},
                {-43, 38, 73, 78, 78, 73, 38, -43},
                {-43, 28, 78, 73, 73, 78, 28, -43},
                {-53, -43, 38, 48, 48, 38, -43, -53},
                {-63, -53, -43, -43, -43, -43, -53, -63}

        };
        */

        int[][] newbbishopPST = new int[][]{
                {-30, -25, -20, -20, -20, -20, -25, -30},
                {-28, 11, 6, 1, 1, 6, 11, -28},
                {-25, 6, 16, 11, 11, 16, 6, -25},
                {1, 1, 16, 21, 21, 16, 1, 1},
                {1, 21, 21, 26, 26, 21, 21, 1},
                {1, 11, 21, 26, 26, 21, 11, 1},
                {-10, 11, 1, 1, 1, 1, 11, -10},
                {-20, -18, -16, -14, -14, -16, -18, -20}

        };
        /*
        int[][] newbbishopEndPST = new int[][]{
                {-38, -18, -8, 2, 2, -8, -18, -38},
                {-18, -8, 2, 7, 7, 2, -8, -18},
                {-8, 2, 10, 12, 12, 10, 2, -8},
                {2, 12, 16, 20, 20, 16, 12, 2},
                {2, 12, 17, 22, 22, 17, 12, 2},
                {-8, 2, 20, 22, 22, 20, 2, -8},
                {-18, -8, 0, 12, 12, 0, -8, -18},
                {-38, -18, -8, 2, 2, -8, -18, -38}

        };
        */

        int[][] newbrookPST = new int[][]{
                {-8, -6, 2, 7, 7, 2, -6, -8},
                {-8, -6, 2, 7, 7, 2, -6, -8},
                {-8, -6, 6, 7, 7, 6, -6, -8},
                {-8, -6, 6, 7, 7, 6, -6, -8},
                {-8, -6, 6, 8, 8, 6, -6, -8},
                {-8, -6, 6, 10, 10, 6, -6, -8},
                {2, 2, 7, 12, 12, 7, 2, 2},
                {-8, -6, 2, 7, 7, 2, -6, -8}

        };
        /*
        int[][] newbrookEndPST = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}

        };
        */

        int[][] newbqueenPST = new int[][]{
                {-26, -16, -6, 4, 4, -6, -16, -26},
                {-16, -11, -1, 4, 4, -1, -11, -16},
                {-6, -6, -1, 4, 4, -1, -6, -6},
                {4, 4, 4, 4, 4, 4, 4, 4},
                {4, 4, 4, 4, 4, 4, 4, 4},
                {4, 4, 4, 4, 4, 4, 4, 4},
                {4, 4, 4, 4, 4, 4, 4, 4},
                {4, 4, 4, 4, 4, 4, 4, 4}

        };
        /*
        int[][] newbqueenEndPST = new int[][]{
                {-46, -41, -31, -26, -26, -31, -41, -46},
                {-31, -26, -16, -6, -6, -16, -26, -31},
                {-16, -1, 14, 24, 24, 14, -1, -16},
                {-6, 9, 24, 34, 34, 24, 9, -6},
                {-6, 9, 24, 34, 34, 24, 9, -6},
                {-6, 9, 24, 34, 34, 24, 9, -6},
                {-16, 4, 19, 29, 29, 19, 4, -16},
                {-26, -6, -1, 4, 4, -1, -6, -26}

        };
        */

        int[][] newbkingMidPST = new int[][]{
                {-20, 0, 0, -10, -10, 0, 0, -20},
                {-30, -30, -30, -35, -35, -30, -30, -30},
                {-40, -40, -45, -50, -50, -45, -40, -40},
                {-50, -50, -55, -60, -60, -55, -50, -50},
                {-55, -55, -60, -70, -70, -60, -55, -55},
                {-55, -55, -60, -70, -70, -60, -55, -55},
                {-55, -55, -60, -70, -70, -60, -55, -55},
                {-55, -55, -60, -70, -70, -60, -55, -55}

        };

        int[][] newbkingEndPST = new int[][]{
                {-20, 0, 0, -10, -10, 0, 0, -20},
                {-30, -30, -30, -35, -35, -30, -30, -30},
                {-40, -40, -45, -50, -50, -45, -40, -40},
                {-50, -50, -55, -60, -60, -55, -50, -50},
                {-55, -55, -60, -70, -70, -60, -55, -55},
                {-55, -55, -60, -70, -70, -60, -55, -55},
                {-55, -55, -60, -70, -70, -60, -55, -55},
                {-55, -55, -60, -70, -70, -60, -55, -55}
        };

        for (int i=0; i<=7; i++){
            for (int j=0; j<=7; j++){
                bpawnPST[i][j] = newbpawnPST[7-i][j]+100;
                brookPST[i][j] = newbrookPST[7-i][j]+500;
                bknightPST[i][j] = newbknightPST[7-i][j]+320;
                bbishopPST[i][j] = newbbishopPST[7-i][j]+330;
                bqueenPST[i][j] = newbqueenPST[7-i][j]+900;
                bkingMidPST[i][j] = newbkingMidPST[7-i][j]+20000;
                bkingEndPST[i][j] = newbkingMidPST[7-i][j]+20000;
            }
        }
    }

}
