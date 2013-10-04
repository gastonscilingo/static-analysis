import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import algorithms.AlgorithmsDominators;

import parser.ProgramParser;

import structures.Edge;
import structures.OwnEdgeFactory;
import structures.Vertex;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		
		File inputScenarioFile = new File(args[0]);
		FileReader fr;
		ProgramParser scenarioParser;
		SimpleDirectedGraph<Vertex,Edge> graph;
		StringBuffer dotFile;
		try {
			//graph = new SimpleDirectedGraph<String,Edge>(Edge.class);
			OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
			graph = new SimpleDirectedGraph<Vertex,Edge>(f);
			fr = new FileReader(inputScenarioFile);
			scenarioParser = new ProgramParser(fr);
			ProgramParser.init(fr);
			
			dotFile = new StringBuffer("digraph name {\n");
			scenarioParser.parseProgram(graph,dotFile);
			dotFile.append("}\n");
			
			
			// Print graph
			System.out.println("\n	Graph : \n");
			System.out.println("Vertex : "+graph.vertexSet().toString());
			System.out.println("Edges : "+graph.edgeSet().toString());
			
			System.out.println(graph.toString());
			

			/* Write output file*/
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter("graph.txt");
				fileWriter.write(dotFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// TODO add your handling code here:
			//Process p = Runtime.getRuntime().exec("dot -T jpg -o graph.jpg graph.txt");
			Process p = Runtime.getRuntime().exec("/opt/local/bin/dot -T jpg -o graph.jpg graph.txt");
	    	InputStream in = p.getInputStream();
	    	InputStreamReader inread = new InputStreamReader(in);
	    	BufferedReader bufferedreader = new BufferedReader(inread);
	    	
	    	// Check for failure
			if (p.waitFor() != 0) {
				System.out.println("exit value = " + p.exitValue());
			}
			System.out.println("Salida : "+bufferedreader.readLine());
			p = Runtime.getRuntime().exec("open graph.jpg");
			
			// Compute Dominators and Post Dominators and show
			AlgorithmsDominators algorithmsDominator = new AlgorithmsDominators ();
			
			algorithmsDominator.computeDominators(graph);
			
			
			// Compute Dominator
			System.out.println("DOMINADORES:");
			for (Vertex v : graph.vertexSet()) {
				System.out.println("Dom("+v.toString()+")="+v.getDominators());
			}
			
			// Compute Post-Dominator
			SimpleDirectedGraph<Vertex,Edge> reverseGraph = algorithmsDominator.reverse(graph);
			algorithmsDominator.computeDominators(reverseGraph);
			System.out.println("POST-DOMINADORES:");
			for (Vertex v : reverseGraph.vertexSet()) {
				System.out.println("Dom("+v.toString()+")="+v.getDominators());
			}
			
			System.out.println("Inmediate POST-DOMINADORES:");
			algorithmsDominator.computeidominator(reverseGraph);
			for (Vertex v : reverseGraph.vertexSet()) {
				System.out.println("iPostDom("+v.toString()+")="+v.getiDominators());
			}
			
			algorithmsDominator.computeDominatorsTree(reverseGraph);
			
			StringBuffer treeFile = algorithmsDominator.getOutputDot();
			
			/* Write output file*/
			try {
				fileWriter = new FileWriter("tree.txt");
				fileWriter.write(treeFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// TODO add your handling code here:
			//Process p = Runtime.getRuntime().exec("dot -T jpg -o graph.jpg graph.txt");
			p = Runtime.getRuntime().exec("/opt/local/bin/dot -T jpg -o tree.jpg tree.txt");
	    	in = p.getInputStream();
	    	inread = new InputStreamReader(in);
	    	bufferedreader = new BufferedReader(inread);
	    	
	    	// Check for failure
			if (p.waitFor() != 0) {
				System.out.println("exit value = " + p.exitValue());
			}
			System.out.println("Salida : "+bufferedreader.readLine());
			p = Runtime.getRuntime().exec("open tree.jpg");
			
			
			
			
			
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (parser.ParseException e) {
			e.printStackTrace();
			System.out.println("error in parsing program !!!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}

}
