/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Fall 2017 
// PROJECT:          (P2)
// FILE:             (TrainHub)
//
// TEAM:    (49)
// Author1: (Jake DeCamp, decamp2@wisc.edu, 9074317984, 002)
// Author2: (Eric DeCamp, edecamp@wisc.edu, 9069958859, 002)
//
// ---------------- OTHER ASSISTANCE CREDITS 
// Persons: Identify persons by name, relationship to you, and email. 
// Describe in detail the the ideas and help they provided. 
// 
// Online sources: avoid web searches to solve your problems, but if you do 
// search, be sure to include Web URLs and description of 
// of any information you find. 
//////////////////////////// 80 columns wide //////////////////////////////////
/**
 * An Iterable list that is implemented using a singly-linked chain of nodes
 * with a header node and without a tail reference.
 * 
 * The "header node" is a node without a data reference that will reference the
 * first node with data once data has been added to list.
 * 
 * The iterator returned is a LinkedListIterator constructed by passing the
 * first node with data.
 * 
 * CAUTION: the chain of nodes in this class can be changed without calling the
 * add and remove methods in this class. So, the size() method must be
 * implemented by counting nodes. This counting must occur each time the size
 * method is called. DO NOT USE a numItems field.
 * 
 * COMPLETE THIS CLASS AND HAND IN THIS FILE
 */
public class LinkedList<E> implements ListADT<E> {

	// YOU MUST IMPLEMENT THE LINKED LIST CLASS AS FOLLOWS:
	//
	// It must be a SINGLY-LINKED chain of ListNode<E> nodes
	// It must have a "header node" ("dummy node" without data)
	// It must NOT have a tail reference
	// It must NOT keep a number of data items
	// NOTE: in this program, the chains of nodes in this program may be
	// changed outside of the LinkedList class, so the actual data count
	// must be determined each time size is called.
	//
	// It must return a LinkedListIterator<E> as its iterator.
	//
	// Note: The "header node"'s data reference is always null and
	// its next references the node with the first data of the list.
	//
	// Be sure to implement this LinkedList<E> using Listnode
	// you must use LinkedListIterator<E> instead of Iterator<E>
	//
	private Listnode<E> head;

	public LinkedList() {
		head = new Listnode<E>(null);
	}

	/**
	 * Returns a reference to the header node for this linked list. The header
	 * node is the first node in the chain and it does not contain a data
	 * reference. It does contain a reference to the first node with data (next
	 * node in the chain). That node will exist and contain a data reference if
	 * any data has been added to the list.
	 * 
	 * NOTE: Typically, a LinkedList would not provide direct access to the
	 * headerNode in this way to classes that use the linked list. We are
	 * providing direct access to support testing and to allow multiple nodes to
	 * be moved as a chain.
	 * 
	 * @return a reference to the header node of this list. 0
	 */
	public Listnode<E> getHeaderNode() {
		return head;
	}

	/**
	 * Must return a reference to a LinkedListIterator for this list.
	 */
	public LinkedListIterator<E> iterator() {
		return new LinkedListIterator<E>(head.getNext());
	}

	@Override
	public void add(E item) {
		// Error check bad data
		if (item == null) {
			throw new IllegalArgumentException();
		}
		// Add item to empty list
		if (head.getNext() == null) {
			head.setNext(new Listnode<E>(item));
		}
		// Loop to the end of the linked list and add
		else {
			Listnode<E> curr = head;
			while (curr.getNext() != null) {

				curr = curr.getNext();
			}
			curr.setNext(new Listnode<E>(item));
		}
	}

	@Override
	public void add(int pos, E item) {
		// Error check bad data
		if (item == null) {
			throw new IllegalArgumentException();
		}
		// Error checks the pos value
		if (pos < 0 || pos > this.size() - 1) {
			throw new IndexOutOfBoundsException();
		}
		// Gives us a reference to the linked list, for traversal purposes
		Listnode<E> curr = head;

		// Traverses chain of nodes until curr points to the node before the
		// pos we want to add
		for (int i = 0; i < pos; i++) {
			curr = curr.getNext();
		}
		// Link in new node
		Listnode<E> newNode = new Listnode<E>(item, curr.getNext());
		curr.setNext(newNode);

	}

	@Override
	public boolean contains(E item) {
		// Error check bad data
		if (item == null) {
			throw new IllegalArgumentException();
		}
		// Use an iterator to check for a match
		LinkedListIterator<E> iterator = this.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(item)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public E get(int pos) {
		// Error check bad data
		if (pos < 0 || pos > this.size() - 1) {
			throw new IndexOutOfBoundsException();
		}
		// Use curr to avoid destroying the linked list
		Listnode<E> curr = head;
		for (int i = 0; i < pos; i++) {
			curr = curr.getNext();
		}
		return curr.getData();

	}

	@Override
	public boolean isEmpty() {
		return (this.size() == 0);
	}

	@Override
	public E remove(int pos) {
		if (pos < 0 || pos > this.size() - 1) {
			throw new IndexOutOfBoundsException();
		}
		// Gives us a reference to the linked list, for traversal purposes
		Listnode<E> curr = head;
		Listnode<E> fugitiveNode = null;
		// Traverses chain of nodes until curr points to the node before the
		// "fugitive"
		for (int i = 0; i < pos; i++) {
			curr = curr.getNext();
		}
		fugitiveNode = curr.getNext();
		curr.setNext(curr.getNext().getNext());

		return fugitiveNode.getData();
	}

	@Override
	public int size() {
		// Iterates through the linked list, keeping a count
		LinkedListIterator<E> iterator = this.iterator();
		int count = 0;
		while (iterator.hasNext()) {
			count++;
			iterator.next();
		}
		return count;
	}
}