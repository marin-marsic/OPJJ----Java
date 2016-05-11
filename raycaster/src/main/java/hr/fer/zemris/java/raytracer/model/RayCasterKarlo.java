package hr.fer.zemris.java.raytracer.model;

import java.util.List;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

public class RayCasterKarlo {

	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	} 

	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {

				System.out.println("Započinjem izračune...");

				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D vectorOG = view.sub(eye).normalize();
				Point3D viewUpNormalized = viewUp.normalize();

				Point3D yAxis = viewUpNormalized.sub(vectorOG.scalarMultiply(vectorOG.scalarProduct(viewUpNormalized)));
				Point3D xAxis = vectorOG.vectorProduct(yAxis);
				Point3D zAxis = yAxis.vectorProduct(xAxis);
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2.0))
						.add(yAxis.scalarMultiply(vertical / 2.0));

				Scene scene = RayTracerViewer.createPredefinedScene();

				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner
								.add(xAxis.scalarMultiply(x / (double) (width - 1) * horizontal))
								.sub(yAxis.scalarMultiply(y / (double) (height - 1) * vertical));
						Ray ray = Ray.fromPoints(eye, screenPoint);

						rgb = tracer(scene, ray, rgb, eye);

						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

						offset++;
					}
				}

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};

	}

	private static short[] tracer(Scene scene, Ray ray, short[] rgb, Point3D eye) {

		List<GraphicalObject> graphicObjects = scene.getObjects();
		RayIntersection closestIntersection = null;
		boolean first = true;
		for (GraphicalObject o : graphicObjects) {
			RayIntersection intersection = o.findClosestRayIntersection(ray);
			if (intersection != null) {
				if (first) {
					closestIntersection = intersection;
					first = false;
				} else if (Math.abs(intersection.getDistance() - closestIntersection.getDistance()) < 1e-7) {
					closestIntersection = intersection;
				}
			}
		}

		if (closestIntersection != null) {
			rgb = determineColorFor(scene, closestIntersection, eye);
		} else {
			rgb[0] = rgb[1] = rgb[2] = 0;
		}

		return rgb;

	}

	private static short[] determineColorFor(Scene scene, RayIntersection intersection, Point3D eye) {

		short[] rgb = new short[3];
		rgb[0] = rgb[1] = rgb[2] = 15;

		List<LightSource> lightSources = scene.getLights();
		for (LightSource ls : lightSources) {
			Ray ray1 = Ray.fromPoints(ls.getPoint(), intersection.getPoint());

			List<GraphicalObject> graphicObjects = scene.getObjects();
			RayIntersection closestIntersection1 = null;
			boolean first = true;
			for (GraphicalObject o : graphicObjects) {
				RayIntersection intersection1 = o.findClosestRayIntersection(ray1);
				if (intersection1 != null) {
					if (first) {
						closestIntersection1 = intersection1;
						first = false;
					} else if (Math.abs(intersection1.getDistance() - closestIntersection1.getDistance()) < 1e-7) {
						closestIntersection1 = intersection1;
					}
				}
			}

			// distance between light source and it closest intersection of ray1
			double distLightClosest = (ls.getPoint().sub(closestIntersection1.getPoint())).norm();
			// distance between light source and intersection provided as
			// argument
			double distLigthIntersection = (ls.getPoint().sub(intersection.getPoint())).norm();

			Point3D n = (intersection.getNormal()).normalize();
			Point3D l = (ls.getPoint().sub(intersection.getPoint())).normalize();
			Point3D v = (eye.sub(intersection.getPoint())).normalize();
			Point3D r = (l.sub(n.scalarMultiply(2).scalarMultiply(l.scalarProduct(n)))).normalize();

			if (closestIntersection1 != null && Math.abs(distLightClosest - distLigthIntersection) < 1e-6) {
				continue;
			} else {
				double Idr = 0;
				double Idg = 0;
				double Idb = 0;
				double Isr = ls.getR() * intersection.getKrr() * Math.pow(r.scalarProduct(v), intersection.getKrn());
				double Isg = ls.getG() * intersection.getKrg() * Math.pow(r.scalarProduct(v), intersection.getKrn());
				double Isb = ls.getB() * intersection.getKrb() * Math.pow(r.scalarProduct(v), intersection.getKrn());

				if (Double.compare(l.scalarProduct(n), 0) > 0) {
					Idr = ls.getR() * intersection.getKdr() * l.scalarProduct(n);
					Idg = ls.getG() * intersection.getKdg() * l.scalarProduct(n);
					Idb = ls.getB() * intersection.getKdb() * l.scalarProduct(n);
				}

				rgb[0] += Math.round(Idr) + Math.round(Isr);
				rgb[1] += Math.round(Idg) + Math.round(Isg);
				rgb[2] += Math.round(Idb) + Math.round(Isb);
			}

		}
		return rgb;
	}

}
