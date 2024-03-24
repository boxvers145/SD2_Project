public class Road {
  private final City pointA;
  private final City pointB;
  private final double distance;

  public Road(City pointA, City pointB, double distance) {
    this.pointA = pointA;
    this.pointB = pointB;
    this.distance = distance;
  }

  public City getPointA() {
    return pointA;
  }

  public City getPointB() {
    return pointB;
  }

  public double getDistance() {
    return distance;
  }
}
