package DijikstraCalcul;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Graph{
	static final int INF=Integer.MAX_VALUE;
	private int V;
	public int getV(){return this.V;}
	private LinkedList<NoeudVoisin>adj[];
public	Graph(int v)
	{
		V=v;
		adj = new LinkedList[V];
		for (int i=0; i<v; ++i)
			adj[i] = new LinkedList<NoeudVoisin>();
	}
	public int addEdge(int u, int v, double weight)
	{
		NoeudVoisin node = new NoeudVoisin(v,weight);
		adj[u].add(node);// Add v to u's list
		return 0;
	}

	// A recursive function used by shortestPath.
	// See below link for details
	public void topologicalSortUtil(int v, Boolean visited[], Stack stack)
	{
		// Mark the current node as visited.
		visited[v] = true;
		Integer i;

		// Recur for all the vertices adjacent to this vertex
		Iterator<NoeudVoisin> it = adj[v].iterator();
		while (it.hasNext())
		{
			NoeudVoisin node =it.next();
			if (!visited[node.getV()])
				topologicalSortUtil(node.getV(), visited, stack);
		}
		// Push current vertex to stack which stores result
		stack.push(new Integer(v));
	}

	// The function to find shortest paths from given vertex. It
	// uses recursive topologicalSortUtil() to get topological
	// sorting of given graph.
	
	
	//Le résultat
	
	
	public  double []  shortestPath(int s)
	{
		Stack stack = new Stack();
		double dist[] = new double[V];

		// Mark all the vertices as not visited
		Boolean visited[] = new Boolean[V];
		for (int i = 0; i < V; i++)
			visited[i] = false;

		// Call the recursive helper function to store Topological
		// Sort starting from all vertices one by one
		for (int i = 0; i < V; i++)
			if (visited[i] == false)
				topologicalSortUtil(i, visited, stack);

		// Initialize distances to all vertices as infinite and
		// distance to source as 0
		for (int i = 0; i < V; i++)
			dist[i] = INF;
		dist[s] = 0;

		// Process vertices in topological order
		while (stack.empty() == false)
		{
			// Get the next vertex from topological order
			int u = (Integer) stack.pop();

			// Update distances of all adjacent vertices
			Iterator<NoeudVoisin> it;
			if (dist[u] != INF)
			{
				it = adj[u].iterator();
				while (it.hasNext())
				{
					NoeudVoisin i= it.next();
					if (dist[i.getV()] > dist[u] + i.getWeight())
						dist[i.getV()] = dist[u] + i.getWeight();
				}
			}
		}
		return dist;

		
	}
}


