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
 * This class represents a train hub and provides the common operations needed
 * for managing the incoming and outgoing trains.
 *
 * It has a LinkedList of Train as a member variable and manages it.
 *
 * Known bug: exception thrown in case 8 If you try to move ethanol from a
 * source train, but that source train does not have ethanol, a null pointer
 * exceptions is thrown. Such a case was not shown to be handled by the
 * Run_train2.txt.
 *
 * @see LinkedList
 * @see Train
 * @see Config
 */
public class TrainHub {

	/** The internal data structure of a hub is a linked list of Trains */
	private LinkedList<Train> trains;

	/**
	 * Constructs and initializes TrainHub object
	 */
	public TrainHub() {
		this.trains = new LinkedList<Train>();
	}

	/**
	 * This method processes the incoming train. It iterates through each of the
	 * cargo car of the incoming train. If there is an outgoing train in the
	 * train list going to the destination city of the cargo car, then it
	 * removes the cargo car from the incoming train and adds the cargo car at
	 * the correct location in the outgoing train. The correct location is to
	 * become the first of the matching cargo cars, with the cargo cars in
	 * alphabetical order by their cargo name.
	 *
	 * If there is no train going to the destination city, it creates a new
	 * train and adds the cargo to this train.
	 *
	 * @param train
	 *            Incoming train (list or cargo cars)
	 */
	public void processIncomingTrain(Train train) {
		// Iterate through the cars of the incoming train
		LinkedListIterator<CargoCar> trainIterator = train.iterator();
		while (trainIterator.hasNext()) {
			// Current car
			CargoCar currentCar = trainIterator.next();
			// if the hub contains an outgoing train to the cars destination
			if (hubContainsOutGoingTrain(currentCar.getDestination())) {
				Train outGoingTrain = returnTrainToDestination(currentCar.getDestination());
				// If the train to that destination contains cargo of the same
				// name,
				// add that cargo car in the correct position
				if (trainContainsCargo(currentCar.getName(), outGoingTrain)) {
					int position = getCargoIndex(currentCar.getName(), outGoingTrain);
					outGoingTrain.add(position, currentCar);
				}
				// If the train does not contain that cargo, add the car in
				// alphabetical order
				else {
					int alphaPos = getAlphabeticalIndex(currentCar.getName(), outGoingTrain);
					if (outGoingTrain.numCargoCars() == alphaPos) {
						outGoingTrain.add(currentCar);
					} else {
						outGoingTrain.add(alphaPos, currentCar);
					}
				}
			}
			// If a train to that destination was not found, create a new train
			// and add it to trainHub
			else {
				Train newTrain = new Train(currentCar.getDestination());
				newTrain.add(currentCar);
				trains.add(newTrain);
			}
		}
	}

	/**
	 * This method tries to find the train in the list of trains, departing to
	 * the given destination city.
	 *
	 * @param dest
	 *            Destination city for which train has to be found.
	 * @return Pointer to the train if the train going to the given destination
	 *         exists. Otherwise returns null.
	 */
	public Train findTrain(String dest) {
		// Iterates through the list of trains
		LinkedListIterator<Train> trainHubIterator = trains.iterator();
		while (trainHubIterator.hasNext()) {
			// If the current train is headed to dest, return it
			Train currTrain = trainHubIterator.next();
			if (currTrain.getDestination().equalsIgnoreCase(dest)) {
				return currTrain;
			}
		}
		// Return null if the train was not found
		return null;
	}

	/**
	 * This method removes the first cargo car going to the given destination
	 * city and carrying the given cargo.
	 *
	 * @param dest
	 *            Destination city
	 * @param name
	 *            Cargo name
	 * @return If there is a train going to the the given destination and is
	 *         carrying the given cargo, it returns the cargo car. Else it
	 *         returns null.
	 */
	public CargoCar removeCargo(String dest, String name) {
		// Finds the train with the given destination
		Train train = findTrain(dest);
		// If the train isn't found return null
		if (train == null) {
			return null;
		}
		// Remove the train
		else {
			return train.removeCargo(name);
		}
	}

