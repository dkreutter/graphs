import java.util.*;

public abstract class Graph {
	protected int[] V;
	protected ArrayList<int[]> E;
	
	abstract public boolean add(int v);
	
	abstract public boolean add(int[] e);
	
	abstract public boolean remove(int v);
	
	abstract public boolean remove(int[] e);
	
	abstract public boolean isAdj(int a, int b);
	
	abstract public boolean hasPath(int a, int b);
	
	abstract public ArrayList<Integer> getPath(int a, int b);
	
	abstract public boolean isConnected();
	
	abstract public Graph inducedSubgraph(int[] v);
	
	public int[] getV() {
		return V;
	}
	
	public ArrayList<int[]> getE(){
		return E;
	}
	
	
}
