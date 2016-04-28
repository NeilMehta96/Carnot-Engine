package com.github.neilmehta1.carnotengine.openingbook;

import com.github.neilmehta1.carnotengine.boardstate.Position;
import com.github.neilmehta1.carnotengine.utils.MoveTuple;

import static com.github.neilmehta1.carnotengine.boardstate.Position.ChessPieces.*;


public final class OpeningBook{

    private OpeningBook(){}

    public static MoveTuple opening(Position position) {
        int moveNum = position.getHalfMoveNumber();
        if (moveNum == 1 && position.getWhitePieces()[28] == wpawn) {
            double c5 = 680618;
            double e5 = 387670;
            double e6 = 217369;
            double c6 = 116055;
            double d6 = 72476;
            double sumGames = c5 + e5 + e6 + c6 + d6;
            double rand = 0.1;
//            double rand = Math.random();
            if (rand < c5 / sumGames) {
                return new MoveTuple(50, 34);
            } else if (rand < (c5 + e5) / sumGames) {
                return new MoveTuple(52, 36);
            } else if (rand < (c5 + e5 + e6) / sumGames) {
                return new MoveTuple(52, 44);
            } else if (rand < (c5 + e5 + e6 + c6) / sumGames) {
                return new MoveTuple(50, 42);
            } else {
                return new MoveTuple(51, 43);
            }
        }
        else if (moveNum==3&&position.getWhitePieces()[28] == wpawn&&position.getBlackPieces()[34] == bpawn){
            if (position.getWhitePieces()[21]==wknight){
                double d6 = 205376;
                double Nc6 = 161170;
                double e6 = 120816;
                double g6 = 10318;
                double a6 = 7892;
                double sumGames = d6 + Nc6 + e6 + g6 + a6;
                double rand = Math.random();
                if (rand < d6 / sumGames) {
                    return new MoveTuple(51,43);
                } else if (rand < (d6 + Nc6) / sumGames) {
                    return new MoveTuple(57, 42);
                } else if (rand < (d6 + Nc6 + e6) / sumGames) {
                    return new MoveTuple(52, 44);
                } else if (rand < (d6 + Nc6 + e6 + g6) / sumGames) {
                    return new MoveTuple(54, 46);
                } else {
                    return new MoveTuple(48, 40);
                }

            }

            else if (position.getWhitePieces()[18]==wknight){
                double Nc6 = 46382;
                double e6 = 11460;
                double d6 = 11009;
                double a6 = 2327;
                double g6 = 2172;
                double sumGames = d6 + Nc6 + e6 + g6 + a6;
                double rand = Math.random();
                if (rand < Nc6 / sumGames) {
                    return new MoveTuple(57, 42);
                } else if (rand < (e6 + Nc6) / sumGames) {
                    return new MoveTuple(52, 44);
                } else if (rand < (d6 + Nc6 + e6) / sumGames) {
                    return new MoveTuple(51,43);
                } else if (rand < (d6 + Nc6 + e6 + a6) / sumGames) {
                    return new MoveTuple(48, 40);
                } else {
                    return new MoveTuple(54, 46);
                }
            }

            else if (position.getWhitePieces()[18]==wpawn){
                double d5 = 19562;
                double Nf6 = 17894;
                double e6 = 5179;
                double d6 = 5057;
                double g6 = 1993;
                double sumGames = d5+Nf6+e6+d6+g6;
                double rand = Math.random();
                if (rand < d5 / sumGames) {
                    return new MoveTuple(51, 35);
                } else if (rand < (d5+Nf6) / sumGames) {
                    return new MoveTuple(62, 45);
                } else if (rand < (d5+Nf6+e6) / sumGames) {
                    return new MoveTuple(52,44);
                } else if (rand < (d5+Nf6+e6+d6) / sumGames) {
                    return new MoveTuple(51, 43);
                } else {
                    return new MoveTuple(54, 46);
                }
            }
            else if (position.getWhitePieces()[27]==wpawn){
                double cxd4 = 11772;
                double e6 = 283;
                double g6 = 57;
                double sumGames = cxd4+e6+g6;
                double rand = Math.random();
                if (rand < cxd4 / sumGames) {
                    return new MoveTuple(34, 27);
                } else if (rand < (cxd4+e6) / sumGames) {
                    return new MoveTuple(52, 44);
                } else {
                    return new MoveTuple(54,46);
                }
            }

            else if (position.getWhitePieces()[29]==wpawn){
                double Nc6 = 2394;
                double d5 = 1836;
                double e6 = 1399;
                double d6 = 1014;
                double g6 = 648;
                double sumGames = Nc6+d5+e6+d6+g6;
                double rand = Math.random();
                if (rand < Nc6 / sumGames) {
                    return new MoveTuple(57, 42);
                } else if (rand < (Nc6+d5) / sumGames) {
                    return new MoveTuple(51, 35);
                } else if (rand < (Nc6+d5+e6) / sumGames) {
                    return new MoveTuple(52,44);
                } else if (rand < (Nc6+d5+e6+d6) / sumGames) {
                    return new MoveTuple(51, 43);
                } else {
                    return new MoveTuple(54, 46);
                }
            }


        }
        else if (moveNum==5&&position.getWhitePieces()[28] == wpawn&&position.getBlackPieces()[34] == bpawn
                &&position.getWhitePieces()[21]==wknight&&position.getBlackPieces()[43]==bpawn) {
            if (position.getWhitePieces()[27] == wpawn) {
                double cxd4 = 156000;
                double Nf6 = 6212;
                double sumGames = cxd4 + Nf6;
                double rand = Math.random();
                if (rand < cxd4 / sumGames) {
                    return new MoveTuple(34, 27);
                } else {
                    return new MoveTuple(62, 45);
                }

            }
        }
        else if (moveNum==7&&position.getWhitePieces()[27]==wknight&&position.getWhitePieces()[28]==wpawn&&position.getBlackPieces()[43]==bpawn){
            return new MoveTuple(62,45);
        }
        else if (moveNum==9&&position.getWhitePieces()[27]==wknight&&position.getWhitePieces()[28]==wpawn&&position.getBlackPieces()[43]==bpawn
                &&position.getBlackPieces()[45]==bknight&&position.getWhitePieces()[18]==wknight){
            double a6 = 84021;
            double g6 = 33871;
            double Nc6 = 21513;
            double sumGames = a6 + g6 + Nc6;
            double rand = Math.random();
            if (rand < a6 / sumGames) {
                return new MoveTuple(48, 40);
            }
            else if (rand < a6+g6){
                return new MoveTuple(54, 46);
            }
            else {
                return new MoveTuple(57, 42);
            }
        }


        return null;
    }

}
