package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import java.util.List;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle) - Completed
    private final Canvas canvas;
    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas; // FIXME - Completed
        this.paint = paint; // FIXME - Completed
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle cr) {
        canvas.drawCircle(0, 0, cr.getRadius(), paint);
        return null;
    }

    //To changes the stroke color and applies it to the shape wrapped inside stroke color
    @Override
    public Void onStrokeColor(final StrokeColor sc) {
        paint.setColor(sc.getColor());
        sc.getShape().accept(this);
        paint.setColor(sc.getColor());
        paint.setStyle(Paint.Style.STROKE);
        return null;
    }

    //To Temporarily switches the paint style to FILL_AND_STROKE to fill the shape.
    @Override
    public Void onFill(final Fill f) {
        Paint.Style oldStyle = paint.getStyle();
        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }

    //To Recursively renders each shape in the Group
    @Override
    public Void onGroup(final Group g) {
        for (Shape s: g.getShapes()) {
            s.accept(this);
        }
        return null;
    }

    //To Translates the canvas to the shape's location, renders the shape, and then restores the original canvas position
    @Override
    public Void onLocation(final Location l) {
        canvas.save();
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.translate(-l.getX(), -l.getY());
        return null;
    }

    //TO Draws a rectangle starting at (0, 0) with the specified width and height.
    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    //To Ensures the shape inside Outline is drawn with an outline (stroke)
    @Override
    public Void onOutline(final Outline o) {
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(Style.STROKE);
        return null;
    }

    //Draws a polygon by connecting consecutive points in the shape.
    @Override
    public Void onPolygon(final Polygon s) {
        List<? extends Point> points = s.getPoints();
        if (points.size() > 1) {
            float[] pts = new float[(points.size() - 1) * 4];
            int i = 0;
            for (int j = 0; j < points.size() - 1; j++) {
                Point p1 = points.get(j);
                Point p2 = points.get(j + 1);
                pts[i++] = p1.getX();
                pts[i++] = p1.getY();
                pts[i++] = p2.getX();
                pts[i++] = p2.getY();
            }
            canvas.drawLines(pts, paint);
            Point first = points.get(0);
            Point last = points.get(points.size() - 1);
            canvas.drawLine(last.getX(), last.getY(), first.getX(), first.getY(), paint);
        }
        return null;
    }
}