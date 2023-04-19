import java.net.{HttpURLConnection, URL}
import java.io.{BufferedReader, InputStreamReader}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

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
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import org.python.util.PythonInterpreter
class HttpSourceFunction2(url: String) extends RichSourceFunction[String] {

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



import org.apache.flink.streaming.api.scala._
import io.github.streamingwithflink.util.{SensorReading, SensorSource, SensorTimeAssigner}
//class MyTimestampExtractor(maxOutOfOrderness: Long) extends BoundedOutOfOrdernessTimestampExtractor[MyEvent](Time.milliseconds(maxOutOfOrderness)) {
// override def extractTimestamp(element: MyEvent): Long = element.timestamp
//}
object Main2 {
  def main(args: Array[String]): Unit = {
    val maxOutOfOrderness = Time.seconds(30)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    var httpStream = env.addSource(new HttpSourceFunction2("https://realstonks.p.rapidapi.com/GOOGL"))
      .assignTimestampsAndWatermarks(
        new BoundedOutOfOrdernessTimestampExtractor[String](Time.seconds(30)) {
          override def extractTimestamp(element: String): Long = System.currentTimeMillis()
        }
      )
      .map(line => {
        val values = line.split(",")
        val price1 = values(0).split(":")
        val regex = """\d+""".r
        val result = regex.findFirstIn(price1(1)).getOrElse("")

        val dict = Map("timestamp" -> System.currentTimeMillis(), "name" -> "google", "price" -> result.toDouble


        )
        dict
      })

    var httpStream2 = env.addSource(new HttpSourceFunction("https://realstonks.p.rapidapi.com/MSFT"))
      .assignTimestampsAndWatermarks(
        new BoundedOutOfOrdernessTimestampExtractor[String](Time.seconds(30)) {
          override def extractTimestamp(element: String): Long = System.currentTimeMillis()
        }
      )
      .map(line => {
        val values = line.split(",")
        val price1 = values(0).split(":")
        val regex = """\d+""".r
        val result = regex.findFirstIn(price1(1)).getOrElse("")

        val dict = Map("timestamp" -> System.currentTimeMillis(), "name" -> "msft", "price" -> result.toDouble


        )
        dict
      })

    var httpStream3 = env.addSource(new HttpSourceFunction("https://realstonks.p.rapidapi.com/AMZN"))
      .assignTimestampsAndWatermarks(
        new BoundedOutOfOrdernessTimestampExtractor[String](Time.seconds(30)) {
          override def extractTimestamp(element: String): Long = System.currentTimeMillis()
        }
      )
      .map(line => {
        val values = line.split(",")
        val price1 = values(0).split(":")
        val regex = """\d+""".r
        val result = regex.findFirstIn(price1(1)).getOrElse("")

        val dict = Map("timestamp" -> System.currentTimeMillis(), "name" -> "amazon", "price" -> result.toDouble


        )
        dict
      })


    var connectedStreams = httpStream.union(httpStream2)
    var output = connectedStreams.union(httpStream3)
    output.print()


    env.execute("HTTP Streaming Example")
  }
}