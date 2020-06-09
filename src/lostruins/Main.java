package lostruins;

import java.util.Map;

public class Main {

	public static void main(String[] args) {
		
		Map<Integer, City> citiesList = XmlUtility.extractMapFromXml("PgAr_Map_5.xml");

		for (int i = 0; i<citiesList.size(); i++) {
			System.out.print(citiesList.get(i).getIndex() + ": " + citiesList.get(i).getName() + " ---> ");
			System.out.println(String.format("(%d, %d, %d)", citiesList.get(i).getX(), citiesList.get(i).getY(), citiesList.get(i).getH()));
			for(int j = 0; j<citiesList.get(i).getLinksList().size(); j++)
				System.out.println(String.format("   %d#: %s", citiesList.get(i).getLinksList().get(j).getIndex(), citiesList.get(i).getLinksList().get(j).getName()));
		}
		
		Route.printMatrix(Route.getDistanceMatrix(citiesList, false));
		
//		XmlUtility.createRouteXmlFile(new ArrayList<City>(), new ArrayList<City>(), "Routes.xml");
		
		System.out.printf("%nProgram closed successfully!");
	}

}
