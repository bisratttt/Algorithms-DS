/* *****************************************************************************
 *  Name:   Bisrat Zerihun
 *  Date:   07/10/2021
 *  Description:    An efficient implementation of computing range search and
 *                  nearest neighbor using kd-trees
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private class Node {
        private Point2D point;
        private Node left, right;
        private boolean isVertical;
        // great way of keeping track of set of points under a subtree
        private RectHV area;

        /**
         * create a node with the coordinates as points
         *
         * @param point the point the node represents
         */
        public Node(Point2D point, boolean isVertical, RectHV area) {
            this.point = point;
            this.left = null;
            this.right = null;
            this.area = area;
            this.isVertical = isVertical;
        }

    }

    private Node root;
    private int size;

    /**
     * construct an empty set of points
     */
    public KdTree() {
        root = null;
        size = 0;
    }

    /**
     * // is the set empty?
     *
     * @return true if empty and false otherwise
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * number of points in the set
     *
     * @return the size of the set
     */
    public int size() {
        return this.size;
    }


    /**
     * add the point to the set (if it is not already in the set)
     *
     * @param p the point to be inserted
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            this.root = insert(this.root, p, true, new RectHV(0, 0, 1, 1));
        }
    }

    private Node insert(Node parent, Point2D p, boolean vertical, RectHV nodeArea) {
        if (parent == null) {
            parent = new Node(p, vertical, nodeArea);
            this.size++;
            return parent;
        }
        if (parent.isVertical) {
            // if point in left side then insert on left subtree with appropriate area
            if (parent.point.x() > p.x()) parent.left = insert(parent.left, p, !parent.isVertical,
                                                               new RectHV(nodeArea.xmin(),
                                                                          nodeArea.ymin(),
                                                                          parent.point.x(),
                                                                          nodeArea.ymax()));
            else parent.right = insert(parent.right, p, !parent.isVertical,
                                       new RectHV(parent.point.x(), nodeArea.ymin(),
                                                  nodeArea.xmax(), nodeArea.ymax()));
        }
        else {
            if (parent.point.y() > p.y()) parent.left = insert(parent.left, p, !parent.isVertical,
                                                               new RectHV(nodeArea.xmin(),
                                                                          nodeArea.ymin(),
                                                                          nodeArea.xmax(),
                                                                          parent.point.y())
            );
            else parent.right = insert(parent.right, p, !parent.isVertical,
                                       new RectHV(nodeArea.xmin(),
                                                  parent.point.y(),
                                                  nodeArea.xmax(),
                                                  nodeArea.ymax()));
        }
        return parent;
    }

    /**
     * does the set contain point p?
     *
     * @param p the point to be checked
     * @return true if it contains and false otherwise
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root != null) return contains(this.root, p);
        return false;
    }

    private boolean contains(Node parent, Point2D p) {
        if (parent == null) return false;
        if (parent.point.equals(p)) return true;
        if (parent.isVertical) {
            // use the insert logic to search
            if (parent.point.x() > p.x()) return contains(parent.left, p);
            else return contains(parent.right, p);
        }
        else {
            if (parent.point.y() > p.y()) return contains(parent.left, p);
            else return contains(parent.right, p);
        }
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        draw(this.root);
    }

    private void draw(Node mroot) {
        if (mroot == null) return;
        mroot.point.draw();
        if (mroot.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            // use the area attribute to draw lines for each node
            StdDraw.line(mroot.point.x(), mroot.area.ymin(), mroot.point.x(), mroot.area.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(mroot.area.xmin(), mroot.point.y(), mroot.area.xmax(), mroot.point.y());
        }
        draw(mroot.left);
        draw(mroot.right);

    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     *
     * @param rect the range rectangle used to check
     * @return set of points inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> inRange = new Stack<Point2D>();
        if (rect == null) throw new IllegalArgumentException();
        if (isEmpty()) return inRange;
        range(rect, this.root, this.root.area, inRange);
        return inRange;
    }

    // helper method
    private void range(RectHV rect, Node parent, RectHV nodeRect, Stack<Point2D> stack) {
        if (!rect.intersects(nodeRect)) return;
        if (rect.contains(parent.point)) stack.push(parent.point);
        // recurse until tree is null and rect contains the points
        if (parent.left != null)
            range(rect, parent.left, parent.left.area, stack);
        if (parent.right != null)
            range(rect, parent.right, parent.right.area, stack);
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p the point to be checked for neighbors
     * @return the nearest neighbor point
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D nearest = this.root.point;
        nearest = nearest(p, this.root, nearest);
        return nearest;
    }

    // helper method
    private Point2D nearest(Point2D p, Node mroot, Point2D nearest) {
        if (mroot == null) return nearest;
        double nearestDist = nearest.distanceSquaredTo(p);
        if (nearestDist >= mroot.area.distanceSquaredTo(p)) {
            if (mroot.point.distanceSquaredTo(p) < nearestDist) {
                nearest = mroot.point;
            }
            // if query point is inside the left side then recurse the left then right
            if (mroot.left != null && mroot.left.area.contains(p)) {
                nearest = nearest(p, mroot.left, nearest);
                nearest = nearest(p, mroot.right, nearest);
            }
            else {
                nearest = nearest(p, mroot.right, nearest);
                nearest = nearest(p, mroot.left, nearest);
            }

        }

        return nearest;
    }

    public static void main(String[] args) {
        // KdTree tree = new KdTree();
        // tree.insert(new Point2D(0.7, 0.2));
        // tree.insert(new Point2D(0.5, 0.4));
        // tree.insert(new Point2D(0.2, 0.3));
        // tree.insert(new Point2D(0.4, 0.7));
        // tree.insert(new Point2D(0.9, 0.6));
        // Point2D near = tree.nearest(new Point2D(0.701, 0.967));
    }
}
