import java.net.{HttpURLConnection, URL}
import java.io.{BufferedReader, InputStreamReader}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

import java.net.Socket
import java.net.{HttpURLConnection, URL}
import java.io.{BufferedReader, InputStreamReader}
import scala.concurrent.duration._
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import io.github.streamingwithflink.util.{SensorReading, SensorSource, SensorTimeAssigner}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.operators.StreamSink
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import org.python.util.PythonInterpreter
//import jep.python;
//import java.util.Properties

class HttpSourceFunction(url: String) extends RichSourceFunction[String] {

  @volatile var running: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    while (running) {

      val connection = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
      connection.setRequestMethod("GET")
      connection.setRequestProperty("X-RapidAPI-Key", "98e4e74cfdmsha302b915c219b37p18ae8ejsne13cfef3c0d1")
      connection.setRequestProperty("X-RapidAPI-Host", "realstonks.p.rapidapi.com")
      connection.setConnectTimeout(5000000)
      connection.setReadTimeout(5000000)

      val inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()))

      var line: String = inputStream.readLine()
      while (line != null) {
        ctx.collectWithTimestamp(line,System.currentTimeMillis())
        line = inputStream.readLine()
      }

      inputStream.close()
      connection.disconnect()

      // Wait for some time before sending the next request
      Thread.sleep(50.second.toMillis)
    }
  }

  override def cancel(): Unit = {
    running = false
  }
}


import java.lang.ProcessBuilder
import org.apache.flink.streaming.api.scala._
import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter, ByteArrayOutputStream, PrintStream}

object Main {
  def main(args: Array[String]): Unit = {
    val maxOutOfOrderness = Time.seconds(30)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    var httpStream = env.addSource(new HttpSourceFunction("https://realstonks.p.rapidapi.com/GOOGL"))
    httpStream.print()
    var bo =httpStream.isInstanceOf[Serializable]
    println(bo)

    println(s"${httpStream.getClass.getSimpleName} is serializable: ${httpStream.isInstanceOf[Serializable]}")




//    output.name.print()
    //var connectedStream2 :DataStream[String] = output.connect(httpStream3)
  // println(connectedStream2.getClass)
    println("Starting Scala program...")

   // var output2 : DataStream[String]= connectedStreams.process(new ConcatenateNestedStreams())
    //val myPythonObject = new MyPythonClass()
   // myPythonObject.runPythonCode()
    //val python = new PythonInterpreter()

   // python.exec("import sys")
    //output2.print()
    env.execute("HTTP Streaming Example")
    println(bo)
    println(s"${httpStream.getClass.getSimpleName} is serializable: ${httpStream.isInstanceOf[Serializable]}")

  }
}


