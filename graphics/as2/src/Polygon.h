/*
 * Polygon.h
 *
 *  Created on: Feb 2, 2009
 *      Author: njoubert
 */

#ifndef POLYGON_H_
#define POLYGON_H_

#include "global.h"
#include "Vertex.h"

class Polygon {
public:
    Polygon();
    Polygon(const Polygon& poly); // copy constructor
    Polygon& operator =(const Polygon& poly); // assignment of a Polygon

    /* Loads the first polygon found in an OBJ file */
    Polygon(string objfile);

    /* Draws the given polygon using OpenGL. */
    void draw();
    /* Draws the given polygon, linearly interpolated at t in interval [0,1] */
    void draw(double t, bool bound=true);

    /* Adds a vertex to the end of this polygon */
    void addVertex(Vertex * v);

    /* Writes out the polygon as an OBJ file. */
    void writeAsOBJ(string filename);
    /* Writes out the polygon interpolated at t in inverval [0,1] as an OBJ. */
    void writeAsOBJ(string filename, const double t);
	
    Vertex* getClosestVertex(vec2 polyCoords, const double tolerance);
private:
    bool _parseLine(string, vector<Vertex> &);
    vector<Vertex> _vertices;
};

inline bool withinTolerance(const vec2 offered, const vec2 posted, const double tolerance) {
	return abs(offered[0] - posted[0]) < tolerance && abs(offered[1] - posted[1]) < tolerance;
}

#endif /* POLYGON_H_ */
