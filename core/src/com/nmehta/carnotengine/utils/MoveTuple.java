package com.nmehta.carnotengine.utils;

/**
 * Created by Neil on 4/22/2016.
 */
public final class  MoveTuple {

    public final PointTuple from;
    public final PointTuple to;

    public MoveTuple(PointTuple from, PointTuple to){
        this.from = from;
        this.to = to;
    }

    public MoveTuple(MoveTuple move) {
        this.from = move.from;
        this.to = move.to;
    }

    @Override
    public boolean equals(Object move){
        MoveTuple newMove = (MoveTuple) move;
        return newMove.from.equals(this.from) && newMove.to.equals(this.to);
    }
}
