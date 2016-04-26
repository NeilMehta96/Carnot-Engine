package com.nmehta.carnotengine.openingbook;

import com.nmehta.carnotengine.boardstate.Position;
import com.nmehta.carnotengine.utils.MoveTuple;
import com.nmehta.carnotengine.utils.PointTuple;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.nmehta.carnotengine.boardstate.Position.ChessPieces.*;

/**
 * Created by Neil on 4/23/2016.
 */
public final class OpeningBook{

    private OpeningBook(){}

    public static MoveTuple opening(Position position) {
        int moveNum = position.halfMoveNumber;
        if (moveNum == 1 && position.whitePieces[3][4] == wpawn) {
            double c5 = 680618;
            double e5 = 387670;
            double e6 = 217369;
            double c6 = 116055;
            double d6 = 72476;
            double sumGames = c5 + e5 + e6 + c6 + d6;
            double rand = 0.1;
            //double rand = Math.random();
            if (rand < c5 / sumGames) {
                return new MoveTuple(new PointTuple(2, 6), new PointTuple(2, 4));
            } else if (rand < (c5 + e5) / sumGames) {
                return new MoveTuple(new PointTuple(4, 6), new PointTuple(4, 4));
            } else if (rand < (c5 + e5 + e6) / sumGames) {
                return new MoveTuple(new PointTuple(4, 6), new PointTuple(4, 5));
            } else if (rand < (c5 + e5 + e6 + c6) / sumGames) {
                return new MoveTuple(new PointTuple(2, 6), new PointTuple(2, 5));
            } else {
                return new MoveTuple(new PointTuple(3, 6), new PointTuple(3, 5));
            }
        }
        return null;
    }

}
