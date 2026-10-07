import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Dimension;

public class SimulationPanel extends JPanel {

    // Размер одной клетки в пикселях
    private static final int CELL_SIZE = 30;

    // Ссылка на модель экосистемы
    private final Environment environment;

    public SimulationPanel(Environment environment) {

        // Сохраняем переданный Environment
        this.environment = environment;

        // Размер панели равен размеру всего поля
        setPreferredSize(new Dimension(
                environment.getWidth() * CELL_SIZE,
                environment.getHeight() * CELL_SIZE
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {

        // Очищаем панель перед новой отрисовкой
        super.paintComponent(g);

        // Устанавливаем шрифт для эмодзи
        g.setFont(new Font(
                "Segoe UI Emoji",
                Font.PLAIN,
                18
        ));

        // Перебираем все строки поля
        for (int y = 0; y < environment.getHeight(); y++) {

            // Перебираем все столбцы поля
            for (int x = 0; x < environment.getWidth(); x++) {

                // Получаем агента из текущей клетки
                Agent agent =
                        environment.getAgentAt(x, y);

                // Координаты клетки
                int cellX = x * CELL_SIZE;
                int cellY = y * CELL_SIZE;

                // Определяем символ
                String symbol;

                if (agent == null) {

                    // Пустая клетка
                    symbol = "·";

                } else if (agent instanceof Plant) {

                    // Растение
                    symbol = "🌱";

                } else if (agent instanceof Herbivore) {

                    // Травоядное
                    symbol = "🐇";

                } else if (agent instanceof Predator) {

                    // Хищник
                    symbol = "🐺";

                } else {

                    // Неизвестный тип агента
                    symbol = "?";
                }

                // Рисуем символ
                g.drawString(
                        symbol,
                        cellX + 6,
                        cellY + 22
                );
            }
        }

        // Рисуем сетку
        drawGrid(g);
    }

    // Рисует сетку поля
    private void drawGrid(Graphics g) {

        // Вертикальные линии
        for (int x = 0;
             x <= environment.getWidth();
             x++) {

            int lineX = x * CELL_SIZE;

            g.drawLine(
                    lineX,
                    0,
                    lineX,
                    environment.getHeight()
                            * CELL_SIZE
            );
        }

        // Горизонтальные линии
        for (int y = 0;
             y <= environment.getHeight();
             y++) {

            int lineY = y * CELL_SIZE;

            g.drawLine(
                    0,
                    lineY,
                    environment.getWidth()
                            * CELL_SIZE,
                    lineY
            );
        }
    }
}