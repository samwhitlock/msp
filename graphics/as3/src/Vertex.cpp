/*
 * Vertex.cpp
 *
 *  Created on: Feb 2, 2009
 *      Author: njoubert
 */

#include "Vertex.h"

Vertex::Vertex(): _pos(0.0,0.0) {}

Vertex::Vertex(vec2 p): _pos(p) {}

Vertex::Vertex(const double x, const double y): _pos(x,y) {}


vec2 Vertex::getPos() const {
    return _pos;
}

void Vertex::setPos(vec2 p) {
    _pos = p;
}

