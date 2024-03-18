import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
  private Map<String, City> cities;
  private Map<Integer, Road> routes;

  public Graph(File citiesFile, File roadsFile) {
    cities = new HashMap<>();
    routes = new HashMap<>();
    parseCities(citiesFile);
      parseRoads(roadsFile);
  }

  //  Read cities.txt and populate the cities Map
  private Map<String, City> parseCities(File citiesFile) {
      Scanner scanner = null;
      try {
          scanner = new Scanner(citiesFile);
      } catch (FileNotFoundException e) {
          throw new RuntimeException(e);
      }
      while (scanner.hasNextLine()) {
      String[] parts = scanner.nextLine().split(",");
      int id = Integer.parseInt(parts[0]);
      String name = parts[1];
      double latitude = Double.parseDouble(parts[2]);
      double longitude = Double.parseDouble(parts[3]);
      cities.put(name, new City(id, name, latitude, longitude));
    }
    scanner.close();
      return cities;
  }

  //  Read roads.txt and populate the routes Map
  private Map<Integer, Road> parseRoads(File roadsFile) {
      Scanner scanner = null;
      try {
          scanner = new Scanner(roadsFile);
      } catch (FileNotFoundException e) {
          throw new RuntimeException(e);
      }
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
      return routes;
  }

  public void showAll(File theVille, File TheRoute){
    cities = parseCities(theVille);
    routes = parseRoads(TheRoute);
    for (City ville : cities.values()) {
      System.out.println("id : " + ville.getId() + " nom : " + ville.getName());
    }
    System.out.println("_______________________________________________________________");
    for (Road route : routes.values()) {
      System.out.println("ville de départ : " + route.getPointA().getName() + " ville d'arrivé : " + route.getPointB().getName());
    }
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