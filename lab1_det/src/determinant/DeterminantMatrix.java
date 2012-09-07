package determinant;

import java.util.Scanner;

public class DeterminantMatrix {
	public int determinantMatrix(int [][] massive){
		int determinant = 0;
	    if (massive.length == 2){
	    	determinant = massive[0][0] * massive[1][1] - massive[1][0] * massive[0][1];
	    	}
	        else {
	            int k = 1;
	            for (int i = 0; i < massive.length; i++) {
	                if (i%2 == 1){ k = -1; }
	                else { k = 1; }
	                determinant += k * massive[0][i] * this.determinantMatrix(this.getMinor(massive,0,i)); 
	            }
	        }
	        return determinant;
	    }
	
	private int[][] getMinor(int[][] massive, int row, int column){
		int minorLength = massive.length - 1;
		int [][] minor = new int [minorLength][minorLength];
	    int dI = 0, dJ = 0;
	    for (int i = 0; i <= minorLength; i++){
	         dJ = 0;
	         for (int j = 0; j <= minorLength; j++){
	             if (i == row) { dI = 1; }
	             else {
	                 if (j == column) { dJ = 1;}
	                 else {
	                    minor[i-dI][j-dJ] = massive[i][j];
	                 }
	             }
	         }
	     }
	    return minor;
	}
	
	private int[][] enterMassive () {
	   	int dimension = 0;
	   	Scanner in = new Scanner(System.in);
	    System.out.print("Enter the dimension of the square matrix to calculate the determinant: \n\t dimension = ");
	    dimension = in.nextInt();

		int elem;
		int[][] massive = new int [dimension][dimension];
        System.out.print("\nEnter the array elements:\n");
        for(int i = 0; i < dimension; i++) {
           	for(int j = 0; j < dimension; j++) {
           		System.out.print("massive [" + i + "][" + j + "] = ");
           		elem = in.nextInt();
           		massive [i][j] = elem;
          		}
           	}
           
        System.out.println("\nInput matrix:");
        for(int i = 0; i < dimension; i++) {
          	System.out.print("\t | ");
           	for(int j = 0; j < dimension; j++) {
          		System.out.print(massive[i][j] + " ");
          		}
           	System.out.print("|\n");
        }
        return massive;
	}
	    
	public static void main(String[] args) {
	    DeterminantMatrix m = new DeterminantMatrix();
	    int result = m.determinantMatrix(m.enterMassive());
	    System.out.println("\nResult: \n\tDet = " + result);
	    }
	}

