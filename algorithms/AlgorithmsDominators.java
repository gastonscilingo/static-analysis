package algorithms;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.graph.SimpleDirectedGraph;

import structures.Edge;
import structures.Vertex;

public class AlgorithmsDominators {

	SimpleDirectedGraph<Vertex,Edge<Vertex>> graph;
	
	private LinkedList<Vertex> getPredecessors(Vertex v){
		LinkedList<Vertex> result = new LinkedList<Vertex>();
		Set<Edge<Vertex>> edgeSet = graph.incomingEdgesOf(v);
		
		for (Edge<Vertex> e : edgeSet){
			result.add(graph.getEdgeSource(e));
		}
		return result;
		
	}
	
	private Set<Vertex> getDominators(){
		LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
		Set<Vertex> vertex = graph.vertexSet();
		Iterator<Vertex> iterator = vertex.iterator();
		
		while(iterator.hasNext()){
			vertexList.add(iterator.next());
		}
		
		for (Vertex v : vertex) {
			v.setDominators((LinkedList<Vertex>)vertexList.clone());
		}
		//TODO here
		return null;
	}
	
}
