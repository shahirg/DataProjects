package friends;

import java.util.ArrayList;
import java.util.HashMap;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		ArrayList<String> path = new ArrayList<String>();
		String[] prev = new String [g.members.length];
		Queue<Person> q = new Queue<Person>();
		q.enqueue(g.members[g.map.get(p1)]);
		boolean[] visited = new boolean[g.members.length];
		visited[g.map.get(p1)] = true;
		while(!q.isEmpty()) {
			Person cur = q.dequeue(); 
			Friend f = cur.first;
			while(f != null) {
				if(!visited[f.fnum]) {
					prev[f.fnum] = cur.name;
					visited[f.fnum] = true;
					q.enqueue(g.members[f.fnum]);
					if(g.members[f.fnum].name.equals(p2)) {
						q.clear();
						break;
					}
				}
				f = f.next;
			}
		}
		if(prev[g.map.get(p2)] == null) {
			return null;
		}
		String ptr = p2;
		while(ptr != null) {
			path.add(0,ptr);
			ptr = prev[g.map.get(ptr)]; 
		}
		return path;
	}

	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		ArrayList<ArrayList<String>> allCliques = new ArrayList<ArrayList<String>>();
		ArrayList<String> clique = new ArrayList<String>();
		boolean[] visited = new boolean[g.members.length];
		Queue<Person> q = new Queue<Person>();
		for(int i = 0; i < g.members.length;i++) {
			if(visited[i]) {
				continue;
			}
			visited[i] = true;
			if(g.members[i].student && g.members[i].school.equals(school)) {
				clique = new ArrayList<String>();
				clique.add(g.members[i].name);
				q = new Queue<Person>();
				q.enqueue(g.members[i]);
				while(!q.isEmpty()) {
					Person cur = q.dequeue();
					Friend f = cur.first;
					while(f != null) {
						if(!visited[f.fnum] && g.members[f.fnum].student 
								&& g.members[f.fnum].school.equals(school)) {
							clique.add(g.members[f.fnum].name);
							visited[f.fnum] = true;
							q.enqueue(g.members[f.fnum]);
	
						}
						f = f.next;
					}
				}
				allCliques.add(clique);
			}
			
		}
		return allCliques;
		
	}

	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
        boolean[] visited = new boolean[g.members.length];
        ArrayList<String> out = new ArrayList<String>();

        int[] dfsNum = new int[g.members.length],back = new int[g.members.length];
        for(int i = 0; i < visited.length; i++) {
            if(!visited[i]) {
                dfs(g,i,visited,1,dfsNum,back, out);
            }
        }
        return out;
	}
	private static void dfs(Graph g, int v,boolean [] visited,int count,
			int[] dfsNum,int[]back,  ArrayList<String> c) {
		visited[v] = true;
		dfsNum[v] = back[v] = count;
		for(Friend f = g.members[v].first; f != null ; f = f.next) {
			if(!visited[f.fnum]) {
				count++;
				dfs(g,f.fnum,visited,count,dfsNum,back,c);
				if((dfsNum[v] <=back[f.fnum] && !c.contains(g.members[v].name)
					&& g.members[v].first.next != null) &&
					((dfsNum[v]!=1) || (dfsNum[v] == 1 && dfsNum[v] < dfsNum[f.fnum] -1))) {
						c.add(g.members[v].name);
				}	
				if(dfsNum[v]>back[f.fnum]){
					back[v] = Math.min(back[f.fnum],back[v]);
				}
			}
			else {
				back[v] = Math.min(back[v], dfsNum[f.fnum]);
			}

		}

		
	}
}

