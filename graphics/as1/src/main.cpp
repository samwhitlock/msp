#include <vector>
#include <iostream>
#include <fstream>
#include <cmath>

//Include our math library
#include <algebra3.h>

#ifdef OSX
#include <GLUT/glut.h>
#include <OpenGL/glu.h>
#else
#include <GL/glut.h>
#include <GL/glu.h>
#endif

using namespace std;

class Viewport {
public:
	int w, h; // width and height
};

class Vertex {
public:
	Vertex(const double x = 0.0, const double y = 0.0) {
		position[VX] = x;
		position[VY] = y;
	}
	
	double getX() const {
		return position[VX];
	}

	double getY() const {
		return position[VY];
	}

	void set(const double x, const double y) {
		position[VX] = x;
		position[VY] = y;
	}
protected:
	vec2 position;
};

class Polygon {
public:
	Polygon() {
		done = false;//FIXME: Is this necessary?
	}
	
	/*
	* Write this polygon out to an OBJ file.
	*
	* Write out the polygon to polygon.obj in the current directory as an OBJ file.
	*/
	void serialize() const {
		ofstream objFile("polygon.obj", ios::out);
		objFile << "#Author: Sam Whitlock" << endl;
		
		//Print out each vertex
		for ( unsigned int i = 0; i < vertices.size(); i++ ) {
			//Print out the vertex
			Vertex temp = vertices[i];
			objFile << "v " << temp.getX() << " " << temp.getY() << endl;
		}

		//Print out the order line
		objFile << "f";

		for ( unsigned int i = 1; i < vertices.size() + 1; i++ ) {
			objFile << " " << i;
		}

		objFile << endl;

		objFile.close();
	}

	/*
	* Ask the polygon to draw itself.
	* 
	* x and y are the mouse coordinates, which are used for the rubberbanding effect.
	*/
	void draw(Vertex &v) const {
		//We only want to draw something if there is a 
		if ( !vertices.empty() ) {
			//Draw all the vertices
			
			glBegin(GL_LINE_STRIP);

			for (unsigned int i = 0; i < vertices.size(); i++) {
				Vertex temp = vertices[i];
				glVertex2f(temp.getX(), temp.getY());
			}

			if ( done ) {
				glVertex2f(vertices[0].getX(), vertices[0].getY());
			} else {
				glVertex2f(v.getX(), v.getY());
			}

			glEnd();
		}
	}

	/*
	* Add a vertice to the collection of vertices in this polygon.
	*
	* This method does not automatically call the draw method.
	*/
	void addVertex(Vertex &v) {
		//Make a new vec2 and add it to the end of vertices
		vertices.push_back(v);
	}

	/*
	* Close the polygon's shape by connecting back to the beginning line
	*/
	void closeShape() {
		if ( !vertices.empty() ) {//safety check
			done = true;
		}
	}

	/*
	* Returns true if this polygon has been closed.
	*/
	bool completed() const {
		return done;
	}

	/*
	* Removes all the vertices.
	*/
	void reset() {
		done = false;
		vertices.clear();
	}
private:
	/*
	* A collection of vertices.
	*
	* The polygon start with the lowest index and terminates (in partial state, potentially) 
	* with the highest-indexed item.
	*/
	vector<Vertex> vertices;
	bool done;
};

//****************************************************
// Global Variables
//****************************************************
Viewport viewport;
Polygon polygon;
Vertex vertex;//to keep track of the rubberbanded vertex
bool trackMouse;
//-------------------------------------------------------------------------------
void display()
{
	//Clear Buffers     
 	glClear(GL_COLOR_BUFFER_BIT);
	glMatrixMode(GL_MODELVIEW);					// indicate we are specifying camera transformations
	glLoadIdentity();							// make sure transformation is "zero'd"

	//Call the draw method on the polygon
	polygon.draw(vertex);
	
	//Now that we've drawn on the buffer, swap the drawing buffer and the displaying buffer.
	glutSwapBuffers();
}

//-------------------------------------------------------------------------------
/// \brief	Called when the screen gets resized
/// 
void reshape(int w, int h)
{
	//Set up the viewport to ignore any size change.
	glViewport(0,0,viewport.w,viewport.h);
	
	//Set up the PROJECTION transformationstack to be a simple orthographic [-1, +1] projection
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(-1, 1, -1, 1, 1, -1);	// resize type = stretch
	
	//Set the MODELVIEW transformation stack to the identity.
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}

//-------------------------------------------------------------------------------
///

void myKeyboardFunc (unsigned char key, int x, int y) {
	switch (key) {
		case 27:			// Escape key
			exit(0);
			break;//This probably doesn't need to be here
	}
}

void myTrackingFunc(int x, int y) {
	if (trackMouse) {
		vertex.set(((double)x - viewport.w/2)/((double)(viewport.w/2)),
				-((double)y - viewport.h/2)/((double)(viewport.h/2)));
		glutPostRedisplay();
	}
}

//-------------------------------------------------------------------------------
///
void myMouseFunc( int button, int state, int x, int y ) {
	if ( state == GLUT_DOWN ) {
		if ( button == GLUT_RIGHT_BUTTON ) {
			//Completed the polygon
			polygon.closeShape();
			polygon.serialize();
			glutPostRedisplay();
		} else if ( button == GLUT_LEFT_BUTTON ) { 
			if ( polygon.completed() )
				polygon.reset();

			trackMouse = true;
			myTrackingFunc(x,y);//This doesn't get called unless the mouse moves
		}
	} else if ( button == GLUT_LEFT_BUTTON && state == GLUT_UP ) {
		trackMouse = false;
		polygon.addVertex(vertex);
	}
}

//-------------------------------------------------------------------------------
///
int main(int argc,char** argv)
{
	//Initialize OpenGL
	glutInit(&argc,argv);
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGBA);
	
	//Set up global variables
	viewport.w = 600;
	viewport.h = 600;
	
	//Create OpenGL Window
	glutInitWindowSize(viewport.w,viewport.h);
	glutInitWindowPosition(0,0);
	glutCreateWindow("Polygonal Drawing");
	
	//Register event handlers with OpenGL.
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(myKeyboardFunc);
	glutMouseFunc(myMouseFunc);
	glutMotionFunc(myTrackingFunc);
	
	//Set the mouse to be a cross-hair
	glutSetCursor(GLUT_CURSOR_CROSSHAIR);

	//And Go!
	glutMainLoop();
}
