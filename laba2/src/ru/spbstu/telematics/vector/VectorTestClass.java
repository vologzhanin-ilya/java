package ru.spbstu.telematics.vector;

import java.util.Scanner;


public class VectorTestClass {
	public static void main(String[] args) throws Exception {
//		Scanner in = new Scanner(System.in);
//        System.out.print("Enter the size of the vector\n\t vectorLength = ");
//      	int vectorLength = in.nextInt();
      	
		int vectorLength = 10;
		Vector<Integer> vector = new Vector<Integer>(vectorLength, vectorLength*10);
		vector.add(3);
		vector.add(666, 0);
		vector.add(111, 1);
		vector.print();
		vector.remove(1);
		vector.print();
		System.out.println(vector.get(1));
		System.out.println(vector.indexOf(111));
		vector.add(4);
		vector.add(5);
		vector.print();
		vector.add(6);
	}
}
