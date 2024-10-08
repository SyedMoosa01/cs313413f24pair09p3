package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        // Calculate the bounding box of the contained shape
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        // Iterate over each shape in the group and calculate the bounding box
        for (Shape shape : g.getShapes()) {
            Location box = shape.accept(this); // Get the bounding box of the shape
            Rectangle rect = (Rectangle) box.getShape(); // Cast to Rectangle to access width and height

            // Update min and max values based on the bounding box
            minX = Math.min(minX, box.getX());
            minY = Math.min(minY, box.getY());
            maxX = Math.max(maxX, box.getX() + rect.getWidth());
            maxY = Math.max(maxY, box.getY() + rect.getHeight());
        }

        // Return a new Location with the combined bounding box of all shapes
        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }


    @Override
    public Location onLocation(final Location l) {
        // Calculate the bounding box of the contained shape
        Location boundingBox = l.getShape().accept(this);

        // Adjust the bounding box coordinates based on the location's coordinates
        int adjustedX = l.getX() + boundingBox.getX();
        int adjustedY = l.getY() + boundingBox.getY();

        // Return a new Location with the adjusted coordinates and the same bounding box shape
        return new Location(adjustedX, adjustedY, boundingBox.getShape());
    }


    @Override
    public Location onRectangle(final Rectangle r) {
        // The bounding box of a rectangle is the rectangle itself at the origin
        return new Location(0, 0, r);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        // Calculate the bounding box of the contained shape
        return c.getShape().accept(this);
    }


    @Override
    public Location onOutline(final Outline o) {
        // Calculate the bounding box of the contained shape
        return o.getShape().accept(this);
    }


    @Override
    public Location onPolygon(final Polygon s) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        // Iterate over each point in the polygon to find the min and max x and y coordinates
        for (Point p : s.getPoints()) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }

        // Create a rectangle with the calculated width and height
        int width = maxX - minX;
        int height = maxY - minY;

        // Return the bounding box located at the top-left (minX, minY)
        return new Location(minX, minY, new Rectangle(width, height));
    }
}