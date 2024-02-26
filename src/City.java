public class City {
  private int id;
  private String name;
  private Double longitude;
  private Double latitude;

  public City(int id, String name, Double longitude, Double latitude) {
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

  public Double getLongitude() {
    return longitude;
  }

  public Double getLatitude() {
    return latitude;
  }
}
