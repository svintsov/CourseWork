package ua.codeconveyor.coursework.model.engine;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ua.codeconveyor.coursework.model.entity.Channel;
import ua.codeconveyor.coursework.model.entity.Node;

public class DijkstraPath {


    private Set<Node> unsettledNodes;
    private Set<Node> settledNodes;
    private Node[] allNodes;
    public int pathWeight;
    private Map<Node, Node> predecessors;
    public Map<Node, Integer> distance;

    private Node findNodeById(int id, Node[] nodes) {
        for (Node node : nodes) {
            if (node.getId() == id)
                return node;
        }
        return null;
    }


    public DijkstraPath() {
    }


    public void findPath(Node nodeStart, Node[] unchecked) {
        pathWeight = 0;
        unsettledNodes = new HashSet<>();
        settledNodes = new HashSet<>();
        allNodes = unchecked;
        Node startNode = nodeStart;
        distance = new HashMap<Node, Integer>();
        predecessors = new HashMap<Node, Node>();
        distance.put(startNode, 0);
        unsettledNodes.add(nodeStart);

        while (unsettledNodes.size() > 0) {
            startNode = getMinimumNode();
            migrateNode(startNode);
            findMinimalDistances(startNode);

        }
    }

    private void migrateNode(Node nodeToMigrate) {
        settledNodes.add(nodeToMigrate);
        unsettledNodes.remove(nodeToMigrate);

    }

    private void findMinimalDistances(Node node) {
        List<Node> adjacentNodes = getNeighbors(node);
        for (Node target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unsettledNodes.add(target);
            }
        }

    }

    public LinkedList<Node> getPath(Node target) {
        LinkedList<Node> path = new LinkedList<Node>();
        Node step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

    private int getDistance(Node node, Node target) {
        for (Channel channel : node.getChannels()) {
            if (channel.getNode2Id() == target.getId()) {
                return channel.getPrice();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Node> getNeighbors(Node currentNode) {
        List<Node> neighbors = new ArrayList<Node>();
        for (Channel channel : currentNode.getChannels()) {
            Node node = findNodeById(channel.getNode2Id(), allNodes);
            if (!settledNodes.contains(node)) {
                neighbors.add(node);
            }
        }
        return neighbors;
    }

    private Node getMinimumNode() {

        Node minimum = null;
        for (Node node : unsettledNodes) {
            if (minimum == null) {
                minimum = node;
            } else {
                if (getShortestDistance(node) < getShortestDistance(minimum)) {
                    minimum = node;
                }
            }

        }
        return minimum;
    }

    private Integer getShortestDistance(Node destinationNode) {

        Integer d = distance.get(destinationNode);
        if (d == null) {
            d = Integer.MAX_VALUE;
            return d;
        }
        return d;


    }

}
