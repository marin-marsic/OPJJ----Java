package hr.fer.zemris.java.raytracer.model;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * RayCaster is used for rendering wanted 3D objects.
 *
 * @author Luka
 *
 */
public class Bzvz {

	/**
	 * Method starts with program.
	 *
	 * @param args
	 *            nothing is received through args.
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0),
				new Point3D(0, 0, 0), new Point3D(0, 0, 10), 20, 20);
	}

	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical, int width, int height,
					long requestNo, IRayTracerResultObserver observer) {
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D og = view.sub(eye).normalize();
				Point3D vuv = viewUp.normalize();

				Point3D yNovi = vuv.sub(
						og.scalarMultiply(og.scalarProduct(vuv))).normalize();

				Point3D yAxis = yNovi;
				Point3D xAxis = og.vectorProduct(yNovi).normalize();
				Point3D zAxis = Ray.fromPoints(view, eye).direction;// možda y
																	// staviti
																	// u
																	// negativan

				Point3D screenCorner = view.sub(
						xAxis.scalarMultiply(horizontal / 2)).add(
						yAxis.scalarMultiply(vertical / 2));
				Scene scene = RayTracerViewer.createPredefinedScene();
				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add(

						xAxis.scalarMultiply((x * horizontal) / (width - 1)))
								.sub(yAxis.scalarMultiply((y * vertical)
										/ (height - 1)));

						Ray ray = Ray.fromPoints(eye, screenPoint);

						tracer(scene, ray, rgb);

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

			/**
			 * Method used for filling screen dot by dot.
			 * 
			 * @param scene
			 *            existing
			 * @param ray
			 * @param rgb
			 *            storage array
			 *
			 */
			private void tracer(Scene scene, Ray ray, short[] rgb) {
				RayIntersection min = null;
				// racunanje minimalno udaljene točke presjeka od gledaoca
				for (GraphicalObject object : scene.getObjects()) {
					RayIntersection inters = object
							.findClosestRayIntersection(ray);
					if (inters == null)
						continue;
					if (min == null) {
						min = inters;
					} else {
						if (inters != null) {
							if (inters.getDistance() < min.getDistance()- 0.01) {
								min = inters;
							}
						}
					}
				}
				if (min == null) {
					rgb[0] = 0;
					rgb[1] = 0;
					rgb[2] = 0;
					return;
				}

				determineColorFor(scene, min, rgb, ray);

			}

			/**
			 * Method determines which color to put in which ray intersection.
			 * 
			 * @param scene
			 *            existing
			 * @param s
			 *            RayIntersection
			 * @param rgb
			 *            array
			 * @param ray
			 *            ray
			 */
			private void determineColorFor(Scene scene, RayIntersection s,
					short[] rgb, Ray ray) {

				// ambient component
				rgb[0] = 15;
				rgb[1] = 15;
				rgb[2] = 15;

				// impact of each light source
				for (LightSource ls : scene.getLights()) {
					// Ray sourceRay =Ray.fromPoints(ls.getPoint(),
					// s.getPoint());
					Ray sourceRay = new Ray(ls.getPoint(), ls.getPoint()
							.sub(s.getPoint()).normalize());
					RayIntersection min = null;

					// racunanje minimalno udaljene točke presjeka od izvora
					for (GraphicalObject object : scene.getObjects()) {
						RayIntersection intersec = object
								.findClosestRayIntersection(sourceRay);
						if (min == null) {
							min = intersec;
						} else {
							if (intersec != null) {
								if (min.getDistance() > intersec.getDistance() + 0.1) {
									min = intersec;
								}
							}
						}
					}


					Point3D n = s.getNormal();

					Point3D l = (ls.getPoint().sub(s.getPoint())).normalize();

					Point3D reflected = (n
							.scalarMultiply(n.scalarProduct(l) * 2));// .scalarMultiply(2);
					reflected = reflected.sub(l).normalize();

					double cosTheta = l.scalarProduct(n);
					double cosAlfa = reflected.scalarProduct(ray.start.sub(
							s.getPoint()).normalize());

					cosAlfa = Math.pow(cosAlfa, s.getKrn());
					rgb[0] += (ls.getR() * s.getKdr() * Math.max(cosTheta, 0));
					rgb[1] += (ls.getG() * s.getKdb() * Math.max(cosTheta, 0));
					rgb[2] += (ls.getB() * s.getKdg() * Math.max(cosTheta, 0));

					rgb[0] += ls.getR() * s.getKrr() * Math.max(cosAlfa, 0);
					rgb[1] += ls.getG() * s.getKrb() * Math.max(cosAlfa, 0);
					rgb[2] += ls.getB() * s.getKrg() * Math.max(cosAlfa, 0);

				}

			}

		};
	}
}