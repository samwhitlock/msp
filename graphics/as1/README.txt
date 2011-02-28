CS184 Spring 2011
Author: Sam Whitlock
Class account: cs184-ck

Assignment 1

Tested platform: Ubuntu Linux 10.10

Extra Features:
Instead of crashing on the next user input after a shape has been completed as described in the spec, the program flow restarts. This entails:
* Clearing the screen of all vertices
* Running normally as it did on the first run.
* OVERWRITING the previous polygon.obj file when the shape is completed as per the spec.
Please note that pressing the escape key to exit at any time is essentially interrupting this loop. I state this so the user doesn't have to worry about such behavior as having the polygon.obj file from the previous iteration be erased before they complete it (i.e. it doesn't erase on restart).

Non-STL Dependencies
* GLUT (needs to be defined in environment)
* OpenGL (needs to be defined in environment)
* algebra3.h
In other words, GLUT and OpenGL need to be installed on the machine because they are not included in the package I submitted.

------------
Submission Objects
- Code in obvious places.
- OBJ and accompanying screen capture images are in the submission directory. Each different shape I created for the screen capture has its own directory, wherein the OBJ file and the cropped to OS window PNG image file can be found.
