import java.io.File;

public class Main {
	public static void main(String[] args) {
		File cities = new File("cities.txt");
		File roads = new File("roads.txt");
		Graph2 graph = new Graph2(cities, roads);
		graph.calculerItineraireMinimisantNombreRoutes("Berlin", "Madrid");
		System.out.println("--------------------------");
		graph.calculerItineraireMinimisantKm("Berlin", "Madrid");
	}

}
