

/**
 * The class used to encapsulate vertex data 
 * @author Dyangelo Grullon
 *
 */
public class Vertex implements Comparable < Vertex >{
		public int id; //the id of the vertex
		public Double weight; //the weight between parent and vertex id
		public int parent; //the parent of the vertex
		public boolean leftToRight;
		/**
		 * The builder method for a Vertex object. Instantiates weight to infinity
		 * and parent to -1
		 * @param id The vertex id to assign
		 */
		public Vertex(int id){
			this.id = id;
			this.weight = Double.POSITIVE_INFINITY;
			this.parent = -1;
		}
		
		@Override
		public int compareTo(Vertex o) { //already documented
			return o.weight.compareTo(this.weight);
		}
		
		@Override
		public String toString(){ //already documented
			return "Parent of " + String.valueOf(id) + " is " + String.valueOf(parent);
			
		}
		
	}