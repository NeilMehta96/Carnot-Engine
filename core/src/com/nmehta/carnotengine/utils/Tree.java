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
    public ArrayList<Position> children = new ArrayList<Position>();


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
        this.root = new Position(root);
        ArrayList<Position> childList = new ArrayList<Position>();
        for (MoveTuple tuple : AI.generatePositionPossibilities(root)){
            Position newPos = new Position(this.root);
            childList.add(newPos.movePiece(tuple));
        }
        Collections.sort(childList);

        if (!root.whitesMove) {
            Collections.reverse(childList);
        }
        children = childList;
        PlayScreen.positionHash.put(this.root,children);
    }


    public Tree(Position root, ArrayList<Position> children){
        this.root = new Position(root);
        this.children = children;
        System.out.println("this happens");
    }


}
