package mhat.sands.comparer;



public class HashComparer {
	//This is going to need to be build to handle large files
	//Over 200k lines we need to start breaking it up.
	
	private String inputfile;
	
	public HashComparer(String inputfile){
		this.inputfile = inputfile;
	}
	
	
	
	
	//Scenario 1 - Check collision file
	// Collisions should only happen near the same time -- sorting unneccesary
	// Files should be small.
	
	//Scenario 2 - Compare Generated Collisions to collision file
	// Collision file still small
	// Generated Collisions can be quite large.
	// Unique Collisions file
	// Add Unique Collisions file to Generated Collisions
	// Sort Generated Collisions
	// Compare like scenario 1
	
	//Scenario 3 - Generate Potential Collisions and compare against collision file.
	// collision file smaller, can be placed into a hashset
	// permutations created and tested in memory, no need for scenario 2
	
	//Generate hashes on the fly
	
	//Scenario 4 - We figured out the order of data
	//Need to create permutations and send on the fly
	
	

}
