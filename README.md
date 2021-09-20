***SOME INFORMATION ABOUT THE DESIGN***

The view is completely separate from the functionality of the program so the UI can be changed with no necessary modifications to the program itself.
The functionality is managed by a controller named ProductManagementFunctions and the view uses the interface of this class. This controller does not
print anything but returns the requested string for the view class to print. The exceptions are caught in the view class as well.

Product and User classes are abstract and are extended by "Assembly, Part" and "Admin, Manager, Employee" classes respectively. 

Assembly is a composite class and it uses the class Product. Part uses the class CatalogueEntry. Part does not have any information about the assembly
it is connected to. This is to reduce coupling. We accomplished the cascading status change functionality by making the Product class implement an
interface called StatusChangeCallback. The parts and the assemblies implement the method onChildStatusChange to let the assembly on top of them know
that a status of a child has changed.

User and Product data is held in UserRepository and ProductRepository respectively. These two classes also have interfaces which are used by the classes
ProductManagementFunctions and InputOutputOperations. 

InputOutputOperations is used to read, write and parse the JSON files.

And finally the Product class uses an abstract class called StatusState to handle status changes. In the current shape of the program, these state classes
do not add much functionality but they add flexibility if in the future each Status needs to do different things.

***LOGIN CREDENTIALS FOR USERS CURRENTLY IN THE SYSTEM***

username: admin
password: 1234
role: admin

username: man1
password: 1234
role: manager

username: emp
password: 123
role: employee

***CURRENT HIERARCHY OF PRODUCTS AND USERS***

name: ass1 (assigned to "man1")
number: 111
cost: 10 (updated dynamically)
status: Not Started
connected assemblies: none
connected parts: {
	name: part (assigned to "emp")
	number: 456
	cost: 10
	status: Not Started
}

***CURRENT LIST OF CATALOGUE ENTRIES***

name: part
number: 456
cost: 10

name: part2
number: 457
cost: 10

name: part3
number: 458
cost: 10

name: part4
number: 459
cost: 10
