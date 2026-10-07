public class Herbivore extends Agent {

    private static final int ENERGY_LOSS = 1;
    private static final int ENERGY_FROM_PLANT = 55;

    private static final int REPRODUCTION_THRESHOLD = 1200;
    private static final int REPRODUCTION_COOLDOWN = 20;

    private static final int HUNGER_COOLDOWN = 3;

    private int reproductionCooldown = 0;
    private int hungerCooldown = 0;

    public Herbivore(int x, int y, int energy) {
        super(x, y, energy);
    }

    @Override
    public void act(Environment environment) {

        energy -= ENERGY_LOSS;

        if (reproductionCooldown > 0) {
            reproductionCooldown--;
        }

        if (hungerCooldown > 0) {
            hungerCooldown--;
        }

        if (!isAlive()) {
            return;
        }

        // Сначала проверяем опасность.
        Predator predator =
                environment.findPredatorNear(x, y);

        if (predator != null) {

            moveAwayFrom(
                    predator.getX(),
                    predator.getY(),
                    environment
            );

        } else if (hungerCooldown > 0) {

            randomMove(environment);

        } else {

            Plant plant =
                    environment.findPlantNear(x, y);

            if (plant != null) {

                if (isAdjacent(plant)) {
                    eat(plant, environment);
                } else {
                    moveTowards(
                            plant.getX(),
                            plant.getY(),
                            environment
                    );
                }

            } else {

                randomMove(environment);
            }
        }

        // Размножение.
        if (energy >= REPRODUCTION_THRESHOLD
                && reproductionCooldown == 0) {

            if (environment.reproduceHerbivore(this)) {
                reproductionCooldown =
                        REPRODUCTION_COOLDOWN;
            }
        }
    }

    private void eat(
            Plant plant,
            Environment environment) {

        energy += ENERGY_FROM_PLANT;

        environment.removeAgent(plant);

        hungerCooldown = HUNGER_COOLDOWN;
    }

    private boolean isAdjacent(Agent agent) {

        int dx =
                Math.abs(agent.getX() - x);

        int dy =
                Math.abs(agent.getY() - y);

        return dx + dy == 1;
    }

    private void moveTowards(
            int targetX,
            int targetY,
            Environment environment) {

        int dx = targetX - x;
        int dy = targetY - y;

        if (Math.abs(dx) >= Math.abs(dy)) {

            if (dx != 0) {

                int newX =
                        x + Integer.signum(dx);

                if (tryMove(
                        newX,
                        y,
                        environment)) {

                    return;
                }
            }

            if (dy != 0) {

                int newY =
                        y + Integer.signum(dy);

                if (tryMove(
                        x,
                        newY,
                        environment)) {

                    return;
                }
            }

        } else {

            if (dy != 0) {

                int newY =
                        y + Integer.signum(dy);

                if (tryMove(
                        x,
                        newY,
                        environment)) {

                    return;
                }
            }

            if (dx != 0) {

                int newX =
                        x + Integer.signum(dx);

                if (tryMove(
                        newX,
                        y,
                        environment)) {

                    return;
                }
            }
        }

        randomMove(environment);
    }

    private void moveAwayFrom(
            int predatorX,
            int predatorY,
            Environment environment) {

        int dx = x - predatorX;
        int dy = y - predatorY;

        int preferredX = x;
        int preferredY = y;

        if (Math.abs(dx) >= Math.abs(dy)) {

            if (dx > 0) {
                preferredX = x + 1;
            } else if (dx < 0) {
                preferredX = x - 1;
            }

            if (tryMove(
                    preferredX,
                    preferredY,
                    environment)) {

                return;
            }

            if (dy > 0) {
                preferredY = y + 1;
            } else if (dy < 0) {
                preferredY = y - 1;
            }

            if (tryMove(
                    x,
                    preferredY,
                    environment)) {

                return;
            }

        } else {

            if (dy > 0) {
                preferredY = y + 1;
            } else if (dy < 0) {
                preferredY = y - 1;
            }

            if (tryMove(
                    preferredX,
                    preferredY,
                    environment)) {

                return;
            }

            if (dx > 0) {
                preferredX = x + 1;
            } else if (dx < 0) {
                preferredX = x - 1;
            }

            if (tryMove(
                    preferredX,
                    y,
                    environment)) {

                return;
            }
        }

        randomMove(environment);
    }

    private void randomMove(
            Environment environment) {

        int[] directions = {
                0, 1, 2, 3
        };

        for (int i = directions.length - 1;
             i > 0;
             i--) {

            int j =
                    environment.getRandom()
                            .nextInt(i + 1);

            int temp =
                    directions[i];

            directions[i] =
                    directions[j];

            directions[j] =
                    temp;
        }

        for (int direction : directions) {

            int newX = x;
            int newY = y;

            switch (direction) {

                case 0 -> newX++;

                case 1 -> newX--;

                case 2 -> newY++;

                case 3 -> newY--;
            }

            if (tryMove(
                    newX,
                    newY,
                    environment)) {

                return;
            }
        }
    }

    private boolean tryMove(
            int newX,
            int newY,
            Environment environment) {

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

            return true;
        }

        return false;
    }
}