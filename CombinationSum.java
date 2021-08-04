import java.util.*;

//This algorithm calculates the possible combinations of numbers whose sums equal a target number
public class CombinationSum{	
	
	
	//Main method used to set up the hardcoded target and list to pass through the wrapper method
	public static void main(String[] args){
		
		//create list for current sums
		ArrayList<Integer> sums = new ArrayList<Integer>();
		
		//hardcoded target number
		int target = 15;
		
		//hardcoded list of integers
		Integer[] numbers = {3,9,8,4,5,7,10};
		
		//calls the wrapper method
		findSums(new ArrayList<Integer>(Arrays.asList(numbers)), target, sums);
		
		
	}
	


	//Wrapper method that calls the recursive method that conducts the algorithm
	// Parameters:
	//	list represents the list of numbers used to calculate the combinations whose sums equal the target number
	//	target represents the target sum that the combinations printed will equal
	//	currentSums represents the current list of sums that are getting passed through the recursion

	public static void findSums(ArrayList<Integer> list, int target, ArrayList<Integer>  currentSums){
	
		//calls the private recursive method
		find_sums_helper(list, target, currentSums);
		
		
	}
	
	//Recursive method that calculates and prints the possible combinations of numbers whose sums equal the target number
	// Parameters:
	//	list represents the list of numbers used to calculate the combinations whose sums equal the target number
	//	target represents the target sum that the combinations printed will equal
	//	currentSums represents the current list of sums that are getting passed through the recursion
	private static void find_sums_helper(ArrayList<Integer> list, int target, ArrayList<Integer>  currentSums){
		//represents the current sum of currentSums
		int currentSum = 0;
		
		//calculate the current sum of all elements combined in currentsums
		for (int value: currentSums){
			currentSum += value;
		}
		
		//if the current sum equals the target sum, then print it to the command line
		if (currentSum == target){
			System.out.println(currentSums);
			return;
		}
		
		//if the current sum has exceeded the target sum, break out of the recursion
		if (currentSum > target){
			return;
		}
		
		 //loop through the elements in the list
		for (int i = 0; i < list.size(); i++){
			
			//represents the list that will be passed through the recursion
			ArrayList<Integer> reducedList = new ArrayList<Integer>();
			
			//get the current entry in the list
			int entry = list.get(i);
			
			//add it to the reduced list in order to allow it to repeat in the combination
			reducedList.add(entry);
			
			//create the new list that will be passed in after current value is added
			for (int j = i + 1; j < list.size(); j++){
				
				reducedList.add(list.get(j));
			}
			
			
			//create a deep copy of the currentSums list and add the current entry to it
			ArrayList<Integer> partial_rec = new ArrayList<Integer>(currentSums);
            		partial_rec.add(entry);
            
            		//recursively call the method
            		find_sums_helper(reducedList,target,partial_rec);
            
            		//remove the duplicate current value from the list
            		reducedList.remove(0);
			
		}
		
		
		
	
	}



}