	/**
	 * This method iterates through all the trains in the list and finds the sum
	 * of weights of given cargo in all trains.
	 *
	 * @param name
	 *            Name of the cargo
	 * @return Total weight of given cargo in all departing trains.
	 */
	public int getWeight(String name) {
		// Iterates through each train in the trainHub
		int cargoSum = 0;
		LinkedListIterator<Train> trainHubIterator = trains.iterator();
		while (trainHubIterator.hasNext()) {
			Train currTrain = trainHubIterator.next();
			LinkedListIterator<CargoCar> trainIterator = currTrain.iterator();
			// Iterate through the current train and sum the weight of the
			// desired cargo
			while (trainIterator.hasNext()) {
				CargoCar currCargo = trainIterator.next();
				if (currCargo.getName().equalsIgnoreCase(name)) {
					cargoSum += currCargo.getWeight();
				}
			}
		}
		return cargoSum;
	}

	/**
	 * This method is used to depart the train to the given destination. To
	 * depart the train, one needs to delete the train from the list.
	 *
	 * @param dest
	 *            Destination city for which corresponding train has to be
	 *            departed/deleted.
	 * @return True if train to the given destination city exists. If not, then
	 *         return false.
	 */
	public boolean departTrain(String dest) {
		// Finds a train with the given destination in the trainHub
		Train removeTrain = findTrain(dest);
		int pos = 0;
		LinkedListIterator<Train> trainHubIterator = trains.iterator();
		while (trainHubIterator.hasNext()) {
			// If train equals the one we wish to remove
			if (trainHubIterator.next().equals(removeTrain)) {
				// Remove train
				trains.remove(pos);
				return true;
			}
			pos++;
		}
		return false;
	}

	/**
	 * This method deletes all the trains.
	 *
	 * @return True if there was at least one train to delete. False if there
	 *         was no train to delete.
	 */
	public boolean departAllTrains() {
		// If there are no trains to depart return false
		if (trains.size() == 0) {
			return false;
		}
		// Creates a new empty link list on the heap and set trains to reference
		// it
		trains = new LinkedList<Train>();
		return true;
	}

	/**
	 * Display the specific train for a destination.
	 *
	 * @param dest
	 *            Destination city for the train the to be displayed.
	 * @return True if train to the given destination city exists. If not, then
	 *         return false.
	 */
	public boolean displayTrain(String dest) {
		// Finds the train with the given destination
		Train displayTrain = findTrain(dest);
		boolean found = false;
		// If the train was found iterate through it and print the name of each
		// cargo car
		if (displayTrain != null) {
			found = true;
			LinkedListIterator<CargoCar> displayTrainIterator = displayTrain.iterator();
			if (displayTrain.numCargoCars() == 0) {
				System.out.print("(" + displayTrain.getDestination() + ")");
			} else {
				System.out.print("(" + displayTrain.getDestination() + ")->");
			}
			// If the train has more cars to display print the name and weight
			// of each one
			while (displayTrainIterator.hasNext()) {
				CargoCar currCar = displayTrainIterator.next();
				System.out.print(currCar.getName() + ":" + currCar.getWeight());
				if (displayTrainIterator.hasNext()) {
					System.out.print("->");
				}
			}
			System.out.print("\n");
		}
		// Return true if a train with the given destination was found
		return found;
	}

	/**
	 * This method is used to display all the departing trains in the train hub.
	 * Train should be displayed in the specified format.
	 *
	 * @return True if there is at least one train to print. False if there is
	 *         no train to print.
	 */
	public boolean displayAllTrains() {
		// Iterates through the list of trains and displays each one. Returns
		// true
		// if there was at least one train
		LinkedListIterator<Train> trainIterator = trains.iterator();
		boolean returnBoolean = trainIterator.hasNext();
		while (trainIterator.hasNext()) {
			Train currTrain = trainIterator.next();
			// Displays the current train in the iteration
			displayTrain(currTrain.getDestination());
		}
		return returnBoolean;
	}

