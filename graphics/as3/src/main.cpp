#include "main.h"

using namespace std;

class Viewport {
public:
    Viewport() { /* empty */ };
	int w, h; // width and height
};

//****************************************************
// File Variables
//****************************************************
static Scene *scene;
static UCB::ImageSaver * imgSaver;
static unsigned int frameCount = 0;
static Viewport viewport;

enum Level_Of_Depth { NONE, BOUNDING_BOX, OUTLINE, FULL };

static inline BoundingBox transformBoundingBox(BoundingBox & bb, mat4 & transformation) {
	//Extract the bounding box coordinates
	vec2 ur, ll;
	bb.getMax(ur);
	bb.getMin(ll);
	
	vec2 ul(ll[0], ur[1]), lr(ur[0], ll[1]);
	
	//Transform them into vec4's
	vec4 ur4(ur), ll4(ll), ul4(ul), lr4(lr);
	
	//Multiply them by the transformation
	vec4 ur4t = transformation * ur4, ll4t = transformation * ll4, ul4t = transformation * ul4, lr4t = transformation * lr4;
	vec2 ur4t2(ur4t), ll4t2(ll4t), ul4t2(ul4t), lr4t2(lr4t);
	
	BoundingBox returnBB;
	
	returnBB.expand(ur4t2);
	returnBB.expand(ll4t2);
	returnBB.expand(ul4t2);
	returnBB.expand(lr4t2);
	
	return returnBB;
}

//TODO: We can probably const a lot of the arguments for the sake of correctness and speed
static BoundingBox renderInstance(SceneInstance *si, vec3 color, int lod, const int time, const int depth) {	 
	 //Override the color, or don't if it would return false
	 si->computeColor(color, time);
	 
	 //Override the LOD
	 si->overrideLOD(lod, time);
	 
	 mat4 transformation;
	 bool transformed;
	 
	 if ((transformed = si->computeTransform(transformation, time))) {
		 //Push the old matrix on the stack. Now the top 2 matrices in the stack are identical
		 glPushMatrix();
		 
		 //Pass the matrix to the OpenGL matrix stack
		 double matrix[16] = {  transformation[0][0], transformation[1][0], transformation[2][0], transformation[3][0],
								transformation[0][1], transformation[1][1], transformation[2][1], transformation[3][1],
								transformation[0][2], transformation[1][2], transformation[2][2], transformation[3][2],
								transformation[0][3], transformation[1][3], transformation[2][3], transformation[3][3] };
		 glMultMatrixd(matrix);
	 }// Else there was no transformation, so no pushing needed
	    
    //Make a BoundingBox to expand for each of the children
	BoundingBox bb;
	
	SceneGroup * sg = si->getChild();
	Polygon * p = sg->getPolygon();
	
	if (p != NULL) {
		glColor3f(color[0], color[1], color[2]);
		if (lod == FULL)
			p->draw(GL_POLYGON);//Draw a polygon at LOD == 3
		else if (lod == OUTLINE)
			p->draw();//GL_LINE_LOOP is default arg
			
		//Expand the bounding box we have to return
		bb.expand(p->getBoundingBox());
	} else {
		//There may be SceneInstances in sg
		BoundingBox bb_temp;
		int childcount = sg->getChildCount();//purely for performance purposes
		for (int i = 0; i < childcount; ++i) {
			//Expand the bounding box
			bb.expand(renderInstance(sg->getChild(i), color, lod, time, depth+1));//TODO: Apply transform here?
		}
	}
    
    //Draw the bounding box
    if (lod > NONE) {
		//Set the color based on the depth
		switch (depth % 6) {//Mod 6 for safety
			case 0:
				glColor3f(1.0,0.0,0.0);//Red
				break;
			case 1:
				glColor3f(1.0,1.0,0.0);//Yellow
				break;
			case 2:
				glColor3f(0.0,1.0,0.0);//Green
				break;
			case 3:
				glColor3f(0.0,1.0,1.0);//Cyan
				break;
			case 4:
				glColor3f(0.0,0.0,1.0);//Blue
				break;
			case 5:
				glColor3f(1.0,0.0,1.0);//Magenta
				break;
		}
		
		bb.draw();
	}
    
    //Pop the glMatrix off the stack if there was a transform at this level
    if (transformed) {
		glPopMatrix();
	}	
	
	return transformBoundingBox(bb, transformation);//Return the transformed one
}

//-------------------------------------------------------------------------------
/// You will be calling all of your drawing-related code from this function.
/// Nowhere else in your code should you use glBegin(...) and glEnd() except code
/// called from this method.
///
/// To force a redraw of the screen (eg. after mouse events or the like) simply call
/// glutPostRedisplay();
static void draw(const unsigned int frm) {
	//Clear Buffers
    glClear(GL_COLOR_BUFFER_BIT);
	glMatrixMode(GL_MODELVIEW);					// indicate we are specifying camera transformations
	glLoadIdentity();							// make sure transformation is "zero'd"

    renderInstance(scene->getRoot(), vec3(1,1,1), FULL, frm, 0); // render at solid LOD

	//Now that we've drawn on the buffer, swap the drawing buffer and the displaying buffer.
	glutSwapBuffers();
}

static void display() {
	draw(frameCount);
}

//Exports the rendered images when the 'a' key is pressed.
static void exportScene () {
	for (unsigned int i = 0; i < 100; ++i) {
		draw(i);
		imgSaver->saveFrame(viewport.w, viewport.h);
	}
	
	glutPostRedisplay();
}

//-------------------------------------------------------------------------------
/// \brief	Called when the screen gets resized.
/// This gives you the opportunity to set up all the relevant transforms.
///
static void reshape(int w, int h) {
	//Set up the viewport to ignore any size change.
	glViewport(0,0,w,h);

	//Set up the PROJECTION transformationstack to be a simple orthographic [-1, +1] projection
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
    if (w < h)
	    glOrtho(-20, 20, -h*20/float(w), h*20/float(w), 1, -1);	// resize type = stretch
    else
        glOrtho(-w*20/float(h), w*20/float(h), -20, 20, 1, -1);	// resize type = stretch

	//Set the MODELVIEW transformation stack to the identity.
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}

//-------------------------------------------------------------------------------
/// Called to handle keyboard events.
static void myKeyboardFunc (unsigned char key, int x, int y) {
	switch (key) {
		case 'a':
			exportScene();
			break;
		case 27:			// Escape key
			exit(0);
			break;
	}
}

//-------------------------------------------------------------------------------
/// Called to update the screen at 30 fps.
static void frameTimer(int value){

    frameCount++;
    glutPostRedisplay();
    glutTimerFunc(1000/30, frameTimer, 1);
}

//-------------------------------------------------------------------------------
/// Initialize the environment
int main(int argc,char** argv) {
	//Initialize OpenGL
	glutInit(&argc,argv);
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGBA);

	//Here we load a scene from a scene file.
	if (argc < 2) {
	    cout << "USAGE: show scene.scd" << endl;
	    exit(1);
	}
    scene = new Scene(argv[1]);

	//Initialize the screen capture class to save BMP captures
	//in the current directory, with the prefix "output"
	imgSaver = new UCB::ImageSaver("./", "output");

	//Save the dimensions
	viewport.w = 600;
	viewport.h = 600;

	//Create OpenGL Window
	glutInitWindowSize(600,600);
	glutInitWindowPosition(0,0);
	glutCreateWindow("Show");

	//Register event handlers with OpenGL.
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(myKeyboardFunc);
	
    frameTimer(0);

	//And Go!
	glutMainLoop();
}
