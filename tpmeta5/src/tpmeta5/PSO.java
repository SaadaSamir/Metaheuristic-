package tpmeta5;

import java.util.Arrays;
import java.util.Random;

public class PSO {

    // Classe représentant une solution (ou une particule)
    static class Solution {
        int[] path; // Le chemin
        double fitness; // La valeur de la fonction de fitness
        double[] velocities; // La vélocité

        public Solution(int[] path, double[] velocities) {
            this.path = path;
            this.velocities = velocities;
            this.fitness = evaluateFitness();
        }

        // Génération d'une solution aléatoire
        public static Solution generateRandomSolution(int n) {
            int[] path = new int[n];
            double[] velocities = new double[n]; // Initialiser la vélocité
            for (int i = 0; i < n; i++) {
                path[i] = i;
                velocities[i] = Math.random(); // Initialiser la vélocité avec des valeurs aléatoires entre 0 et 1
            }
            shuffleArray(path); // Mélanger le tableau des indices de ville
            return new Solution(path, velocities);
        }

        // Évaluation de la fonction de fitness (distance totale du chemin)
        public double evaluateFitness() {
            double distance = 0;
            for (int i = 0; i < path.length; i++) {
                // Calculer la distance entre les villes consécutives
                int currentCity = path[i];
                int nextCity = (i == path.length - 1) ? path[0] : path[i + 1]; // Cyclic handling
                distance += distances[currentCity][nextCity];
            }
            return distance;
        }
    }

    static double[][] distances; // Matrice des distances entre les villes
    static int N; // Taille de la population
    static int tmax; // Nombre maximum d'itérations
    static double C1; // Constante cognitive
    static double C2; // Constante sociale
    static double W; // Poids d'inertie
    static String[] cityNames; // Noms des villes

    public static void main(String[] args) {
        // Initialiser les paramètres PSO
        Random random = new Random();
        N = random.nextInt(7) + 5; // Taille de la population aléatoire entre 3 et 7
        tmax = 10; // Nombre maximum d'itérations
        C1 = 1.5; // Constante cognitive
        C2 = 1.5; // Constante sociale
        W = 0.7; // Poids d'inertie

        // Initialiser les noms des villes
        cityNames = new String[N];
        for (int i = 0; i < N; i++) {
            cityNames[i] = "Ville " + i;
        }

        // Initialiser la matrice des distances (ici, une matrice aléatoire pour les besoins du test)
        distances = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                distances[i][j] = Math.round(random.nextDouble() * 100 * 100.0) / 100.0; // Distances aléatoires entre 0 et 100 avec deux décimales
            }
        }

        // Implémenter PSO pour résoudre le problème du Voyageur de Commerce
        PSO pso = new PSO();
        Solution bestSolution = pso.solveTSP();

        // Afficher la meilleure solution trouvée
        System.out.println("\nMeilleure solution trouvée : " + Arrays.toString(bestSolution.path));
        System.out.println("Distance totale : " + String.format("%.2f", bestSolution.fitness ));
        
        // Afficher l'instance initiale du problème et les distances entre les villes
        System.out.println("Instance initiale du problème : ");
        System.out.print("         ");
        for (int i = 0; i < N; i++) {
            System.out.printf("%10s", cityNames[i]);
        }
        System.out.println();
        for (int i = 0; i < N; i++) {
            System.out.printf("%10s", cityNames[i]);
            for (int j = 0; j < N; j++) {
                System.out.printf("%10.2f", distances[i][j]);
            }
            System.out.println();
        }
    }

    // Implémentation de l'algorithme PSO pour résoudre le problème du Voyageur de Commerce
    Solution solveTSP() {
        // Initialisation des particules
        Solution[] particles = new Solution[N];
        for (int i = 0; i < N; i++) {
            particles[i] = Solution.generateRandomSolution(N);
        }

        // Initialisation du personal best et du global best
        Solution[] Pbest = particles.clone();
        Solution gBest = particles[0];

        // Algorithme PSO
        for (int t = 0; t < tmax; t++) {
            System.out.println("\nÉtape " + (t + 1) + " :");
            for (int i = 0; i < N; i++) {
                // Mise à jour de la vélocité et de la solution
                updateVelocity(particles[i], Pbest[i], gBest);
                // Mise à jour du personal best
                if (particles[i].fitness < Pbest[i].fitness) {
                    Pbest[i] = particles[i];
                }
                // Mise à jour du global best
                if (Pbest[i].fitness < gBest.fitness) {
                    gBest = Pbest[i];
                }
                // Afficher la solution actuelle
                System.out.println("Particle " + i + " : " + Arrays.toString(particles[i].path) + ", Fitness : " + String.format("%.2f", particles[i].fitness));
            }
        }
        return gBest;
    }

    void updateVelocity(Solution particle, Solution Pbest, Solution gBest) {
        Random random = new Random();
        double[] v = new double[N]; // Nouvelle vélocité
        for (int i = 0; i < N; i++) {
            double r1 = random.nextDouble();
            double r2 = random.nextDouble();
            v[i] = W * particle.velocities[i] + C1 * r1 * (Pbest.path[i] - particle.path[i]) + C2 * r2 * (gBest.path[i] - particle.path[i]);
        }
        // Mettre à jour le chemin de la particule en utilisant la vélocité calculée
        int[] newPath = new int[N];
        boolean[] visited = new boolean[N];
        for (int i = 0; i < N; i++) {
            int newIndex = (particle.path[i] + ((int) Math.round(v[i])) + N) % N; // Utilisation de modulo pour rester dans les limites de la population
            while (visited[newIndex]) { // Trouver un nouvel index non visité
                newIndex = (newIndex + 1) % N;
            }
            newPath[i] = newIndex;
            visited[newIndex] = true;
        }
        particle.path = newPath; // Mettre à jour le chemin de la particule
    }



    // Fonction pour mélanger un tableau (utile pour générer une solution aléatoire)
    static void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
