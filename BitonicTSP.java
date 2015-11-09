import java.text.DecimalFormat;

/**
 * @author Dyangelo Grullon (dag4202)
 * Part 4 of the Traveling Salesman Problem. 
 * Finds the optimal bitonic path
 */
public class BitonicTSP {
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	/**
	 * @author Dyangelo Grullon (dag4202)
	 * Private quicksort implementation to sort an array of vertex ids
	 * in a graph by increasing x-coordinate
	 */
	private static class VertexSort {
		
		/**
		 * Determines if vid a has a smaller x-coordinate than vid b 
		 * @param a A vertex id
		 * @param b A vertex id
		 * @param graph The graph the vertices reside on
		 * @return True if 'a's x-coordinate is smaller than 'b's
		 */
		private static boolean less(int a, int b, Graph graph){
			return graph.getXcoor(a) < graph.getXcoor(b);
		}
		
		/**
		 * Swaps two integers in an array
		 * @param a The array of vertices
		 * @param i The index of the first vertex id
		 * @param j The index of the second vertex id
		 */
		private static void exch(int a[], int i, int j){
			int temp = a[i];
			a[i] = a[j];
			a[j] = temp;
		}
		
		/**
		 * Partitions the array of vertex ids.  
		 * @param a The array to partition
		 * @param lo The lower bound of the a array 
		 * @param hi The upper bound of the a array
		 * @param graph The graph the vertices reside on
		 * @return The new upper bound
		 */
		private static int partition(int a[], int lo, int hi, Graph graph){
			int i = lo, j = hi +1;
			while(true){
				while(less(a[++i], a[lo], graph)) if (i == hi) break;
				while (less(a[lo], a[--j], graph)) if (j == lo) break;
				if ( i >= j ) break;
				exch(a, i, j);
			}
			exch(a, lo, j);
			return j;
		}
		
		/**
		 * Private helper function to manage partitioning and sorting 
		 * @param a The array of vertex ids
		 * @param lo The lower bound of the vertex id array
		 * @param hi The upper bound of the vertex id array
		 * @param graph The graph of the 
		 */
		private static void sort(int [] a, int lo, int hi, Graph graph){
			if (hi <= lo) return;
			int j = partition(a, lo, hi, graph);
			sort(a, lo, j-1, graph);
			sort(a, j+1, hi, graph);
		}
		
		/**
		 * Publicly accessible sorting function to sort an array of vids
		 * @param a The array of vids
		 * @param graph The graph the vids reside on
		 */
		public static void sort(int [] a, Graph graph){
			sort(a, 0, a.length - 1, graph);
		}
	}
	
	/**
	 * Private implementation of the dynamic programming 
	 * solution to the traveling salesman problem. Finds 
	 * the bitonic tour.
	 * @author Dyangelo Grullon (dag4202)
	 */
	private static class TourFinder {
		private double[][] L; //The L-Table
		private int[][] N; //The N-Table
		private int n; //The number of vertices
		private int[] vertices; //The array of sorted vertices
		Graph graph; //The graph the vertices reside on
		
		/**
		 * Initializes the tour finder.
		 * @param graph  The graph 
		 * @param n The number of vertices
		 */
		public TourFinder(Graph graph, int n){
			this.graph = graph;
			this.n = n;
		}
		
		/**
		 * Initializes the N and L table
		 * The N table is initialized to -1 values
		 * The L table is initialized to 0.0
		 * @param n the number of vertices
		 */
		private void initTables(int n){
			L = new double[n][n];
			N = new int[n][n];
			for (int i = 0; i < n; i++){
				N[i][i] = -1;
				for (int j = i + 1; j < n; j++){
					N[i][j] = -1;
					N[j][i] = -1;
				}
			}
			
		}
		/**
		 * Populates the N-table and L-table with the information needed
		 * to find the bitonic tour.
		 * @param vertices The array of vertices.
		 */
		public void findTour(int[] vertices ){
			this.vertices = vertices; //stores the vertices for later use
			initTables(n); //Initialize the tables
			for (int j = 1; j < n; j++){ //for all j: 1..n-1
				for (int i = 0; i < j; i++){ //for all i: 0..j-1
					if (i == 0 && j==1) { //if the first row of the N and L table
						L[i][j] = graph.getWeight(vertices[i], vertices[j]); //populate L -table with the distance between 
																	//vertex i and j in the array of vids
						N[i][j] = i; //Make the neighbor of j, i, in this path
					} else if (j > i + 1){ 
						L[i][j] = L[i][j-1] + graph.getWeight(vertices[j-1], vertices[j]);
						N[i][j] = j - 1;
					} else { 
						L[i][j] = Double.POSITIVE_INFINITY;
						for (int k = 0; k < i; k++){
							double q = L[k][i] + graph.getWeight(vertices[k], vertices[j]);
							if (q < L[i][j]){
								L[i][j] = q;
								N[i][j] = k;
							}
						}
					}
				}
			}
		}
		/**
		 * Backtraces the N table to find the bitonic path
		 * @return The path in the proper order with values converted to vids
		 */
		public int[] backTrace(){
			int k = 0; //Refers to the current stack in pathHold
			int i = n-2; //Start backtrace from the right-most vertex and its immediate neighbor
			int j = n - 1; 
			int[][] pathHold = new int[2][n]; //the left-to-right/right-to-left "stacks"
			int[] cur = new int[2]; //the current position in both stacks is stored in cur
			int[] tempResult = new int[n]; //The path as an array of indices in the vertices array
			while ( j > 0 ){ //While the current path end-point is > 0
				pathHold[k][cur[k]++] = j; //add the end-point to the appropriate path (init: left-to-right)
				j = N[i][j]; //get the neighbor to j in the bitonic path
				if (j < i){ //if the right endpoint is actually a left-endpoint 
					int temp = i; //then i is going in the reverse direction
					i = j; //swap i and j (to look at N[i][j])
					j = temp;
					k = 1 - k; //toggle the appropriate stack to add j to
				}
			}
			
			pathHold[0][cur[0]++] = 0; //add 0 to the left-to-right path 
			while (cur[1] > 0){ //while the right-to-left stack is not empty
				pathHold[0][cur[0]++] = pathHold[1][--cur[1]]; //add the right-to-left elements in the correct place on the path
			}
			int zeroIndex = 0; //used to find the index of the vertex with id: 0
			for (int a = 0; a < n; a++){ //for every vertex in the path
				tempResult[a] = vertices[pathHold[0][--cur[0]]]; //convert to a vertex id and add the vertex to a temp array
				if (tempResult[a] == 0){
					zeroIndex = a; //identifies the index of the vertex w/ id == 0 in the temporary path representation
				}
			}
			int posT = zeroIndex; //start looking in the temp array at this index
			int[] result = new int[n + 1]; //create an array to hold the final, formatted path
			int posR = 0; //start appending to this new result array at vertex 0.
			do{ 
				result[posR++] = tempResult[posT++]; //shifts the path so that left-most value is 0
				if (posT == n){ //wraps around the temporary result once posT reaches the end of the temp array
					posT = 0;
				}
			} while (posR < n + 1); //do this for until we get an array with the path
			return result; //return it.
		}
		
