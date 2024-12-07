import java.util.*;

public class Tp_metaa2 {

    public static void main(String[] args) {
        // Génération et résolution de plusieurs instances aléatoires

        int numItems = generateRandomNumItems(5, 10); // Générer un nombre aléatoire d'objets
        int numKnapsacks = generateRandomNumKnapsacks(2, 4); // Générer un nombre aléatoire de sacs à dos

        // Générer des objets aléatoires avec leurs valeurs et poids respectifs
        int[] values = generateRandomValues(numItems);
        int[] weights = generateRandomWeights(numItems);

        // Générer des capacités aléatoires pour chaque sac à dos
        int[] capacities = generateRandomCapacities(numKnapsacks);

        // Afficher l'instance générée aléatoirement
        System.out.println("Instance générée aléatoirement avec " + numItems + " objets et " + numKnapsacks + " sacs à dos:");
        System.out.println("Valeurs des objets : " + Arrays.toString(values));
        System.out.println("Poids des objets : " + Arrays.toString(weights));
        System.out.println("Capacités des sacs à dos : " + Arrays.toString(capacities));

        // Résolution du MKP avec l'algorithme DFS
        solveMKP_DFS(values, weights, capacities);
    }

    // Fonction pour générer un nombre aléatoire d'objets
    private static int generateRandomNumItems(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    // Fonction pour générer un nombre aléatoire de sacs à dos
    private static int generateRandomNumKnapsacks(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    // Fonction pour générer des valeurs aléatoires pour les objets
    private static int[] generateRandomValues(int numItems) {
        Random random = new Random();
        int[] values = new int[numItems];
        for (int i = 0; i < numItems; i++) {
            values[i] = random.nextInt(20) + 1; // Valeurs aléatoires entre 1 et 20
        }
        return values;
    }

    // Fonction pour générer des poids aléatoires pour les objets
    private static int[] generateRandomWeights(int numItems) {
        Random random = new Random();
        int[] weights = new int[numItems];
        for (int i = 0; i < numItems; i++) {
            weights[i] = random.nextInt(10) + 1; // Poids aléatoires entre 1 et 10
        }
        return weights;
    }

    // Fonction pour générer des capacités aléatoires pour les sacs à dos
    private static int[] generateRandomCapacities(int numKnapsacks) {
        Random random = new Random();
        int[] capacities = new int[numKnapsacks];
        for (int i = 0; i < numKnapsacks; i++) {
            capacities[i] = random.nextInt(30) + 1; // Capacités aléatoires entre 1 et 30
        }
        return capacities;
    }

    // Résolution du MKP avec l'algorithme DFS
    public static void solveMKP_DFS(int[] values, int[] weights, int[] capacities) {
        MKP_DFS mkpDfsInstance = new MKP_DFS();
        mkpDfsInstance.solveMKP(values, weights, capacities);
    }

    // Classe principale MKP_DFS pour résoudre le Problème des Sacs à Dos Multiples avec DFS
    public static class MKP_DFS {
        private int[] values;
        private int[] weights;
        private int[] capacities;
        private int[] currentSolution;
        private int[] bestSolution;
        private int bestValue;

        public void solveMKP(int[] values, int[] weights, int[] capacities) {
            this.values = values;
            this.weights = weights;
            this.capacities = capacities;
            this.currentSolution = new int[values.length];
            this.bestSolution = new int[values.length];
            this.bestValue = 0;

            dfs(0);

            // Afficher le résultat
            System.out.println("Meilleure solution trouvée : " + Arrays.toString(bestSolution));
            System.out.println("Valeur totale : " + bestValue);
        }

        private void dfs(int itemIndex) {
            if (itemIndex == values.length) {
                // Vérifier si la solution actuelle est meilleure que la meilleure solution actuelle
                int totalValue = calculateTotalValue(currentSolution);
                if (totalValue > bestValue) {
                    System.arraycopy(currentSolution, 0, bestSolution, 0, currentSolution.length);
                    bestValue = totalValue;
                }
                return;
            }

            // Essayer d'ajouter l'objet actuel à chaque sac à dos
            for (int i = 0; i < capacities.length; i++) {
                if (capacities[i] >= weights[itemIndex]) {
                    capacities[i] -= weights[itemIndex];
                    currentSolution[itemIndex] = i + 1; // Les indices des sacs à dos commencent à partir de 1
                    dfs(itemIndex + 1);
                    capacities[i] += weights[itemIndex]; // Retour en arrière (backtrack)
                    currentSolution[itemIndex] = 0;
                }
            }

            // Ne pas ajouter l'objet actuel à aucun sac à dos
            currentSolution[itemIndex] = 0;
            dfs(itemIndex + 1);
        }

        // Fonction utilitaire pour calculer la valeur totale de la solution
        private int calculateTotalValue(int[] solution) {
            int totalValue = 0;
            for (int i = 0; i < solution.length; i++) {
                if (solution[i] != 0) { // Si l'objet est ajouté à un sac à dos
                    totalValue += values[i];
                }
            }
            return totalValue;
        }
    }
}
