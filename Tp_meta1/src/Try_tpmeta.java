import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Try_tpmeta {

    public static void main(String[] args) {
        // Génération aléatoire du nombre de villes entre 5 et 15 inclus
        int numCities = generateRandomNumCities(5, 15);

        // Génération aléatoire des noms des villes et des distances
        String[] villes = generateRandomCities(numCities);
        int[][] distances = generateRandomDistances(numCities);

        List<String> randomSolution = generateRandomSolution(villes);
        System.out.println("Solution générée aléatoirement : " + randomSolution);

        if (estSolutionValide(randomSolution, villes)) {
            double distance = calculateTotalDistance(randomSolution, distances);
            System.out.println("Distance parcourue : " + distance);
        } else {
            System.out.println("Solution non valide.");
        }
    }

    private static int generateRandomNumCities(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

 
    private static String[] generateRandomCities(int numCities) {
        String[] cities = new String[numCities];
        for (int i = 0; i < numCities; i++) {
            cities[i] = "Ville_" + (i + 1);
        }
        List<String> shuffledCities = new ArrayList<>(List.of(cities));
        Collections.shuffle(shuffledCities);
        return shuffledCities.toArray(new String[0]);
    }

   
    private static int[][] generateRandomDistances(int numCities) {
        int[][] distances = new int[numCities][numCities];
        Random random = new Random();

        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
            	int distance = random.nextInt(102) - 1;
                distances[i][j] = distance;
                distances[j][i] = distance; 
            }
        }
        return distances;
    }

    private static List<String> generateRandomSolution(String[] cities) {
        List<String> randomSolution = new ArrayList<>(List.of(cities));
        Collections.shuffle(randomSolution);
        return randomSolution;
    }

    private static double calculateTotalDistance(List<String> sequence, int[][] distances) {
        double totalDistance = 0.0;
        for (int i = 0; i < sequence.size() - 1; i++) {
            int fromIndex = getIndex(sequence.get(i), sequence);
            int toIndex = getIndex(sequence.get(i + 1), sequence);
            totalDistance += distances[fromIndex][toIndex];
        }
        return totalDistance;
    }

    private static int getIndex(String city, List<String> sequence) {
        return sequence.indexOf(city);
    }

    private static boolean estSolutionValide(List<String> solution, String[] cities) {
        // Vérifier si le chemin forme un cycle
        if (solution.size() != cities.length || !solution.get(0).equals(solution.get(solution.size() - 1))) {
            return false;
        }

        // Vérifier qu'il existe un chemin entre chaque paire de villes consécutives
        for (int i = 0; i < solution.size() - 1; i++) {
            int fromIndex = getIndex(solution.get(i), solution);
            int toIndex = getIndex(solution.get(i + 1), solution);
            if (fromIndex == -1 || toIndex == -1) {
                return false;
            }
        }

        // Vérifier que chaque ville est visitée une seule fois
        return new HashSet<>(solution).size() == solution.size();
    }
}
