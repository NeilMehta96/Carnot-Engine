package com.nmehta.carnotengine.utils;

/**
 * Created by Neil on 4/22/2016.
 */
public final class  MoveTuple {

    public final int from;
    public final int to;

    public MoveTuple(int from, int to){
        this.from = from;
        this.to = to;
    }


    @Override
    public boolean equals(Object move){
        MoveTuple newMove = (MoveTuple) move;
        return newMove.from==this.from && newMove.to==this.to;
    }
}
