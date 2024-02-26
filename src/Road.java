public class Road {
  private City pointA;
  private City pointB;
  private Double distance;

  public Road(int id, City pointA, City pointB, Double distance) {
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

  public Double getDistance() {
    return distance;
  }
}
