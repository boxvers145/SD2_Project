public class City {
  private final int id;
  private final String name;
  private final double longitude;
  private final double latitude;

  public City(int id, String name, double longitude, double latitude) {
    this.id = id;
    this.name = name;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getLongitude() {
    return longitude;
  }

  public double getLatitude() {
    return latitude;
  }
}
