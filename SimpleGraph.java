import java.util.ArrayList;
import java.util.Arrays;

public class SimpleGraph extends Graph {
	
	public SimpleGraph() {	//Constructs the graph with a single vertex
		V = new int[1];
		V[0] = 0;
		E = new ArrayList<int[]>();
	}
	
	public SimpleGraph(int numVert) {  //Constructs the graph with numVert vertices and no edges
		V = new int[numVert];
		
		for(int i = 0; i < V.length; i++) {
			V[i] = i;
		}
		
		E = new ArrayList<int[]>();
	}
	
	private SimpleGraph(int numVert, ArrayList<int[]> edges) {  //Constructs the graph with numVert vertices and the edge set defined by edges.
		V = new int[numVert];
		E = new ArrayList<int[]>();
		
		for(int i = 0; i < numVert; i++) 
			V[i] = i;
		
		for(int i = 0; i < edges.size(); i++) {  //If an edge of edges has length greater than 2, constructs the graph with no edges.
			if(edges.get(i).length != 2) {
				System.out.println("Invalid Edge Set");
				E.clear();
				return;
			}
			
			for(int j = 0; j < edges.get(i).length; j++) {  //If edges contains an edge with a vertex not in V, constructs the graph with no edges.
				if (edges.get(i)[j] > numVert) {
					System.out.println("Invalid Edge Set");
					E.clear();
					return;
				}
			}
			
			E.add(edges.get(i));	
		}	
	}
	
	private SimpleGraph(int[] v, ArrayList<int[]> e) {
		V = v;
		E = e;
	}
	
	public static SimpleGraph K(int numVert) { //Returns the complete graph on numVert vertices
		int[] v = new int[numVert];
		
		for(int i = 0; i < numVert; i++) 
			v[i] = i;
		
		ArrayList<int[]> edges = new ArrayList<int[]>();
		
		for(int i = 0; i < numVert; i++) {
			for(int j = 0; j < numVert; j++) {
				int[] edge = {i,j};
				edges.add(edge);
			}	
		}
		
		SimpleGraph G = new SimpleGraph(numVert, edges);
		return G;
	}
	
	public static SimpleGraph K(int a, int b) {  //Returns the complete bipartite graph with partite set sizes a, b
		
		ArrayList<int[]> edges = new ArrayList<int[]>();
		
		for(int i = 0; i < a; i++) {
			for(int j = a; j < a+b; j++) {
				int[] e = {i,j};
				edges.add(e);	
			}
		}
		
		SimpleGraph G = new SimpleGraph(a+b, edges);
		return G;
		}
	
	public String toString() {
		String v = Arrays.toString(this.V);
		StringBuilder sb = new StringBuilder();
		
		for (int[] e : this.E)
		{
		    sb.append(Arrays.toString(e));
		    sb.append(", ");
		}
		
		String s = sb.toString();
		return "(" + v + ", {" + s.substring(0, s.length()-2) + "})";
	}
	
	public SimpleGraph inducedSubgraph(int[] v) {
		ArrayList<int[]> edges = new ArrayList<int[]>();
		
		for (int i = 0; i < this.E.size(); i++)
		{
			if (in(v, this.E.get(i)[0]) && in(v, this.E.get(i)[1]))
				edges.add(E.get(i));
		}
		
		return new SimpleGraph(v, edges);
	}
	
	public boolean remove(int v) {  //Removes a vertex v and all incident edges from this. Returns false if v is not in this.V
		if(!in(this.V, v))
			return false;
		
		int[] newV = new int[this.V.length-1];
		int j = 0;
		
		for(int i = 0; i < this.V.length; i++) {
			if (!(this.V[i] == v)) {
				newV[j] = this.V[i];
				j++;
			}
		}
		
		this.V = this.inducedSubgraph(newV).V;
		this.E = this.inducedSubgraph(newV).E;
		return true;
	}
	
