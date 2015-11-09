#Curve-Intersection-Project

About the code: Written in Java version 1.8.0_60, using Eclipse Mars Version 4.5.0

To run: compile the java files together in an Eclipse project or elsewhere.


About the project: This is a project to analyze the intersection patterns of closed geodesics on surfaces.
The applet produces a crude GUI which allows the user to input a gluing pattern for a polygon which determines a punctured surface, and an edge-crossing sequence which determines a closed curve on the surface.
The code then processes the input (if it's mathematically valid) and produces a visualization of the curve on the polygon which minimizes the intersection number. The code also determines whether or not the complement of the curve consists of one or more simply-connected pieces - a property called "filling".


Note: All surface word inputs should be written using the standard presentation for the fundamental group of S_g.
e.g. abAb, abABcdCD, etc...
Sample Input: Surface word: abAbcdCD Curve word: aBdcABCCBAb Output: A visualization of the free homotopy class of the curve aBdcABCCBAb with minimal self intersections on a punctured surface of genus two, and a determination of whether or not the free homotopy class is a filling curve.

