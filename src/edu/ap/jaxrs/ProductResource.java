package edu.ap.jaxrs;

import java.io.*;

import java.util.*;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.xml.bind.*;

@RequestScoped
@Path("/products")
public class ProductResource {
	
	@GET
	@Produces({"text/html"})
	public String getProductsHTML() {
		String htmlString = "<html><body>";
		try {
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductJSON.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONfile = new File("/Users/Joris/workspace/JAX-RS(test)/Product.json");
			ProductJSON productJSON = (ProductJSON)jaxbUnmarshaller.unmarshal(JSONfile);
			ArrayList<Product> listOfProducts = productJSON.getProducts();
			
			for(Product product : listOfProducts) {
				htmlString += "Id : " + product.getId() + "<br>";
				htmlString += "Price : " + product.getPrice() + "<br>";
				htmlString += "<b>name : " + product.getName() + "</b><br>";
				htmlString += "Brand : " + product.getBrand() + "<br>";
				htmlString += "Description : " + product.getDescription() + "<br>";
				
				htmlString += "<br><br>";
			}
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return htmlString;
	}
	
	@GET
	@Produces({"text/xml"})
	public String getProductsXML() {
		String xmlString = "<products>";
		try {
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductJSON.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONfile = new File("/Users/Joris/workspace/JAX-RS(test)/Product.json");
			ProductJSON productJSON = (ProductJSON)jaxbUnmarshaller.unmarshal(JSONfile);
			ArrayList<Product> listOfProducts = productJSON.getProducts();
			
			for(Product product : listOfProducts) {
				xmlString += "<id> : " + product.getId() + "</id>";
				xmlString += "<price> : " + product.getPrice() + "</price>";
				xmlString += "<name> : " + product.getName() + "</name>";
				xmlString += "<brand> : " + product.getBrand() + "</brand>";
				xmlString += "<description> : " + product.getDescription() + "</description>";
				
			}
			xmlString = xmlString.substring(0, xmlString.length()-1);
			xmlString += "</products>";
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return xmlString;
	}
	
	@GET
	@Produces({"application/json"})
	public String getProductJSON() {
		String content = "";
		File JSONfile = new File("/Users/Joris/workspace/JAX-RS(test)/Product.json");
		try {
			content = new Scanner(JSONfile).useDelimiter("\\Z").next();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return content;
	}

	@GET
	@Path("/{name}")
	@Produces({"application/json"})
	public String getProductJSON(@PathParam("name") String name) {
		String jsonString = "";
		try {
			// get all products
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductJSON.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONfile = new File("/Users/Joris/workspace/JAX-RS(test)/Product.json");
			ProductJSON productJSON = (ProductJSON)jaxbUnmarshaller.unmarshal(JSONfile);
			ArrayList<Product> listOfProducts = productJSON.getProducts();
			
			// look for the product, using the name
			for(Product product : listOfProducts) {
				if(name.equalsIgnoreCase(product.getName())) {
					jsonString += "\"id\" : " + product.getId() + ",";
					jsonString += "\"price\" : " + product.getPrice() + "},";
					jsonString += "{\"name\" : \"" + product.getName() + "\",";
					jsonString += "\"brand\" : \"" + product.getBrand() + "\",";
					jsonString += "\"description\" : \"" + product.getDescription() + "\",";
				}
			}
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return jsonString;
	}
	
	@GET
	@Path("/{name}")
	@Produces({"application/json"})
	public String getProductJSON(@PathParam("name") String name) {
		String jsonString = "";
		try {
			// get all products
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductJSON.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONfile = new File("/Users/Joris/workspace/JAX-RS(test)/Product.json");
			ProductJSON productJSON = (ProductJSON)jaxbUnmarshaller.unmarshal(JSONfile);
			ArrayList<Product> listOfProducts = productJSON.getProducts();
			
			// look for the product, using the name
			for(Product product : listOfProducts) {
				if(name.equalsIgnoreCase(product.getName())) {
					JAXBContext jaxbContext2 = JAXBContext.newInstance(Product.class);
					Marshaller jaxbMarshaller = jaxbContext2.createMarshaller();
					StringWriter sw = new StringWriter();
					jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
					jaxbMarshaller.marshal(product, sw);
					jsonString = sw.toString();
				}
			}
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return jsonString;
	}
	
	@POST
	@Consumes({"application/json"})
	public void processFromJSON(String productJSON) {
		
		/* newProductJSON should look like this :
		 *  
		 {"id": "BEE-15",
  "price": 36.99,
  "name": "Beefeater 1.5L",
  "brand": "Beefeater",
  "description": "De Beefeater is een absolute klassieker en 
  een van de meest herkenbare en typische gins op de markt. 
  Het is een London Dry Gin die heel geliefd is bij de liefhebbers van een lekkere Martini. 
  De fles en zijn smaak zijn zeer herkenbaar en beide zijn door de jaren heen ongewijzigd gebleven."
		 */
		
		try {
			// get all products
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductJSON.class);
			Unmarshaller jaxbUnmarshaller1 = jaxbContext1.createUnmarshaller();
			File JSONfile = new File("/Users/Joris/workspace/JAX-RS(test)/Product.json");
			ProductJSON productJSON = (ProductJSON)jaxbUnmarshaller1.unmarshal(JSONfile);
			ArrayList<Product> listOfProducts = productJSON.getProducts();
			
			// unmarshal new product
			JAXBContext jaxbContext2 = JAXBContext.newInstance(Product.class);
			Unmarshaller jaxbUnmarshaller2 = jaxbContext2.createUnmarshaller();
			StringReader reader = new StringReader(productJSON);
			Product newProduct = (Product)jaxbUnmarshaller2.unmarshal(reader);
			
			// add product to existing product list 
			// and update list of products in  productJSON
			listOfProducts.add(newProduct);
			productJSON.setProducts(listOfProducts);
			
			// marshal the updated productJSON object
			Marshaller jaxbMarshaller = jaxbContext1.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(productJSON, JSONfile);
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
	}
}