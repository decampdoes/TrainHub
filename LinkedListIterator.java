
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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The iterator implementation for LinkedList. The constructor for this class
 * requires that a reference to a Listnode with the first data item is passed
 * in.
 * 
 * If the Listnode reference used to create the LinkedListIterator is null, that
 * implies there is no data in the LinkedList and this iterator should handle
 * that case correctly.
 * 
 * COMPLETE THIS CLASS AND HAND IN THIS FILE
 */
public class LinkedListIterator<T> implements Iterator<T> {
	private Listnode<T> curr;

	/**
	 * Constructs a LinkedListIterator when given the first node with data for a
	 * chain of nodes.
	 * 
	 * Tip: do not construct with a "blank" header node.
	 * 
	 * @param a
	 *            reference to a List node with data.
	 */
	public LinkedListIterator(Listnode<T> head) {
		curr = head;
	}

	/**
	 * Returns the next element in the iteration and then advances the iteration
	 * reference.
	 * 
	 * @return the next data item in the iteration that has not yet been
	 *         returned
	 * @throws NoSuchElementException
	 *             if the iteration has no more elements
	 */
	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();
		// Get data and advance the pointer
		T item = curr.getData();
		curr = curr.getNext();
		return item;
	}

	/**
	 * Returns true if their are no more data items to iterate through for this
	 * list.
	 * 
	 * @return true if their are any remaining data items to iterator through
	 */
	@Override
	public boolean hasNext() {
		return curr != null;
	}

	/**
	 * The remove operation is not supported by this iterator
	 * 
	 * @throws UnsupportedOperationException
	 *             if the remove operation is not supported by this iterator
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}