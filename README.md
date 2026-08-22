# Traffic Simulation - Design Brief
A running design brief for SSDCTN's traffic simulation project.  
_Please update this document in step with our progress, goals, and ideas._  


## Vehicle
Vehicles travel within `Road` objects (subclass of `Path`)  

A vehicle's default action is to accelerate towards its top speed.  
A vehicle can prevented from doing this from Road speed limits or obstacles.  

Vehicle's will have a collision zone that checks for obstacles and will alter their velocity accordingly.  

_Cyclist is the same as vehicles?_  


## Pedestrian
Pedestrians travel within `Footpath` objects (subclass of `Path`)  

Pedestrians can move in any direction, at or below a specified velocity.  


## Path
`Path` is the parent class for `Road` and `Footpath`  

A `Path` is a bounding box for objects.  
Paths may contain `Obstacle` objects, and subclasses contain other objects.  

### Road
A `Road` contains `Vehicle` objects and has a speed limit.  
Maybe also specify a direction of travel?  

### Footpath
A `Footpath` contains `Pedestrian` objects.  


## Obstacles
Obstacles prevent or alter the movement of Vehicles and Pedestrians who encounter them.  

Obstacles may be  
 - **stationary**, e.g. potholes, stopped vehicles
 - **dynamic**, e.g. moving vehicles, 

_From a vehicle's perspective, pedestrians crossing the road probably function as temporary stationary obstacle. Like, cars won't try and drive in between them_  


## Design Evolution
### Current Goal
A 4-way intersection with vehicles moving N <-> S and E <-> W.  
Traffic lights should control an alternating flow of vehicles through the intersection.  
Buses can stop at bus stops (holding up traffic)?

**First Conceptual Design** – _week 3 class_  
<img src="images/conceptualDesign1.jpg" width="400">  


### In next design, include:  
 - footpaths  
 - crossings for pedestrians  
 - bus stops
 - representation for all vehicle types
 - unified traffic light design