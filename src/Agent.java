public abstract class Agent {

    protected int x;    //protected означает переменная доступна самому классу Agent и классам-наследникам
    protected int y;
    protected int energy;

    public Agent(int x, int y, int energy) { //Конструктора класса, вызывается, когда создаётся объект класса-наследника
        this.x = x;    //this.x — переменная конкретного объекта, x — параметр конструктора
        this.y = y;
        this.energy = energy;
    }

    public abstract void act(Environment environment); //Абстрактный метод act(). Означает «совершить действие за один ход»
    //Каждый конкретный агент должен иметь метод act. Например, plant проверяет возможность размножения
    //Методу передаётся объект Environment. То есть агенту нужно взаимодейстоввать с окружающец средой, спросить, есть ли рядом растение, травоядное

    public boolean isAlive() {   //Жив ли агент?  boolean-результат может быть true и false
        return energy > 0;
    }

    public int getX() {        //Метод возвращает координату x
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEnergy() {
        return energy;
    }
}