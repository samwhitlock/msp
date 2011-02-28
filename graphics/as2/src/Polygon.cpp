/*
 * Polygon.cpp
 *
 *  Created on: Feb 2, 2009
 *      Author: njoubert
 */
#include "Polygon.h"

Polygon::Polygon() {
    // Leave it empty.
}

/**
 * This parses an OBJ file for the first polygon available in it.
 */
Polygon::Polygon(string objfile) {
    // Leave this as it is.
    std::cout << "Parsing OBJ file " << objfile << std::endl;

    vector<Vertex> tempVerts;
    ifstream inFile(objfile.c_str(), ifstream::in);
    if (!inFile) {
        std::cout << "Could not open given obj file " << objfile << std::endl;
    }
    while (inFile.good()) {
        string line;
        getline(inFile, line);
        if (!_parseLine(line, tempVerts)) {
            std::cout << "Failed to parse OBJ file." << std::endl;
            break;
        }
        if (_vertices.size() > 0) // take the first face in the file.
            break;
    }
    inFile.close();

    std::cout << "Parsed an OBJ file with " << _vertices.size() << " vertices."
            << endl;
}

bool Polygon::_parseLine(string line, vector<Vertex> & temp) {
    string operand;
    bool success = true;

    if (line.empty())
        return true;
    stringstream ss(stringstream::in | stringstream::out);
    ss.str(line);
    ss >> operand;

    if (operand[0] == '#') {

        return true;

    } else if (operand == "v") {

        double x, y;
        ss >> x >> y;
        temp.push_back(Vertex(x, y));

    } else if (operand == "f") {

        while (!ss.eof()) {
            int i;
            ss >> i;
            addVertex(&temp[i - 1]); // copy vertex in to polygon
        }

    } else {

        cout << "Unknown operand in scene file, skipping line: " << operand
                << endl;

    }

    if (ss.fail()) {
        std::cout
                << "The bad bit of the input file's line's stringstream is set! Couldn't parse:"
                << std::endl;
        std::cout << "  " << line << std::endl;
        success = false;
    }

    return success;
}

void Polygon::draw() {
    if (1 > _vertices.size())
        return;
    glColor3f(0.6f, 0.6f, 0.6f);//kinda grey?
    glBegin(GL_LINE_LOOP);
    for (vector<Vertex>::iterator it = _vertices.begin(); it != _vertices.end(); ++it)
        glVertex2d(it->getPos()[0], it->getPos()[1]);
    glEnd();
}

void Polygon::draw(double t, bool bound) {//AUTHOR: Me
	if (1 > _vertices.size())
		return;
	else {
		vec2 upperRight = _vertices[0].getPos(t), lowerLeft = _vertices[0].getPos(t);//to keep track of the bounding box
		glColor3f(1.0f, 1.0f, 1.0f);//All white
		glBegin(GL_LINE_LOOP);
		for (vector<Vertex>::iterator it = _vertices.begin(); it != _vertices.end(); ++it) {
			vec2 temp = it->getPos(t);
			glVertex2d(temp[0], temp[1]);

			//Update the bounding box coordinates
			if (temp[0] > upperRight[0])
				upperRight[0] = temp[0];
			else if (temp[0] < lowerLeft[0])
				lowerLeft[0] = temp[0];

			if (temp[1] > upperRight[1])
				upperRight[1] = temp[1];
			else if (temp[1] < lowerLeft[1])
				lowerLeft[1] = temp[1];
		}
		glEnd();

		if (bound) {
			//Draw the green bounding box
			glColor3f(0.0f, 1.0f, 0.0f);//RGB: all green
	
			glBegin(GL_LINE_LOOP);
	
			glVertex2d(lowerLeft[0], lowerLeft[1]);
			glVertex2d(upperRight[0], lowerLeft[1]);
			glVertex2d(upperRight[0], upperRight[1]);
			glVertex2d(lowerLeft[0], upperRight[1]);

			glEnd();
		}
	}
}

void Polygon::addVertex(Vertex *v) {
    _vertices.push_back(*v);
}

void Polygon::writeAsOBJ(string filename) {
    writeAsOBJ(filename, 0);
}

void Polygon::writeAsOBJ(string filename, const double t) {
	ofstream objFile(filename.c_str());
	objFile << "#Author: Sam Whitlock" << endl;

	//Print out each vertex
	for (vector<Vertex>::iterator it = _vertices.begin(); it != _vertices.end(); ++it) {
		vec2 temp = it->getPos(t);
		objFile << "v " << temp[0] << " " << temp[1] << endl;
	}

	//Print out the face order
	objFile << "f";

	for (unsigned int i = 1; i <= _vertices.size(); ++i) {
		objFile << " " << i;
	}

	objFile << endl;

	objFile.close();
}

Vertex* Polygon::getClosestVertex(vec2 polyCoords, const double tolerance) {
	Vertex* returnVert = NULL;

	for (unsigned int i = 0; i < _vertices.size(); ++i) {
		if (withinTolerance(polyCoords, _vertices[i].getPos(1.0), tolerance)) {
			returnVert = &_vertices[i];
			break;
		}
	}

	return returnVert;
}
