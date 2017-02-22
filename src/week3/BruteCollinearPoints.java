package week3;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Finds {@link LineSegment}s for a given array of {@link Point}s. A LineSegment
 * has only 4 Points. Subsegments are not included.
 * <p>
 * Runs in O(n^4) time.
 * 
 * @author esanders
 *
 */
public class BruteCollinearPoints {

    private LineSegment[] lineSegments = new LineSegment[4];
    private int size = 0;

    /**
     * Finds {@link LineSegment}s that contain 4 {@link Point}s.
     * <p>
     * Subsegments should not be included. Each segment should be included only
     * once (i.e. reverse should not be included)
     * <p>
     * Creates a copy of the parameter array. Checks for invalid points.
     * 
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {

        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pointsCopy[i] = points[i];
        }

        Arrays.sort(pointsCopy);

        checkArrayParameter(pointsCopy);

        findLineSegments(pointsCopy);
    }

    /**
     * Brute force approach to find the {@link LineSegment}s for a given set of
     * {@link Point}s.
     * <p>
     * Subsegments should not be included. Each segment should be included only
     * once (i.e. reverse should not be included)
     * 
     * @param points
     */
    private void findLineSegments(Point[] points) {

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {

                    double ijSlope = points[i].slopeTo(points[j]);
                    double ikSlope = points[i].slopeTo(points[k]);
                    // If the first 3 don't form a line, then there
                    // is no need to check 4.
                    if (ijSlope == ikSlope) {
                        for (int m = k + 1; m < points.length; m++) {
                            if (points[i].slopeTo(points[m]) == ikSlope) {
                                addNewLineSegment(points[i], points[j], points[k], points[m]);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Add a new {@link LineSegment} to the array.
     * 
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     */
    private void addNewLineSegment(Point p1, Point p2, Point p3, Point p4) {

        // find the smallest endpoint of the line segment
        Point smallEndpoint = findSmallest(p1, p2, p3, p4);
        // find the largest endpoint of the line segment
        Point largeEndpoint = findLargest(p1, p2, p3, p4);

        // If the smallest endpoint of the line segment
        // is not p1, then don't add this line segment
        // as this line segment is a subsegment
        if (smallEndpoint != p1) {
            return;
        }

        LineSegment lineSegment = new LineSegment(smallEndpoint, largeEndpoint);
        if (size >= lineSegments.length) {
            resizeArray(lineSegments.length * 2);
        }

        lineSegments[size++] = lineSegment;

    }

    /**
     * Find the largest {@link Point}
     * 
     * @param points
     * @return
     */
    private Point findLargest(Point... points) {

        if (points.length <= 0) {
            return null;
        }

        Point largest = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(largest) > 0) {
                largest = points[i];
            }
        }

        return largest;
    }

    /**
     * Find the smallest {@link Point}
     * 
     * @param points
     * @return
     */
    private Point findSmallest(Point... points) {

        if (points.length <= 0) {
            return null;
        }

        Point smallest = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(smallest) < 0) {
                smallest = points[i];
            }
        }

        return smallest;
    }

    /**
     * Returns the number of {@link LineSegment}s
     * 
     * @return number of LineSegments
     */
    public int numberOfSegments() {

        return size;
    }

    /**
     * Returns the {@link LineSegment}s
     * 
     * @return LineSegments
     */
    public LineSegment[] segments() {

        LineSegment[] returnedLineSegments = new LineSegment[size];
        for (int i = 0; i < size; i++) {
            returnedLineSegments[i] = lineSegments[i];
        }
        return returnedLineSegments;
    }

    /**
     * Resizes the lineSegments array to a given array size.
     * 
     * @param newSize
     */
    private void resizeArray(int newSize) {

        LineSegment[] resizedArray = new LineSegment[newSize];

        for (int i = 0; i < size; i++) {
            resizedArray[i] = lineSegments[i];
        }

        lineSegments = resizedArray;

    }

    /**
     * Checks the array for equal {@link Point}s.
     * 
     * @param points
     */
    private void checkArrayParameter(Point[] points) {

        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        System.out.println(collinear.segments().length);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
