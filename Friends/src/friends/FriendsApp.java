package friends;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class FriendsApp {
	public static void main(String[]args) throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);
		//System.out.print("Enter graph file: ");
		//String fname = sc.nextLine();
		Scanner scfile;
		Graph g; 
		scfile = new Scanner(new File("test1.txt"));
		g = new Graph(scfile);
/*
		for(int i = 0; i<g.members.length; i++) {
			if(i<10)
				System.out.print(" "+ i+"-" + g.members[i].name +": ");
			else
				System.out.print(i+"-" + g.members[i].name +": ");
			for(int j= g.members[i].name.length(); j <8 ; j++) {
				System.out.print(" ");
			}
			for(Friend f = g.members[i].first; f != null ; f = f.next) {
				System.out.print(g.members[f.fnum].name + " _ ");
			}
			System.out.println();
		}*/
		//System.out.println(Friends.shortestChain(g, "nick", "samir").toString());
		//System.out.println(Friends.cliques(g,"ucla").toString());
		for(int i = 0;i< g.members.length;i++)
			System.out.println(i+" " +Friends.connectors(g).toString());
	//}
	}
}
