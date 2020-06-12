package lostruins;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Route {

	/**
	 * calcola una matrice con le distanze tra le città di una mappa
	 * @param map la mappa contenente le città come oggetti e con ognuna di esse lincate ai propri collegamenti
	 * @param team true se si vuole calcolare la matrice delle distanze per il team Tonatiuh, false per quello Metzli
	 * @return una matrice con: 
	 * righe = numero totale delle città nella mappa
	 * colonne = 2 
	 */
	public static double[][] getDistanceMatrix(Map<Integer, City> map, boolean team){
		//inizializzo la matrice ci metto in tutte le caselle lo 0.0
		double[][] matrix = new double[map.size()][2];	
		for (int i = 0; i<map.size(); i++)
			for (int j = 0; j<2; j++)
				matrix[i][j] = 0.0;
		//inizializzo il pointer a 0 (città di partenza) e la distanza a 0.0
		int pointer = 0;
		double distance = 0.0;
		//count serve per far finire il ciclo anche nel caso ci fossero città a cui non si può arrivare dalla città iniziale
		int count = 0;
		//creo una mappa per tener conto delle città già "lavorate"
		TreeMap<Integer, Boolean> checked = new TreeMap<Integer, Boolean>();
		for(int i = 0; i<map.size(); i++)
			checked.put(i, false);
		
		//ciclo che cicla tutte le città e i rispettivi "link" e ne calcola le distanze
		do {
			for(int i = 0; i<map.get(pointer).getLinksList().size(); i++) {
				if (checked.get(map.get(pointer).getLinksList().get(i).getIndex()) == false) {
					distance = distance(map.get(pointer), map.get(pointer).getLinksList().get(i), team);
					if (pointer == 0) {
						matrix[map.get(pointer).getLinksList().get(i).getIndex()][0] = distance;
						matrix[map.get(pointer).getLinksList().get(i).getIndex()][1] = (double)pointer;
					}
					else {
						distance += matrix[pointer][0];	
						if (matrix[map.get(pointer).getLinksList().get(i).getIndex()][0] == distance) {
							matrix[map.get(pointer).getLinksList().get(i).getIndex()][1] = getOptimalRoute(matrix, pointer, (int)matrix[map.get(pointer).getLinksList().get(i).getIndex()][1]);
						}
						else if (matrix[map.get(pointer).getLinksList().get(i).getIndex()][0] == 0.0 || matrix[map.get(pointer).getLinksList().get(i).getIndex()][0]>distance) {
							matrix[map.get(pointer).getLinksList().get(i).getIndex()][0] = distance;
							matrix[map.get(pointer).getLinksList().get(i).getIndex()][1] = (double)pointer;
						}
					}
				}
			}
			count++;
			//imposto il valore della città appena "lavorata" a true
			checked.put(pointer, true);
			//imposto il pointer alla città con la distanza minore ancora non lavorata
			pointer = getColumnMin(matrix, checked);
			//si può uscire dal ciclo solo se sono state scartate tutte le città
		}while(checkAllCheckedMap(checked) == false && count<map.size());
		
		return matrix;
	}
	
	/**
	 * mette a confronto 2 percorsi in una una matrice e ne calcola l'ottimale
	 * prima vede quale percorso attraverso meno città
	 * nel caso il numero fosse uguale fa un'ulteriore confronto per capire quale percorso attraversa la città con l'id più alto
	 * @param matrix la matrice di riferimento
	 * @param pointer primo percorso
	 * @param indexCity secondo percorso
	 * @return si ha un valore double con l'indice del percorso ottimale
	 */
	public static double getOptimalRoute(double[][] matrix, int pointer, int indexCity) {
		int[] pointerArray = Arrays.copyOf(getCitiesRouteNumber(matrix, pointer), 2);
		int[] indexArray = Arrays.copyOf(getCitiesRouteNumber(matrix, indexCity), 2);
		
		if (pointerArray[0] < indexArray[0])
			return (double)pointer;
		else if (pointerArray[0] > indexArray[0])
			return (double)indexCity;
		
		if (indexArray[1] > pointerArray[1])
			return (double)indexCity;
		else
			return (double)pointer;
	}
	
	/**
	 * metodo che calcola il numero di città attraversate e l'id più alto tra le città attraversate
	 * @param matrix la matrice di riferimento
	 * @param index da controllare
	 * @return un array di 2 caselle:
	 * 1. il numero delle città
	 * 2. l'id più alto
	 */
	public static int[] getCitiesRouteNumber(double[][] matrix, int index) {
		
		int prec = (int)matrix[index][1];
		int[] result = {0, prec};
		if (index == 0)
			result[0]--;
			
		while(prec != 0) {
			prec = (int)matrix[prec][1];
			result[0]++;
			if (prec > result[1])
				result[1] = prec;
		}	
		return result;
	}
	
	public static double[] getCitiesRoute(double[][] matrix, int city) {
		double[] route = new double[getCitiesRouteNumber(matrix, city)[0] + 2];
		int prec = (int)matrix[city][1];
		int index = route.length-1;
		
		route[index] = city;
		route[0] = matrix[0][1];
		
		while(prec != 0) {
			route[--index] = prec;
			prec = (int)matrix[prec][1];
		}	
		return route;
	}
	
	/**
	 * calcola il minimo della prima colonna di una matrice
	 * tenendo conto dei valori scartati seconda la mappa 
	 * e ignorando i valori a 0.0 (quindi non ancora impostati)
	 * @param matrix la matrice delle distanze
	 * @param checked la mappa con i valori scartati
	 * @return il valore della riga su cui sta la distanza minima
	 */
	public static int getColumnMin(double[][] matrix, Map<Integer, Boolean> checked) {
		 
		double min = -1.0;
		int minRow = 0;
		for (int i = 0; i<matrix.length; i++) {
			if (checked.get(i) == false)
				if (matrix[i][0] != 0.0)
					if (min == -1.0 || matrix[i][0] < min) {
						min = matrix[i][0];
						minRow = i;
					}				
		}		
		
		return minRow;
	}
	
	/**
	 * metodo che controlla se tutti i valori della mappa sono true
	 * @param checked la mappa delle città scartate rispetto a quelle non
	 * @return true solo se sono tutte true, altrimenti false
	 */
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
