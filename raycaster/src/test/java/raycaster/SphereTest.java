package raycaster;

import static org.junit.Assert.*;
import junit.framework.Assert;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Sphere;
 




import org.junit.Test;
 
/**
 * @author Marin Maršić
 *
 */
public class SphereTest {
 
        @Test
        public void findClosestRayIntersection_TwoIntersections_Test() {
        	Sphere sphere = new Sphere(new Point3D(0, 0, 0), 10, 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6);
    		Ray ray = Ray.fromPoints(new Point3D(-100, 0, 0), new Point3D(-99, 0, 0));
    		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
    		assertEquals(-10, intersection.getPoint().x, 0.00001);
    		assertEquals(0, intersection.getPoint().y, 0.00001);
    		assertEquals(0, intersection.getPoint().z, 0.00001);
    		assertEquals(90, intersection.getDistance(), 0.00001);
    		
    		Point3D normal = intersection.getNormal();
    		assertEquals(-1, normal.x, 0.00001);
    		assertEquals(0, normal.y, 0.00001);
    		assertEquals(0, normal.z, 0.00001);

    		assertEquals(0, intersection.getKdr(), 0.00001);
    		assertEquals(0.1, intersection.getKdg(), 0.00001);
    		assertEquals(0.2, intersection.getKdb(), 0.00001);
    		
    		assertEquals(0.3, intersection.getKrr(), 0.00001);
    		assertEquals(0.4, intersection.getKrg(), 0.00001);
    		assertEquals(0.5, intersection.getKrb(), 0.00001);
    		
    		assertEquals(0.6, intersection.getKrn(), 0.00001);
        }
        
        
        @Test
        public void findClosestRayIntersection_OneIntersection_Test() {
        	Sphere sphere = new Sphere(new Point3D(0, 0, 0), 10, 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6);
    		Ray ray = Ray.fromPoints(new Point3D(-100, 10, 0), new Point3D(-99, 10, 0));
    		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
    		assertEquals(0, intersection.getPoint().x, 0.00001);
    		assertEquals(10, intersection.getPoint().y, 0.00001);
    		assertEquals(0, intersection.getPoint().z, 0.00001);
    		assertEquals(100, intersection.getDistance(), 0.00001);
    		
    		Point3D normal = intersection.getNormal();
    		assertEquals(0, normal.x, 0.00001);
    		assertEquals(1, normal.y, 0.00001);
    		assertEquals(0, normal.z, 0.00001);
        }
        
        
        @Test
        public void findClosestRayIntersection_RayInsideTheSphere_Test() {
        	Sphere sphere = new Sphere(new Point3D(0, 0, 0), 10, 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6);
    		Ray ray = Ray.fromPoints(new Point3D(1, 0, 0), new Point3D(2, 0, 0));
    		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
    		assertEquals(10, intersection.getPoint().x, 0.00001);
    		assertEquals(0, intersection.getPoint().y, 0.00001);
    		assertEquals(0, intersection.getPoint().z, 0.00001);
    		assertEquals(9, intersection.getDistance(), 0.00001);
    		
    		Point3D normal = intersection.getNormal();
    		assertEquals(1, normal.x, 0.00001);
    		assertEquals(0, normal.y, 0.00001);
    		assertEquals(0, normal.z, 0.00001);
        }
 
        @Test
        public void findClosestRayIntersection_NoIntersection_Test() {
        	Sphere sphere = new Sphere(new Point3D(0, 0, 0), 10, 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6);
    		Ray ray = Ray.fromPoints(new Point3D(11, 0, 0), new Point3D(12, 0, 0));
    		RayIntersection intersection = sphere.findClosestRayIntersection(ray);
    		assertTrue(null == intersection);
        }
        
        
}
