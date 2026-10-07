import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {

    private static final int MAX_STEPS = 10300;

    // Текущая задержка между шагами
    private int stepDelay = 200;

    private final Environment environment;
    private final SimulationPanel simulationPanel;

    private final Timer timer;

    private int step = 0;

    // Счётчики
    private final JLabel stepLabel;
    private final JLabel plantsLabel;
    private final JLabel herbivoresLabel;
    private final JLabel predatorsLabel;


    public SimulationFrame() {

        setTitle("Artificial Life");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );


        environment =
                new Environment(20, 20);

        createInitialPopulation();


        // ==========================================
        // ПОЛЕ
        // ==========================================

        simulationPanel =
                new SimulationPanel(environment);

        JPanel fieldPanel =
                new JPanel(
                        new GridBagLayout()
                );

        fieldPanel.add(simulationPanel);


        // ==========================================
        // СЧЁТЧИКИ
        // ==========================================

        stepLabel =
                new JLabel("Шаг: 0");

        plantsLabel =
                new JLabel(
                        "Растения: "
                                + environment.countPlants()
                );

        herbivoresLabel =
                new JLabel(
                        "Травоядные: "
                                + environment.countHerbivores()
                );

        predatorsLabel =
                new JLabel(
                        "Хищники: "
                                + environment.countPredators()
                );


        JPanel infoPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                20,
                                5
                        )
                );

        infoPanel.add(stepLabel);
        infoPanel.add(plantsLabel);
        infoPanel.add(herbivoresLabel);
        infoPanel.add(predatorsLabel);


        // ==========================================
        // КНОПКИ
        // ==========================================

        JButton startButton =
                new JButton("Старт");

        JButton pauseButton =
                new JButton("Пауза");

        JButton stepButton =
                new JButton("Шаг");


        JPanel buttonsPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                5
                        )
                );

        buttonsPanel.add(startButton);
        buttonsPanel.add(pauseButton);
        buttonsPanel.add(stepButton);


        // ==========================================
        // СКОРОСТЬ
        // ==========================================

        JLabel speedLabel =
                new JLabel("Скорость:");

        JSlider speedSlider =
                new JSlider(
                        50,
                        1000,
                        stepDelay
                );

        speedSlider.setMajorTickSpacing(250);
        speedSlider.setMinorTickSpacing(50);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);


        JPanel speedPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                5
                        )
                );

        speedPanel.add(speedLabel);
        speedPanel.add(speedSlider);


        // ==========================================
        // НИЖНЯЯ ПАНЕЛЬ УПРАВЛЕНИЯ
        // ==========================================

        JPanel controlPanel =
                new JPanel(
                        new BorderLayout()
                );

        controlPanel.add(
                speedPanel,
                BorderLayout.NORTH
        );

        controlPanel.add(
                buttonsPanel,
                BorderLayout.SOUTH
        );


        // ==========================================
        // РАЗМЕЩЕНИЕ ЭЛЕМЕНТОВ
        // ==========================================

        add(
                infoPanel,
                BorderLayout.NORTH
        );

        add(
                fieldPanel,
                BorderLayout.CENTER
        );

        add(
                controlPanel,
                BorderLayout.SOUTH
        );


        // ==========================================
        // ТАЙМЕР
        // ==========================================

        timer =
                new Timer(
                        stepDelay,
                        e -> makeStep()
                );


        // ==========================================
        // КНОПКА "СТАРТ"
        // ==========================================

        startButton.addActionListener(e -> {

            if (!timer.isRunning()) {

                timer.start();
            }
        });


        // ==========================================
        // КНОПКА "ПАУЗА"
        // ==========================================

        pauseButton.addActionListener(e -> {

            timer.stop();
        });


        // ==========================================
        // КНОПКА "ШАГ"
        // ==========================================

        stepButton.addActionListener(e -> {

            if (!timer.isRunning()) {

                makeStep();
            }
        });


        // ==========================================
        // ПОЛЗУНОК СКОРОСТИ
        // ==========================================

        speedSlider.addChangeListener(e -> {

            stepDelay =
                    speedSlider.getValue();

            timer.setDelay(stepDelay);
        });


        pack();

        setLocationRelativeTo(null);

        setVisible(true);
    }


    // ==========================================
    // НАЧАЛЬНАЯ ПОПУЛЯЦИЯ
    // ==========================================

    private void createInitialPopulation() {

        int width =
                environment.getWidth();

        int height =
                environment.getHeight();


        // Растения.
        for (int i = 0; i < 80; i++) {

            addRandomPlant(width, height);
        }


        // Травоядные.
        for (int i = 0; i < 20; i++) {

            addRandomHerbivore(width, height);
        }


        // Хищники.
        for (int i = 0; i < 5; i++) {

            addRandomPredator(width, height);
        }
    }


    // ==========================================
    // СЛУЧАЙНОЕ РАЗМЕЩЕНИЕ РАСТЕНИЯ
    // ==========================================

    private void addRandomPlant(
            int width,
            int height) {

        for (int attempt = 0; attempt < 100; attempt++) {

            int x =
                    environment.getRandom()
                            .nextInt(width);

            int y =
                    environment.getRandom()
                            .nextInt(height);


            if (environment.isEmpty(x, y)) {

                environment.addAgent(
                        new Plant(x, y)
                );

                return;
            }
        }
    }


    // ==========================================
    // СЛУЧАЙНОЕ РАЗМЕЩЕНИЕ ТРАВОЯДНОГО
    // ==========================================

    private void addRandomHerbivore(
            int width,
            int height) {

        for (int attempt = 0; attempt < 100; attempt++) {

            int x =
                    environment.getRandom()
                            .nextInt(width);

            int y =
                    environment.getRandom()
                            .nextInt(height);


            if (environment.isEmpty(x, y)) {

                environment.addAgent(
                        new Herbivore(
                                x,
                                y,
                                1200
                        )
                );

                return;
            }
        }
    }


    // ==========================================
    // СЛУЧАЙНОЕ РАЗМЕЩЕНИЕ ХИЩНИКА
    // ==========================================

    private void addRandomPredator(
            int width,
            int height) {

        for (int attempt = 0; attempt < 100; attempt++) {

            int x =
                    environment.getRandom()
                            .nextInt(width);

            int y =
                    environment.getRandom()
                            .nextInt(height);


            if (environment.isEmpty(x, y)) {

                environment.addAgent(
                        new Predator(
                                x,
                                y,
                                1500
                        )
                );

                return;
            }
        }
    }


    // ==========================================
    // ОДИН ШАГ СИМУЛЯЦИИ
    // ==========================================

    private void makeStep() {

        // Запасное ограничение по количеству шагов.
        if (step >= MAX_STEPS) {

            timer.stop();

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "СИМУЛЯЦИЯ ОСТАНОВЛЕНА"
            );
            System.out.println(
                    "Достигнуто максимальное число шагов: "
                            + MAX_STEPS
            );
            System.out.println(
                    "================================"
            );

            return;
        }


        step++;

        environment.makeStep();


        // Обновляем отображение поля.
        simulationPanel.repaint();

        // Обновляем счётчики.
        updateCounters();


        // Вывод текущего состояния в консоль.
        int plants =
                environment.countPlants();

        int herbivores =
                environment.countHerbivores();

        int predators =
                environment.countPredators();


        System.out.println(
                "Шаг: "
                        + step
                        + " | "
                        + "Растения: "
                        + plants
                        + " | "
                        + "Травоядные: "
                        + herbivores
                        + " | "
                        + "Хищники: "
                        + predators
        );


        // ==========================================
        // ПРОВЕРКА ИСЧЕЗНОВЕНИЯ ВИДА
        // ==========================================

        if (plants == 0
                || herbivores == 0
                || predators == 0) {

            timer.stop();


            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "СИМУЛЯЦИЯ ОСТАНОВЛЕНА"
            );


            if (plants == 0) {

                System.out.println(
                        "Причина: исчезли растения."
                );
            }

            if (herbivores == 0) {

                System.out.println(
                        "Причина: исчезли травоядные."
                );
            }

            if (predators == 0) {

                System.out.println(
                        "Причина: исчезли хищники."
                );
            }


            System.out.println(
                    "Шаг: " + step
            );

            System.out.println(
                    "Растения: " + plants
            );

            System.out.println(
                    "Травоядные: " + herbivores
            );

            System.out.println(
                    "Хищники: " + predators
            );

            System.out.println(
                    "================================"
            );

            return;
        }


        // ==========================================
        // ПРОВЕРКА МАКСИМАЛЬНОГО ЧИСЛА ШАГОВ
        // ==========================================

        if (step >= MAX_STEPS) {

            timer.stop();

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "СИМУЛЯЦИЯ ОСТАНОВЛЕНА"
            );
            System.out.println(
                    "Достигнуто максимальное число шагов: "
                            + MAX_STEPS
            );
            System.out.println(
                    "================================"
            );
        }
    }


    // ==========================================
    // ОБНОВЛЕНИЕ СЧЁТЧИКОВ
    // ==========================================

    private void updateCounters() {

        stepLabel.setText(
                "Шаг: " + step
        );

        plantsLabel.setText(
                "Растения: "
                        + environment.countPlants()
        );

        herbivoresLabel.setText(
                "Травоядные: "
                        + environment.countHerbivores()
        );

        predatorsLabel.setText(
                "Хищники: "
                        + environment.countPredators()
        );
    }


    // ==========================================
    // ЗАПУСК ПРОГРАММЫ
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                SimulationFrame::new
        );
    }
}