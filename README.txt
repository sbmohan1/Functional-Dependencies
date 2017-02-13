READ ME


The first method, boolean checkDepPres essentially just uses the algorithm provided on the instructions and in the comments and computes it for each FunctionalDependency in fds. It uses helper functions such as closure and action.

The second method, boolean checkLossless first computes the intersection of t1 and t2, then computes the closure of that intersection. If the closure of the intersection contains all of t2 or all of t1, then the decomposition is lossless. 

The third method is a helper method that computes either intersection or union based on the parameter that is passed in. To compute intersection, loop through one of the attribute sets (t1) and if t2 contains any of those, then the similar ones are added to a new attribute set called intersection. To compute union, clone one of the attribute sets into a new one called union. Then find all attributes in t2 that do not belong to union. These are then added to the attribute set named union as well.

The fourth method is the closure helper method which instantiates two new AttributeSets. It also converts fds to an array of Objects, making it easier to iterate over. Then a do while loop executes that checks to see if the functional dependency on the left side of the arrow is contained in the newAttributes set. If so, it adds the attributes on the right side of the arrow to newAttrs. This goes on until the previous set of attributes is no longer less than the new set of attributes. Return type is AttributeSet.


