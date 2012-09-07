package ru.spbstu.telematics.vector;

/**
 * Саморасширяющаяся коллекция. Объекты хранятся в массиве в порядке добавления.
 */
public interface IVector<T> {

    /**
     * Добавляет объект в коллекцию
     * @param o
     */
    void add(T obj);
    /**
     * Добавляет объект в коллекцию на указанную позицию
     * @param o
     * @param pos
     */
    void add(T obj, int pos);
    /**
     * Удаляет объект из коллекции, находящийся на указанной позиции
     * @param index
     */
    boolean remove(int index);
    /**
     * Возвращает объект, находящийся на определенной позиции
     * @param index
     * @return
     * @throws Exception 
     */
    T get(int index);
    /**
     * Возвращает индекс объекта, если такой есть в векторе. Если такого нет, то -1.
     * @param o
     * @return
     */
    int indexOf(T obj);
}