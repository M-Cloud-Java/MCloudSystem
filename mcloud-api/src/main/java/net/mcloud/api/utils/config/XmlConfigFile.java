package net.mcloud.api.utils.config;

import net.mcloud.api.MCloudAPI;
import net.mcloud.api.utils.config.types.CloudConfig;
import net.mcloud.api.utils.config.types.Server;
import net.mcloud.api.utils.logger.ConsoleColor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class XmlConfigFile {
    private String path;
    private String fileName;
    private File file;

    public XmlConfigFile(String path, String fileName){
        this.path = path;
        this.fileName = fileName;
        this.file = new File(this.path + "/" + this.fileName);
    }

    public Record loadFile(ConfigType configType) {
        switch (configType) {
            case SERVER -> {
                try {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(file);
                    document.getDocumentElement().normalize();

                    if(!document.getDocumentElement().getNodeName().equals("mcloud"))
                        return null;

                    NodeList list = document.getElementsByTagName("server");
                    Node node = list.item(0);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        String serverName = element.getElementsByTagName("name").item(0).getTextContent();
                        String serverVersion = element.getElementsByTagName("version").item(0).getTextContent().toUpperCase();
                        int minMemory = Integer.parseInt(element.getElementsByTagName("minMemory").item(0).getTextContent());
                        int maxMemory = Integer.parseInt(element.getElementsByTagName("maxMemory").item(0).getTextContent());
                        Server server = new Server(serverName, serverVersion, minMemory, maxMemory);
                        return server;
                    }

                } catch (ParserConfigurationException | IOException | SAXException e) {
                    throw new RuntimeException(e);
                }
            }
            case CLOUD_CONFIG -> {
                try {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(file);
                    document.getDocumentElement().normalize();

                    if(!document.getDocumentElement().getNodeName().equals("mcloud"))
                        return null;

                    NodeList list = document.getElementsByTagName("config");
                    MCloudAPI.getApi().getLogger().info("[DEBUG] NodeList length: " + list.item(0).getNodeName(), ConsoleColor.BLUE);
                    Node node = list.item(0);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        int tcpPort = Integer.parseInt(element.getElementsByTagName("tcpPort").item(0).getTextContent());
                        int udpPort = Integer.parseInt(element.getElementsByTagName("udpPort").item(0).getTextContent());
                        boolean deprecatedEvent = Boolean.parseBoolean(element.getElementsByTagName("deprecatedEvents").item(0).getTextContent());
                        CloudConfig config = new CloudConfig(tcpPort, udpPort, deprecatedEvent);
                        return config;
                    }

                } catch (ParserConfigurationException | IOException | SAXException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    public void updateFile(Record record) {
        if(record instanceof CloudConfig cloudConfig) {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(file);
                document.getDocumentElement().normalize();

                if (!document.getDocumentElement().getNodeName().equals("mcloud"))
                    return;

                NodeList list = document.getElementsByTagName("server");
                Node node = list.item(0);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    element.getElementsByTagName("tcpPort").item(0).setTextContent(String.valueOf(cloudConfig.tcpPort()));
                    element.getElementsByTagName("udpPort").item(0).setTextContent(String.valueOf(cloudConfig.udpPort()));
                    element.getElementsByTagName("deprecatedEvents").item(0).setTextContent(String.valueOf(cloudConfig.deprecatedEvents()));
                }

            } catch (ParserConfigurationException | IOException | SAXException e) {
                throw new RuntimeException(e);
            }
        }
        if(record instanceof Server server) {
                try {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(file);
                    document.getDocumentElement().normalize();

                    if(!document.getDocumentElement().getNodeName().equals("mcloud"))
                        return;

                    NodeList list = document.getElementsByTagName("server");
                    Node node = list.item(0);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        element.getElementsByTagName("name").item(0).setTextContent(server.serverName());
                        element.getElementsByTagName("version").item(0).setTextContent(server.serverVersion().toUpperCase());
                        element.getElementsByTagName("minMemory").item(0).setTextContent(String.valueOf(server.minMemory()));
                        element.getElementsByTagName("maxMemory").item(0).setTextContent(String.valueOf(server.maxMemory()));
                    }

                } catch (ParserConfigurationException | IOException | SAXException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    public Record createFile(Record record) {

        if(record instanceof Server server) {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document newDocument = documentBuilder.newDocument();
                Element rootElement = newDocument.createElement("mcloud");
                newDocument.appendChild(rootElement);

                Element serverElement = newDocument.createElement("server");
                rootElement.appendChild(serverElement);

                Element serverName = newDocument.createElement("name");
                serverName.setTextContent(server.serverName());
                serverElement.appendChild(serverName);

                Element serverVersion = newDocument.createElement("version");
                serverVersion.setTextContent(server.serverVersion());
                serverElement.appendChild(serverVersion);

                Element minMemory = newDocument.createElement("minMemory");
                minMemory.setTextContent(String.valueOf(server.minMemory()));
                serverElement.appendChild(minMemory);

                Element maxMemory = newDocument.createElement("maxMemory");
                maxMemory.setTextContent(String.valueOf(server.maxMemory()));
                serverElement.appendChild(maxMemory);

                try(FileOutputStream output = new FileOutputStream(file.getPath())) {
                    writeXml(newDocument, output);
                    return server;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        } else if(record instanceof CloudConfig config) {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document newDocument = documentBuilder.newDocument();
                Element rootElement = newDocument.createElement("mcloud");
                newDocument.appendChild(rootElement);

                Element configElement = newDocument.createElement("config");
                rootElement.appendChild(configElement);

                Element tcpPortElement = newDocument.createElement("tcpPort");
                tcpPortElement.setTextContent(String.valueOf(config.tcpPort()));
                configElement.appendChild(tcpPortElement);

                Element udpPortElement = newDocument.createElement("udpPort");
                udpPortElement.setTextContent(String.valueOf(config.udpPort()));
                configElement.appendChild(udpPortElement);

                Element deprecatedEventElement = newDocument.createElement("deprecatedEvents");
                deprecatedEventElement.setTextContent(String.valueOf(config.deprecatedEvents()));
                configElement.appendChild(deprecatedEventElement);

                try(FileOutputStream output = new FileOutputStream(file.getPath())) {
                    writeXml(newDocument, output);
                    return config;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void writeXml(Document document, OutputStream outputStream) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(outputStream);

            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public boolean exists() {
        return file.exists();
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }
}
