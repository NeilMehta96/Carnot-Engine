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

    public static boolean ruleCheck(MoveTuple move, Position position, boolean checkForCheck) {
        ChessPieces fromPiece;
        if (position.isWhitesMove()) {
            if (position.getWhitePieces()[move.from] == empty) {
                return false;
            }
            if (position.getWhitePieces()[move.to] != empty) {
                return false;
            }
            fromPiece = position.getWhitePieces()[move.from];
        } else {
            if (position.getBlackPieces()[move.from] == empty) {
                return false;
            }
            if (position.getBlackPieces()[move.to] != empty) {
                return false;
            }
            fromPiece = position.getBlackPieces()[move.from];
        }

        if (fromPiece == wpawn) {
            return whitePawnRuleCheck(move, position, checkForCheck);
        } else if (fromPiece == bpawn) {
            return blackPawnRuleCheck(move, position, checkForCheck);
        } else if (fromPiece == wrook || fromPiece == brook) {
            return rookRuleCheck(move, position, checkForCheck);
        } else if (fromPiece == wknight || fromPiece == bknight) {
            return knightRuleCheck(move, position, checkForCheck);
        } else if (fromPiece == wbishop || fromPiece == bbishop) {
            return bishopRuleCheck(move, position, checkForCheck);
        } else if (fromPiece == wqueen || fromPiece == bqueen) {
            return queenRuleCheck(move, position, checkForCheck);
        } else if (fromPiece == wking) {
            return whiteKingRuleCheck(move, position, checkForCheck);
        } else if (fromPiece == bking) {
            return blackKingRuleCheck(move, position, checkForCheck);
        }
        return false;

    }

    private static boolean notInCheck(MoveTuple move, Position oldPosition) {

        Position position = new Position(oldPosition);
        position = position.movePiece(move);
        return notInCheck(position);

    }

    private static boolean notInCheck(Position position) {
        boolean moveAllowed = false;
        int i = 0;
        while (!moveAllowed && i <= 63) {
            if (position.isWhitesMove()) {
                moveAllowed = Rules.ruleCheck(new MoveTuple(i, position.getBlackKingPos()), position, false);
            } else {
                moveAllowed = Rules.ruleCheck(new MoveTuple(i, position.getWhiteKingPos()), position, false);
            }
            i++;
        }
        return !moveAllowed;
    }


    private static boolean whitePawnRuleCheck(MoveTuple move, Position position, boolean checkForCheck) {

        //TODO: implement En Passant
        int to = move.to;
        int from = move.from;
        boolean moveAllowed = false;
        if (to%8 == from%8 && to/8 - 2 == from/8 && from/8 == 1 && position.getAllPieces()[to] == empty && position.getAllPieces()[to-8] == empty) {
            moveAllowed = true;
        } else if (to%8 == from%8 && to/8 - 1 == from/8 && position.getAllPieces()[to] == empty) {
            moveAllowed = true;

        } else if ((to/8 - 1 == from/8 && ((to%8 - 1 == from%8 || to%8 + 1 == from%8) && position.getBlackPieces()[to] != empty))) {
            moveAllowed = true;
        }
        /**
         else if (state.doublePawnMove&&from/8==state.enPassant/8&&to/8-1==from/8&&to%8==state.enPassant%8&&
         (to%8-1==from%8||to%8+1==from%8)){
         moveAllowed = whiteNotInCheck(to,getAllPieces(),getWhitePieces(),state,getBlackPieces());
         if (moveAllowed) {
         getBlackPieces()[to/8 - 1][to%8] = empty;

         }
         }
         */

        if (moveAllowed && checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }

        return moveAllowed;
    }

    private static boolean blackPawnRuleCheck(MoveTuple move, Position position, boolean checkForCheck) {

        //TODO: implement En Passant
        int to = move.to;
        int from = move.from;
        boolean moveAllowed = false;
        if (to%8 == from%8 && to/8 + 2 == from/8 && from/8 == 6 && position.getAllPieces()[to] == empty && position.getAllPieces()[to+8] == empty) {
            moveAllowed = true;
        } else if (to%8 == from%8 && to/8 + 1 == from/8 && position.getAllPieces()[to] == empty) {
            moveAllowed = true;

        } else if ((to/8 + 1 == from/8 && ((to%8 - 1 == from%8 || to%8 + 1 == from%8) && position.getWhitePieces()[to] != empty))) {
            moveAllowed = true;

        }
        /**
         else if (state.doublePawnMove&&from/8==state.enPassant/8&&to/8-1==from/8&&to%8==state.enPassant%8&&
         (to%8-1==from%8||to%8+1==from%8)){
         moveAllowed = whiteNotInCheck(to,getAllPieces(),getWhitePieces(),state,getBlackPieces());
         if (moveAllowed) {
         getBlackPieces()[to/8 - 1][to%8] = empty;

         }
         }
         */

        if (moveAllowed && checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        return moveAllowed;
    }

    private static boolean rookRuleCheck(MoveTuple move, Position position, boolean checkForCheck) {
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
                while (position.getAllPieces()[yIter] == empty && yIter/8 != to/8) {
                    yIter += direction;
                }
                if (yIter/8 == to/8) {
                    moveAllowed = true;
                    if (checkForCheck) {
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
                while (position.getAllPieces()[xIter] == empty && xIter%8 != to%8) {
                    xIter = xIter + direction;
                }
                if (xIter%8 == to%8) {
                    moveAllowed = true;
                    if (checkForCheck) {
                        moveAllowed = notInCheck(move, position);
                    }
                }
            }

        }


        return moveAllowed;

    }


    private static boolean knightRuleCheck(MoveTuple move, Position position, boolean checkForCheck) {
        /*
        boolean moveAllowed = false;
        if ((Math.abs(to%8-from%8)+Math.abs(to/8-from/8)==3) && to%8!=from%8 && to/8!=from/8){
            moveAllowed = notInCheck(from,to, getAllPieces(), getWhitePieces(), state, getBlackPieces());
        }
        return moveAllowed;
        */
        int to = move.to;
        int from = move.from;
        boolean moveAllowed = ((Math.abs(to%8 - from%8) + Math.abs(to/8 - from/8) == 3) && to%8 != from%8 && to/8 != from/8);
        if (moveAllowed && checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        return moveAllowed;
    }


    private static boolean bishopRuleCheck(MoveTuple move, Position position, boolean checkForCheck) {
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
            while (position.getAllPieces()[iter] == empty && iter != to) {
                iter += xDir + yDir;
            }
            if (iter == to) {
                moveAllowed = true;
                if (checkForCheck) {
                    moveAllowed = notInCheck(move, position);
                }
            }
        }

        return moveAllowed;

    }


    private static boolean queenRuleCheck(MoveTuple move, Position position, boolean checkForCheck) {
        return rookRuleCheck(move, position, checkForCheck)
                || bishopRuleCheck(move, position, checkForCheck);

    }

    private static boolean whiteKingRuleCheck(MoveTuple move, Position position, boolean checkForCheck) {
        int to = move.to;
        int from = move.from;

        boolean moveAllowed = ((to%8 - from%8) * (to%8 - from%8) + (to/8 - from/8) * (to/8 - from/8) < 4);
        if (moveAllowed && checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        else if (position.isWhiteCanCastle()&&((to%8==2&&position.getWhitePieces()[0]==wrook)||(to%8==6&&position.getWhitePieces()[7]==wrook))&&to/8==0) {
            if (to%8 == 2) {
                moveAllowed = position.getAllPieces()[1] == empty && position.getAllPieces()[2] == empty &&
                        position.getAllPieces()[3] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, 3), position);
            } else {
                moveAllowed = position.getAllPieces()[5] == empty && position.getAllPieces()[6] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, 5), position);
            }
            if (moveAllowed){
                Position checkPos = new Position(position,false);
                moveAllowed = notInCheck(checkPos);
            }
