package imagingbook.common.geometry.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SlopeInterceptLineTest {
	
	static double TOL = 1e-6;

	@Test
	public void test1() {
		for (int k = -10; k <= 10; k++) {
			for (int d = -10; d <= 10; d++) {
				SlopeInterceptLine line = new SlopeInterceptLine(k, d);
				assertEquals(0 + d, line.getY(0), TOL);
				assertEquals(k + d, line.getY(1), TOL);
				assertEquals(-k + d, line.getY(-1), TOL);
			}
		}
	}
	
	@Test
	public void test2() {
		for (int k = -1000; k <= 1000; k+=33) {
			for (int d = -1000; d <= 1000; d+=77) {
				SlopeInterceptLine sl1 = new SlopeInterceptLine(k, d);
				AlgebraicLine al1 = AlgebraicLine.from(sl1);
				SlopeInterceptLine sl2 = SlopeInterceptLine.from(al1);
				assertTrue(sl1.equals(sl2, TOL));
				assertTrue(sl2.equals(sl1, TOL));
			}
		}
	}

}
