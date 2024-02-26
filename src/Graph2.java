import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph2 {
  private Map<String, City> cities;
  private Map<Integer, Road> routes;

  public Graph2(File citiesFile, File roadsFile) throws FileNotFoundException {
    cities = new HashMap<>();
    routes = new HashMap<>();
    parseCities(citiesFile);
    parseRoads(roadsFile);
  }

  //  Read cities.txt and populate the cities Map
  private void parseCities(File citiesFile) throws FileNotFoundException {
    Scanner scanner = new Scanner(citiesFile);
    while (scanner.hasNextLine()) {
      String[] parts = scanner.nextLine().split(",");
      int id = Integer.parseInt(parts[0]);
      String name = parts[1];
      double latitude = Double.parseDouble(parts[2]);
      double longitude = Double.parseDouble(parts[3]);
      cities.put(name, new City(id, name, latitude, longitude));
    }
    scanner.close();
  }

  //  Read roads.txt and populate the routes Map
  private void parseRoads(File roadsFile) throws FileNotFoundException {
    Scanner scanner = new Scanner(roadsFile);
    while (scanner.hasNextLine()) {
      String[] parts = scanner.nextLine().split(",");
      int id1 = Integer.parseInt(parts[0]);
      int id2 = Integer.parseInt(parts[1]);

      City city1 = cities.values().stream()
          .filter(city -> city.getId() == id1)
          .findFirst().orElse(null);
      City city2 = cities.values().stream()
          .filter(city -> city.getId() == id2)
          .findFirst().orElse(null);

      if (city1 != null && city2 != null) {
        double distance = Util.distance(city1.getLatitude(), city1.getLongitude(),
            city2.getLatitude(), city2.getLongitude());
        routes.put(city1.getId() * 1000 + city2.getId(), new Road(0, city1, city2, distance));
        routes.put(city2.getId() * 1000 + city1.getId(), new Road(0, city2, city1, distance));
      }
    }
    scanner.close();
  }

  // Implementation of calculateItineraryMinimizingNumberOfRoutes (BFS)
  public void calculerItineraireMinimisantNombreRoutes(String startCityName, String endCityName) {
    // ... (Implement Breadth-First Search )
    System.out.println("test1 : " + startCityName);
  }

  // Implementation of calculateItineraryMinimizingDistance (Dijkstra's)
  public void calculerItineraireMinimisantKm(String startCityName, String endCityName) {
    // ... (Implement Dijkstra's Algorithm)
    System.out.println("test2 : " + endCityName);
  }
}


// City and Route classes (similar to the previous example)