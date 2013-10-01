package algorithms;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.graph.SimpleDirectedGraph;

import structures.Edge;
import structures.OwnEdgeFactory;
import structures.Vertex;

public class AlgorithmsDominators {
	
	public AlgorithmsDominators() {
	}

	private LinkedList<Vertex> getPredecessors(Vertex v, SimpleDirectedGraph<Vertex,Edge> graph){
		LinkedList<Vertex> result = new LinkedList<Vertex>();
		Set<Edge> edgeSet = graph.incomingEdgesOf(v);
		
		for (Edge<Vertex> e : edgeSet){
			result.add(graph.getEdgeSource(e));
		}
		return result;
		
	}
	
	public void computeDominators(SimpleDirectedGraph<Vertex,Edge> graph){
		LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
		Set<Vertex> vertex = graph.vertexSet();
		Iterator<Vertex> iterator = vertex.iterator();
		
		while(iterator.hasNext()){
			vertexList.add(iterator.next());
		}
		
		for (Vertex v : vertex) {
			if (!v.isBegin()){
				v.setDominators((LinkedList<Vertex>)vertexList.clone());
			}else{
				LinkedList<Vertex> l = new LinkedList<Vertex>();
				l.add(v);
				v.setDominators(l);
			}
		}
		Boolean done = false;
		while (!done){
			done = true;
			for (Vertex v : vertex){
				int length = v.getDominators().size();
				if(!v.isBegin()){
					LinkedList<Vertex> newDom = v.getDominators();
					LinkedList<Vertex> pre = getPredecessors(v,graph);
					for (Vertex p : pre){
						newDom = intersection(newDom, p.getDominators());
					}
					v.setDominators(newDom);
					v.addDominators(v);
				}
				if(length != v.getDominators().size()){
					done = false;
				}
			}
		}
	}
	
	private LinkedList<Vertex> intersection(LinkedList<Vertex> newDom,LinkedList<Vertex> otherDoms){
		LinkedList<Vertex> result = new LinkedList<Vertex>() ;
		for (Vertex v : newDom){
			if(otherDoms.contains(v)){
				result.add(v);
			}
		}
		return result;
	}
	
	public SimpleDirectedGraph<Vertex, Edge> reverse(SimpleDirectedGraph<Vertex, Edge> graph){
		OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
		SimpleDirectedGraph<Vertex, Edge> reverseGraph = new SimpleDirectedGraph<Vertex, Edge>(f);
		
		for (Vertex v : graph.vertexSet()){
			reverseGraph.addVertex(v);
		}
		System.out.println("add vertex");
		
		Set<Edge> set = graph.edgeSet();
		Vertex source;
		Vertex target;
		for (Edge<Vertex> e : set){
			System.out.println(""+graph.edgeSet());
			source = graph.getEdgeTarget(e);
			target = graph.getEdgeSource(e);
			reverseGraph.addEdge(source,target);
			//reverseGraph.addEdge((Vertex)e.getTarget(), (Vertex)e.getSource());
		}
		return reverseGraph;
		
	}
	
	
}
