import java.util.Scanner;

public class ConsoleMain {

    public static void main(String[] args) {

        // Создаём поле 10×10.
        Environment environment = new Environment(10, 10);

        // Начальные растения — 20 штук.
        // Растения распределены по разным частям поля.
        environment.addAgent(new Plant(1, 1));
        environment.addAgent(new Plant(3, 1));
        environment.addAgent(new Plant(5, 1));
        environment.addAgent(new Plant(8, 1));

        environment.addAgent(new Plant(0, 3));
        environment.addAgent(new Plant(2, 3));
        environment.addAgent(new Plant(5, 3));
        environment.addAgent(new Plant(8, 3));

        environment.addAgent(new Plant(1, 5));
        environment.addAgent(new Plant(4, 5));
        environment.addAgent(new Plant(7, 5));
        environment.addAgent(new Plant(9, 5));

        environment.addAgent(new Plant(0, 7));
        environment.addAgent(new Plant(3, 7));
        environment.addAgent(new Plant(6, 7));
        environment.addAgent(new Plant(8, 7));

        environment.addAgent(new Plant(1, 9));
        environment.addAgent(new Plant(4, 9));
        environment.addAgent(new Plant(6, 9));
        environment.addAgent(new Plant(9, 9));

        // Начальные травоядные — 4 штуки, энергия 30.
        environment.addAgent(new Herbivore(2, 2, 30));
        environment.addAgent(new Herbivore(7, 2, 30));
        environment.addAgent(new Herbivore(2, 6, 30));
        environment.addAgent(new Herbivore(7, 6, 30));

        // Начальный хищник — 1 штука, энергия 40.
        environment.addAgent(new Predator(5, 5, 40));

        Scanner scanner = new Scanner(System.in);

        // Номер текущего шага.
        int step = 1;

        // Максимальное количество итераций — 1000.
        while (step <= 1000) {

            System.out.println();
            System.out.println("Шаг: " + step);
            environment.printField();

            System.out.println();
            System.out.println("Растения: " + environment.countPlants());
            System.out.println("Травоядные: " + environment.countHerbivores());
            System.out.println("Хищники: " + environment.countPredators());

            // Общее количество живых агентов.
            int total = environment.countPlants()
                    + environment.countHerbivores()
                    + environment.countPredators();

            // Если все агенты погибли, завершаем симуляцию.
            if (total == 0) {
                System.out.println();
                System.out.println("Все агенты погибли. Симуляция остановлена.");
                break;
            }

            // Ждём нажатия Enter перед следующим шагом.
            System.out.println();
            System.out.println("Нажмите Enter для следующего шага...");
            scanner.nextLine();

            // Выполняем один шаг симуляции.
            environment.makeStep();

            // Переходим к следующему номеру шага.
            step++;
        }

        // Если достигнут лимит в 1000 итераций.
        if (step > 1000) {
            System.out.println();
            System.out.println("Достигнуто максимальное количество итераций: 1000.");
        }

        scanner.close();
    }
}