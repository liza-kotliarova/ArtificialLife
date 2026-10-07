import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {

    private final int width;
    private final int height;

    private final Agent[][] field;
    private final List<Agent> agents;

    private final Random random = new Random(42);

    private int currentStep = 0;

    public Environment(int width, int height) {

        this.width = width;
        this.height = height;

        field = new Agent[height][width];
        agents = new ArrayList<>();
    }

    public Random getRandom() {
        return random;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    // =========================================================
    // Работа с полем
    // =========================================================

    public boolean isInside(int x, int y) {

        return x >= 0
                && x < width
                && y >= 0
                && y < height;
    }

    public boolean isEmpty(int x, int y) {

        return isInside(x, y)
                && field[y][x] == null;
    }

    public Agent getAgentAt(int x, int y) {

        if (!isInside(x, y)) {
            return null;
        }

        return field[y][x];
    }

    public void addAgent(Agent agent) {

        if (!isInside(
                agent.getX(),
                agent.getY())) {

            return;
        }

        if (!isEmpty(
                agent.getX(),
                agent.getY())) {

            return;
        }

        field
                [agent.getY()]
                [agent.getX()] = agent;

        agents.add(agent);
    }

    public void moveAgent(
            Agent agent,
            int newX,
            int newY) {

        if (!isInside(
                newX,
                newY)) {

            return;
        }

        if (!isEmpty(
                newX,
                newY)) {

            return;
        }

        field
                [agent.getY()]
                [agent.getX()] = null;

        agent.x = newX;
        agent.y = newY;

        field[newY][newX] = agent;
    }

    public void removeAgent(
            Agent agent) {

        if (agent == null) {
            return;
        }

        if (isInside(
                agent.getX(),
                agent.getY())
                && field
                [agent.getY()]
                [agent.getX()] == agent) {

            field
                    [agent.getY()]
                    [agent.getX()] = null;
        }

        agents.remove(agent);
    }

    // =========================================================
    // Поиск ближайшего растения
    // =========================================================

    public Plant findPlantNear(
            int x,
            int y) {

        Plant closest = null;

        int closestDistance =
                Integer.MAX_VALUE;

        for (int dy = -2; dy <= 2; dy++) {

            for (int dx = -2; dx <= 2; dx++) {

                int newX = x + dx;
                int newY = y + dy;

                if (!isInside(
                        newX,
                        newY)) {

                    continue;
                }

                Agent agent =
                        field[newY][newX];

                if (agent instanceof Plant) {

                    int distance =
                            Math.abs(dx)
                                    + Math.abs(dy);

                    if (distance < closestDistance) {

                        closest =
                                (Plant) agent;

                        closestDistance =
                                distance;
                    }
                }
            }
        }

        return closest;
    }

    // =========================================================
    // Поиск ближайшего травоядного
    // =========================================================

    public Herbivore findHerbivoreNear(
            int x,
            int y) {

        Herbivore closest = null;

        int closestDistance =
                Integer.MAX_VALUE;

        for (int dy = -2; dy <= 2; dy++) {

            for (int dx = -2; dx <= 2; dx++) {

                int newX = x + dx;
                int newY = y + dy;

                if (!isInside(
                        newX,
                        newY)) {

                    continue;
                }

                Agent agent =
                        field[newY][newX];

                if (agent instanceof Herbivore) {

                    int distance =
                            Math.abs(dx)
                                    + Math.abs(dy);

                    if (distance < closestDistance) {

                        closest =
                                (Herbivore) agent;

                        closestDistance =
                                distance;
                    }
                }
            }
        }

        return closest;
    }

    // =========================================================
    // Поиск ближайшего хищника
    // =========================================================

    public Predator findPredatorNear(
            int x,
            int y) {

        Predator closest = null;

        int closestDistance =
                Integer.MAX_VALUE;

        for (int dy = -2; dy <= 2; dy++) {

            for (int dx = -2; dx <= 2; dx++) {

                if (dx == 0 && dy == 0) {
                    continue;
                }

                int newX = x + dx;
                int newY = y + dy;

                if (!isInside(
                        newX,
                        newY)) {

                    continue;
                }

                Agent agent =
                        field[newY][newX];

                if (agent instanceof Predator) {

                    int distance =
                            Math.abs(dx)
                                    + Math.abs(dy);

                    if (distance < closestDistance) {

                        closest =
                                (Predator) agent;

                        closestDistance =
                                distance;
                    }
                }
            }
        }

        return closest;
    }

    // =========================================================
    // Размножение растений
    // =========================================================

    public boolean reproducePlant(
            Plant parent) {

        int maxPlants =
                (width * height * 3) / 8;

        // Для 20x20 = максимум 150 растений.

        if (countPlants() >= maxPlants) {
            return false;
        }

        List<int[]> freeCells =
                new ArrayList<>();

        for (int dy = -1; dy <= 1; dy++) {

            for (int dx = -1; dx <= 1; dx++) {

                if (dx == 0 && dy == 0) {
                    continue;
                }

                int newX =
                        parent.getX() + dx;

                int newY =
                        parent.getY() + dy;

                if (isEmpty(
                        newX,
                        newY)) {

                    freeCells.add(
                            new int[]{
                                    newX,
                                    newY
                            }
                    );
                }
            }
        }

        if (freeCells.isEmpty()) {
            return false;
        }

        int[] cell =
                freeCells.get(
                        random.nextInt(
                                freeCells.size()
                        )
                );

        Plant child =
                new Plant(
                        cell[0],
                        cell[1]
                );

        addAgent(child);

        return true;
    }

    // =========================================================
    // Размножение травоядных
    // =========================================================

    public boolean reproduceHerbivore(
            Herbivore parent) {

        int maxHerbivores =
                (width * height) / 10;

        // Для 20x20 = максимум 40.

        if (countHerbivores()
                >= maxHerbivores) {

            return false;
        }

        List<int[]> freeCells =
                new ArrayList<>();

        for (int dy = -1; dy <= 1; dy++) {

            for (int dx = -1; dx <= 1; dx++) {

                if (dx == 0 && dy == 0) {
                    continue;
                }

                int newX =
                        parent.getX() + dx;

                int newY =
                        parent.getY() + dy;

                if (isEmpty(
                        newX,
                        newY)) {

                    freeCells.add(
                            new int[]{
                                    newX,
                                    newY
                            }
                    );
                }
            }
        }

        if (freeCells.isEmpty()) {
            return false;
        }

        int[] cell =
                freeCells.get(
                        random.nextInt(
                                freeCells.size()
                        )
                );

        Herbivore child =
                new Herbivore(
                        cell[0],
                        cell[1],
                        1000
                );

        addAgent(child);

        return true;
    }

    // =========================================================
    // Размножение хищников
    // =========================================================

    public boolean reproducePredator(
            Predator parent) {

        int maxPredators =
                (width * height) / 40;

        // Для 20x20 = максимум 10.

        if (countPredators()
                >= maxPredators) {

            return false;
        }

        List<int[]> freeCells =
                new ArrayList<>();

        for (int dy = -1; dy <= 1; dy++) {

            for (int dx = -1; dx <= 1; dx++) {

                if (dx == 0 && dy == 0) {
                    continue;
                }

                int newX =
                        parent.getX() + dx;

                int newY =
                        parent.getY() + dy;

                if (isEmpty(
                        newX,
                        newY)) {

                    freeCells.add(
                            new int[]{
                                    newX,
                                    newY
                            }
                    );
                }
            }
        }

        if (freeCells.isEmpty()) {
            return false;
        }

        int[] cell =
                freeCells.get(
                        random.nextInt(
                                freeCells.size()
                        )
                );

        Predator child =
                new Predator(
                        cell[0],
                        cell[1],
                        1300
                );

        addAgent(child);

        return true;
    }

    // =========================================================
    // Один шаг
    // =========================================================

    public void makeStep() {

        currentStep++;

        // -------------------------------
        // Растения
        // -------------------------------

        List<Agent> currentAgents =
                new ArrayList<>(agents);

        for (Agent agent : currentAgents) {

            if (agents.contains(agent)
                    && agent instanceof Plant
                    && agent.isAlive()) {

                agent.act(this);
            }
        }

        // -------------------------------
        // Травоядные
        // -------------------------------

        currentAgents =
                new ArrayList<>(agents);

        for (Agent agent : currentAgents) {

            if (agents.contains(agent)
                    && agent instanceof Herbivore
                    && agent.isAlive()) {

                agent.act(this);
            }
        }

        // -------------------------------
        // Хищники
        // -------------------------------

        currentAgents =
                new ArrayList<>(agents);

        for (Agent agent : currentAgents) {

            if (agents.contains(agent)
                    && agent instanceof Predator
                    && agent.isAlive()) {

                agent.act(this);
            }
        }

        // -------------------------------
        // Удаление умерших
        // -------------------------------

        List<Agent> deadAgents =
                new ArrayList<>();

        for (Agent agent : agents) {

            if (!agent.isAlive()) {
                deadAgents.add(agent);
            }
        }

        for (Agent agent : deadAgents) {
            removeAgent(agent);
        }
    }

    // =========================================================
    // Подсчёт растений
    // =========================================================

    public int countPlants() {

        int count = 0;

        for (Agent agent : agents) {

            if (agent instanceof Plant) {
                count++;
            }
        }

        return count;
    }

    // =========================================================
    // Подсчёт травоядных
    // =========================================================

    public int countHerbivores() {

        int count = 0;

        for (Agent agent : agents) {

            if (agent instanceof Herbivore) {
                count++;
            }
        }

        return count;
    }

    // =========================================================
    // Подсчёт хищников
    // =========================================================

    public int countPredators() {

        int count = 0;

        for (Agent agent : agents) {

            if (agent instanceof Predator) {
                count++;
            }
        }

        return count;
    }

    // =========================================================
    // Вывод поля
    // =========================================================

    public void printField() {

        System.out.println(
                "Шаг: " + currentStep
                        + " | Растения: "
                        + countPlants()
                        + " | Травоядные: "
                        + countHerbivores()
                        + " | Хищники: "
                        + countPredators()
        );

        System.out.println(
                "=============================="
        );

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                Agent agent =
                        field[y][x];

                if (agent instanceof Plant) {

                    System.out.print("P ");

                } else if (agent instanceof Herbivore) {

                    System.out.print("H ");

                } else if (agent instanceof Predator) {

                    System.out.print("X ");

                } else {

                    System.out.print(". ");
                }
            }

            System.out.println();
        }
    }
}