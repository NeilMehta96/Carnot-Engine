package com.nmehta.carnotengine.rules;

import com.nmehta.carnotengine.boardstate.Position;
import com.nmehta.carnotengine.utils.MoveTuple;
import com.nmehta.carnotengine.utils.PointTuple;

import java.lang.Math;

import static com.nmehta.carnotengine.boardstate.Position.ChessPieces;
import static com.nmehta.carnotengine.boardstate.Position.ChessPieces.*;

/**
 * Created by Neil on 4/19/2016.
 */

public class Rules {

    public static boolean ruleCheck(MoveTuple move, Position position) {
        ChessPieces fromPiece;
        if (position.whitesMove) {
            if (position.whitePieces[move.from.y][move.from.x] == empty) {
                return false;
            }
            if (position.whitePieces[move.to.y][move.to.x] != empty) {
                return false;
            }
            fromPiece = position.whitePieces[move.from.y][move.from.x];
        } else {
            if (position.blackPieces[move.from.y][move.from.x] == empty) {
                return false;
            }
            if (position.blackPieces[move.to.y][move.to.x] != empty) {
                return false;
            }
            fromPiece = position.blackPieces[move.from.y][move.from.x];
        }

        if (fromPiece == wpawn) {
            return whitePawnRuleCheck(move, position);
        } else if (fromPiece == bpawn) {
            return blackPawnRuleCheck(move, position);
        } else if (fromPiece == wrook || fromPiece == brook) {
            return rookRuleCheck(move, position);
        } else if (fromPiece == wknight || fromPiece == bknight) {
            return knightRuleCheck(move, position);
        } else if (fromPiece == wbishop || fromPiece == bbishop) {
            return bishopRuleCheck(move, position);
        } else if (fromPiece == wqueen || fromPiece == bqueen) {
            return queenRuleCheck(move, position);
        } else if (fromPiece == wking) {
            return whiteKingRuleCheck(move, position);
        } else if (fromPiece == bking) {
            return blackKingRuleCheck(move, position);
        }
        return false;

    }

    private static boolean notInCheck(MoveTuple move, Position oldPosition) {

        Position position = new Position(oldPosition);
        position.movePiece(move);
        return notInCheck(position);

    }

    private static boolean notInCheck(Position position) {
        position.checkForCheck = false;
        boolean moveAllowed = false;
        int i = 0;
        int j = 0;
        while (!moveAllowed && i <= 7) {
            while (!moveAllowed && j <= 7) {
                if (position.whitesMove) {
                    moveAllowed = Rules.ruleCheck(new MoveTuple(new PointTuple(i, j), position.blackKingPos), position);
                } else {
                    moveAllowed = Rules.ruleCheck(new MoveTuple(new PointTuple(i, j), position.whiteKingPos), position);
                }
                j++;

            }
            i++;
            j = 0;
        }
        return !moveAllowed;
    }


    private static boolean whitePawnRuleCheck(MoveTuple move, Position position) {

        //TODO: implement En Passant
        PointTuple to = move.to;
        PointTuple from = move.from;
        boolean moveAllowed = false;
        if (to.x == from.x && to.y - 2 == from.y && from.y == 1 && position.allPieces[to.y][to.x] == empty && position.allPieces[to.y - 1][to.x] == empty) {
            moveAllowed = true;
        } else if (to.x == from.x && to.y - 1 == from.y && position.allPieces[to.y][to.x] == empty) {
            moveAllowed = true;

        } else if ((!position.doublePawnMove) && (to.y - 1 == from.y && ((to.x - 1 == from.x || to.x + 1 == from.x) && position.blackPieces[to.y][to.x] != empty))) {
            moveAllowed = true;
        }
        /**
         else if (state.doublePawnMove&&from.y==state.enPassant.y&&to.y-1==from.y&&to.x==state.enPassant.x&&
         (to.x-1==from.x||to.x+1==from.x)){
         moveAllowed = whiteNotInCheck(to,allPieces,whitePieces,state,blackPieces);
         if (moveAllowed) {
         blackPieces[to.y - 1][to.x] = empty;

         }
         }
         */

        if (moveAllowed && position.checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }

        return moveAllowed;
    }

