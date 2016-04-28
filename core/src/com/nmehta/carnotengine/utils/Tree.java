package com.nmehta.carnotengine.utils;

import com.nmehta.carnotengine.ai.AI;
import com.nmehta.carnotengine.boardstate.Position;
import com.nmehta.carnotengine.rules.Rules;
import com.nmehta.carnotengine.screens.PlayScreen;

import java.util.*;

/**
 * Created by Neil on 4/21/2016.
 */
public class Tree {

    public Position root;
    public List<Position> children = new ArrayList<Position>();


    /*
    public Tree(Position root){
        this.root = new Position(root);
        for (MoveTuple tuple : AI.generatePositionPossibilities(root)){
            //System.out.println()
            Position newPos = new Position(this.root);
            children.add(newPos.movePiece(tuple));
        }

    }
    */



    public Tree(Position root){
        this.root = root;
        List<Position> childList = new ArrayList<Position>();
        for (MoveTuple tuple : AI.generatePositionPossibilities(root)){
            childList.add(this.root.movePiece(tuple));
        }
        Collections.sort(childList);

        if (!root.whitesMove) {
            Collections.reverse(childList);
        }
        children = childList;
//        PlayScreen.positionHash.put(root.moveList,children);
    }


    public Tree(Position root, List<Position> children){
        this.root = new Position(root);
        this.children = Position.copyList(children);
        //System.out.println("this happens");
    }


}
