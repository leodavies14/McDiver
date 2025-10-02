package diver;

import datastructures.PQueue;
import datastructures.SlowPQueue;
import game.*;
import graph.DirectedGraph;
import graph.ShortestPaths;

import java.util.*;


/** This is the place for your implementation of the {@code SewerDiver}.
 */
public class McDiver implements SewerDiver {

    HashSet<Long> visited = new HashSet<>();
    PQueue<Long> been = new SlowPQueue<>();


    PQueue<NodeStatus> frontier = new SlowPQueue<>();

    Long last_step;


    /** See {@code SewerDriver} for specification. */
    @Override
    public void seek(SeekState state) {
        // TODO : Look for the ring and return.
        // DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
        // Instead, write your method (it may be recursive) elsewhere, with a
        // good specification, and call it from this one.
        //
        // Working this way provides you with flexibility. For example, write
        // one basic method, which always works. Then, make a method that is a
        // copy of the first one and try to optimize in that second one.
        // If you don't succeed, you can always use the first one.
        //
        // Use this same process on the second method, scram.
        DFS_walk3(state);
    }

    public void DFS_walk(SeekState state) {
        long u = state.currentLocation();
        if(state.distanceToRing()== 0){
            return;
        }
        visited.add(u);
        for (NodeStatus x : state.neighbors()) {
            if (!visited.contains(x.getId())) {
                state.moveTo(x.getId());
                DFS_walk(state);
                if(state.distanceToRing()== 0){
                    return;
                }
                state.moveTo(u);
            }
        }
    }


    public void DFS_walk2(SeekState state) {

        //PQueue<NodeStatus> visited_distances = new SlowPQueue<>();
        long u = state.currentLocation();
        if (state.distanceToRing() == 0) {
            return;
        }
        visited.add(u);
        been.add(u, 1/(been.size()+1));
//        int visited_neighbors = 0;
//        boolean all_n_visited = false;
        for (NodeStatus x : state.neighbors()) {
            if(!visited.contains(x.getId())){
                try {
                    frontier.add(x, x.getDistanceToRing());
                }catch (IllegalArgumentException e){

                }

            }
        }

           NodeStatus next = frontier.extractMin();;

            if(!visited.contains(next.getId())){
                goToFront(state, next);
                DFS_walk2(state);
                if(state.distanceToRing()== 0){
                    return;
                }
               state.moveTo(u);
            }

    }

    public void goToFront(SeekState state, NodeStatus frontier){
        if(state.neighbors().contains(frontier)){
            state.moveTo(frontier.getId());
        }
        else{
            while (!state.neighbors().contains(frontier)){
                state.moveTo(been.extractMin());
            }
            state.moveTo(frontier.getId());
        }
    }

    public void DFS_walk3(SeekState state) {
        long u = state.currentLocation();
        if (state.distanceToRing() == 0) {
            return;
        }
        visited.add(u);
        List<NodeStatus> near = new ArrayList<>();
//        PQueue<NodeStatus> closest_unvisted = new SlowPQueue<>();
//        PQueue<NodeStatus> closest = new SlowPQueue<>();
        for (NodeStatus x : state.neighbors()) {
                near.add(x);
        }
        Collections.sort(near);
        long place = state.currentLocation();
        for( NodeStatus n : near){
            if(!visited.contains(n.getId())){
                state.moveTo(n.getId());
                DFS_walk3(state);
                if(state.distanceToRing() == 0){
                    return;
                }
                else{
                    state.moveTo(place);
                }
            }
        }
    }

    /** See {@code SewerDriver} for specification. */
    @Override
    public void scram(ScramState state) {
        // TODO: Get out of the sewer system before the steps are used up.
        // DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
        // with a good specification, and call it from this one.
        djk_with_treasure2(state);
    }
    public void djk(ScramState state){
        // Maze s = new Maze(state);
        //singleSourceDistances
        Maze bottom = new Maze((Set<Node>) state.allNodes());
        ShortestPaths sewer = new ShortestPaths<>(bottom);
        sewer.singleSourceDistances(state.currentNode());
        List<Edge>  exit_path = sewer.bestPath(state.exit());
        for(Edge e : exit_path){
            state.moveTo(e.destination());
        }
    }

    public void djk_with_treasure(ScramState state){
        Maze bottom = new Maze((Set<Node>) state.allNodes());
        ShortestPaths sewer = new ShortestPaths<>(bottom);
        sewer.singleSourceDistances(state.currentNode());

        List<Edge> max_path = find_max(state);
        while(state.stepsToGo()  > sewer.getDistance(state.exit()) + 2*max_path.get(0).length()){
            state.moveTo(max_path.get(0).destination());
            max_path.remove(0);
            if(max_path.size() == 0){
                max_path = find_max(state);
            }

            sewer.singleSourceDistances(state.currentNode());
        }


        sewer.singleSourceDistances(state.currentNode());
        List<Edge>  exit_path = sewer.bestPath(state.exit());
        for(Edge e : exit_path){
            state.moveTo(e.destination());
        }
//        return;

    }


    public void djk_with_treasure2(ScramState state){
        Maze bottom = new Maze((Set<Node>) state.allNodes());
        ShortestPaths sewer = new ShortestPaths<>(bottom);
        sewer.singleSourceDistances(state.currentNode());

        List<Edge> max_path = find_max(state);
        List<Edge> nearest_path = find_nearest(state);
        boolean found_max = false;
        while((!found_max && state.stepsToGo()  > sewer.getDistance(state.exit()) + 2*max_path.get(0).length()) || (state.stepsToGo()  > sewer.getDistance(state.exit()) + 2*nearest_path.get(0).length())){
            if(max_path.isEmpty()){
                nearest_path = find_nearest(state);
                state.moveTo(nearest_path.get(0).destination());
                nearest_path = find_nearest(state);
            }
            else{
                state.moveTo(max_path.get(0).destination());
                max_path.remove(0);
                if(max_path.isEmpty()){
                    found_max = true;
                }
            }

            sewer.singleSourceDistances(state.currentNode());
        }


        sewer.singleSourceDistances(state.currentNode());
        List<Edge>  exit_path = sewer.bestPath(state.exit());
        for(Edge e : exit_path){
            state.moveTo(e.destination());
        }
//        return;

    }

    public List<Edge> find_nearest(ScramState state){
        Maze bottom = new Maze((Set<Node>) state.allNodes());
        ShortestPaths sewer = new ShortestPaths<>(bottom);
        sewer.singleSourceDistances(state.currentNode());

        for(Node n : state.currentNode().getNeighbors()){
            if(n.getTile().coins() > 0){
                return sewer.bestPath(n);
            }
        }
        for (Node n : state.currentNode().getNeighbors()){
            for(Node e: n.getNeighbors()){
                if(e.getTile().coins() > 0){
                    return sewer.bestPath(e);
                }
            }
        }
        return find_max(state);
    }

    public List<Edge> find_max(ScramState state){
        Maze bottom = new Maze((Set<Node>) state.allNodes());
        ShortestPaths sewer = new ShortestPaths<>(bottom);
        sewer.singleSourceDistances(state.currentNode());

        int max = 0;
        Node max_val = state.currentNode();
        for(Node n : state.allNodes()){
            if(n.getTile().coins() > max){
                max_val = n;
            }
        }
        return sewer.bestPath(max_val);

    }

}
