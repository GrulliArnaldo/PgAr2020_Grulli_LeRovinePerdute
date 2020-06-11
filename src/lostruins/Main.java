package lostruins;

import java.util.Map;

public class Main {

	public static void main(String[] args) {
		
		Map<Integer, City> citiesList = XmlUtility.extractMapFromXml("PgAr_Map_50.xml");
		
//		Map<Integer, City> citiesListB = XmlUtility.extractMapFromXml("PgAr_Map_200.xml");
		
//		for(int i = 0; i<citiesListB.size(); i++) {
//			System.out.println(String.format("%d# %s(%d,%d,%d)", i, citiesListB.get(i).getName(), citiesListB.get(i).getX(), citiesListB.get(i).getY(), citiesListB.get(i).getH()));
//			for (int j = 0; j < citiesListB.get(i).getLinksList().size(); j++)
//				System.out.println(String.format("   %d ---> %s", citiesListB.get(i).getLinksList().get(j).getIndex(), citiesListB.get(i).getLinksList().get(j).getName()));
//		}
		
		XmlUtility.createRouteXmlFile(citiesList, Route.getDistanceMatrix(citiesList, true), Route.getDistanceMatrix(citiesList, false), "Routes.xml");
		
		System.out.println("File Routes.xml was created successfully!");
	}

}
