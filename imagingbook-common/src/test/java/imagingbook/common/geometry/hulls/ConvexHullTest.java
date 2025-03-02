package imagingbook.common.geometry.hulls;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import imagingbook.common.geometry.basic.Pnt2d;

public class ConvexHullTest {

	@Test
	public void test1() {
		List<Pnt2d> points = Arrays.asList(
				Pnt2d.from(2, 5),
				Pnt2d.from(5, 4),
				Pnt2d.from(5, 7),
				Pnt2d.from(7, 6),
				Pnt2d.from(9, 9),
				Pnt2d.from(6, 2)
				);
		ConvexHull hull = new ConvexHull(points);
		Pnt2d[] vertices = hull.getVertices();
		assertNotNull(vertices);
		assertEquals(4, vertices.length);
		
		for (Pnt2d p : points) {
			assertTrue(hull.contains(p));
		}
	}
	
	@Test
	public void test2() {
		List<Pnt2d> points = Arrays.asList(
				Pnt2d.from(2, 5),
				Pnt2d.from(5, 4)
				);
		ConvexHull hull = new ConvexHull(points);
		Pnt2d[] vertices = hull.getVertices();
		assertNotNull(vertices);
		assertEquals(2, vertices.length);
	}
	
	@Test
	public void test3() {	// single point only!
		List<Pnt2d> points = Arrays.asList(
				Pnt2d.from(2, 5)
				);
		ConvexHull hull = new ConvexHull(points);
		Pnt2d[] vertices = hull.getVertices();
		assertNotNull(vertices);
		assertEquals(1, vertices.length);
	}

	
	@Test
	public void test4() {
		int N = 100; 	// number of random points
		int K = 100;	// number of tries
		Random rg = new Random(17);
		Pnt2d[] pointArray = new Pnt2d[N];
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < N; i++) {
				pointArray[i] = Pnt2d.from(100 * rg.nextDouble(), 100 * rg.nextDouble());
			}
			
			ConvexHull hull = new ConvexHull(pointArray);
			for (Pnt2d p : pointArray) {
				assertTrue(hull.contains(p));
			}
		}
	}

}
