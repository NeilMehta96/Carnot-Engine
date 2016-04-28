package com.nmehta.carnotengine.rules;

import com.nmehta.carnotengine.boardstate.Position;
import com.nmehta.carnotengine.utils.MoveTuple;

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
            if (position.whitePieces[move.from] == empty) {
                return false;
            }
            if (position.whitePieces[move.to] != empty) {
                return false;
            }
            fromPiece = position.whitePieces[move.from];
        } else {
            if (position.blackPieces[move.from] == empty) {
                return false;
            }
            if (position.blackPieces[move.to] != empty) {
                return false;
            }
            fromPiece = position.blackPieces[move.from];
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
        position = position.movePiece(move);
        return notInCheck(position);

    }

    private static boolean notInCheck(Position position) {
        position.checkForCheck = false;
        boolean moveAllowed = false;
        int i = 0;
        while (!moveAllowed && i <= 63) {
            if (position.whitesMove) {
                moveAllowed = Rules.ruleCheck(new MoveTuple(i, position.blackKingPos), position);
            } else {
                moveAllowed = Rules.ruleCheck(new MoveTuple(i, position.whiteKingPos), position);
            }
            i++;
        }
        return !moveAllowed;
    }


    private static boolean whitePawnRuleCheck(MoveTuple move, Position position) {

        //TODO: implement En Passant
        int to = move.to;
        int from = move.from;
        boolean moveAllowed = false;
        if (to%8 == from%8 && to/8 - 2 == from/8 && from/8 == 1 && position.allPieces[to] == empty && position.allPieces[to-8] == empty) {
            moveAllowed = true;
        } else if (to%8 == from%8 && to/8 - 1 == from/8 && position.allPieces[to] == empty) {
            moveAllowed = true;

        } else if ((!position.doublePawnMove) && (to/8 - 1 == from/8 && ((to%8 - 1 == from%8 || to%8 + 1 == from%8) && position.blackPieces[to] != empty))) {
            moveAllowed = true;
        }
        /**
         else if (state.doublePawnMove&&from/8==state.enPassant/8&&to/8-1==from/8&&to%8==state.enPassant%8&&
         (to%8-1==from%8||to%8+1==from%8)){
         moveAllowed = whiteNotInCheck(to,allPieces,whitePieces,state,blackPieces);
         if (moveAllowed) {
         blackPieces[to/8 - 1][to%8] = empty;

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
        int to = move.to;
        int from = move.from;
        boolean moveAllowed = false;
        if (to%8 == from%8 && to/8 + 2 == from/8 && from/8 == 6 && position.allPieces[to] == empty && position.allPieces[to+8] == empty) {
            moveAllowed = true;
        } else if (to%8 == from%8 && to/8 + 1 == from/8 && position.allPieces[to] == empty) {
            moveAllowed = true;

        } else if ((!position.doublePawnMove) && (to/8 + 1 == from/8 && ((to%8 - 1 == from%8 || to%8 + 1 == from%8) && position.whitePieces[to] != empty))) {
            moveAllowed = true;

        }
        /**
         else if (state.doublePawnMove&&from/8==state.enPassant/8&&to/8-1==from/8&&to%8==state.enPassant%8&&
         (to%8-1==from%8||to%8+1==from%8)){
         moveAllowed = whiteNotInCheck(to,allPieces,whitePieces,state,blackPieces);
         if (moveAllowed) {
         blackPieces[to/8 - 1][to%8] = empty;

         }
         }
         */

        if (moveAllowed && position.checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        return moveAllowed;
    }

    private static boolean rookRuleCheck(MoveTuple move, Position position) {
        int to = move.to;
        int from = move.from;
        boolean moveAllowed = false;
        if (to%8 != from%8 ^ to/8 != from/8) {
            moveAllowed = true;
        }
        if (moveAllowed) {
            moveAllowed = false;
            if (to%8 == from%8) {
                int direction;
                if (to/8 > from/8) {
                    direction = 8;
                } else {
                    direction = -8;
                }
                int yIter = from + direction;
                while (position.allPieces[yIter] == empty && yIter/8 != to/8) {
                    yIter += direction;
                }
                if (yIter/8 == to/8) {
                    moveAllowed = true;
                    if (position.checkForCheck) {
                        moveAllowed = notInCheck(move, position);
                    }
                }
            } else {
                int direction;
                if (to%8 > from%8) {
                    direction = 1;
                } else {
                    direction = -1;
                }
                int xIter = from + direction;
                while (position.allPieces[xIter] == empty && xIter%8 != to%8) {
                    xIter = xIter + direction;
                }
                if (xIter%8 == to%8) {
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
        if ((Math.abs(to%8-from%8)+Math.abs(to/8-from/8)==3) && to%8!=from%8 && to/8!=from/8){
            moveAllowed = notInCheck(from,to, allPieces, whitePieces, state, blackPieces);
        }
        return moveAllowed;
        */
        int to = move.to;
        int from = move.from;
        boolean moveAllowed = ((Math.abs(to%8 - from%8) + Math.abs(to/8 - from/8) == 3) && to%8 != from%8 && to/8 != from/8);
        if (moveAllowed && position.checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        return moveAllowed;
    }


    private static boolean bishopRuleCheck(MoveTuple move, Position position) {
        int to = move.to;
        int from = move.from;
        boolean moveAllowed = false;
        if (Math.abs(to%8 - from%8) == Math.abs(to/8 - from/8) && to%8 != from%8) {

            int xDir;
            int yDir;
            if (to%8 - from%8 >= 1) {
                xDir = 1;
            } else {
                xDir = -1;
            }
            if (to/8 - from/8 >= 1) {
                yDir = 8;
            } else {
                yDir = -8;
            }
            int iter = from + xDir + yDir;
            while (position.allPieces[iter] == empty && iter != to) {
                iter += xDir + yDir;
            }
            if (iter == to) {
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
        int to = move.to;
        int from = move.from;

        boolean moveAllowed = ((to%8 - from%8) * (to%8 - from%8) + (to/8 - from/8) * (to/8 - from/8) < 4);
        if (moveAllowed && position.checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        else if (position.whiteCanCastle&&((to%8==2&&position.whitePieces[0]==wrook)||(to%8==6&&position.whitePieces[7]==wrook))&&to/8==0) {
            if (to%8 == 2) {
                moveAllowed = position.allPieces[1] == empty && position.allPieces[2] == empty &&
                        position.allPieces[3] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, 3), position);
            } else {
                moveAllowed = position.allPieces[5] == empty && position.allPieces[6] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, 5), position);
            }
            if (moveAllowed){
                Position checkPos = new Position(position);
                checkPos.whitesMove = false;
                moveAllowed = notInCheck(checkPos);
            }
//            position.checkForCheck = true;
//            position.whitesMove = true;
        }

        return moveAllowed;


    }

    private static boolean blackKingRuleCheck(MoveTuple move, Position position) {
        int to = move.to;
        int from = move.from;
        boolean moveAllowed = ((to%8 - from%8) * (to%8 - from%8) + (to/8 - from/8) * (to/8 - from/8) < 4);
        if (moveAllowed && position.checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        else if (position.blackCanCastle&&((to%8==2&&position.blackPieces[56]==brook)||(to%8==6&&position.blackPieces[63]==brook))&&to/8==7) {
            if (to%8 == 2) {
                moveAllowed = position.allPieces[57] == empty && position.allPieces[58] == empty &&
                        position.allPieces[59] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, 59), position);
            } else {
                moveAllowed = position.allPieces[61] == empty && position.allPieces[62] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, 61), position);
           }
            if (moveAllowed){
                Position checkPos = new Position(position);
                checkPos.whitesMove = true;
                moveAllowed = notInCheck(checkPos);
            }
//            position.checkForCheck = true;
//            position.whitesMove = false;
        }
        return moveAllowed;
    }


}
