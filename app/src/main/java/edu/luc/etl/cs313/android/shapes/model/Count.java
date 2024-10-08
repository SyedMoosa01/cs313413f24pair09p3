package edu.luc.etl.cs313.android.shapes.model;

/**
 * A visitor to compute the number of basic shapes in a (possibly complex)
 * shape.
 */
public class Count implements Visitor<Integer> {

    // TODO entirely your job

    @Override
    public Integer onPolygon(final Polygon p) {
        // A polygon is a basic shape, so count it as 1
        return 1;
    }


    @Override
    public Integer onCircle(final Circle c) {
        // A circle is a basic shape, so count it as 1
        return 1;
    }


    @Override
    public Integer onGroup(final Group g) {
        int count = 0;
        // Iterate through each shape in the group and count it
        for (Shape shape : g.getShapes()) {
            count += shape.accept(this); // Use the visitor pattern to count each shape
        }
        return count;
    }

    @Override
    public Integer onRectangle(final Rectangle q) {
        // A rectangle is a basic shape, so count it as 1
        return 1;
    }


    @Override
    public Integer onOutline(final Outline o) {
        // Count the number of basic shapes within the outlined shape
        return o.getShape().accept(this);
    }

    @Override
    public Integer onFill(final Fill c) {
        // Count the number of basic shapes within the filled shape
        return c.getShape().accept(this);
    }

    @Override
    public Integer onLocation(final Location l) {
        // Count the number of basic shapes within the located shape
        return l.getShape().accept(this);
    }


    @Override
    public Integer onStrokeColor(final StrokeColor c) {
        // Count the number of basic shapes within the shape with stroke color
        return c.getShape().accept(this);
    }
}