	public boolean remove(int[] e) {  //Removes an edge e from this. Returns false if e is not in this.E
		if(!(e.length == 2))
			return false;
		
		int[] eSym = {e[1], e[0]};
		
		for(int i = 0; i < this.E.size(); i++) {
			if(Arrays.equals(e, this.E.get(i)) || Arrays.equals(eSym, this.E.get(i))) {
				this.E.remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean add(int v) {  //Adds a vertex v to this. Returns false if v is already in this.V
		int[] newV = new int[this.V.length+1];
		
		for(int i = 0; i < this.V.length; i++) {
			if(this.V[i] == v)
				return false;
			
			newV[i] = this.V[i];
		}
		
		newV[newV.length-1] = v;
		this.V = newV;
		return true;
	}
	
	public boolean add(int[] e) {  //Adds an edge e to this
		if(!(e.length == 2))  //Returns false if e has length not equal to 2
			return false;
		
		if(!(in(this.V, e[0]) && in(this.V, e[1])))  //Or if the vertices joined by e are not in this.V
			return false;
		
		int[] eSym = {e[1], e[0]};
		
		for (int[] edge : this.E) {  //Or if e is already in this.E
			if(Arrays.equals(edge, e) || Arrays.equals(edge, eSym))
				return false;
		}
		
		this.E.add(e);
		return true;
	}
	
	public boolean isAdj(int a, int b) {
		int[] e1 = {a, b};
		int[] e2 = {b, a};
		
		if(in(this.E, e1) || in(this.E, e2))  //Checks the symmetric edge to satisfy defn of simple graph
			return true;
		
		return false;
	}
	
	public boolean hasPath(int a, int b) {  //Returns true if there is a path in this from a to b
		if(!(in(V, a) && in(V, b)))  //Returns false if a, b are not both in this
			return false;
		
		if(a == b)
			return true;
		
		if(this.isAdj(a, b))  //Returns true if a and b are adjacent
			return true;
		
		int[] withoutA = new int[V.length-1];
		int j = 0;
		
		for(int i = 0; i < V.length; i++) { //Gets list of all vertices in this other than a
			if (V[i] != a) {
				withoutA[j] = V[i];
				j++;
			}	
		}
		
		for(int v : V) {  //Recursively checks if any of a's adjacent vertices have a path to b, not including a.
			if(this.isAdj(a, v) && this.inducedSubgraph(withoutA).hasPath(v, b))
				return true;
		}
		
		return false;
	}
	
	public ArrayList<Integer> getPath(int a, int b) {  //Returns a shortest path in this from a to b. If no path exists, returns an empty ArrayList
		if(!(in(V, a) && in(V, b) && this.hasPath(a, b)))
			return new ArrayList<Integer>();
		
		if(this.isAdj(a, b)) {
			ArrayList<Integer> path = new ArrayList<Integer>();
			path.add(a);
			path.add(b);
			return path;
		}
		
		if(a == b){
			ArrayList<Integer> path = new ArrayList<Integer>();
			path.add(a);
			return path;
		}	
		
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();  //Initializes list to store all paths
		int[] withoutA = new int[V.length-1];
		int j = 0;
		
		for(int i = 0; i < V.length; i++) {  //Constructs list of all vertices except a
			if (V[i] != a) {
				withoutA[j] = V[i];
				j++;
			}	
		}
		
		for(int v : withoutA) {  //For all of a's adjacent vertices v, recursively gets shortest path from v to b, not using a
			if (this.isAdj(a, v)) {
				ArrayList<Integer> vPath = this.inducedSubgraph(withoutA).getPath(v, b);
				
				if (vPath.size() > 0) {  //For each nonempty path (nonempty -> there exists a path from v to b) insert a at front to get a path from a to b
					vPath.add(0, a);
					paths.add(vPath);
				}
			}
		}
		
		ArrayList<Integer> shortestPath = paths.get(0);
		
		for(ArrayList<Integer> path : paths) { //Iterates through paths to findest shortest path
			if (path.size() < shortestPath.size())
				shortestPath = path;
		}
		
		return shortestPath;
	}
	
	public int [] getAdj(int a) { //Gets list of vertices adjacent to a
		int i = 0;
		
		for (int v : V) {
			if(this.isAdj(a, v))
				i++;
		}
		
		int [] adj = new int [i];
		i = 0;
		
		for (int j = 0; j < V.length; j++) {
			if (this.isAdj(a, V[j])) {
				adj[i] = V[j];
				j++;
			}
		}
		
		return adj;
	}
	
	public boolean isConnected() {
		int[] marked = new int[V.length];
		marked[0] = 1;  //Marks an arbitrary vertex of this
		boolean wasMarked = true;
		
		while(wasMarked) {  //If a vertex was marked in the last iteration
			wasMarked = false;
			for (int i = 0; i < marked.length; i++) {  //For every marked vertex v, mark every unmarked vertex adjacent to v
				if(marked[i] == 1) {
					marked[i] = 2;  //Sets the marking of v to 2 to denote that adjacent vertices of v have been marked
					for(int j = 0; j < marked.length; j++) {
						if(this.isAdj(V[i], V[j]) && marked[j] == 0) {
							wasMarked = true;
							marked[j] = 1;
						}
					}
				}
			}
		}
		
		return !in(marked, 0);	//If there is still an unmarked vertex in this, then it is disconnected
	}
	
	public static SimpleGraph getUndirected(DirectedGraph G) {  //Constructs the corresponding simple graph from directed graph G
		ArrayList<int[]> edges = new ArrayList<int[]>();
		
		for (int i = 0; i < G.E.size(); i++) {
			int[] eSym = {G.E.get(i)[1], G.E.get(i)[0]};
			
			if(!in(edges, G.E.get(i)) && !in(edges, eSym))
				edges.add(G.E.get(i));		
		}
		
		return new SimpleGraph(G.V, edges);	
	}
	
	public static boolean in(ArrayList<int[]> edges, int[] e) {

		for (int[] edge : edges) {
			if(Arrays.equals(edge, e))
				return true;
		}
		
		return false;
	}
	
	public static boolean in(int [] arr, int x) {
		for (int a : arr) {
			if (a == x)
				return true;
		}
		
		return false;
	}
	
	public static int[] arrayWithout(int[] array, int a) {
		int[] withoutA = new int[array.length-1];
		int j = 0;
		
		for(int i = 0; i < array.length; i++) {
			if (array[i] != a) {
				withoutA[j] = array[i];
				j++;
			}
		}	
			
		return withoutA;
	}
	
	public static void main(String[] args) {
		
	}	
}
