

import java.util.Random;
import java.util.Stack;
import java.text.DecimalFormat;
import java.lang.Math;

/**
 * @author Dyangelo Grullon (dag4202)
 * The class to be used in future implementations of the Traveling Salesman Problem to represent a completely
 * connected euclidean graph.
 */
public class Graph {
	private double[][] graph; //the weighted matrix where the index values represent vertex number and the stored
										//value is the distance between those two vertices in the graph
	private int N; //The number of vertices
	private int E; //The number of edges
	private int[][] vertices; // Represented as an array of arrays holding x,y pairs in order of vertex number
	private Edge[] edges; //Encapsulates the edges in an array of edges
	/**
	 * Builder function for a matrix representing a completely connected euclidean graph. 
	 * @param N the number of vertices
	 * @param seed the seed used to randomly generate the x and y coordinates for this graph
	 * @return a completely connected euclidean graph 
	 */
	public  Graph(int N, long seed){
		this.graph = new double[N][N];
		this.N = N;
		this.E = (N * (N-1))/2;
		this.vertices = new int[N][2]; //second array is for coordinates
		this.edges = new Edge[E];
		Random generatorX = new Random(seed);
		Random generatorY = new Random(seed * 2);
		int count = 0; //Vertex Count
		boolean[] visited = new boolean[N]; //keeps track of x coordinates between 0 and N-1 that have already been created
		while(count < N){
			int x = generatorX.nextInt(N); 
			int y = generatorY.nextInt(N);
			if(!visited[x]){
				vertices[count][0]=x;
				vertices[count][1] = y;
				
				count++;
				visited[x] = true;
			}
		}
		
		double distance;
		count = 0;
		for (int vertex1 = 0; vertex1 < N; vertex1++){
			for(int vertex2=vertex1 + 1; vertex2 < N; vertex2++){
				distance = Math.sqrt(Math.pow(vertices[vertex1][0]-vertices[vertex2][0],2) +
						Math.pow((vertices[vertex1][1]-vertices[vertex2][1]), 2)); //Computes Euclidean distance
				this.graph[vertex1][vertex2] = distance; 
				this.graph[vertex2][vertex1] = distance;
				this.edges[count] = new Edge(vertex1, vertex2, distance); //New for part 2, appends an edge to edge array
				count++;
			}
		}
		
		
		
	}

	/**
	 * Publicly accessible method which updates the graph data structure with 
	 * specific edges, based on an array of edges. Resets the graph before
	 * adding the edges
	 * @param edges an array of edges 
	 */
	public void updateGraph(Edge[] edges){
		this.graph = new double[N][N];
		this.edges = edges;
		for (Edge edge : edges){
			graph[edge.getRow()][edge.getCol()] = edge.getWeight();
			graph[edge.getCol()][edge.getRow()] = edge.getWeight();
		}
	}
	
	/**
	 * Publicly accessible method which updates the graph data structure with
	 * a specific vertex.  
	 * @param vertex
	 */
	public void updateGraph(Vertex vertex){
		graph[vertex.id][vertex.parent] = vertex.weight;
		graph[vertex.parent][vertex.id] = vertex.weight;
	}
	
	/**
	 * Constructs an empty graph
	 * @param N Space allocated
	 */
	public Graph(int N){
		this.graph = new double[N][N];
		this.N = N;
		this.edges = null;
		this.vertices = null;
		this.E = 0;
	}
	
	/**
	 * Publicly accessible method which retrieves all edges in the graph and 
	 * returns them in an array of Edge types
	 * @return an array of edges
	 */
	public Edge[] getEdges(){
		return edges;
	}
	
	
	/**
	 * Publicly accessible method which returns the vertices of the graph
	 * @return an array holding arrays of [x,y] pairs
	 */
	public int[][] getVertices(){
		return this.vertices;
		
	}
	
	/**
	 * Publicly accessible method which prints the vertices
	 */
	public void printVertices(){
		System.out.println("X-Y Coordinates:");
		for (int i = 0; i < N; i++){
			System.out.printf("v%d: (%d,%d) ", i, vertices[i][0], vertices[i][1]);
		}
		System.out.println();
	}
	
	/**
	 * Publicly accessible method which retrieves the distance between two vertices
	 * @param v1 a vertex number
	 * @param v2 a second vertex number
	 * @return the distance between both points
	 */
	public double getWeight(int v1, int v2){
		return graph[v1][v2];
	}
	/**
	 * Publicly accessible method which returns the number of vertices
	 * @return N the number of vertices
	 */
	public int getN(){
		return this.N;
	}
	
	/**
	 * Publicly accessible method which prints a formatted representation of the matrix
	 */
	public void printMatrix(){
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("\nAdjacency matrix of graph weights:\n");
		for(int x = 0; x < N; x++){ 
			System.out.printf("      %d",x);
		}
		System.out.println("\n");
		for(int x=0; x < N; x++){
			System.out.printf("%d  ",x);
			for (int xtwo = 0; xtwo < N; xtwo++){
				System.out.printf(" %s  ",df.format(graph[x][xtwo]));
			}
			System.out.println("\n");
		}
		
	}

	/**
	 * An iterative approach to the depth first search algorithm which 
	 * finds the first-encountered path in the graph data structure
	 * @param start the start vertex 
	 * @return the path in an array of int types
	 */
	public int[] DFS(int start){
		boolean[] discovered = new boolean[N];
		int[] path = new int[N + 1];
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(start);
		int v;
		int count = 0;
		while(!stack.isEmpty()){
			v = stack.pop();
			if (!discovered[v]){
				path[count++] = v;
				discovered[v] = true;
				for (int i = N -1; i >= 0; i--){
					if( graph[v][i] == 0.0 ){
						continue;
					}
					stack.push(i);
				}
			}
		}
		return path;
	}
	
	/**
	 * Publicly accessible method to get the x coordinate of a vertex 
	 * @param vid The vertex id
	 * @return The vertex x-coordinate
	 */
	public int getXcoor(int vid){
		return vertices[vid][0];
	}
	
	/**
	 * Prints a vertex in a certain format
	 * @param v	The vid.
	 */
	public void printVertex(int v){
		System.out.printf("v%d: (%d,%d) ", v, vertices[v][0], vertices[v][1]);
	}
}

