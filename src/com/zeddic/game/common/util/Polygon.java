/*
 * Copyright (C) 2010 Geo Siege Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zeddic.game.common.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Path;
import android.util.FloatMath;

public class Polygon extends Shape {

  public Vector2d[] edges;
  public Vector2d[] points;
  
  protected Polygon(Vector2d[] points) {
    this.points = points;
    edges = new Vector2d[points.length];
    buildEdges();
    buildPath();
    buildWidthHeight();
  }
  
  public void buildEdges() {
    
    Vector2d prev = points[0];
    Vector2d edge;
    Vector2d point;
 
    for (int i = 1 ; i <= points.length ; i++) {
      point = points[i % points.length];
      edge = new Vector2d(
          point.x - prev.x,
          point.y - prev.y);
      edges[i - 1] = edge;
      prev = point;
    }
  }
  
  public void buildWidthHeight() {
    float minX = Float.MAX_VALUE;
    float maxX = -Float.MAX_VALUE;
    float minY = Float.MAX_VALUE;
    float maxY = -Float.MAX_VALUE;
    
    Vector2d point;
    for ( int i = 0; i < points.length; i++) {
      point = points[i];
      minX = Math.min(point.x, minX);
      maxX = Math.max(point.x, maxX);
      minY = Math.min(point.y, minY);
      maxY = Math.max(point.y, maxY);
    }
  
    float newWidth = maxX - minX;
    float newHeight = maxY - minY;
    if (width != newWidth || height != newHeight) {
      width = newWidth;
      height = newHeight;
      float halfWidth = width / 2;
      float halfHeight = height / 2;
      radius = (float) Math.sqrt(halfWidth * halfWidth + halfHeight * halfHeight);
    }
  }
  
  public void rebuildEdges() {
    Vector2d prev = points[0];
    Vector2d edge;
    Vector2d point;
    for (int i = 1 ; i <= points.length ; i++) {
      point = points[i % points.length];
      edge = edges[i - 1];
      edge.x = point.x - prev.x;
      edge.y = point.y - prev.y;
      prev = point;
    }
  }
  
  @Override
  public void buildPath() {
    path = new Path();
    Vector2d point = points[0];
    path.moveTo(point.x, point.y);
 
    for (int i = 1 ; i <= points.length ; i++) {
      point = points[i % points.length];
      path.lineTo(point.x, point.y);
    }
  }
  
  @Override
  public void projectOnAxis(
      float xOffset,
      float yOffset,
      Vector2d axis,
      Span span) {

    Vector2d point = points[0];
    
    // For performance reasons the dot product is done inline. The overhead
    // of a new method call can be very expensive for android.
    float dotProduct = axis.x * (point.x + xOffset) + axis.y * (point.y + yOffset);
    float min = dotProduct;
    float max = dotProduct;
    for (int i = 1; i < points.length; i++) {
      point = points[i]; 
      dotProduct = axis.x * (point.x + xOffset) + axis.y * (point.y + yOffset);
      if (dotProduct < min) {
        min = dotProduct;
      } else if (dotProduct > max) {
        max = dotProduct;
      }
    }
    
    span.min = min;
    span.max = max;
  }
  
  @Override
  public Shape copy() {
    Vector2d[] pointsCopy = new Vector2d[points.length];
    for (int i = 0 ; i < points.length ; i++) {
      pointsCopy[i] = points[i].copy();
    }
    Polygon copy = new Polygon(pointsCopy);
    return copy;
  }
  
  @Override
  public void transform(float rotation, float scale, Shape otherShape) {
    
    if (otherShape == null)
      return;
    
    if (!(otherShape instanceof Polygon))
      return;

    Polygon other = (Polygon) otherShape;
    
    // Pre-calculate some of the trig values so they are done outside the loop.
    float rotationRadians = (float) Math.toRadians((double) rotation);
    float cosRotation = FloatMath.cos(rotationRadians);
    float sinRotation = FloatMath.sin(rotationRadians);

    Vector2d rawPoint;
    Vector2d point;
    for ( int i = 0; i < other.points.length; i++) {
      rawPoint = points[i];
      point = other.points[i];
 
      point.x = cosRotation * rawPoint.x - sinRotation * rawPoint.y;
      point.y = sinRotation * rawPoint.x + cosRotation * rawPoint.y;
      point.x *= scale;
      point.y *= scale;
    }
    other.rebuildEdges();
    
    float newWidth = height * scale;
    float newHeight = width * scale;
    if (newWidth != other.width || newHeight != other.height) {
      other.width = newWidth;
      other.height = newHeight;
      other.radius = (float) Math.sqrt(
          other.width * other.width / 4 +
          other.height * other.height / 4);
    }
  }
  
  
  /**
   * A builder for easily creating complex polygons by specifying the 
   * different vertices.
   */
  public static class PolygonBuilder {
    List<Vector2d> points;
    public PolygonBuilder() {
      points = new ArrayList<Vector2d>();
    }
    
    public PolygonBuilder add(float x, float y) {
      points.add(new Vector2d(x, y));
      return this;
    }
    
    public Polygon build() {
      return new Polygon(points.toArray(new Vector2d[points.size()]));
    }
  }
}
