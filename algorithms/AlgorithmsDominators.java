package algorithms;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.graph.SimpleDirectedGraph;

import structures.Edge;
import structures.OwnEdgeFactory;
import structures.Vertex;
import structures.VertexType;

public class AlgorithmsDominators {
	
	static StringBuffer outputDot;
	
	
	
	public static StringBuffer getOutputDot() {
		return outputDot;
	}

	public AlgorithmsDominators() {
		outputDot = new StringBuffer("digraph name {\n");
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
		Set<Vertex> vertexs = graph.vertexSet();
		Iterator<Vertex> iterator = vertexs.iterator();
		
		while(iterator.hasNext()){
			vertexList.add(iterator.next());
		}
		
		for (Vertex v : vertexs) {
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
			for (Vertex v : vertexs){
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
			if(v.isBegin()){
				v.setType(VertexType.END);
			}else{
				if(v.isEnd()){
					v.setType(VertexType.BEGIN);
				}
			}
			reverseGraph.addVertex(v);
		}
		System.out.println("finished add vertexs");
		
		Set<Edge> set = graph.edgeSet();
		Vertex source;
		Vertex target;
		System.out.println(" "+set);
		for (Edge<Vertex> e : set){
			System.out.println("edge : "+e);
			source = graph.getEdgeTarget(e);
			System.out.println("edge : "+source);
			target = graph.getEdgeSource(e);
			System.out.println("edge : "+target);
			reverseGraph.addEdge(source,target);
		}
		System.out.println("reverse graph "+reverseGraph.edgeSet());
		return reverseGraph;
		
	}
	
	public void computeidominator(SimpleDirectedGraph<Vertex,Edge> graph) throws Exception{
		LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
		Set<Vertex> vertexs = graph.vertexSet();
		
		for (Vertex v : vertexs){
			LinkedList<Vertex> myDoms = v.getSDominators();
			System.out.println("myDoms size : "+myDoms.size());
			int i = 0;
			while(i<myDoms.size()){
				Vertex dom = myDoms.get(i);
				LinkedList<Vertex> intersectList =  intersection(myDoms,dom.getSDominators());
				System.out.println("myDoms : "+myDoms);
				myDoms.removeAll(intersectList);
				System.out.println("myDoms : "+myDoms);
				i++;
			}
			if(myDoms.size()>1){
				System.out.println("Bad");
				throw new Exception("there more than one idominators"); 
			}else{
				if(myDoms.size()==1)
					v.setiDominators(myDoms.getFirst());
			}
		}
	}
	
	public SimpleDirectedGraph<Vertex, Edge> computeDominatorsTree(SimpleDirectedGraph<Vertex, Edge> graph){
		OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
		SimpleDirectedGraph<Vertex, Edge> dominatorsTree = new SimpleDirectedGraph<Vertex, Edge>(f);
		
		
		
		for (Vertex v : graph.vertexSet()){
			if(v.getiDominators()!=null){
				dominatorsTree.addVertex(v);
				dominatorsTree.addVertex(v.getiDominators());
				dominatorsTree.addEdge(v.getiDominators(),v);
				writeGraph(v.getiDominators(),v);
			}
		}
		outputDot.append("}\n");	
		
		
		return dominatorsTree;
	}
	
	private static void writeGraph(Vertex a , Vertex b){
		  
	  	outputDot.append("\""+a.getNum()+". "+a.getLine()+"\""+ "  ->  " + "\""+b.getNum()+". "+b.getLine() +"\""+ "\n");
	  
	  }
	  
	
	
	
}
