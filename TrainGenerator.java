
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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * This class provide methods for generating a Train.
 * 
 * COMPLETE THIS CLASS and HAND IN THIS FILE
 * 
 * @see Config
 */
public class TrainGenerator {

	/**
	 * Get a new train generated randomly. The constant variables for
	 * determining how many cargo, what cargo and how heavy are in
	 * {@link Config}.
	 * 
	 * @return a train generated randomly
	 */
	public static Train getIncomingTrain() {
		Train incomingTrain = new Train("TrainHub");

		Random rand = new Random(System.currentTimeMillis());

		// How many freight cars
		int cartNum = rand.nextInt(Config.MAX_CART_NUM - Config.MIN_CART_NUM + 1) + Config.MIN_CART_NUM;

		for (int i = 0; i < cartNum; i++) {
			// What kind of cargo
			int loadIndex = rand.nextInt(Config.CARGO_ARRAY.length);
			String load = Config.CARGO_ARRAY[loadIndex];

			// How heavy
			int weight = rand.nextInt(Config.MAX_WEIGHT - Config.MIN_WEIGHT + 1) + Config.MIN_WEIGHT;

			// Where to
			int destIndex = rand.nextInt(Config.DEST_ARRAY.length);
			String dest = Config.DEST_ARRAY[destIndex];

			incomingTrain.add(new CargoCar(load, weight, dest));
		}

		return incomingTrain;
	}

	/**
	 * Get a new train generated from a file. Files for generating a train must
	 * have the format as follow
	 * <p>
	 * {destination},{cargo},{weight}<br>
	 * {destination},{cargo},{weight}<br>
	 * ...
	 * <p>
	 * where {destination} is a string among Config.DEST_ARRAY, {cargo} is a
	 * string among Config.CARGO_ARRAY, and {weight} is a string for any
	 * positive integer.
	 * <p>
	 * Ignore the line if it is not matched in this format. See the sample
	 * in/outputs provided in the assignment description to get more details.
	 * 
	 * @param filename
	 *            train input file name
	 * @return a train generated from a file
	 */
	public static Train getIncomingTrainFromFile(String filename) {
		// Create new file object and new Train to hold incoming cars
		File file = new File(filename);
		Train newTrain = new Train("lala land");
		try {
			// Open file with scanner and create strings to hold input
			Scanner scnr = new Scanner(file);
			String line = null;
			String[] trainInfo = null;
			// Read each line
			while (scnr.hasNextLine()) {
				// Split each line at the comma
				String nameofCargo = null;
				int weight = 0;
				String destination = null;
				line = scnr.nextLine();
				trainInfo = line.split(",");

				// Error checking
				if (trainInfo.length != 3) {
					continue;
				}
				// Trim the input
				destination = trainInfo[0].trim();
				nameofCargo = trainInfo[1].trim();
				// Check that input matches required format
				try {
					weight = Integer.parseInt(trainInfo[2].trim());
					if (weight < 0) {
						continue;
					}
				} catch (Exception e) {
					continue;
				}
				// Fix the generic trains destination
				CargoCar newCar = new CargoCar(nameofCargo, weight, destination);
				newTrain.add(newCar);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error occured to load a train from file : ");
			e.printStackTrace();
		}
		// Return the newly loaded train
		return newTrain;
	}
}
