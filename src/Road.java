public class Road {
  private City pointA;
  private City pointB;
  private double distance;

  public Road(int id, City pointA, City pointB, double distance) {
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
