/*
 * CS 4320 Assignment 3
 * Part 2: Implementation
 * Sishir Mohan, Wei Wang, Wodan Zhou
 * 
 */

import java.util.*;

public class FDChecker {

	/**
	 * Checks whether a decomposition of a table is dependency
	 * preserving under the set of functional dependencies fds
	 * 
	 * @param t1 one of the two tables of the decomposition
	 * @param t2 the second table of the decomposition
	 * @param fds a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is dependency preserving, false otherwise
	 **/
	public static boolean checkDepPres(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		//your code here
		//a decomposition is dependency preserving, if local functional dependencies are
		//sufficient to enforce the global properties
		//To check a particular functional dependency a -> b is preserved, 
		//you can run the following algorithm
		//result = a
		//while result has not stabilized
		//	for each table in the decomposition
		//		t = result intersect table 
		//		t = closure(t) intersect table
		//		result = result union t
		//if b is contained in result, the dependency is preserved
		int sizeBefore;
		
		List<AttributeSet> ab = new ArrayList<AttributeSet>();
		//add the attribute sets to the array list
		ab.add(t2); 
		ab.add(t1);
		Object[] f = fds.toArray(); //convert to array of objects, easier to loop through
		
		
		for(int i = 0; i < f.length; i++) //for each local functional dependency
		{
			FunctionalDependency func = (FunctionalDependency) f[i];
			AttributeSet result = (AttributeSet)func.left.clone(); //result = a
			Attribute b = func.right; //right side in a --> b
			
			int sizeNow = result.size();
			do { //execute and then check to see if result is not stable
				sizeBefore = sizeNow;
				
				for(AttributeSet table : ab) //for each table in the decomposition 
				{
					AttributeSet t = action("Intersection",result, table); // t = result intersect table
					 //t = closure(t) intersect table
					t = action("Intersection",closure(t, fds), table);
					//result = result union t
					result = action("Union",result, t);
				}
				sizeNow = result.size();
			}while(sizeBefore < sizeNow);
			if(!result.contains(b))  //b is not contained in the result, dependency not preserved
			{
				return false;
			}
			
		}
		return true; //b is contained in the result, the dependency is preserved
	}

	/**
	 * Checks whether a decomposition of a table is lossless
	 * under the set of functional dependencies fds
	 * 
	 * @param t1 one of the two tables of the decomposition
	 * @param t2 the second table of the decomposition
	 * @param fds a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is lossless, false otherwise
	 **/
	public static boolean checkLossless(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) 
	{
		//your code here
		//Lossless decompositions do not lose information, the natural join is equal to the 
		//original table.
		//a decomposition is lossless if the common attributes for a superkey for one of the
		//tables.
		boolean isLossless = false; //flag
		
		AttributeSet isLoss = closure(action("Intersection", t1, t2), fds); //closure(t1 intersect t2)
		if(isLoss.containsAll(t2) || isLoss.containsAll(t1)) //compare  with t1 and t2
		{
			isLossless = true; //if either statement is true, decomposition is lossless
		}
		return isLossless;

	}
	
	private static AttributeSet action(String action, AttributeSet t1, AttributeSet t2)
	{
		if(action.equals("Intersection")) //union
		{
			AttributeSet intersection = new AttributeSet(); //set to store the attributes after intersecting
			for(Attribute attribute: t1)
			{
				if(t2.contains(attribute)) //if contains, means it is in both sets, therefore add to intersection
				{
					intersection.add(attribute);
				}
			}
			return intersection;
		}
		else //compute union
		{
			AttributeSet union = new AttributeSet();
			union = (AttributeSet)t2.clone(); //add t2 to the union set
			for(Attribute attribute : t1) //iterate through t1
			{
				if(!t2.contains(attribute))
				{
					union.add(attribute); //add everything that is not in t2, but in t2 to the union set
				}
			}
			return union;
		}
	}
	

	//recommended helper method
	//finds the total set of attributes implied by attrs
	private static AttributeSet closure(AttributeSet attrs, Set<FunctionalDependency> fds) 
	{
		AttributeSet prevAttrs = new AttributeSet();
		AttributeSet newAttrs = new AttributeSet();
		newAttrs = attrs;
		int prevSize = prevAttrs.size(); //size of F
		int attrSize = newAttrs.size();//size of F+
		Object[] f = fds.toArray();
		
		do
		{
			
			prevAttrs = (AttributeSet) newAttrs.clone();
			for(int i = 0; i < f.length; i++)
			{
				FunctionalDependency func = (FunctionalDependency) f[i];
				if(newAttrs.containsAll(func.left))
				{
					newAttrs.add(func.right); //add to create F+
				}
			}
		}while(prevAttrs.size() < newAttrs.size());
		
		return newAttrs;
		
	}
}
