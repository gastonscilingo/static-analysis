package utils;

import java.io.IOException;

import org.jgrapht.graph.SimpleDirectedGraph;

import structures.Edge;
import structures.Vertex;

public class Outputs {
	
	private StringBuffer outputDotBody;
	private String graphName;

	public Outputs() {
		outputDotBody = new StringBuffer("");
	}
	
	public StringBuffer getOutputDot() {
		StringBuffer outputDot = new StringBuffer("digraph "+graphName+" {\n");
		outputDot.append(outputDotBody) ;
		outputDot.append("}\n");
		return outputDot;
	}

	public StringBuffer getOutputBody() {
		return outputDotBody;
	}
	
	
	public void writeGraph(Vertex a , Vertex b,String color){
		  
	  	outputDotBody.append("\""+a.getNum()+". "+a.getLine()+"\""+ "  ->  " + "\""+b.getNum()+". "+b.getLine() +"\""+"[color="+color+"]"+ "\n");
	  
	  }
	
	public void writeGraphWithColor(Vertex a,String color){
		  
	  	outputDotBody.append("\""+a.getNum()+". "+a.getLine()+"\""+ " [style=filled, fillcolor="+color+"];"+ "\n");
	  
	  }

	public void flushOutputDot(String newName) {
		graphName = newName;
		outputDotBody = new StringBuffer(""); 
	}
	
	public void printDomitators(SimpleDirectedGraph<Vertex,Edge> graph) {
		for (Vertex v : graph.vertexSet()) {
			System.out.println("Dom("+v.toString()+")="+v.getDominators());
		}
	}
	
	public void printIDomitators(SimpleDirectedGraph<Vertex,Edge> graph) {
		for (Vertex v : graph.vertexSet()) {
			System.out.println("Dom("+v.toString()+")="+v.getiDominators());
		}
	}

	public void printGraph(SimpleDirectedGraph<Vertex,Edge> graph){
		System.out.println("\n	Graph : \n");
		System.out.println("Vertex : "+graph.vertexSet().toString());
		System.out.println("Edges : "+graph.edgeSet());
		System.out.println(graph.toString());
	}
	
	/*
	 * This function takes the name of the dot (without extension) and generates
	 * its corresponding -jpg file.   
	 */
	public void dot2image(String fileName){
		String dotPath;
		if(System.getProperty("os.name").startsWith("MAC"))
			dotPath = "/usr/local/bin/dot";
		else
			dotPath = "/usr/bin/dot";				
		
		try {
			
			Runtime.getRuntime().exec(dotPath+" -T jpg -o "+fileName+".jpg "+fileName+".txt");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void openImage(String fileName){
		String fileViewer;
		
		if(System.getProperty("os.name").startsWith("MAC"))
			fileViewer = "open";
		else
			fileViewer = "shotwell";
		
		try {
			Runtime.getRuntime().exec(fileViewer+" "+fileName+".jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
