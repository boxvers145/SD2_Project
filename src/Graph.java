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

    private void reconstructPath(Map<City, City> visited, City startCity, City endCity) {
        List<City> path = new ArrayList<>();
        City currentCity = endCity;
        double totalDistance = 0;

        while (currentCity != null) {
            path.add(0, currentCity);
            City previousCity = visited.get(currentCity);
            if (previousCity != null) {
                totalDistance += Util.distance(previousCity.getLatitude(), previousCity.getLongitude(),
                        currentCity.getLatitude(), currentCity.getLongitude());
            }
            currentCity = previousCity;
        }

        System.out.println("Trajet de " + startCity.getName() + " à " + endCity.getName() + " : " + (path.size() - 1) + " routes et " + totalDistance + " km"); //

        for (int i = 0; i < path.size() - 1; i++) {
            City cityA = path.get(i);
            City cityB = path.get(i + 1);
            double distanceAB = Util.distance(cityA.getLatitude(), cityA.getLongitude(),
                    cityB.getLatitude(), cityB.getLongitude());
            System.out.println(cityA.getName() + " -> " + cityB.getName() + " (" + distanceAB + " km)");
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
                if (road.getPointA().equals(currentCity)) {
                    City neighbor = road.getPointB();
                    if (!visited.containsKey(neighbor)) {
                        queue.add(neighbor);
                        visited.put(neighbor, currentCity);
                    }
                } else if (road.getPointB().equals(currentCity)) { // Check for roads in either direction
                    City neighbor = road.getPointA();
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
                if (road.getPointA().equals(currentCity)) {
                    neighbor = road.getPointB();
                } else if (road.getPointB().equals(currentCity)) {
                    neighbor = road.getPointA();
                } else {
                    continue; // Road not connected to the current city
                }

                double tentativeDistance = distances.get(currentCity) + road.getDistance();
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