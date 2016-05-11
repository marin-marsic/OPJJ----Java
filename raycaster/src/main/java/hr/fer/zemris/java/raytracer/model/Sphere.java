package hr.fer.zemris.java.raytracer.model;

public class Sphere extends GraphicalObject {

	private Point3D center;
	private double radius;
	private double kdr;
	private double kdg;
	private double kdb;
	private double krr;
	private double krg;
	private double krb;
	private double krn;

	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {

		Point3D startMinusCenter = ray.start.sub(center);

		double b = ray.direction.scalarProduct(startMinusCenter);
		double c = Math.pow(startMinusCenter.norm(), 2) - radius * radius;
		double underSqareRoot = b * b - c;

		if (Double.compare(underSqareRoot, 0) < 0) {
			return null;
		}

		Point3D closestPoint = ray.start;

		if (Double.compare(underSqareRoot, 0) == 0) {
			closestPoint.modifyAdd(ray.direction.scalarMultiply(-b));
		}

		double d1 = -b + Math.sqrt(underSqareRoot);
		double d2 = -b - Math.sqrt(underSqareRoot);

		//		double distance = 0.0;
		if (d1 < d2) {
			closestPoint.modifyAdd(ray.direction.scalarMultiply(d1));
			//			distance = d1;
		} else {
			closestPoint.modifyAdd(ray.direction.scalarMultiply(d2));
			//			distance = d2;
		}

		return new RayIntersection(closestPoint, ray.start.sub(closestPoint).norm(), true) {

			@Override
			public Point3D getNormal() {
				return closestPoint.sub(center);
			}

			@Override
			public double getKrr() {
				return krr;
			}

			@Override
			public double getKrn() {
				return krn;
			}

			@Override
			public double getKrg() {
				return krg;
			}

			@Override
			public double getKrb() {
				return krb;
			}

			@Override
			public double getKdr() {
				return kdr;
			}

			@Override
			public double getKdg() {
				return kdg;
			}

			@Override
			public double getKdb() {
				return kdb;
			}
		};

	}
}