	/**
	 * Move all cargo cars that match the cargo name from a source (src) train
	 * to a destination (dst) train.
	 *
	 * The matching cargo cars are added before the first cargo car with a name
	 * match in the destination train.
	 *
	 * All matching cargo is to be moved as one chain of nodes and inserted into
	 * the destination train's chain of nodes before the first cargo matched
	 * node.
	 *
	 * PRECONDITION: there is a source train and a destination train, and the
	 * source train of nodes has at least one node with matching cargo. We will
	 * not test other conditions.
	 *
	 * NOTE: This method requires direct access to the chain of nodes of a Train
	 * object. Therefore, the Train class has a method in addition to ListADT
	 * methods so that you can get direct access to header node of the train's
	 * chain of nodes.
	 *
	 * @param src
	 *            a reference to a Train that contains at least one node with
	 *            cargo.
	 *
	 * @param dst
	 *            a reference to an existing Train. The destination is the train
	 *            that will have the cargo added to it. If the destination chain
	 *            does not have any matching cargo, add the chain at its correct
	 *            location alphabetically. Otherwise, add the chain of cargo
	 *            nodes before the first matching cargo node.
	 *
	 * @param cargoName
	 *            The name of cargo to be moved from one chain to another.
	 */
	public static void moveMultipleCargoCars(Train srcTrain, Train dstTrain, String cargoName) {
		// TODO Implement this method last. It is not needed for other parts of
		// program

		// get references to train header nodes
		// get references to train header nodes
		Listnode<CargoCar> srcHeader, dstHeader, prev, curr, currDst, curr2Dst;
		srcHeader = srcTrain.getHeaderNode();
		dstHeader = dstTrain.getHeaderNode();

		Listnode<CargoCar> first_prev = null, first = null, last = null;
		boolean hasFound = false;

		// 1. Find references to the node BEFORE the first matching cargo node
		// and a reference to the last node with matching cargo.
		curr = srcHeader;

		while (curr.getNext() != null) {
			if (!curr.getNext().getData().getName().equalsIgnoreCase(cargoName)) {
				curr = curr.getNext();
			}
			//System.out.println(curr.getData().getWeight());
			if (curr.getNext().getData().getName().equalsIgnoreCase(cargoName)) {
				break;
			}
		}
		first_prev = curr;
		first = curr.getNext();
		last = first;

		// last points to the last cargo car with same name
		while (last.getData().getName().equalsIgnoreCase(cargoName) && last.getNext() != null) {
			// Case when only one cargo car in chain
			if (!last.getNext().getData().getName().equalsIgnoreCase(cargoName)) {
				break;
			}
			last = last.getNext();
		}

		// NOTE : We know we can find this cargo,
		// so we are not going to deal with other exceptions here.

		// 2. Remove from matching chain of nodes from src Train
		// by linking node before match to node after matching chain
		curr.setNext(last.getNext());
		// 3-1. Find reference to first matching cargo in dst Train
		currDst = dstHeader.getNext();
		while (currDst != null) {
			if (currDst.getData().getName().equalsIgnoreCase(cargoName)) {
				hasFound = true;
				break;
			}
			currDst = currDst.getNext();
		}

		// 3-2. If found, insert them before cargo found in dst
		if (hasFound) {
			curr2Dst = dstHeader;
			while (curr2Dst.getNext() != currDst) {
				curr2Dst = curr2Dst.getNext();
			}
			last.setNext(currDst);
			curr2Dst.setNext(first);
		}
		// 3-3. If no matching cargo, add at correct location in dst
		if (dstTrain.numCargoCars() == 0) {
			last.setNext(currDst);
			curr2Dst = dstHeader;
			curr2Dst.setNext(first);
		}
		// If no match, add in alphabetical order
		if (!hasFound) {
			String cargo = first.getData().getName();
			int alphaPos = getAlphabeticalIndex(cargo, dstTrain);
			Listnode<CargoCar> counterNode = dstTrain.getHeaderNode();
			for (int i = 0; i < alphaPos; i++) {
				if (counterNode.getNext() == null) {
					break;
				}
				counterNode = counterNode.getNext();
			}
			last.setNext(counterNode.getNext());
			counterNode.setNext(first);
		}
	}

