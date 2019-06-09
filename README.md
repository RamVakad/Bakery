
# Bakery


## Usage:

  - Import into IntelliJ IDEA
  - Change input file name argument through run configuration
  - (OR) Command Line: Compile, then "java Main [inputFilename.txt]"
  
## Example Outputs:

    Reading input file ... 
	New instance of bakery created with 5 different shapes.
	Valid Order: 2 G
    Valid Order: 5 R
    Valid Order: 1 R
    Valid Order: 5 R 1 R 4 G
    Valid Order: 3 R
    Valid Order: 5 R
    Valid Order: 3 R 5 R 1 R
    Valid Order: 3 R
    Valid Order: 2 G
    Valid Order: 5 R 1 R
    Valid Order: 2 G
    Valid Order: 5 R
    Valid Order: 4 G
    Valid Order: 5 R 4 G
	Finding best baking configuration ... 
	Config lock for Shape 5 exists: R
	Config lock for Shape 3 exists: R
	Config lock for Shape 2 exists: G
	Config lock for Shape 2 exists: G
	Config lock for Shape 5 exists: R
	Iterating Configuration: R	G	R	G	R	
	Cheapest bake configuration that will make all customers happy: 
	R	G	R	G	R


-----

	Reading input file ... 
	New instance of bakery created with 1 different shapes.
	Valid Order: 1 R
    Valid Order: 1 G
	Finding best baking configuration ... 
	Config lock for Shape 1 exists: R
	Config lock conflict with: G. No solution exists.
	Exiting.
	
-----

	Reading input file ... 
    New instance of bakery created with 5 different shapes.
    Valid Order: 1 G 3 R 5 R
    Valid Order: 2 R 3 G 4 R
    Valid Order: 5 G
    Finding best baking configuration ... 
    Iterating Configuration: R	R	R	R	G	
    Cheapest bake configuration that will make all customers happy: 
    R	R	R	R	G	
	
-----

	Reading input file ... 
	New instance of bakery created with 2 different shapes.
	Valid Order: 1 R 2 G
    Valid Order: 1 G
	Finding best baking configuration ... 
	Iterating Configuration: G	R	
	Iterating Configuration: G	G	
	Cheapest bake configuration that will make all customers happy: 
	G	G