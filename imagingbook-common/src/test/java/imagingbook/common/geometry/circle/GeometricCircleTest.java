package imagingbook.common.geometry.circle;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import imagingbook.common.geometry.shape.ShapeChecker;

public class GeometricCircleTest {

	@Test	// test constructors
	public void test1() {
		AlgebraicCircle ac1 = new AlgebraicCircle(1.3, -2, 5, 1);
		GeometricCircle gc1 = new GeometricCircle(ac1);
		
		AlgebraicCircle ac2 = new AlgebraicCircle(gc1);
		GeometricCircle gc2 = new GeometricCircle(ac2);
		
		assertTrue(ac1.equals(ac2, 1e-6));
		assertTrue(gc1.equals(gc2, 1e-6));
	}

	
	@Test	// test duplication
	public void test2() {
		GeometricCircle gc1 = new GeometricCircle(17, -23, 7);
		GeometricCircle gc2 = gc1.duplicate();
		assertTrue(gc1.equals(gc2, 1e-6));
	}
	
	@Test	// test AWT Shape generation
	public void test3() {
		GeometricCircle gc = new GeometricCircle(17, -23, 7);
		Assert.assertTrue("produced Shape does not match line", new ShapeChecker().check(gc, gc.getShape()));
		
	}

}
