#include "main.h"

using namespace std;

//****************************************************
// Some Classes
//****************************************************
class Viewport {
public:
    Viewport(): mousePos(0.0,0.0) { /* empty */ };
	int w, h; // width and height
	vec2 mousePos;
	vec2* mouseDownPos;//Used to track the click of a mouse for click-drag-release
};
enum MouseState { NO_CLICK, LEFT_CLICK, RIGHT_CLICK };
MouseState mouseState = NO_CLICK;
//****************************************************
// Global Variables
//****************************************************
Viewport viewport;
Polygon * polygon;
Vertex * tempVertex;
UCB::ImageSaver * imgSaver;
vec2 * oldEnd;
static bool morphMode;//When true, right mouse button is clicked, so draw that way
static bool exportMode;//when true, does not respond to user input
//-------------------------------------------------------------------------------
/// You will be calling all of your drawing-related code from this function.
/// Nowhere else in your code should you use glBegin(...) and glEnd() except code
/// called from this method.
///
/// To force a redraw of the screen (eg. after mouse events or the like) simply call
/// glutPostRedisplay();
void draw(const double t) {
	//Clear Buffers
	glClear(GL_COLOR_BUFFER_BIT);
	glMatrixMode(GL_MODELVIEW);					// indicate we are specifying camera transformations
	glLoadIdentity();							// make sure transformation is "zero'd"

	if (exportMode) {
		polygon->draw(t, false);
	} else {
		//Draw the polygon in its starting state
		polygon->draw();
		if (morphMode) {
			polygon->draw((viewport.mousePos[0]+1.0)/2.0);
		} else {
			//Draw the polygon in its finished state
			polygon->draw(1.0);
		}
	}
	//Now that we've drawn on the buffer, swap the drawing buffer and the displaying buffer.
	glutSwapBuffers();
}

void display() {
	draw(0.0);
}

//-------------------------------------------------------------------------------
/// \brief	Called when the screen gets resized.
/// This gives you the opportunity to set up all the relevant transforms.
void reshape(int w, int h) {
	//Set up the viewport to ignore any size change.
	int min = w > h ? h : w;
	viewport.w = min;
	viewport.h = min;

	glViewport(0,0,min,min);

	//Set up the PROJECTION transformationstack to be a simple orthographic [-1, +1] projection
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(-1, 1, -1, 1, 1, -1);	// resize type = stretch

	//Set the MODELVIEW transformation stack to the identity.
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	
	glutPostRedisplay();
}

void exportBMP() {
	exportMode = true;
	for (unsigned int i = 0; i < 100; ++i) {//100 frames of animation
		draw(((double)(i))/100.0);
		imgSaver->saveFrame(viewport.w, viewport.h);
	}
	exportMode = false;
	glutPostRedisplay();
}

//-------------------------------------------------------------------------------
/// Called to handle keyboard events.
void myKeyboardFunc (unsigned char key, int x, int y) {
	if (mouseState == NO_CLICK ) {
		switch (key) {
			case 27:			// Escape key
				exit(0);
				break;
			case 'a':
				exportBMP();
				break;
			case 's':
				polygon->writeAsOBJ("polygon2.obj", 1.0);
				break;
		}
	}
}

//-------------------------------------------------------------------------------
/// This function is used to convert the pixel coordinates on the screen to the
/// coordinates of your view volume. Thus, it converts a pixel location to a coordinate
/// that can be used for picking and selection.
vec2 inverseViewportTransform(vec2 screenCoords) {
    //Create a vec2 on the local stack. See algebra3.h
    vec2 viewCoords(0.0,0.0);

    viewCoords[0] = ((float)screenCoords[0] - viewport.w/2)/((float)(viewport.w/2));
    viewCoords[1] = ((float)screenCoords[1] - viewport.h/2)/((float)(viewport.h/2));
    //Flip the values to get the correct position relative to our coordinate axis.
    viewCoords[1] = -viewCoords[1];

    //C++ will copy the whole vec2 to the calling function.
    return viewCoords;
}

//-------------------------------------------------------------------------------
///
void myMouseFunc( int button, int state, int x, int y ) {
    //Convert the pixel coordinates to view coordinates.
    vec2 screenCoords((double) x, (double) y);
    vec2 viewCoords = inverseViewportTransform(screenCoords);

	if ( state==GLUT_DOWN && mouseState == NO_CLICK ) {
		if ( button == GLUT_LEFT_BUTTON ) {
			mouseState = LEFT_CLICK;
			tempVertex = polygon->getClosestVertex(viewCoords, 9.0/(double)viewport.w);

			if (tempVertex != NULL) {
				//Set old mouse pos
				viewport.mouseDownPos = new vec2(viewCoords);
				oldEnd = new vec2(tempVertex->getEnd());
			}
		} else if ( button == GLUT_RIGHT_BUTTON ) {
			morphMode = true;
			mouseState = RIGHT_CLICK;
		}
		
		cout << viewCoords[0] << ", " << viewCoords[1] << endl;
	} else if ( state == GLUT_UP ) {
		if ( button == GLUT_LEFT_BUTTON && mouseState == LEFT_CLICK ) {
			if (tempVertex != NULL) {
				tempVertex = NULL;
				delete viewport.mouseDownPos;
				delete oldEnd;
			}
			mouseState = NO_CLICK;
		} else if ( button == GLUT_RIGHT_BUTTON && mouseState == RIGHT_CLICK ) {
			morphMode = false;
			mouseState = NO_CLICK;
		}
	}

	//Force a redraw of the window
	glutPostRedisplay();
}

//-------------------------------------------------------------------------------
/// Called whenever the mouse moves while a button is pressed
void myActiveMotionFunc(int x, int y) {
	//Record the mouse location for drawing crosshairs
	viewport.mousePos = inverseViewportTransform(vec2((double)x,(double)y));
    	
	if (tempVertex != NULL) {
		//Calculate how much the mouse has changed
		tempVertex->setEndPos(*oldEnd + (viewport.mousePos - *viewport.mouseDownPos));
	}

	//Force a redraw of the window.
	glutPostRedisplay();
}

//-------------------------------------------------------------------------------
/// Initialize the environment
int main(int argc,char** argv) {
	//Initialize OpenGL
	glutInit(&argc,argv);
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGBA);

	//Set up global variables
	viewport.w = 600;
	viewport.h = 600;

	if (argc < 2) {
	    cout << "USAGE: morph poly.obj" << endl;
	    exit(1);
	}

	//Initialize the screen capture class to save BMP captures
	//in the current directory, with the prefix "morph"
	imgSaver = new UCB::ImageSaver("./", "morph");

	//Parse the OBJ file.
	polygon = new Polygon(argv[1]);

	//Create OpenGL Window
	glutInitWindowSize(viewport.w,viewport.h);
	glutInitWindowPosition(0,0);
	glutCreateWindow("Morph");

	//Register event handlers with OpenGL.
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(myKeyboardFunc);
	glutMouseFunc(myMouseFunc);
	glutMotionFunc(myActiveMotionFunc);
	
	//Set the mouse to be a cross-hair
	glutSetCursor(GLUT_CURSOR_CROSSHAIR);

	//And Go!
	glutMainLoop();
}
