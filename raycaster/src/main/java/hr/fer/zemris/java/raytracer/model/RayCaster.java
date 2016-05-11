package hr.fer.zemris.java.raytracer.model;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Application representing simplification of a ray-tracer for rendering of 3D
 * scenes. It shows predefined scene created by our professor.
 * 
 * @author Marin Maršić
 *
 */
public class RayCaster {

	static ForkJoinPool pool = new ForkJoinPool();

	/**
	 * Method that executes once the program starts.
	 * 
	 * @param args
	 *            Command line arguments. Not needed here.
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0),
				new Point3D(0, 0, 0), new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Method which parallelizes 3D scene rendering process.
	 * 
	 * @param pool
	 *            ForkJoinPool instance.
	 * @param eye
	 *            Coordinates of viewr's eye. Viewer is a cyclops (or a camera).
	 * @param view
	 *            Viewer's viewing point.
	 * @param horizontal
	 *            Width of the viewed plane.
	 * @param vertical
	 *            Height of the viewed plane.
	 * @param width
	 *            Number of pixels horizontally.
	 * @param height
	 *            Number of pixels vertically.
	 * @param red
	 *            Array which stores red component of pixels color.
	 * @param green
	 *            Array which stores green component of pixels color.
	 * @param blue
	 *            Array which stores blue component of pixels color.
	 * @param screenCorner
	 *            Coordinates of upper-left corner of the viewed plane.
	 * @param xAxis
	 *            Normalized vector of x-axis direction.
	 * @param yAxis
	 *            Normalized vector of y-axis direction.
	 * @param scene
	 *            Predefined scene being observed.
	 */
	private static void parallelTrace(ForkJoinPool pool, Point3D eye,
			Point3D view, double horizontal, double vertical, int width,
			int height, short[] red, short[] green, short[] blue,
			Point3D screenCorner, Point3D xAxis, Point3D yAxis, Scene scene) {

		/**
		 * Class for separating rendering job to a more smaller ones. Each Job
		 * has some number of lines on y-axis to compute. Jobs are being split
		 * until the job has less than 50 rows on y-axis to compute.
		 * 
		 * @author Marin Maršić
		 *
		 */
		@SuppressWarnings("serial")
		class Job extends RecursiveAction {
			private int fromY;
			private int toY;

			/**
			 * Constructor for creating one Job instance.
			 * 
			 * @param fromY
			 *            Row on y-axis to start rendering from.
			 * @param toY
			 *            Row on y-axis to stop rendering from.
			 */
			public Job(int fromY, int toY) {
				super();
				this.fromY = fromY;
				this.toY = toY;
			}

			@Override
			protected void compute() {
				if (toY - fromY > 50) {
					Job p1 = new Job(fromY, fromY + (toY - fromY) / 2);
					Job p2 = new Job(fromY + (toY - fromY) / 2, toY);
					invokeAll(p1, p2);
				} else {
					computeDirect();
				}
			}

			/**
			 * Method for calculating color of each pixel of the pixel between
			 * fromY and toY lines on y-axis.
			 */
			private void computeDirect() {
				for (int y = fromY; y < toY; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add(
								xAxis.scalarMultiply((x * horizontal)
										/ (double) (width - 1))).sub(
								yAxis.scalarMultiply((y * vertical)
										/ (double) (height - 1)));

						Ray ray = Ray.fromPoints(eye, screenPoint);

						short[] rgb = new short[3];
						tracer(scene, ray, rgb);

						red[y * width + x] = rgb[0] > 255 ? 255 : rgb[0];
						green[y * width + x] = rgb[1] > 255 ? 255 : rgb[1];
						blue[y * width + x] = rgb[2] > 255 ? 255 : rgb[2];
					}
				}
			}

			/**
			 * Method for the color of the certain pixel.
			 * 
			 * @param scene
			 *            Predefined scene for rendering.
			 * @param ray
			 *            Ray going from viewers eye to a certain pixel.
			 * @param rgb
			 *            Array containing value of red, green and blue
			 *            component of the color.
			 */
			private void tracer(Scene scene, Ray ray, short[] rgb) {
				List<GraphicalObject> graphicalObjects = scene.getObjects();
				List<LightSource> lightSources = scene.getLights();

				RayIntersection closestIntersection = null;

				// FIND CLOSEST VISIBLE INTERSECTION IN DIRECTION OF RAY
				for (GraphicalObject go : graphicalObjects) {
					RayIntersection intersection = go
							.findClosestRayIntersection(ray);
					if (intersection == null) {
						continue;
					}
					if (closestIntersection == null
							|| closestIntersection.getDistance() > intersection
									.getDistance() + 0.1) {
						closestIntersection = intersection;
					}
				}

				// NO OBJECTS VISIBLE IN THIS DIRECTION
				if (closestIntersection == null) {
					rgb[0] = 0;
					rgb[1] = 0;
					rgb[2] = 0;
					return;
				}

				// AMBIENT COMPONENT OF THE COLOR
				rgb[0] = 15;
				rgb[1] = 15;
				rgb[2] = 15;

				// FIND OUT IF ANY LIGHT SOURCE ILLUMINATES THIS INTERSECTION
				for (LightSource ls : lightSources) {
					Ray lightRay = Ray.fromPoints(ls.getPoint(),
							closestIntersection.getPoint());

					RayIntersection closestLightIntersection = null;

					for (GraphicalObject go : graphicalObjects) {
						RayIntersection intersection = go
								.findClosestRayIntersection(lightRay);
						if (intersection == null) {
							continue;
						}
						if (closestLightIntersection == null
								|| closestLightIntersection.getDistance() > intersection
										.getDistance() + 0.1) {
							closestLightIntersection = intersection;
						}
					}

					double distance = (ls.getPoint().sub(closestIntersection
							.getPoint())).norm();

					// IF THIS LIGHT GETS TO THIS INTERSECTION, COLOR IT
					if (closestLightIntersection != null
							&& Math.abs(closestLightIntersection.getDistance()
									- distance) < 0.001) {

						// normal vector of intersection
						Point3D normalVector = closestIntersection.getNormal();
						// vector from intersection to light source
						Point3D l = lightRay.direction.negate();

						// vector of reflected ray
						Point3D reflected = normalVector
								.scalarMultiply(
										2 * normalVector.scalarProduct(l))
								.sub(l).normalize();

						// vector from intersection to eye
						Point3D v = ray.start.sub(
								closestIntersection.getPoint()).normalize();

						// DIFUSE COMPONENT
						rgb[0] += ls.getR() * closestLightIntersection.getKdr()
								* Math.max(0, normalVector.scalarProduct(l));
						rgb[1] += ls.getG() * closestLightIntersection.getKdg()
								* Math.max(0, normalVector.scalarProduct(l));
						rgb[2] += ls.getB() * closestLightIntersection.getKdb()
								* Math.max(0, normalVector.scalarProduct(l));

						// REFLECTED COMPONENT
						rgb[0] += ls.getR()
								* closestLightIntersection.getKrr()
								* Math.pow(
										Math.max(0, reflected.scalarProduct(v)),
										closestLightIntersection.getKrn());
						rgb[1] += ls.getG()
								* closestLightIntersection.getKrg()
								* Math.pow(
										Math.max(0, reflected.scalarProduct(v)),
										closestLightIntersection.getKrn());
						rgb[2] += ls.getB()
								* closestLightIntersection.getKrb()
								* Math.pow(
										Math.max(0, reflected.scalarProduct(v)),
										closestLightIntersection.getKrn());
					}
				}
			}
		}

		Job job = new Job(0, height);
		pool.invoke(job);
	}

	/**
	 * @return Returns {@link IRayTracerProducer}.
	 */
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

				Point3D OG = (view.sub(eye)).normalize();
				Point3D VUV = viewUp.normalize();
				Point3D yAxis = VUV.sub(
						OG.scalarMultiply(OG.scalarProduct(VUV))).normalize();
				Point3D xAxis = OG.vectorProduct(yAxis).normalize();

				Point3D screenCorner = view.add(xAxis.scalarMultiply(
						-horizontal / (double) 2).add(
						yAxis.scalarMultiply(vertical / (double) 2)));

				Scene scene = RayTracerViewer.createPredefinedScene();

				parallelTrace(pool, eye, viewUp, horizontal, vertical, width,
						height, red, green, blue, screenCorner, xAxis, yAxis,
						scene);

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}
}