//            checkForCheck = true;
//            position.isWhitesMove() = true;
        }

        return moveAllowed;


    }

    private static boolean blackKingRuleCheck(MoveTuple move, Position position, boolean checkForCheck) {
        int to = move.to;
        int from = move.from;
        boolean moveAllowed = ((to%8 - from%8) * (to%8 - from%8) + (to/8 - from/8) * (to/8 - from/8) < 4);
        if (moveAllowed && checkForCheck) {
            moveAllowed = notInCheck(move, position);
        }
        else if (position.isBlackCanCastle()&&((to%8==2&&position.getBlackPieces()[56]==brook)||(to%8==6&&position.getBlackPieces()[63]==brook))&&to/8==7) {
            if (to%8 == 2) {
                moveAllowed = position.getAllPieces()[57] == empty && position.getAllPieces()[58] == empty &&
                        position.getAllPieces()[59] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, 59), position);
            } else {
                moveAllowed = position.getAllPieces()[61] == empty && position.getAllPieces()[62] == empty && notInCheck(move, position)
                        && notInCheck(new MoveTuple(from, 61), position);
           }
            if (moveAllowed){
                Position checkPos = new Position(position, true);
                moveAllowed = notInCheck(checkPos);
            }
        }
        return moveAllowed;
    }


}
