package com.anjesproductions.dungeons;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

public class RouteFinder {

public class Node {
  List<?> neighbors;
  Node pathParent;
}

protected List<Node> constructPath(Node node) {
  LinkedList<Node> path = new LinkedList<Node>();
  while (node.pathParent != null) {
    path.addFirst(node);
    node = node.pathParent;
  }
  return path;
}

public List<Node> search(Node startNode, Node goalNode) {
  // list of visited nodes
  LinkedList<Node> closedList = new LinkedList<Node>();
  
  // list of nodes to visit (sorted)
  LinkedList<Node> openList = new LinkedList<Node>();
  openList.add(startNode);
  startNode.pathParent = null;
  
  while (!openList.isEmpty()) {
    Node node = (Node)openList.removeFirst();
    if (node == goalNode) {
      // path found!
      return constructPath(goalNode);
    }
    else {
      closedList.add(node);
      
      // add neighbors to the open list
      Iterator<?> i = node.neighbors.iterator();
      while (i.hasNext()) {
        Node neighborNode = (Node)i.next();
        if (!closedList.contains(neighborNode) &&
          !openList.contains(neighborNode)) 
        {
          neighborNode.pathParent = node;
          openList.add(neighborNode);
        }
      }
    }
  }
  
  // no path found
  return null;
}
}