		/**
		 * Prints the L table of the bitonic path algorithm
		 */
		public void printLTable(){
			System.out.println("L-Table:");
			for (int i = 0; i < n; i++ ){
				System.out.print(" ");
				for (int j = 0; j< n; j++){
					if (L[i][j] < 10.00){
						System.out.printf(" %s ", df.format(L[i][j]));
					} else {
						System.out.printf("%s ", df.format(L[i][j]));
					}
				}
				System.out.println();
			}
		}
		/**
		 * Prints the N table for the bitonic path algorithm
		 */
		public void printNTable(){
			System.out.println("N-Table:");
			for (int i = 0; i < n; i++){
				for (int j = 0; j < n; j++){
					int neighbor = N[i][j];
					if (neighbor < 0){
						System.out.printf("%d ", neighbor);
					} else {
						System.out.printf(" %d ", neighbor);
					}
				}
				System.out.println();
			}
		}
		
		/**
		 * Gets the optimal bitonic path distance as L[n-2, n-1] + dist(n-2, n-1)
		 * @return The distance of the optimal bitonic path
		 */
		public double getDistance(){
			return L[n-2][n-1] + graph.getWeight(vertices[n-2], vertices[n-1]);
		}
	}
	
	public static void main(String args[]){
		if (args.length < 2){ // checks to see if the number of arguments is correct
			System.out.println("Usage: java GreedyTSP n seed");
			System.exit(0);
		}
		int N;
		long seed;
		try { //the try catch block to check if the arguments are actually numbers
			N =Integer.parseInt(args[0]);
			seed = Long.parseLong(args[1]);
			
		} catch(NumberFormatException e){
			System.out.println("Command line args must be integers");
			System.exit(0);
			return;
		}
		if (N < 1 ){ //the check to see if the number of vertices is not below 1 
			System.out.println("Number of vertices must be greater than 0");
			System.exit(0);
		}
		Graph graph = new Graph(N, seed);//creates the graph with the given N and seed
		if (N <= 10){ //if the number of vertices is less than 10, then print the vertices and the matrix
			graph.printVertices();//prints the vertices
			graph.printMatrix();//prints the graph representation
		}
		long start = System.currentTimeMillis(); // start measuring time
		TourFinder btFinder = new TourFinder(graph, N);
		int[] vertices = new int [N];
		for (int i = 0; i < N; i++){
			vertices[i] = i;
		} //get array of vertex ids
		VertexSort.sort(vertices, graph); //sort by increasing x-coordinate
		
		btFinder.findTour(vertices); //finds the N-table and L-table
		if (N <= 10){ //Does all print jobs
			System.out.println("Sorted X-Y Coordinates:");
			for (int v : vertices){
				graph.printVertex(v);
			}
			System.out.println("\n");
			btFinder.printLTable();
			System.out.println();
			btFinder.printNTable();
		}
		System.out.println();
		int [] result = btFinder.backTrace(); //get the path
		double distance = btFinder.getDistance(); //get the distance
		long end =  System.currentTimeMillis(); // stop measuring time
		System.out.printf("Distance using bitonic: %s for path ", df.format(distance)); //print results and distance
		for (int v: result){
			System.out.printf("%d ", v);
		}
		System.out.println();
		System.out.printf("Runtime for bitonic TSP   : %d milliseconds\n\n", end - start);
	}
}
	
