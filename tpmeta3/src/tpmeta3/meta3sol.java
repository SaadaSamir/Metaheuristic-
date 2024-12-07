package tpmeta3;
import java.util.*;

class Node {
    int[] path;
    double g;
    double h;
    double f;

    Node(int[] path, double g, double h) {
        this.path = path;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    @Override
    public String toString() {
        return "Node{" +
                "path=" + Arrays.toString(path) +
                ", g=" + g +
                ", h=" + h +
                ", f=" + f +
                '}';
    }
}

class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node node1, Node node2) {
        return Double.compare(node1.f, node2.f);
    }
}

public class meta3sol {
    static class MKPProblem {
        private int[] capacities;
        private int[] weights;
        private int[] values;

        public MKPProblem(int[] capacities, int[] weights, int[] values) {
            this.capacities = capacities;
            this.weights = weights;
            this.values = values;
        }

        public int[] getCapacities() {
            return capacities;
        }

        public int[] getWeights() {
            return weights;
        }

        public int[] getValues() {
            return values;
        }
    }

    public static List<Integer> aStarSearch(MKPProblem problem) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(new NodeComparator());
        Set<String> closedSet = new HashSet<>();

        int[] initialPath = new int[0]; // Starting with no items in any sack
        Node initialNode = new Node(initialPath, 0, calculateHeuristic(initialPath, problem));
        openSet.add(initialNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            if (isGoal(currentNode.path, problem)) {
                List<Integer> solution = new ArrayList<>();
                for (int sack : currentNode.path) {
                    solution.add(sack);
                }
                return solution;
            }

            closedSet.add(Arrays.toString(currentNode.path));

            List<Node> successors = generateSuccessors(currentNode, problem);

            for (Node successor : successors) {
                if (!closedSet.contains(Arrays.toString(successor.path))) {
                    openSet.add(successor);
                }
            }
        }

        return null; // No solution found
    }


    private static List<Node> generateSuccessors(Node node, MKPProblem problem) {
        List<Node> successors = new ArrayList<>();

        int[] capacities = problem.getCapacities();
        int[] weights = problem.getWeights();
        int[] values = problem.getValues();

        for (int i = 0; i < capacities.length; i++) {
            if (node.path.length < weights.length) {
                int[] newPath = Arrays.copyOf(node.path, node.path.length + 1);
                newPath[node.path.length] = i;

                double g = node.g + values[node.path.length];
                double h = calculateHeuristic(newPath, problem);

                successors.add(new Node(newPath, g, h));
            }
        }

        return successors;
    }

    private static boolean isGoal(int[] path, MKPProblem problem) {
        return path.length == problem.getWeights().length;
    }

    private static double calculateHeuristic(int[] path, MKPProblem problem) {
        int[] capacities = problem.getCapacities();
        int[] weights = problem.getWeights();
        int[] values = problem.getValues();

        int[] remainingCapacity = Arrays.copyOf(capacities, capacities.length);

        // Calcul du poids total des objets déjà placés dans les sacs
        int totalWeight = 0;
        for (int i = 0; i < path.length; i++) {
            totalWeight += weights[i];
            remainingCapacity[path[i]] -= weights[i];
        }

        // Calcul de la valeur totale des objets déjà placés dans les sacs
        int totalValue = 0;
        for (int i = 0; i < path.length; i++) {
            totalValue += values[i];
        }

        // Estimation de la valeur restante maximale en fonction de la capacité restante de chaque sac
        double maxRemainingValue = 0;
        for (int i = 0; i < remainingCapacity.length; i++) {
            if (remainingCapacity[i] >= 0) {
                maxRemainingValue += remainingCapacity[i] * values[values.length - 1]; // Valeur maximale d'un objet
            }
        }

        return totalValue + maxRemainingValue;
    }



    public static void main(String[] args) {
        Random random = new Random();

        // Generating random instance
        int numberOfSacks = random.nextInt(7) + 2; // At least 2 sacks
        int[] capacities = new int[numberOfSacks];
        int[] weights = new int[random.nextInt(5) + 5]; // At least 5 items
        int[] values = new int[weights.length];
        
        // Random capacities for each sack
        for (int i = 0; i < capacities.length; i++) {
            capacities[i] = random.nextInt(20) + 10; // Random capacity between 10 and 29
        }

        // Random weights and values for each item
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextInt(15) + 1; // Random weight between 1 and 15
            values[i] = random.nextInt(20) + 5; // Random value between 5 and 24
        }

        MKPProblem mkpProblem = new MKPProblem(capacities, weights, values);

        System.out.println("Instance du problème du Sac à Dos Multiple :");
        System.out.println("Nombre de sacs : " + numberOfSacks);
        System.out.println("Poids : " + Arrays.toString(weights));
        System.out.println("Valeurs : " + Arrays.toString(values));
        System.out.println("Capacités : " + Arrays.toString(capacities));

        List<Integer> solution = aStarSearch(mkpProblem);

        if (solution != null) {
            int maxValue = 0;
            for (int sack : solution) {
                maxValue += values[sack];
            }
            System.out.println("Solution trouvée : " + solution);
            System.out.println("Valeur maximale de la solution : " + maxValue);
        } else {
            System.out.println("Aucune solution trouvée.");
        }
    }
}

