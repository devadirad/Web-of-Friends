package fg;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class Friend {
	public int vertexNum;
	public Friend next;
	public Friend(int vnum, Friend frnd) {
		this.vertexNum = vnum;
		next = frnd;
	}
}

class Vertex {
	String name;
	String school;
	Friend adjList;
	int distance = -1;
	Vertex path;
	
	Vertex(String name, String school, Friend adjList) {
		this.name = name;    
		this.adjList = adjList;
		this.school = school;
	}
}

public class FriendshipGraph {
	Vertex[] adjLists;

	FriendshipGraph(String file) throws FileNotFoundException{
		
		 Scanner sc = new Scanner(new File(file));
		 String line, name;
		 int pipePos, v1, v2;

		 adjLists = new Vertex[Integer.parseInt(sc.nextLine())];

		 // read vertices
		 for (int v=0; v < adjLists.length; v++) {
			line = sc.nextLine();
			adjLists[v] = getVertex(line);
		 }

		 // read edges
		 while (sc.hasNext()) {
			line = sc.nextLine();

			// find edge
			// read vertex names and translate to vertex numbers
			pipePos = line.indexOf("|");
			
			name = line.substring(0, pipePos);
			v1 = indexForName(name);
			
			name = line.substring(pipePos + 1);
			v2 = indexForName(name);

			// add v2 to front of v1's adjacency list and
			// add v1 to front of v2's adjacency list
			adjLists[v1].adjList = new Friend(v2, adjLists[v1].adjList);
			adjLists[v2].adjList = new Friend(v1, adjLists[v2].adjList);
		 }
	}

	private int indexForName(String name) {
		for (int v=0; v < adjLists.length; v++) {
			 if (adjLists[v].name.equalsIgnoreCase(name)) {
				 return v;
			 }
		 }
		return -1;
	}
	
	private Vertex getVertexForName(String name) {
		for (int v=0; v < adjLists.length; v++) {
			 if (adjLists[v].name.equalsIgnoreCase(name)) {
				 return adjLists[v];
			 }
		 }
		return null;
	}

	// get Vertex from the line read from the file example: sam|y|rutgers
	private Vertex getVertex (String line) {
		
		int pipePos = line.indexOf("|");
		int nextPipePos = line.indexOf("|", pipePos + 1);

		String name = null, ifStudent = null, school = null;
		
		// name
		name = line.substring(0, pipePos);
		
		// if student
		if (nextPipePos != -1) {
			ifStudent = line.substring(pipePos + 1, nextPipePos);			

			if ("y".equalsIgnoreCase(ifStudent)) {
				school = line.substring(nextPipePos + 1);			
			}
		}


		return ( new Vertex(name, school, null) );
	}
	
	public void unweightedBSF (String startName, String endName) {
		
		Queue<Vertex> q = new LinkedList<Vertex>();
		
		for (Vertex  v : adjLists) {
			v.distance = -1;
			v.path = null;
		}

		Vertex s = getVertexForName(startName);

		if (s != null) {
			q.add(s);
			s.distance = 0;
		}

		while( !q.isEmpty() ) {
			Vertex v = q.remove();
			
			// stop at the end name
			if (endName.equalsIgnoreCase(v.name)) {
				break;
			}

			for (Friend frnd = v.adjList; frnd != null; frnd = frnd.next) {
				
				if (adjLists[frnd.vertexNum].distance == -1) {

					adjLists[frnd.vertexNum].distance = v.distance + 1;					
					adjLists[frnd.vertexNum].path = v;
					q.add(adjLists[frnd.vertexNum]);
				}
			}
		}

		// Display the shortest path using stack
		Stack<String> sp = new Stack<String>();
		Vertex endVx = getVertexForName(endName);
		 
		for (Vertex vx = endVx; vx.path != null; vx = vx.path) {
			sp.push(vx.name);
		}

		if (sp.isEmpty()) {
			System.out.print(startName + " cannot connect to " + endName);			
		} else {
			System.out.print(startName);
		}

		while(!sp.isEmpty()) {
			System.out.print("--" + sp.pop());
		}
	}
	
	
	 public void print() {
		 System.out.println();
		 for (int v=0; v < adjLists.length; v++) {
			 System.out.print(adjLists[v].name);
			 if (adjLists[v].school != null) {
				 System.out.print("|");
				 System.out.print(adjLists[v].school);				 
			 }
			 for (Friend nbr=adjLists[v].adjList; nbr != null;nbr=nbr.next) {
				 System.out.print(" --> " + adjLists[nbr.vertexNum].name);
			 }
			 System.out.println("");
		 }
	 }

	public static void main(String[] args) {

		try {
			
			String file;
			Scanner scstdin = new Scanner(System.in);
			while (true) {
				System.out.print("\nEnter graph file name, or hit return to quit => ");

				
				file = scstdin.nextLine();
				if (file.length() == 0) {
					System.out.print(" Quit");
					return;
				}
				
				break;
			}
			
			FriendshipGraph fs = new FriendshipGraph(file);
//			TODO: Remove print() statement before submission
			fs.print();
			
			String option;

			while (true) {
				System.out.print("\nEnter an option");
				System.out.print("\n1. Students at a school");
				System.out.print("\n2. Shortest intro chain");
				System.out.print("\n3. Cliques");
				System.out.print("\n4. Connectors");
				System.out.print("\nReturn. Hit return to quit");
				System.out.print("\n");

				option = scstdin.nextLine();
				if (option.length() == 0) {
					System.out.print(" Quit");
					return;
				}
				
				switch (option) {
					case "1":
						System.out.println("Selected option: Students at a school");
						break;
		
					case "2":
						System.out.println("Selected option: Shortest intro chain");
						System.out.print("\tEnter Name1:");
						String name1 = scstdin.nextLine();
						System.out.print("\tEnter Name2:");
						String name2 = scstdin.nextLine();

						fs.unweightedBSF(name1, name2);
						System.out.println("");
						break;
					
					case "3":
						System.out.println("Selected option: Cliques at school");
						break;
					
					case "4":
						System.out.println("Selected option: Connectors");
						break;
				}
			}

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