	/**
	 * Returns true if the train hub contains the train to the given destination
	 * 
	 * @param destination,
	 *            the destination you want to send the cargo to
	 *
	 * @return true if the train hub contains a train to the given destination
	 */
	private boolean hubContainsOutGoingTrain(String destination) {
		// Iterates through the trainHub to find a train to the given
		// destination
		// Returns true if it is found
		LinkedListIterator<Train> trainsIterator = trains.iterator();
		boolean found = false;
		while (trainsIterator.hasNext()) {
			Train train = trainsIterator.next();
			if (train.getDestination().equalsIgnoreCase(destination)) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Returns the train which matches the given destination
	 * 
	 * @param destination,
	 *            the destination you want to send the cargo to
	 *
	 * @return the Train with the given destination
	 */
	private Train returnTrainToDestination(String destination) {
		// Iterates through the trainHub to find the train to the given
		// destination
		LinkedListIterator<Train> trainsIterator = trains.iterator();
		while (trainsIterator.hasNext()) {
			Train train = trainsIterator.next();
			if (train.getDestination().equalsIgnoreCase(destination)) {
				return train;
			}
		}
		// Return null if the train isn't found
		return null;
	}

	/**
	 * Returns the index corresponding of cargoName in the given train
	 * 
	 * @param cargoName
	 *            the cargo that will be added
	 * @param train
	 *            the train to add to
	 * 
	 * @return the index where cargoName is located
	 */
	private int getCargoIndex(String cargoName, Train train) {
		int pos = 0;
		LinkedListIterator<CargoCar> iterator = train.iterator();
		// Iterates through the train to see if it contains cargoName and
		// returns its pos
		// Returns 0 if the train does not contain cargoName
		while (iterator.hasNext()) {
			CargoCar currentCar = iterator.next();
			if (currentCar.getName().equalsIgnoreCase(cargoName)) {
				return pos;
			}
			pos++;
		}
		return 0;
	}

	/**
	 * Returns true if the given train contains cargoName
	 * 
	 * @param cargoName
	 *            the cargo that will be added
	 * @param train
	 *            the train to add to
	 * 
	 * @return true if train contains cargo
	 */
	private boolean trainContainsCargo(String cargoName, Train train) {
		boolean found = false;
		LinkedListIterator<CargoCar> iterator = train.iterator();
		// Iterates through train to see if it contains cargoName
		while (iterator.hasNext()) {
			CargoCar currentCar = iterator.next();
			if (currentCar.getName().equalsIgnoreCase(cargoName)) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Returns the index corresponding to correct alphabetical place if
	 * cargoName were to be inserted into train
	 * 
	 * @param cargoName
	 *            the cargo that will be added
	 * @param train
	 *            the train to add to
	 * 
	 * @return the index where cargoName should be added
	 */
	private static int getAlphabeticalIndex(String cargoName, Train train) {
		LinkedListIterator<CargoCar> iterator = train.iterator();
		int pos = 0;
		while (iterator.hasNext()) {
			CargoCar nextCar = iterator.next();
			String nextCarName = nextCar.getName();
			// If the given cargoName is greater than the current name exit the
			// loop
			if (cargoName.compareTo(nextCarName) < 0) {
				break;
				// If the given cargoName is less than the current name
				// increment pos
			} else {
				pos++;
			}
		}
		return pos;
	}
}
