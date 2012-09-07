package ru.spbstu.telematics.vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Vector<T> implements IVector<T>, Iterable<T> {

		private int current_size = 0; 	// количество элементов множества
		private T[] mass; 				// массив для работы с множеством
		private int maxSizeVector = 0;	// максимальный размер вектора
		int n; 		
		
		public Vector (int size, int maxSizeVector){
			mass = (T[]) new Object[size];
			this.maxSizeVector = maxSizeVector;
		}
		
		// Добавляет объект в коллекцию 
		public void add (T obj){
			// добавляем элемент на следующую позицию
			mass[current_size] = obj;
			current_size++;
			System.out.println("Add element '" + obj + "' in vector");
			checkSizeCollectionsFull();
		}
		
		// Добавляет объект в коллекцию на указанную позицию
		public void add (T obj, int index){
			if (index > (current_size + 1))
				index = current_size + 1;
			int i = current_size;
			while (i > index){
				mass[i] = mass[i - 1];
				i--;
			}
			mass[index] = obj;
			current_size++;
			System.out.println("Add element '" + obj + "' in vector with index " + index);
			checkSizeCollectionsFull();
		}
		
		
		// Удаляет объект из коллекции, находящийся на указанной позиции
		public boolean remove (int index){
			System.out.println("Delete elemet vector with index: " + index);
			if (index < 0 || index > current_size || current_size == 0) 
				return false;
			int i = current_size;
			while (i > index){
				mass[i] = mass[i - 1];
				i--;
			}
			current_size--;
			checkSizeCollectionsEmpty();
			return true;
		}
		
		// Вывод вектора на экран
		public void print(){
			if (current_size < 0)
				System.out.println("\nVector empty!");																	
			else {
				System.out.println("\nVector:");
				for (int i = 0;  i < current_size; i++)
					System.out.println("mass[" + i + "] = " + mass[i] + " ");
			}
			System.out.println("\nThe number of vector: " + current_size + "(" + mass.length + ")\n");
		} 
		
		// Возвращает объект, находящийся на определенной позиции
		public T get(int index) {
			System.out.print("\nGets the element at position " + index + "\n");
			if((index > current_size) || (index <= 0)){
				throw new IndexOutOfBoundsException();
			}
			return mass[(index-1)];
		}

		
		//  Возвращает индекс объекта, если такой есть в векторе. Если такого нет, то -1.
		public int indexOf(T obj) {
			System.out.print("\nSearch index element vector " + obj.toString() + "\n");
			for(int i = 0; i < current_size; i++){
				if(obj == mass[i])
					return (i + 1);
			}
			System.out.print("No such element in vector ");
			return -1;
		}
		
		
		// Методы работы с саморасширяющейся коллекцией
		// При заполнении коллекции - увеличиваем вдвое, при опустении на 3/4 - уменьшаем вдвое
		// Чтобы коллекция не былп бесконечной - устанавливаем ограничение по размеру коллекции
		// при его превышении - создаем новый массив
		public void checkSizeCollectionsFull(){
			if(mass.length == current_size) {
				System.out.print("\nVector is full! The size will be increased");
				@SuppressWarnings("unchecked")
				T[] newMass = (T[]) new Object[mass.length*2];
				for(int i = 0; i < current_size; i++)
					newMass[i] = mass[i];
				mass = newMass;
				System.out.println("\nVector size: " + newMass.length);
			}
			else if (mass.length >= maxSizeVector){
				System.out.print("Maximum size of the collection! Create a new array");
				T[] newMass = (T[]) new Object[mass.length];
				mass = newMass;
			}
			
		}
		
		public void checkSizeCollectionsEmpty() {
			if (current_size <= (mass.length/4)){
				System.out.print("\nVektor almost empty. The vector size is reduced\n");
				T[] newMass = (T[]) new Object[(mass.length/2)];
				for(int i = 0; i < current_size; i++)
					newMass[i] = mass[i];
				mass = newMass;
				System.out.println("\nVector size: " + newMass.length);
			}
		}
		
		// Интерфейс итератор
		private class Iter implements Iterator<T> {
			// Метод hasNext позволяет определить, остались ли еще не обработанные элементы в коллекции
			@Override
			public boolean hasNext() {
				if (n < current_size)
					return true;
				else 
					return false;
			}

			// Метод next() возвращает очередной элемент коллекции
			@Override
			public T next() {
				if (n == current_size)
					throw new NoSuchElementException();
				else {
					T result = mass[n];
					n++;
					return result;
				}	
			}
		
			// Метод remove удаляет элемент, ранее возвращенный next()
			@Override
			public void remove() {
				Vector.this.remove(n);
			}	
		}
		
		public Iterator<T> iterator() {
				return new Iter();
		}



}
