/* *****************************************************************************
 *  Name:   Bisrat Zerihun
 *  Date:   07/09/2021
 *  Description: Brute force implementation of finding points in a range and
 *               nearest neighbor search
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    // uses a balanced binary tree
    private SET<Point2D> set;

    /**
     * construct the set using a balanced binary tree
     */
    public PointSET() {
        set = new SET<Point2D>();

    }

    /**
     * check if the set of points is empty
     *
     * @return true is empty and false otherwise
     */
    public boolean isEmpty() {
        return set.isEmpty();
    }

    /**
     * number of points in the set
     *
     * @return number of points
     */
    public int size() {
        return set.size();
    }

    /**
     * add a point into the set if it is not already in it
     *
     * @param p the point to be added
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        set.add(p);
    }

    /**
     * check if point is in the set
     *
     * @param p the point checked
     * @return true if inside set and false otherwise
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    /**
     * draw evey point inside the set
     */
    public void draw() {
        for (Point2D point : set) {
            point.draw();
        }
    }

    /**
     * all the points that are inside the rectangle including boundary
     *
     * @param rect the rectangle area to be checked
     * @return a set of points inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> insideRange = new Stack<>();
        // check every point inside tree and check
        for (Point2D point : set) {
            if (rect.contains(point)) {
                insideRange.push(point);
            }
        }
        return insideRange;
    }

    /**
     * the nearest point inside the set to point p
     * null if set is empty
     *
     * @param p the point to be checked
     * @return the nearest point
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        double minDist = Double.MAX_VALUE;
        Point2D minPoint = null;
        // check every point inside tree and compare
        for (Point2D point : set) {
            double dist = point.distanceTo(p);
            if (dist < minDist) {
                minDist = dist;
                minPoint = point;
            }
        }
        return minPoint;
    }

    public static void main(String[] args) {

    }
}
