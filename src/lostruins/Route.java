package lostruins;

import java.util.HashMap;
import java.util.Map;

public class Route {

	
	public static Double[][] getDistanceMatrix(Map<Integer, City> map, boolean team){
		Double[][] matrix = new Double[map.size()][2];
		
		for (int i = 0; i<map.size(); i++)
			for (int j = 0; j<2; j++)
				matrix[i][j] = 0.0;
		
		int pointer = 0;
		double distance = 0.0;
		Map<Integer, Boolean> checked = new HashMap<Integer, Boolean>();
		for(int i = 0; i<map.size(); i++)
			checked.put(i, false);
		
		do {
			for(int i = 0; i<map.get(pointer).getLinksList().size(); i++) {
				if (checked.get(map.get(pointer).getLinksList().get(i).getIndex()) == false) {
					if (pointer == 0) {
						distance = distance(map.get(pointer), map.get(pointer).getLinksList().get(i), team);
						matrix[map.get(pointer).getLinksList().get(i).getIndex()][0] = distance;
						matrix[map.get(pointer).getLinksList().get(i).getIndex()][1] = (double)pointer;
					}
					else {
						distance = distance(map.get(pointer), map.get(pointer).getLinksList().get(i), team);
						distance += matrix[pointer][0];
						
						if (matrix[map.get(pointer).getLinksList().get(i).getIndex()][0] == 0.0 || matrix[map.get(pointer).getLinksList().get(i).getIndex()][0]>distance) {
							matrix[map.get(pointer).getLinksList().get(i).getIndex()][0] = distance;
							matrix[map.get(pointer).getLinksList().get(i).getIndex()][1] = (double)pointer;
						}
					}
				}
			}
			checked.put(pointer, true);
			pointer = getColumnMin(matrix, checked);
		}while(checkAllCheckedMap(checked) == false);
		
		return matrix;
	}
	
	public static void printMatrix(Double[][] matrix) {
		for (int i = 0; i<matrix.length; i++) {
			System.out.print(i + "# |");
			for (int j = 0; j<matrix[i].length; j++) {
				System.out.print(matrix[i][j]+ " | ");
			}
			System.out.printf("%n");
		}
	}
	
	public static int getColumnMin(Double[][] matrix, Map<Integer, Boolean> checked) {
		 
		double min = -1.0;
		int minRow = 0;
		for (int i = 0; i<matrix.length; i++) {
			if (checked.get(i) == false)
				if (min == -1.0 || matrix[i][0] < min) {
					min = matrix[i][0];
					minRow = i;
				}				
		}		
		
		return minRow;
	}
	
	public static boolean checkAllCheckedMap(Map<Integer, Boolean> checked) {
		for(int i = 0; i<checked.size(); i++)
			if (checked.get(i) == false)
				return false;
		return true;
	}
	
	/**
	 * calcola la distanza tra le città per entrambe le squadra
	 * @param a la prima città
	 * @param b la seconda città
	 * @param team per indicare la squadra per la quale vogliamo calcolare la distanza
	 * true: team Tonatiuh 
	 * false: team Metztli
	 * @return un valore di tipo double con la distanza
	 */
	public static double distance(City a, City b, boolean team) {
		if (team)
			return Math.sqrt( Math.pow(b.getX()-a.getX(),2) + Math.pow(b.getY()-a.getY(),2));
		else	
			return Math.abs(a.getH()-b.getH());
	}
}
