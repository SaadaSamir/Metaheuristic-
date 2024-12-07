package tpmeta4;
import java.util.*;

public class gentiquemeta {
    private int numGenerations; // Nombre de générations
    private int populationSize; // Taille de la population
    private double crossoverProbability; // Probabilité de croisement
    private double mutationProbability; // Probabilité de mutation
    private int[][] distances; // Matrice des distances entre les villes
    private int numCities; // Nombre de villes

    // Constructeur
    public gentiquemeta(int numGenerations, int populationSize, double crossoverProbability,
                            double mutationProbability, int[][] distances) {
        this.numGenerations = numGenerations;
        this.populationSize = populationSize;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.distances = distances;
        this.numCities = distances.length;
    }

    // Génération aléatoire d'une population initiale
    private List<List<Integer>> generateRandomPopulation() {
        List<List<Integer>> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            List<Integer> individual = new ArrayList<>();
            List<Integer> cities = new ArrayList<>();
            for (int j = 0; j < numCities; j++) {
                cities.add(j);
            }
            Collections.shuffle(cities); // Mélanger les villes
            individual.addAll(cities);
            population.add(individual);
        }

        // Vérification des chemins entre deux villes consécutives
        for (List<Integer> individual : population) {
            for (int i = 0; i < numCities - 1; i++) { 
                if (distances[individual.get(i)][individual.get(i + 1)] == -1) {
                    // S'il n'existe pas de chemin entre deux villes consécutives, réorganiser les villes
                    Collections.shuffle(individual);
                    i = -1; // Recommencer la vérification à partir du début
                }
            }
        }

        // Affichage de la population générée
        System.out.println("Population initiale:");
        printPopulation(population);
        return population;
    }



    // Fonction d'évaluation de la solution (Fitness Function)
    private int evaluateFitness(List<Integer> route) {
        int totalDistance = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            int city1 = route.get(i);
            int city2 = route.get(i + 1);
            totalDistance += distances[city1][city2]; // Calcul de la distance entre deux villes consécutives
        }
        totalDistance += distances[route.get(numCities - 1)][route.get(0)]; // Ajout de la distance pour revenir à la ville de départ
        return totalDistance;
    }

    // Opérateur de croisement de deux solutions (Crossover)
    private List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        if (Math.random() >= crossoverProbability) {
            // Si la probabilité de croisement n'est pas atteinte, renvoyer l'un des parents
            return Math.random() < 0.5 ? parent1 : parent2;
        }

        int startPos = new Random().nextInt(numCities-1);
        int endPos = new Random().nextInt(numCities-1);
        if (endPos < startPos) {
            int temp = startPos;
            startPos = endPos;
            endPos = temp;
        }
        List<Integer> child = new ArrayList<>(Collections.nCopies(numCities, -1));
        for (int i = startPos; i < endPos; i++) {
            child.set(i, parent1.get(i)); // Ajouter les éléments du parent1 dans l'enfant
        }
        int currentIndex = endPos;
        for (int i = 0; i < numCities; i++) {
            if (!child.contains(parent2.get(i))) {
                while (child.get(currentIndex % numCities) != -1) {
                    currentIndex++;
                }
                child.set(currentIndex % numCities, parent2.get(i)); // Ajouter les éléments du parent2 manquants dans l'enfant
            }
        }

        // Affichage du croisement
        System.out.println("Croisement:");
        System.out.println("Parent 1: " + parent1);
        System.out.println("Parent 2: " + parent2);
        System.out.println("Enfant: " + child);
        return child;
    }

    // Opérateur de mutation d'une solution
    private void mutate(List<Integer> individual) {
        for (int i = 0; i < individual.size(); i++) {
            if (Math.random() < mutationProbability) {
                int j = (int) (Math.random() * individual.size());
                Collections.swap(individual, i, j); 
            }
        }

        // Affichage de la mutation
        System.out.println("Mutation:");
        System.out.println("Individu muté: " + individual);
    }

    // Sélection des parents
    private List<Integer> selectParent(List<List<Integer>> population) {
        Collections.shuffle(population);
        return population.get(0);
    }

    // Remplacement de la population
    private List<List<Integer>> replacePopulation(List<List<Integer>> population, List<List<Integer>> offspring) {
        population.addAll(offspring);
        population.sort(Comparator.comparingInt(this::evaluateFitness)); // Trier la population en fonction de la fitness
        List<List<Integer>> newPopulation = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            newPopulation.add(population.get(i)); // Sélectionner les meilleurs individus pour former la nouvelle population
        }
        return newPopulation;
    }

    // Affichage de la population
    private void printPopulation(List<List<Integer>> population) {
        for (List<Integer> individual : population) {
            System.out.println(individual);
        }
    }

    // Résolution du problème du voyageur de commerce en utilisant l'algorithme génétique
    public List<Integer> solveTSP() {
        List<List<Integer>> population = null;
        List<Integer> bestRoute = null;
        int bestFitness = Integer.MAX_VALUE;

        // Boucle à travers les générations
        for (int generation = 0; generation < numGenerations; generation++) {
            // Génération de la population initiale avant la première génération
            if (generation == 0) {
                population = generateRandomPopulation();
            }

            List<List<Integer>> offspring = new ArrayList<>();

            // Génération des enfants par croisement et mutation
            for (int i = 0; i < populationSize / 2; i++) {
                List<Integer> parent1 = selectParent(population);
                List<Integer> parent2 = selectParent(population);

                // S'assurer que les deux parents sont différents
                while (parent1.equals(parent2)) {
                    parent2 = selectParent(population);
                }

                List<Integer> child = crossover(parent1, parent2); // Croisement
                mutate(child); // Mutation
                offspring.add(child); // Ajout des enfants à la liste des enfants
            }

            // Remplacement de la population actuelle par les enfants
            population = replacePopulation(population, offspring);

            // Recherche de la meilleure solution dans la population
            for (List<Integer> solution : population) {
                int fitness = evaluateFitness(solution);
                if (fitness < bestFitness) {
                    bestFitness = fitness;
                    bestRoute = new ArrayList<>(solution);
                }
            }

            // Affichage de l'étape actuelle
            System.out.println("Génération: " + (generation + 1));
            System.out.println("Population:");
            printPopulation(population);
        }

        // Affichage de la meilleure solution
        bestRoute.add(bestRoute.get(0));
        System.out.println("Meilleure route trouvée: " + bestRoute);
        System.out.println("Distance de la meilleure route: " + bestFitness);

        return bestRoute; // Retourne la meilleure route trouvée
    }

    public static void main(String[] args) {
        int numGenerations = 10; // Nombre de générations
        int populationSize = 9; // Taille de la population
        double crossoverProbability = 0.9; // Probabilité de croisement
        double mutationProbability = 0.05; // Probabilité de mutation
        int[][] distances = {
            {0, 10, 15, 20},// 15+10+30+35
            {10, 0, 25, 30},
            {15, 25, 0, 5},
            {20, 30, 5, 0}
        };

        gentiquemeta gaTSP = new gentiquemeta(numGenerations, populationSize,
                crossoverProbability, mutationProbability,
                distances);
        List<Integer> bestRoute = gaTSP.solveTSP(); // Résolution du problème TSP

        if (bestRoute != null) {
            
        } else {
            System.out.println("Aucune solution trouvée.");
        }
    }
}
