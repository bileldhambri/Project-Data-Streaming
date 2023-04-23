# Data Streaming Application using Apache Flink
This is an example  HTTP streaming application written using the Apache Flink streaming framework. The code reads stock data from the RapidAPI service provider for the company Google (GOOGL) and prints it to the console
## Prerequisites
- Java 8 or higher
- Apache Flink 1.13.0 or higher
- Scala 2.12 or higher
## Configuration
The HttpSourceFunction class takes a URL as a parameter to specify the RapidAPI endpoint to retrieve the stock data from. This can be changed to any other valid URL that provides the desired data.
The Thread.sleep method inside the run method can also be adjusted to control the frequency at which the HTTP requests are sent to the RapidAPI endpoint.
Logic of the application 
This application uses the Apache Flink streaming framework to retrieve stock data from the RapidAPI service provider through an HTTP endpoint. The HttpSourceFunction class sends an HTTP GET request to the endpoint and retrieves the data as a string, which is then emitted to the Flink data stream using the ctx.collect(line) method. The run method runs continuously until the cancel method is called, sleeping for a specified time interval between requests using Thread.sleep. Finally, the main method sets up the Flink execution environment, adds the HTTP data source, and prints the data to the console using the print method.
## Data visualisation
The captured stock data can be difficult to interpret when presented in its raw format. To better understand the data, we used the Matplotlib library to visualize the data in the form of graphs .

Here's a screenshot representing the visualization of stock prices over time using Matplotlib:

Stock Prices Over Time

As you can see, the visualization provides a clear picture of the stock prices over time, this way allows for identifying trends and patterns that not immediately apparent in the raw data which can be useful for making informed investment decisions.

 ![App Screenshot](https://github.com/bileldhambri/Project-Data-Streaming/blob/main/Screenshot/capture1.png?raw=true)
 
## Acknowledgments
This project was inspired by the Apache Flink documentation and the RapidAPI service provider elaborated by Mohamed el Ilmi , Bilel Dhambri , Amen Allah Guetif, Mohamed Achref Langliz and Feriel Garouachi.
