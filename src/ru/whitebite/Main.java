package ru.whitebite;

import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Main {

    static NodeList nList;

    private final static String findStr = "Гонконгских долларов";
    private final static String urlStr = "http://www.cbr.ru/scripts/XML_daily.asp";

    public static void main(String[] args) {
        menu(args);
    }

    private static void menu(String[] args) {
        if (args.length == 0) {
            initNodeList(urlStr);
            findValute();
            return;
        }
        switch (args[0]) {
            case "read":
                readXML();
                break;
            case "help":
                help();
                break;
            default:
                initNodeList(urlStr);
                findValute();
        }
    }

    private static void initNodeList(String strUrl) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new URL(strUrl).openStream());
            doc.getDocumentElement().normalize();

            nList = doc.getElementsByTagName("Valute");
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static void readXML() {
        initNodeList(Main.urlStr);
        printDoc();
    }

    private static void findValute() {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (eElement.getElementsByTagName("Name").item(0).getTextContent().equals(Main.findStr)) {
                    printElem(eElement);

                    System.out.println("----------------------------");
                    System.out.println("       VALUE : " + eElement.getElementsByTagName("Value").item(0).getTextContent());
                    System.out.println("----------------------------");
                }
            }
        }
    }

    private static void printDoc() {
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                printElem(eElement);
            }
        }
    }

    private static void printElem(Element eElement) {
        System.out.println("Valute  id : " + eElement.getAttribute("ID"));
        System.out.println("NumCode : " + eElement.getElementsByTagName("NumCode").item(0).getTextContent());
        System.out.println("CharCode : " + eElement.getElementsByTagName("CharCode").item(0).getTextContent());
        System.out.println("Nominal : " + eElement.getElementsByTagName("Nominal").item(0).getTextContent());
        System.out.println("Name : " + eElement.getElementsByTagName("Name").item(0).getTextContent());
        System.out.println("Value : " + eElement.getElementsByTagName("Value").item(0).getTextContent());
    }

    private static void help() {
        System.out.println("=|=================HELP============================|=");
        System.out.println(" |  use \"help\" for help                          |");
        System.out.println(" |  use any or empty value for find                |");
        System.out.println(" |  exchange rate of Hong Kong dollars in rubles   |");
        System.out.println(" |  use \"read\" for read CBR doc  from            |");
        System.out.println(" |  http://www.cbr.ru/scripts/XML_daily.asp        |");
        System.out.println("=|=================================================|=");
    }

}