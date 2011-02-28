/*
 * Vertex.cpp
 *
 *  Created on: Feb 2, 2009
 *      Author: njoubert
 */

#include "Vertex.h"

Vertex::Vertex(): _startPos(0.0,0.0), _endPos(0.0,0.0) {
}

Vertex::Vertex(vec2 p): _startPos(p), _endPos(p) {

}
Vertex::Vertex(double x, double y): _startPos(x,y), _endPos(x,y) {

}

vec2 Vertex::getPos() {
    return _startPos;
}

vec2 Vertex::getEnd() {
    return _endPos;
}

vec2 Vertex::getPos(double t) {
	return (_startPos * (1.0-t)) + (_endPos * t);
}

void Vertex::setStartPos(vec2 p) {
    _startPos = p;
}

void Vertex::setEndPos(vec2 p) {
    _endPos = p;
}
