package lostruins;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class XmlUtility {
	
	/**
	 * metodo che estrapola i dati delle città e i rispettivi collegamenti
	 * @param fileName il nome del file xml da dove prendere i dati secondo un formato pre stabilito
	 * @return un ogetto di tipo mappa 
	 */
	public static Map<Integer, City> extractMapFromXml(String fileName){
		
		XMLStreamReader xmlReader = initializeXmlReader(fileName);
		Map<Integer, City> citiesMap = new HashMap<Integer, City>();
		String[] cityAtt = new String[5];
		int count = 0;
		
		//estraggo prima le città le loro proprietà, ma non i link alle altre città
		try {
			while(xmlReader.hasNext()) {
				if(xmlReader.getEventType() == XMLStreamConstants.START_ELEMENT && xmlReader.getLocalName().equalsIgnoreCase("city")) {
					for (int i = 0; i<xmlReader.getAttributeCount(); i++)
						cityAtt[i] = xmlReader.getAttributeValue(i);
					City city = new City(count, cityAtt[1], Integer.valueOf(cityAtt[2]), Integer.valueOf(cityAtt[3]), Integer.valueOf(cityAtt[4]));
					citiesMap.put(Integer.valueOf(cityAtt[0]), city);
					count++;
				}
				xmlReader.next();
			}
		} catch (XMLStreamException e) {
			System.out.println("Errore nella lettura del reader");
			System.out.println(e.getMessage());
		}
		
		xmlReader = initializeXmlReader(fileName);
		
		//ora aggiungo i link
		int index = 0;
		try {
			while(xmlReader.hasNext()) {
				if(xmlReader.getEventType() == XMLStreamConstants.START_ELEMENT && xmlReader.getLocalName().equalsIgnoreCase("city")) {
					xmlReader.next();
					for(;;) {
						if(xmlReader.getEventType() == XMLStreamConstants.END_ELEMENT && xmlReader.getLocalName().equalsIgnoreCase("city"))
							break;
						if (xmlReader.getEventType() == XMLStreamConstants.START_ELEMENT && xmlReader.getLocalName().equalsIgnoreCase("link"))
							citiesMap.get(index).getLinksList().add(citiesMap.get(Integer.valueOf(xmlReader.getAttributeValue(0))));
						xmlReader.next();;	
					}
					index++;
				}
				xmlReader.next();
			}
		} catch (XMLStreamException e) {
			System.out.println("Errore nella lettura del reader");
			System.out.println(e.getMessage());
		}
		
		return citiesMap;
	}
	
	public static void createRouteXmlFile(Map<Integer, City> map, double[][] tonatiuhMatrix, double[][] metztliMatrix, String fileName) {
		
		XMLStreamWriter xmlWriter = initializeXmlWriter(fileName);
		try {
			xmlWriter.writeStartElement("routes"); //apertura tag routes
			
			//team Tonatiuh
			xmlWriter.writeStartElement("route");
			xmlWriter.writeAttribute("team", "Tonatiuh");
			xmlWriter.writeAttribute("cost", Integer.toString((int)tonatiuhMatrix[tonatiuhMatrix.length-1][0]));
			xmlWriter.writeAttribute("cities", Integer.toString(Route.getCitiesRouteNumber(tonatiuhMatrix, tonatiuhMatrix.length-1)[0]));
			double[] routeT = Arrays.copyOf(Route.getCitiesRoute(tonatiuhMatrix, tonatiuhMatrix.length-1), Route.getCitiesRouteNumber(tonatiuhMatrix, tonatiuhMatrix.length-1)[0]+2);
			for(int i = 0; i<routeT.length; i++) {
				xmlWriter.writeStartElement("city");
				xmlWriter.writeAttribute("id", Integer.toString((int)routeT[i]));
				xmlWriter.writeAttribute("name", map.get((int)routeT[i]).getName());
				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();
			
			//team Metztli
			xmlWriter.writeStartElement("route");
			xmlWriter.writeAttribute("team", "Metztli");
			xmlWriter.writeAttribute("cost", Integer.toString((int)metztliMatrix[metztliMatrix.length-1][0]));
			xmlWriter.writeAttribute("cities", Integer.toString(Route.getCitiesRouteNumber(metztliMatrix, metztliMatrix.length-1)[0]));
			double[] routeM = Arrays.copyOf(Route.getCitiesRoute(metztliMatrix, metztliMatrix.length-1), Route.getCitiesRouteNumber(metztliMatrix, metztliMatrix.length-1)[0]+2);
			for(int i = 0; i<routeM.length; i++) {
				xmlWriter.writeStartElement("city");
				xmlWriter.writeAttribute("id", Integer.toString((int)routeM[i]));
				xmlWriter.writeAttribute("name", map.get((int)routeM[i]).getName());
				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement(); 
			
			xmlWriter.writeEndElement(); //chiusura tag routes
			
			xmlWriter.writeEndDocument(); // scrittura della fine del documento
			xmlWriter.flush(); // svuota il buffer e procede alla scrittura
			xmlWriter.close(); // chiusura del documento e delle risorse impiegate
		} catch (XMLStreamException e) {
			System.out.println("Errore nella scrittura del writer");
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * metodo che inizializza un writer
	 * @param filename il nome del file dove dobbiamo scrivere
	 * @return restituisce l'oggetto xmlstreamwriter
	 */
	public static XMLStreamWriter initializeXmlWriter(String filename) {
		XMLOutputFactory xmloutput = null;
		XMLStreamWriter xmlwriter = null;
		try {
			 xmloutput = XMLOutputFactory.newInstance();
			 xmlwriter = xmloutput.createXMLStreamWriter(new FileOutputStream(filename), "utf-8");
			 xmlwriter.writeStartDocument("utf-8", "1.0");
		} catch (Exception e) {
			 System.out.println("Errore nell'inizializzazione del writer:");
			 System.out.println(e.getMessage());
		}
		return xmlwriter;
	}
	
	/**
	 * metodo che inizializza un reader
	 * @param filename il nome del file dove dobbiamo leggere
	 * @return restituisce l'oggetto xmlstreamreader
	 */
	public static XMLStreamReader initializeXmlReader(String fileName) {
		XMLInputFactory xmlinput = null;
		XMLStreamReader xmlreader = null;
		try {
			xmlinput = XMLInputFactory.newInstance();
			xmlreader = xmlinput.createXMLStreamReader(fileName, new FileInputStream(fileName));
		}catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del reader: ");
			System.out.println(e.getMessage());
		}
		return xmlreader;
	}
}
