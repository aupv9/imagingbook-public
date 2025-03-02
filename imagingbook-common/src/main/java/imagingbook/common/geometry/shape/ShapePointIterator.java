package imagingbook.common.geometry.shape;

import java.awt.Shape;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.util.Iterator;

import imagingbook.common.geometry.basic.Pnt2d;

/**
 * A small wrapper class to create a simple {@link Iterator} of
 * {@link Pnt2d} to step over the individual points of
 * a AWT {@link Shape} object. Curved shapes are flattened by a
 * {@link FlatteningPathIterator}.
 * 
 * @author WB
 * @see PathIterator
 * @see FlatteningPathIterator
 */
public class ShapePointIterator implements Iterator<Pnt2d> {
	
	private final PathIterator pi;
	private final double coords[] = new double[6];
	
	public ShapePointIterator(Shape shape, double flatness) {
		this.pi = shape.getPathIterator(null, flatness);
	}

	@Override
	public boolean hasNext() {
		return !pi.isDone();
	}

	@Override
	public Pnt2d next() {
		if (pi.isDone()) {
			return null;
		}
		else {
			pi.currentSegment(coords);
			pi.next();
			return Pnt2d.from(coords[0], coords[1]);
		}
	}

	
//	public static void main(String[] args) {
//		Pnt2d ctr = Pnt2d.from(20, 30);
//		GeometricCircle gc = new GeometricCircle(20, 30, 100);
//		Shape circle = gc.getShape();
//		System.out.println("circle = " + circle);
//		
////		ShapePointIterator iter = new ShapePointIterator(circle, 0.01);
////		while(iter.hasNext()) {
////			Pnt2d p = iter.next();
////			System.out.println(p + " dist = " + gc.getDistance(p));
////		}
//		
//		System.out.println("checkShape = " + gc.checkShape(circle, 0.5));
//		
//	}
}
