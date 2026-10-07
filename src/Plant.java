public class Plant extends Agent {

    private static final int REPRODUCTION_CHANCE = 8;

    public Plant(int x, int y) {
        super(x, y, 1);
    }

    @Override
    public void act(Environment environment) {

        int chance = environment.getRandom().nextInt(100);

        if (chance < REPRODUCTION_CHANCE) {
            environment.reproducePlant(this);
        }
    }
}