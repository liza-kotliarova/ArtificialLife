import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Predator extends Agent {

    private static final int ENERGY_LOSS = 1;

    private static final int ENERGY_FROM_HERBIVORE = 80;

    private static final int REPRODUCTION_THRESHOLD = 2600;
    private static final int REPRODUCTION_COOLDOWN = 50;

    private int reproductionCooldown = 0;

    public Predator(int x, int y, int energy) {
        super(x, y, energy);
    }

    @Override
    public void act(Environment environment) {

        energy -= ENERGY_LOSS;

        if (!isAlive()) {
            return;
        }

        if (reproductionCooldown > 0) {
            reproductionCooldown--;
        }

        // Размножение только при большом запасе энергии.
        if (energy >= REPRODUCTION_THRESHOLD
                && reproductionCooldown == 0) {

            if (environment.reproducePredator(this)) {

                reproductionCooldown =
                        REPRODUCTION_COOLDOWN;
            }
        }

        Herbivore herbivore =
                environment.findHerbivoreNear(x, y);

        if (herbivore == null) {

            moveRandomly(environment);

            return;
        }

        int targetX =
                herbivore.getX();

        int targetY =
                herbivore.getY();

        if (isAdjacent(
                targetX,
                targetY)) {

            eatHerbivore(
                    environment,
                    herbivore
            );

            return;
        }

        moveTowards(
                environment,
                targetX,
                targetY
        );
    }

    private boolean isAdjacent(
            int targetX,
            int targetY) {

        int dx =
                Math.abs(x - targetX);

        int dy =
                Math.abs(y - targetY);

        return dx + dy == 1;
    }

    private void eatHerbivore(
            Environment environment,
            Herbivore herbivore) {

        if (environment.getAgentAt(
                herbivore.getX(),
                herbivore.getY())
                != herbivore) {

            return;
        }

        environment.removeAgent(
                herbivore
        );

        energy += ENERGY_FROM_HERBIVORE;
    }

    private void moveTowards(
            Environment environment,
            int targetX,
            int targetY) {

        List<int[]> directions =
                new ArrayList<>();

        int dx =
                targetX - x;

        int dy =
                targetY - y;

        if (Math.abs(dx) >= Math.abs(dy)) {

            if (dx > 0) {
                directions.add(
                        new int[]{1, 0}
                );
            } else if (dx < 0) {
                directions.add(
                        new int[]{-1, 0}
                );
            }

            if (dy > 0) {
                directions.add(
                        new int[]{0, 1}
                );
            } else if (dy < 0) {
                directions.add(
                        new int[]{0, -1}
                );
            }

        } else {

            if (dy > 0) {
                directions.add(
                        new int[]{0, 1}
                );
            } else if (dy < 0) {
                directions.add(
                        new int[]{0, -1}
                );
            }

            if (dx > 0) {
                directions.add(
                        new int[]{1, 0}
                );
            } else if (dx < 0) {
                directions.add(
                        new int[]{-1, 0}
                );
            }
        }

        Collections.shuffle(
                directions,
                environment.getRandom()
        );

        for (int[] direction : directions) {

            int newX =
                    x + direction[0];

            int newY =
                    y + direction[1];

            if (environment.isInside(
                    newX,
                    newY)
                    && environment.isEmpty(
                    newX,
                    newY)) {

                environment.moveAgent(
                        this,
                        newX,
                        newY
                );

                return;
            }
        }

        moveRandomly(environment);
    }

    private void moveRandomly(
            Environment environment) {

        List<int[]> directions =
                new ArrayList<>();

        directions.add(
                new int[]{1, 0}
        );

        directions.add(
                new int[]{-1, 0}
        );

        directions.add(
                new int[]{0, 1}
        );

        directions.add(
                new int[]{0, -1}
        );

        Collections.shuffle(
                directions,
                environment.getRandom()
        );

        for (int[] direction : directions) {

            int newX =
                    x + direction[0];

            int newY =
                    y + direction[1];

            if (environment.isInside(
                    newX,
                    newY)
                    && environment.isEmpty(
                    newX,
                    newY)) {

                environment.moveAgent(
                        this,
                        newX,
                        newY
                );

                return;
            }
        }
    }
}