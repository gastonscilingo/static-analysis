import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Set;


import org.jgraph.graph.DefaultEdge;
import org.jgrapht.alg.ConnectivityInspector;
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
		boolean macOS = false;
		if (args.length == 2 ){
			if (args[1].equals("-macOS"))
				macOS = true;
		}else{
			System.out.println("use with 2 params for Mac OS: <file path> <-macOS>");
		}
			
		File inputScenarioFile = new File(args[0]);
		FileReader fr;
		ProgramParser scenarioParser;
		SimpleDirectedGraph<Vertex,Edge> graph;
		SimpleDirectedGraph<Vertex,Edge> reverseGraph;
		SimpleDirectedGraph<Vertex,Edge> dominatorsTree;
		SimpleDirectedGraph<Vertex,Edge> cdg;
		SimpleDirectedGraph<Vertex,Edge> ddg;
		//SimpleDirectedGraph<Vertex,Edge> ddg; never made only written .
		StringBuffer dotFile;
		StringBuffer pdg = new StringBuffer("digraph pdg {\n");
		try {
			OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
			graph = new SimpleDirectedGraph<Vertex,Edge>(f);
			fr = new FileReader(inputScenarioFile);
			scenarioParser = new ProgramParser(fr);
			ProgramParser.init(fr);
			
			// Parse program and write CFG in dot file
			dotFile = new StringBuffer("digraph name {\n");
			scenarioParser.parseProgram(graph,dotFile);
			dotFile.append("}\n");
			/* Write output file*/
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter("graph.txt");
				fileWriter.write(dotFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
						
			// Print graph for debug
			//printGraph(graph);
			
			// Run dot program to generate image and show it
			Process p;
			if (!macOS){
				p = Runtime.getRuntime().exec("/usr/bin/dot -T jpg -o graph.jpg graph.txt");
			}else{
				p = Runtime.getRuntime().exec("/opt/local/bin/dot -T jpg -o graph.jpg graph.txt");
			}
			
	    	// Check for failure
			if (p.waitFor() != 0) {
				System.out.println("exit value = " + p.exitValue());
			}
			
			
			// Compute Dominator
			AlgorithmsDominators algorithmsDominator = new AlgorithmsDominators ();
			algorithmsDominator.computeDominators(graph);
			//System.out.println("DOMINADORES:");
			//printDomitators(graph);
			
			// Compute Post-Dominator
			reverseGraph = algorithmsDominator.reverse(graph);
			algorithmsDominator.computeDominators(reverseGraph);
			//System.out.println("POST-DOMINADORES:");
			//printDomitators(graph);

			// Compute Immediate Dominators
			algorithmsDominator.computeidominator(reverseGraph);
			//System.out.println("Inmediate POST-DOMINADORES:");
			printIDomitators(reverseGraph);
			
			// Compute Post Dominators Tree and write dot file
			dominatorsTree = algorithmsDominator.computeDominatorsTree(reverseGraph);
			StringBuffer treeFile = algorithmsDominator.getOutputDot();
			try {
				fileWriter = new FileWriter("tree.txt");
				fileWriter.write(treeFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Run dot program to generate image and show it
			if (!macOS){
				p = Runtime.getRuntime().exec("/usr/bin/dot -T jpg -o tree.jpg tree.txt");
			}else{
				p = Runtime.getRuntime().exec("/opt/local/bin/dot -T jpg -o tree.jpg tree.txt");
			}
	    	// wait for failure
			if (p.waitFor() != 0) {
				System.out.println("exit value = " + p.exitValue());
			}
			
			
			
			// step a: compute set S
			LinkedList<Edge<Vertex>> S = algorithmsDominator.edgesNotAncestralsInTree(graph, dominatorsTree);
			
			cdg = algorithmsDominator.computeControlDependenceGraph(graph, dominatorsTree);
			StringBuffer cdgFile = algorithmsDominator.getOutputDot();
			pdg.append(algorithmsDominator.getOutputBody());
			try {
				fileWriter = new FileWriter("cdg.txt");
				fileWriter.write(cdgFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			algorithmsDominator.computeReachigDefinitios(graph);
			
			
			
			// Run dot program to generate image and show it
			if(!macOS){
				p = Runtime.getRuntime().exec("/usr/bin/dot -T jpg -o cdg.jpg cdg.txt");
			}else{
				p = Runtime.getRuntime().exec("/opt/local/bin/dot -T jpg -o cdg.jpg cdg.txt");
			}

	    	// Check for failure
			if (p.waitFor() != 0) {
				System.out.println("exit value = " + p.exitValue());
			}
			
			
			

			algorithmsDominator.computeAvailableExpressions(graph);
			algorithmsDominator.showAvailableExpressions(graph);
			algorithmsDominator.showReachingDefinitions(graph);

			ddg = algorithmsDominator.computeDataDependenceGraph(graph);
			
			
			StringBuffer ddgFile = algorithmsDominator.getOutputDot();
			pdg.append(algorithmsDominator.getOutputBody());
			pdg.append("}\n");
			try {
				fileWriter = new FileWriter("ddg.txt");
				fileWriter.write(ddgFile.toString());
				fileWriter.close();
				fileWriter = new FileWriter("pdg.txt");
				fileWriter.write(pdg.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(!macOS){
				p = Runtime.getRuntime().exec("/usr/bin/dot -T jpg -o ddg.jpg ddg.txt");
			}else{
				p = Runtime.getRuntime().exec("/opt/local/bin/dot -T jpg -o ddg.jpg ddg.txt");
			}
			
			
			if(!macOS){
				p = Runtime.getRuntime().exec("/usr/bin/dot -T jpg -o pdg.jpg pdg.txt");
			}else{
				p = Runtime.getRuntime().exec("/opt/local/bin/dot -T jpg -o pdg.jpg pdg.txt");
			}
			
			
			
			Vertex s = algorithmsDominator.getVertexByNum(cdg.vertexSet(),8);
			System.out.print("Selected : "+s.toString());
			algorithmsDominator.computeSlice(cdg, ddg, s);
			
			StringBuffer sliceGraph = algorithmsDominator.getOutputDot();
			try {
				fileWriter = new FileWriter("slice.txt");
				fileWriter.write(sliceGraph.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(!macOS){
				p = Runtime.getRuntime().exec("/usr/bin/dot -T jpg -o slice.jpg slice.txt");
			}else{
				p = Runtime.getRuntime().exec("/opt/local/bin/dot -T jpg -o slice.jpg slice.txt");
			}
			
			
			if (!macOS){
				p = Runtime.getRuntime().exec("shotwell graph.jpg");
			}else{
				p = Runtime.getRuntime().exec("open graph.jpg");
			}
			
			if (!macOS){
				p = Runtime.getRuntime().exec("shotwell tree.jpg");
			}
			else{
				p = Runtime.getRuntime().exec("open tree.jpg");
			}
			
			if (!macOS){
				p = Runtime.getRuntime().exec("shotwell cdg.jpg");
			}
			else{
				p = Runtime.getRuntime().exec("open cdg.jpg");
			}
			
			if (!macOS){
				p = Runtime.getRuntime().exec("shotwell ddg.jpg");
			}
			else{
				p = Runtime.getRuntime().exec("open ddg.jpg");
			}
			
			
			if (!macOS){
				p = Runtime.getRuntime().exec("shotwell pdg.jpg");
			}
			else{
				p = Runtime.getRuntime().exec("open pdg.jpg");
			}
			
			if (!macOS){
				p = Runtime.getRuntime().exec("shotwell slice.jpg");
			}
			else{
				p = Runtime.getRuntime().exec("open slice.jpg");
			}
			
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (parser.ParseException e) {
			e.printStackTrace();
			System.out.println("error in parsing program !!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private static void printDomitators(SimpleDirectedGraph<Vertex,Edge> graph) {
		for (Vertex v : graph.vertexSet()) {
			System.out.println("Dom("+v.toString()+")="+v.getDominators());
		}
	}
	
	private static void printIDomitators(SimpleDirectedGraph<Vertex,Edge> graph) {
		for (Vertex v : graph.vertexSet()) {
			System.out.println("Dom("+v.toString()+")="+v.getiDominators());
		}
	}

	static void printGraph(SimpleDirectedGraph<Vertex,Edge> graph){
		System.out.println("\n	Graph : \n");
		System.out.println("Vertex : "+graph.vertexSet().toString());
		System.out.println("Edges : "+graph.edgeSet());
		System.out.println(graph.toString());
	}
	
	
	
	
	
}
