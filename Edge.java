import java.text.DecimalFormat;

/**
 * @author Dyangelo Grullon (dag4202)
 * The class used to encapsulate edge data
 */
public class Edge implements Comparable<Edge>{

	private Integer row; // the first vertex
	private Integer col; // the second vertex
	private Double weight; //the distance between both points
	
	/**
	 * Builder function for an Edge type representing an edge in a graph
	 * @param row
	 * @param col
	 * @param weight
	 */
	public Edge(int row, int col, double weight){
		this.row = row;
		this.col = col;
		this.weight = weight;
	}
	
	@Override
	public int compareTo(Edge o) { //already documented
		int result = weight.compareTo(o.weight);
		if (result == 0){
			result = row.compareTo(o.row);
			if (result == 0){
				result = col.compareTo(o.col);
			}
		}
		return result;
	}
	
	/**
	 * Retrieves the row of the edge in a matrix
	 * @return the first vertex of the edge
	 */
	public int getRow(){
		return row;
	}
	/**
	 * Retrieves the col of the edge in a matrix
	 * @return the second vertex of the edge
	 */
	public int getCol(){
		return col;
	}
	
	/**
	 * Retrieves the weight of the edge between both vertices
	 * @return the weight of the edge
	 */
	public double getWeight(){
		return weight;
	}
	
	@Override
	public String toString(){ //Already documented 
		DecimalFormat df = new DecimalFormat("0.00");
		return String.valueOf(row) + " " + String.valueOf(col) + " weight = " + df.format(weight);
		
	}
}
