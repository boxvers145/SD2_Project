import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
    private final Map<String, City> cities;
    private final Map<Integer, Road> routes;

    public record City(int id, String name, double longitude, double latitude) { }
    public record Road(City pointA, City pointB, double distance) { }

    public Graph(File citiesFile, File roadsFile) {
        cities = new HashMap<>();
        routes = new HashMap<>();
        parseCities(citiesFile);
        parseRoads(roadsFile);
    }

    //  Read cities.txt and populate the cities Map
    private void parseCities(File citiesFile) {
        Scanner scanner;
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
    }

    //  Read roads.txt and populate the routes Map
    private void parseRoads(File roadsFile) {
        Scanner scanner;
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
                    .filter(city -> city.id() == id1)
                    .findFirst().orElse(null);
            City city2 = cities.values().stream()
                    .filter(city -> city.id() == id2)
                    .findFirst().orElse(null);

            if (city1 != null && city2 != null) {
                double distance = Util.distance(city1.latitude(), city1.longitude(),
                        city2.latitude(), city2.longitude());
                routes.put(city1.id() * 1000 + city2.id(), new Road(city1, city2, distance));
                routes.put(city2.id() * 1000 + city1.id(), new Road(city2, city1, distance));
            }
        }
        scanner.close();
    }

    private void reconstructPath(Map<City, City> visited, City startCity, City endCity) {
        List<City> path = new ArrayList<>();
        City currentCity = endCity;
        double totalDistance = 0;

        while (currentCity != null) {
            path.add(0, currentCity);
            City previousCity = visited.get(currentCity);
            if (previousCity != null) {
                totalDistance += Util.distance(previousCity.latitude(), previousCity.longitude(),
                        currentCity.latitude(), currentCity.longitude());
            }
            currentCity = previousCity;
        }

        assert endCity != null;
        System.out.println("Trajet de " + startCity.name() + " à " + endCity.name() + " : " + (path.size() - 1) + " routes et " + totalDistance + " km"); //

        for (int i = 0; i < path.size() - 1; i++) {
            City cityA = path.get(i);
            City cityB = path.get(i + 1);
            double distanceAB = Util.distance(cityA.latitude(), cityA.longitude(),
                    cityB.latitude(), cityB.longitude());
            System.out.println(cityA.name() + " -> " + cityB.name() + " (" + distanceAB + " km)");
        }
    }

    public void calculerItineraireMinimisantNombreRoutes(String startCityName, String endCityName) {
        City startCity = cities.get(startCityName);
        City endCity = cities.get(endCityName);

        if (startCity == null || endCity == null) {
            System.out.println("Start or end city not found!");
            return;
        }

        Queue<City> queue = new LinkedList<>();
        Map<City, City> visited = new HashMap<>();

        queue.add(startCity);
        visited.put(startCity, null); // Mark the start city as visited

        while (!queue.isEmpty()) {
            City currentCity = queue.poll();

            if (currentCity.equals(endCity)) {
                reconstructPath(visited, startCity, endCity);
                return;
            }

            // Iterate over roads connected to the current city
            for (Road road : routes.values()) {
                if (road.pointA().equals(currentCity)) {
                    City neighbor = road.pointB();
                    if (!visited.containsKey(neighbor)) {
                        queue.add(neighbor);
                        visited.put(neighbor, currentCity);
                    }
                } else if (road.pointB().equals(currentCity)) { // Check for roads in either direction
                    City neighbor = road.pointA();
                    if (!visited.containsKey(neighbor)) {
                        queue.add(neighbor);
                        visited.put(neighbor, currentCity);
                    }
                }
            }
        }

        System.out.println("No route found between " + startCityName + " and " + endCityName);
    }

    // Implementation of calculateItineraryMinimizingDistance (Dijkstra's)
    public void calculerItineraireMinimisantKm(String startCityName, String endCityName) {
        City startCity = cities.get(startCityName);
        City endCity = cities.get(endCityName);

        if (startCity == null || endCity == null) {
            System.out.println("Start or end city not found!");
            return;
        }

        // Initialization
        Map<City, Double> distances = new HashMap<>();
        Map<City, City> previous = new HashMap<>();
        PriorityQueue<City> unvisited = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        for (City city : cities.values()) {
            distances.put(city, Double.POSITIVE_INFINITY); // Set initial distances to infinity
        }
        distances.put(startCity, 0.0); // Distance from start to itself is 0
        unvisited.add(startCity);

        // Main Dijkstra Loop
        while (!unvisited.isEmpty()) {
            City currentCity = unvisited.poll(); // City with the smallest known distance

            if (currentCity.equals(endCity)) {
                reconstructPath(previous, startCity, endCity); // We found the destination
                return;
            }

            // Iterate over neighbors of the current city
            for (Road road : routes.values()) {
                City neighbor;
                if (road.pointA().equals(currentCity)) {
                    neighbor = road.pointB();
                } else if (road.pointB().equals(currentCity)) {
                    neighbor = road.pointA();
                } else {
                    continue; // Road not connected to the current city
                }

                double tentativeDistance = distances.get(currentCity) + road.distance();
                if (tentativeDistance < distances.get(neighbor)) {
                    distances.put(neighbor, tentativeDistance);
                    previous.put(neighbor, currentCity);
                    unvisited.add(neighbor);
                }
            }
        }

        System.out.println("No route found between " + startCityName + " and " + endCityName);
    }
}