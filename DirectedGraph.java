import java.util.ArrayList;
import java.util.Arrays;

public class DirectedGraph extends Graph {

	public DirectedGraph() {
		V = new int[1];
		V[0] = 0;
		E = new ArrayList<int[]>();
	}
	
	public DirectedGraph(int numvert) {
		V = new int[numvert];
		for(int i = 0; i < V.length; i++) {
			V[i] = i;
		}
		E = new ArrayList<int[]>();
	}
	
	private DirectedGraph(int [] v, ArrayList<int[]> e) {
		V = v;
		E = e;
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
	
	public DirectedGraph inducedSubgraph(int[] v) {
		ArrayList<int[]> edges = new ArrayList<int[]>();
		
		for (int i = 0; i < this.E.size(); i++)
		{
			if (in(v, this.E.get(i)[0]) && in(v, this.E.get(i)[1]))
				edges.add(E.get(i));
		}
		return new DirectedGraph(v, edges);
	}
	
	public boolean remove(int v) {
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
	
	public boolean remove(int[] e) {
		if(!(e.length == 2))
			return false;
		
		
		for(int i = 0; i < this.E.size(); i++) {
			if(Arrays.equals(e, this.E.get(i))) {
				this.E.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public boolean add(int v) {
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
	
	public boolean add(int[] e) {
		if(!(e.length == 2))
			return false;
		if(!(in(this.V, e[0]) && in(this.V, e[1])))
			return false;
		for (int[] edge : this.E) {
			if(Arrays.equals(edge, e))
				return false;
		}
		this.E.add(e);
		return true;
	}
	
	public boolean isAdj(int a, int b) {
		int[] e = {a, b};
		
		if(in(this.E, e))
			return true;
		return false;
	}
	
	public boolean hasPath(int a, int b) {
		if(!(in(V, a) && in(V, b)))
			return false;
		
		if(this.isAdj(a, b))
			return true;
		
		int[] withoutA = new int[V.length-1];
		int j = 0;
		for(int i = 0; i < V.length; i++) {
			if (V[i] != a) {
				withoutA[j] = V[i];
				j++;
			}	
		}
		
		for(int v : V) {
			
			if(this.isAdj(a, v) && this.inducedSubgraph(withoutA).hasPath(v, b))
				return true;
		}
		
		return false;
		
	}
	
	public ArrayList<Integer> getPath(int a, int b) {
		if(!(in(V, a) && in(V, b) && this.hasPath(a, b)))
			return new ArrayList<Integer>();
		
		if(this.isAdj(a, b)) {
			ArrayList<Integer> path = new ArrayList<Integer>();
			path.add(a);
			path.add(b);
			return path;
		}
		
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
		
		int[] withoutA = new int[V.length-1];
		int j = 0;
		for(int i = 0; i < V.length; i++) {
			if (V[i] != a) {
				withoutA[j] = V[i];
				j++;
			}	
		}
		
		for(int v : withoutA) {
			if (this.isAdj(a, v)) {
				ArrayList<Integer> vPath = this.inducedSubgraph(withoutA).getPath(v, b);
				if (vPath.size() > 0) {
					vPath.add(0, a);
					paths.add(vPath);
				}
			}
		}
		
		ArrayList<Integer> shortestPath = paths.get(0);
		
		for(ArrayList<Integer> path : paths) {
			if (path.size() < shortestPath.size())
				shortestPath = path;
		}
		
		return shortestPath;
		
		
	}
	
	public int [] getAdj(int a) {
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
		for (int i = 0; i < V.length; i++) {
			if(!this.hasPath(V[i], V[0]))
				return false;
		}
		
		return true;	
		
	}

	public boolean isWeakConnected() {
		return SimpleGraph.getUndirected(this).isConnected();
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
		
		DirectedGraph K = new DirectedGraph(7);
		K.add(new int[] {0,1});
		K.add(new int[] {0,2});
		K.add(new int[] {1,3});
		K.add(new int[] {1,4});
		K.add(new int[] {2,5});
		K.add(new int[] {2,6});
		
		System.out.println(K.isConnected());
		System.out.println(K.isWeakConnected());
		
		
		
		
	
	}	
}
