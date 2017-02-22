package week3;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Finds {@link LineSegment}s for a given array of {@link Point}s. A LineSegment
 * has 4 or more points in it. Subsegments are not included.
 * <p>
 * Runs in O(n^2 log n) time.
 * 
 * @author esanders
 *
 */
public class FastCollinearPoints {

    private LineSegment[] lineSegments = new LineSegment[256];
    private int size = 0;

    /**
     * Finds {@link LineSegment}s that contain 4 or more {@link Point}s.
     * <p>
     * Subsegments should not be included. Each segment should be included only
     * once (i.e. reverse should not be included)
     * <p>
     * Creates a copy of the parameter array. Checks for invalid points.
     * 
     * @param points
     */
    public FastCollinearPoints(Point[] points) {

        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pointsCopy[i] = points[i];
        }

        checkArrayParameter(pointsCopy);

        findLineSegments(points, pointsCopy);
    }

    /**
     * Finds the {@link LineSegment}s for a given set of {@link Point}s and a
     * copy that is not immutable and can be sorted.
     * <p>
     * Subsegments should not be included. Each segment should be included only
     * once (i.e. reverse should not be included)
     * 
     * @param points
     * @param pointsCopy
     */
    private void findLineSegments(Point[] points, Point[] pointsCopy) {

        // iterate through each Point 'p' where p will act as the origin
        // all of the other points will be sorted by the slope they make
        // with p.
        for (int i = 0; i < points.length; i++) {
            // sort array based on points[i] slope order;
            Arrays.sort(pointsCopy, points[i].slopeOrder());

            // find 4 or more points that have the same slope
            findCollinearPoints(pointsCopy);

        }

    }

    /**
     * Finds 4 or more {@link Point}s that have the same slope.
     * <p>
     * Takes the first Point in the array 'p' and finds 3 other points that all
     * have the same slope. If there are at least 3 points that have the same
     * slope, then we have at least 4 points (including the origin point 'p')
     * that form a line segment.
     * <p>
     * A line segment is added ONLY IF the origin point 'p' (index 0) is the
     * smallest. If 'p' is not the smallest, then we ignore the segment. We do
     * this to ensure that no subsegments are added. If 'p' is not the smallest,
     * then the line segment it is a part of will be added when the smallest
     * point of the line segment has its chance as the origin (if it has already
     * had its chance as the origin, then it is already included as a
     * LineSegment).
     * <p>
     *
     * @param orderedPoints
     */
    private void findCollinearPoints(Point[] orderedPoints) {

        // always start with a collinearCount of 2.
        // Because there is always at least 2 points in a segment
        int collinearCount = 2;
        // Keep track of the largest point we have seen for each line segment
        // so that we can accurately create a line segment.
        Point largestSeen = orderedPoints[0];
        // Keep track of the smallest point so that we know if the line
        // is a sub segment or not.
        Point smallestSeen = orderedPoints[0];
        // iterate through all of the points and find when we should
        // create a new line segment. We create a new line segment
        // when the next point in the array does not have the same slope.
        for (int i = 1; i < orderedPoints.length - 1; i++) {

            Point p1 = orderedPoints[i];
            Point p2 = orderedPoints[i + 1];
            double p1Slope = orderedPoints[0].slopeTo(p1);
            double p2Slope = orderedPoints[0].slopeTo(p2);

            // If the slopes are the same, we need to check
            // if the origin Point 'p' is still the smallest point.
            // We must also keep track of the largest Point seen.
            if (p1Slope == p2Slope) {

                if (p1.compareTo(smallestSeen) < 0) {
                    smallestSeen = p1;
                }

                if (p2.compareTo(smallestSeen) < 0) {
                    smallestSeen = p2;
                }

                if (p1.compareTo(largestSeen) > 0) {
                    largestSeen = p1;
                }

                if (p2.compareTo(largestSeen) > 0) {
                    largestSeen = p2;
                }

                // Point i+1 is a part of the line
                collinearCount++;
            }

            // If point i+1 is not a part of the line,
            // then we need to check if we have a Line Segment
            // and create a new Line Segment if we do.
            // Then reset and find other Line Segments.
            if (p1Slope != p2Slope) {
                if (collinearCount >= 4) {
                    if (smallestSeen == orderedPoints[0]) {
                        addNewLineSegment(smallestSeen, largestSeen);
                    }
                }

                // reset
                collinearCount = 2;
                largestSeen = orderedPoints[0];
                smallestSeen = orderedPoints[0];
            }
            // also have to consider that we may be at
            // the end of the array
            else if ((i + 1) >= orderedPoints.length - 1) {
                if (collinearCount >= 4) {
                    if (smallestSeen == orderedPoints[0]) {
                        addNewLineSegment(smallestSeen, largestSeen);
                    }
                }
            }

        }
    }

    /**
     * Add a new {@link LineSegment} to the array.
     * 
     * @param start
     *            the starting point of the LineSegment
     * @param end
     *            the ending point of the LineSegment
     */
    private void addNewLineSegment(Point start, Point end) {

        LineSegment lineSegment = new LineSegment(start, end);

        // resize the array if need be
        if (size >= lineSegments.length) {
            resizeArray(lineSegments.length * 2);
        }

        lineSegments[size++] = lineSegment;

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

        // Need to create a new array the size of the number of LineSegments
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

        Arrays.sort(points);
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
        FastCollinearPoints collinearFast = new FastCollinearPoints(points);
        for (LineSegment segment : collinearFast.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}