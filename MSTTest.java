package app;

import java.io.IOException;
//import java.util.ArrayList;
import java.util.Scanner;

import structures.*;
import structures.Graph;

import java.util.ArrayList;
import java.util.Iterator;
import structures.PartialTree;

/**
 * Minimum Spanning Tree Driver
 * @author Saaqeb 
 *
 */
public class MSTTest {
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		while (true) {
			Graph graph;
			while (true) {
				System.out.print("\nName of graph file: ");
				try { 
					graph = new Graph(sc.next()); 
					break;
				} catch (IOException e) {
					System.out.println("\nFile does not exist!");
				}
			}
			PartialTreeList ptl = PartialTreeList.initialize(graph);
			Iterator<PartialTree> n = ptl.iterator();
			while(n.hasNext()) {
				PartialTree pt = n.next();
				System.out.println(pt.toString());
			}
			//PartialTreeList MST = new PartialTreeList();
			ArrayList<Arc> al = new ArrayList<Arc>();
			al = PartialTreeList.execute(ptl);	
			System.out.println(al);//ArrayList<Arc> al = 
			System.out.print("\nMST Arcs: ");
			for (int i = 0; i < al.size(); i++) {
				System.out.print(al.get(i).toString() +" ");
			}
			System.out.print("\n\nTry another file? (y or n): ");
			String s = sc.next();
			while (!s.equals("y") && !s.equals("n")) {
				System.out.print("\nIncorrect input!\n\nTry another file? (y or n): ");
				s = sc.next();
			}
			if (s.equals("n")) break;
		}
		sc.close();
	}

}
