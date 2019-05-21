package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.MinHeap;
import structures.PartialTree;
import structures.Vertex;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		/* COMPLETE THIS METHOD */
		PartialTreeList partialList = new PartialTreeList();
		for(int j = 0; j < graph.vertices.length; j++) {
			PartialTree parTree = new PartialTree(graph.vertices[j]);
			//Node initialize = new Node(indTree);
			Vertex vertex1 = graph.vertices[j];
			Vertex.Neighbor neighborsV = graph.vertices[j].neighbors;
			while(neighborsV != null) {
				Vertex neighborOfV = neighborsV.vertex;
				int weight = neighborsV.weight;
				Arc arcV = new Arc(vertex1, neighborOfV, weight);
				parTree.getArcs().insert(arcV);
				neighborsV = neighborsV.next;
			}
			partialList.append(parTree);
		}
		return partialList;
	}
	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {
		/* COMPLETE THIS METHOD */
		ArrayList<Arc> arcs = new ArrayList<Arc>();
		while(ptlist.size > 1) {
			Arc arc = null;
			Vertex vertex1 = null;
			Vertex vertex2 = null;
			boolean check = true;
			PartialTree parTree = ptlist.remove();
			//System.out.println("First In List: " + firstParTree.toString());
			while(check) {
				arc = parTree.getArcs().deleteMin(); // (A, C, 1) // E D
				vertex1 = arc.getv1(); // A E
				vertex2 = arc.getv2(); // C D
				check = rootCheck(vertex1, vertex2);
			}
			if(!check) {
				arcs.add(arc);
			}
			PartialTree secParTree = ptlist.removeTreeContaining(vertex2);
			System.out.println("Here: " + secParTree.toString());
			parTree.merge(secParTree);
			ptlist.append(parTree);
		}
		return arcs;
	}
	
	private static boolean rootCheck(Vertex v1, Vertex v2) {
		if(v1.getRoot().equals(v2.getRoot())) {
			return true; // if true v2 is there
		}
		return false;
	}
	
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
    	/* COMPLETE THIS METHOD */
    	Node ptr = this.rear.next;
    	Node prev = this.rear;
    	PartialTree treeS = null;
    	do {
    		/*System.out.println("Remove: " + front.tree.toString());
    		System.out.println("Check: " + front.tree.getRoot().parent);
    		System.out.println("Name: " + vertex + " Parent: " + vertex.parent);*/
    	    if(rear.next == rear && rootCheck(rear.tree.getRoot(), vertex)) {
    	    	//System.out.println("I am here rear");
    	    	treeS = rear.tree;
    	    	rear = null;
    	    	size--;
    	    	return treeS;
    	    }
    	    //System.out.println("Name: " + front.tree.getRoot().name);
    	    if(rootCheck(ptr.tree.getRoot(), vertex)) {
    	    	treeS = ptr.tree;
    	    	if(ptr == rear) {
    	    		rear = prev;
    	    	}
    	    	prev.next = ptr.next;
    	    	size--;
    	    	return treeS;
    	    }
    	    prev = ptr;
    		ptr = ptr.next;
    	}while(ptr != this.rear.next);
    	throw new NoSuchElementException("No Such Element Found");
    }
    	
    /*private static boolean check(Vertex parent, Vertex v2) { // B D
    	System.out.println("Hello");
    	if(parent.equals(v2)) {
    		System.out.println("returning true");
    		return true;
    	}
    	Vertex curr = v2;
    	while(!(curr.equals(curr.parent))) {
    		System.out.println("Inside");
    		curr = curr.parent;
    		if(curr.equals(parent)) {
    			System.out.println("returning true");
    			return true;
    		}
    	}
		return false;
    }*/
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