    private static boolean blackPawnRuleCheck(MoveTuple move, Position position) {

        //TODO: implement En Passant
        PointTuple to = move.to;
        PointTuple from = move.from;
        boolean moveAllowed = false;
        if (to.x == from.x && to.y + 2 == from.y && from.y == 6 && position.allPieces[to.y][to.x] == empty && position.allPieces[to.y + 1][to.x] == empty) {
            moveAllowed = true;
        } else if (to.x == from.x && to.y + 1 == from.y && position.allPieces[to.y][to.x] == empty) {
            moveAllowed = true;

        } else if ((!position.doublePawnMove) && (to.y + 1 == from.y && ((to.x - 1 == from.x || to.x + 1 == from.x) && position.whitePieces[to.y][to.x] != empty))) {
            moveAllowed = true;

        }
        /**
         else if (state.doublePawnMove&&from.y==state.enPassant.y&&to.y-1==from.y&&to.x==state.enPassant.x&&
         (to.x-1==from.x||to.x+1==from.x)){
         moveAllowed = whiteNotInCheck(to,allPieces,whitePieces,state,blackPieces);
         if (moveAllowed) {
         blackPieces[to.y - 1][to.x] = empty;

         }
         }
         */

        if (moveAllowed && position.checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        return moveAllowed;
    }

    private static boolean rookRuleCheck(MoveTuple move, Position position) {
        PointTuple to = move.to;
        PointTuple from = move.from;
        boolean moveAllowed = false;
        if (to.x != from.x ^ to.y != from.y) {
            moveAllowed = true;
        }
        if (moveAllowed) {
            moveAllowed = false;
            if (to.x == from.x) {
                int direction;
                if (to.y > from.y) {
                    direction = 1;
                } else {
                    direction = -1;
                }
                int yIter = from.y + direction;
                while (position.allPieces[yIter][to.x] == empty && yIter != to.y) {
                    yIter = yIter + direction;
                }
                if (yIter == to.y) {
                    moveAllowed = true;
                    if (position.checkForCheck) {
                        moveAllowed = notInCheck(move, position);
                    }
                }
            } else {
                int direction;
                if (to.x > from.x) {
                    direction = 1;
                } else {
                    direction = -1;
                }
                int xIter = from.x + direction;
                while (position.allPieces[to.y][xIter] == empty && xIter != to.x) {
                    xIter = xIter + direction;
                }
                if (xIter == to.x) {
                    moveAllowed = true;
                    if (position.checkForCheck) {
                        moveAllowed = notInCheck(move, position);
                    }
                }
            }

        }


        return moveAllowed;

    }


    private static boolean knightRuleCheck(MoveTuple move, Position position) {
        /*
        boolean moveAllowed = false;
        if ((Math.abs(to.x-from.x)+Math.abs(to.y-from.y)==3) && to.x!=from.x && to.y!=from.y){
            moveAllowed = notInCheck(from,to, allPieces, whitePieces, state, blackPieces);
        }
        return moveAllowed;
        */
        PointTuple to = move.to;
        PointTuple from = move.from;
        boolean moveAllowed = ((Math.abs(to.x - from.x) + Math.abs(to.y - from.y) == 3) && to.x != from.x && to.y != from.y);
        if (moveAllowed && position.checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        return moveAllowed;
    }


    private static boolean bishopRuleCheck(MoveTuple move, Position position) {
        PointTuple to = move.to;
        PointTuple from = move.from;
        boolean moveAllowed = false;
        if (Math.abs(to.x - from.x) == Math.abs(to.y - from.y) && to.x != from.x) {

            int xDir;
            int yDir;
            if (to.x - from.x >= 1) {
                xDir = 1;
            } else {
                xDir = -1;
            }
            if (to.y - from.y >= 1) {
                yDir = 1;
            } else {
                yDir = -1;
            }
            int xIter = from.x + xDir;
            int yIter = from.y + yDir;
            while (position.allPieces[yIter][xIter] == empty && xIter != to.x) {
                xIter = xIter + xDir;
                yIter = yIter + yDir;
            }
            if (xIter == to.x) {
                moveAllowed = true;
                if (position.checkForCheck) {
                    moveAllowed = notInCheck(move, position);
                }
            }
        }

        return moveAllowed;

    }


    private static boolean queenRuleCheck(MoveTuple move, Position position) {
        return rookRuleCheck(move, position)
                || bishopRuleCheck(move, position);

    }

    private static boolean whiteKingRuleCheck(MoveTuple move, Position position) {
        PointTuple to = move.to;
        PointTuple from = move.from;

        boolean moveAllowed = ((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y) < 4);
        if (moveAllowed && position.checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        else if (position.whiteCanCastle&&((to.x==2&&position.whitePieces[0][0]==wrook)||(to.x==6&&position.whitePieces[0][7]==wrook))&&to.y==0) {
            if (to.x == 2) {
                moveAllowed = position.allPieces[0][1] == empty && position.allPieces[0][2] == empty &&
                        position.allPieces[0][3] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, new PointTuple(3, 0)), position) && notInCheck(position);
            } else {
                moveAllowed = position.allPieces[0][5] == empty && position.allPieces[0][6] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, new PointTuple(5, 0)), position) && notInCheck(position);
            }
            position.checkForCheck = true;
            position.whitesMove = true;
            //position.halfMoveNumber--;
        }

        return moveAllowed;


    }

    private static boolean blackKingRuleCheck(MoveTuple move, Position position) {
        PointTuple to = move.to;
        PointTuple from = move.from;
        boolean moveAllowed = ((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y) < 4);
        if (moveAllowed && position.checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        else if (position.blackCanCastle&&((to.x==2&&position.blackPieces[7][0]==brook)||(to.x==6&&position.blackPieces[7][7]==brook))&&to.y==7) {
            if (to.x == 2) {
                moveAllowed = position.allPieces[7][1] == empty && position.allPieces[7][2] == empty &&
                        position.allPieces[7][3] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, new PointTuple(3, 7)), position) && notInCheck(position);
            } else {
                moveAllowed = position.allPieces[7][5] == empty && position.allPieces[7][6] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, new PointTuple(5, 7)), position) && notInCheck(position);
           }
            position.checkForCheck = true;
            position.whitesMove = false;
            //position.halfMoveNumber--;
        }
        return moveAllowed;
    }


}
