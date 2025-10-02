# McDiver

# Problem

The problem statement is as follows: 
The London Sewer System is well known for its complex web of underground tunnels. In this project, you will model and navigate this sewer network, represented as a graph. Our protagonist, McDiver, is tasked with finding a special power ring that has been hidden in the sewer. The job is to navigate the maze to find the ring. However, this does not have to be a completely blind search. Fortunately, there is a detector that can measure the distance to the ring, so the McDiver can tell when they are getting closer.

When McDiver finds the ring, it turns out that it is booby-trapped. McDiver is dropped into a lower level of tunnels where there is additional treasure sprinkled all through the maze. Fortunately, McDiver immediately finds the map to this lower level and wants to collect some treasure on the way out. But toxic fumes in the sewer are starting to take a toll. So the goal is to head to the exit in the prescribed number of steps while still picking up as much treasure as possible along the way — quickly enough to avoid falling victim to those noxious fumes!

# Implementation

The goal of the first stage is to navigate the blind maze and find the exit, with the current distance to the exit being the only information present. This involved a graph traversal. The implementation used in this solution utilized a Depth-First Search approach until the exit (ring) was found.

The second stage involved getting out of the sewer with a limited number of steps, but collecting as many treasures as possible beforehand. In this stage, the graph is known, along with the location and value of all the treasures and exit. Since the graph is known, this implementation used dijkstra's algorithm to find the distances to items. In order to optimize treasure value, the solution in this code operated as such: 
- Check if McDiver needs to go to the exit now, or if he has remaining steps available to take.
- If he has availible steps, find the treasure with the "highest" value in that radius and move there (picking up additional treasures along the way if present).
- Repeat unitl McDiver has to exit

The implementation of these items are in the /src/diver/McDiver.java file of this repo. Thanks for visiting!
