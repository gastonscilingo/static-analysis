
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;




import org.jgrapht.graph.SimpleDirectedGraph;

import algorithms.Algorithms;

import parser.ProgramParser;

import structures.Edge;
import structures.OwnEdgeFactory;
import structures.Vertex;
import structures.AnalysisType;

import utils.Outputs;

public class Main {

	static boolean macOS = false;
	static int lineNumberToSlice;
	static AnalysisType analysisType;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String filePath = parsingParameters(args);

		if (filePath==null)
			return;

		File inputScenarioFile = new File(filePath);
		FileReader fr;
		ProgramParser scenarioParser;
		SimpleDirectedGraph<Vertex,Edge> graph;
		SimpleDirectedGraph<Vertex,Edge> reverseGraph;
		SimpleDirectedGraph<Vertex,Edge> dominatorsTree;
		SimpleDirectedGraph<Vertex,Edge> cdg;
		SimpleDirectedGraph<Vertex,Edge> ddg;
		//SimpleDirectedGraph<Vertex,Edge> ddg; never will make, only will write in file
		Outputs out = new Outputs();
		StringBuffer dotFile = new StringBuffer("");
		StringBuffer pdg = new StringBuffer("digraph pdg {\n");
		
		try {
			OwnEdgeFactory<Vertex,Edge> f = new OwnEdgeFactory<Vertex,Edge>(Edge.class);
			graph = new SimpleDirectedGraph<Vertex,Edge>(f);
			fr = new FileReader(inputScenarioFile);
			scenarioParser = new ProgramParser(fr);
			ProgramParser.init(fr);
			
			// Parse program, compute Control Flow Graph and write in dot file
			scenarioParser.parseProgram(graph,dotFile);

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
			
			// Compute Dominator
			Algorithms algorithms = new Algorithms (out);
			algorithms.computeDominators(graph);
			
			// Compute Post-Dominator
			reverseGraph = algorithms.reverse(graph);
			algorithms.computeDominators(reverseGraph);

			// Compute Immediate Dominators
			algorithms.computeidominator(reverseGraph);
			
			// Compute Post Dominators Tree and write dot file
			dominatorsTree = algorithms.computeDominatorsTree(reverseGraph);
			StringBuffer treeFile = out.getOutputDot(); // algorithmsDominator.getOutputDot();
			try {
				fileWriter = new FileWriter("tree.txt");
				fileWriter.write(treeFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Compute Control Dependence Graph and write dot file
			cdg = algorithms.computeControlDependenceGraph(graph, dominatorsTree);
			StringBuffer cdgFile = out.getOutputDot();
			pdg.append(out.getOutputBody());
			try {
				fileWriter = new FileWriter("cdg.txt");
				fileWriter.write(cdgFile.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			algorithms.computeReachigDefinitios(graph);
			algorithms.computeAvailableExpressions(graph);
			
			//algorithmsDominator.showAvailableExpressions(graph);
			//algorithmsDominator.showReachingDefinitions(graph);

			ddg = algorithms.computeDataDependenceGraph(graph);
			
			StringBuffer ddgFile = out.getOutputDot();
			pdg.append(out.getOutputBody());
			StringBuffer sliceGraph = new StringBuffer(pdg.toString());
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
			
			Vertex s = algorithms.getVertexByNum(cdg.vertexSet(),lineNumberToSlice);
			if (analysisType == AnalysisType.SG){
				System.out.println("Selected node to doing slicing: "+s.toString());
			}
			algorithms.computeSlice(cdg, ddg, s);
			
			sliceGraph.append(out.getOutputBody());
			sliceGraph.append("}\n");
			try {
				fileWriter = new FileWriter("slice.txt");
				fileWriter.write(sliceGraph.toString());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			// Run dot program to generate image and show it
			Process p;
			
			// Run dot program to generate image and show it
			p = out.dot2image("graph");
			p = out.dot2image("tree");
			p = out.dot2image("cdg");
			p = out.dot2image("ddg");
			p = out.dot2image("pdg");
			p = out.dot2image("slice");
			
	    	// Check for failure
			if (p.waitFor() != 0) {
				System.out.println("exit value = " + p.exitValue());
			}
			
			
			switch (analysisType){
				case SG		:	{out.openImage("slice"); break;}
				case CFG	:	{out.openImage("graph");break;}
				case PDT	:	{out.openImage("tree");break;}
				case IPDT	:	{out.printIDomitators(reverseGraph);break;}
				case CDG	:	{out.openImage("cdg");break;}
				case DDG	:	{out.openImage("ddg");break;}
				case PDG	:	{out.openImage("pdg");break;}
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

	
	private static String parsingParameters(String [] args) {
		System.out.println("Run on : "+System.getProperty("os.name"));
		String filePath = null;
		
		if (args.length < 2) {
			System.out.println("use with at less 2 parameters: $java -jar static-analysis.jar <file path> <type of analysis>");
			System.out.println("	if use SG flag them must provide node number to slicing in a third param !");
			System.out.println("\nTypes of analysis flags: CFG PDT IPDT CDG DDG PDG SG\n");
			return null;
		}
		
		if (args.length  >= 2) {
			if (!args[0].contains(".txt")){
				System.out.println("wrong file name, provide a text file (.txt) with code for analyze");
				return null;
			}else{
				filePath = args[0];
			}
			
			if (args[1].contains("SG")){
				analysisType = AnalysisType.SG;
				if (args.length > 2){
					try{
						lineNumberToSlice = Integer.valueOf(args[2]).intValue();
					}catch(NumberFormatException e){
						System.out.println("Third parameter must be a number");
						return null;
					}
				}else{
					System.out.println("Please select node to slice");
					return null;
				}
			}
			if (args[1].contains("CFG")){
				analysisType = AnalysisType.CFG;
			}			
			if (args[1].contains("PDT")){
				analysisType = AnalysisType.PDT;
			}
			if (args[1].contains("IPDT")){
				analysisType = AnalysisType.IPDT;
			}
			if (args[1].contains("CDG")){
				analysisType = AnalysisType.CDG;
			}	
			if (args[1].contains("DDG")){
				analysisType = AnalysisType.DDG;
			}
			if (args[1].contains("PDG")){
				analysisType = AnalysisType.PDG;
			}
			if (analysisType == null){
				System.out.println("Wrong second parameter, must be valid flag: CFG, PDT, IPDT, CDG, DDG, PDG, SG.");
				return null;
			}
		}
		return filePath;
		
	}


	
	
}
