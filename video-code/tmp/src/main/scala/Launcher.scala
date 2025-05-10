import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.blaze.server.BlazeServerBuilder
import cats.effect._
import cats.effect.unsafe.IORuntime
import cats.effect.unsafe.implicits.global

import java.security.KeyStore
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import java.io.FileInputStream

object Launcher {

  // Simple HTML page to serve
  val htmlPage: String =
    """<!DOCTYPE html>
      |<html>
      |<head>
      |    <title>Secure HTTP4s Example</title>
      |</head>
      |<body>
      |    <h1>Hello from HTTP4s with HTTPS!</h1>
      |    <p>This page is served securely.</p>
      |</body>
      |</html>""".stripMargin

  // HTTP route
  val htmlRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root =>
      Ok(htmlPage).map(_.withContentType(headers.`Content-Type`(MediaType.text.html)))
  }

  // Load SSL context from keystore
  def sslContext(keystorePath: String, keystorePassword: String): Resource[IO, SSLContext] = {
    Resource.make(IO.blocking {
      val ks = KeyStore.getInstance("JKS")
      val keystore = new FileInputStream(keystorePath)
      ks.load(keystore, keystorePassword.toCharArray)
      keystore.close()

      val kmf = KeyManagerFactory.getInstance("SunX509")
      kmf.init(ks, keystorePassword.toCharArray)

      val tmf = TrustManagerFactory.getInstance("SunX509")
      tmf.init(ks)

      val sslContext = SSLContext.getInstance("TLS")
      sslContext.init(kmf.getKeyManagers, tmf.getTrustManagers, null)
      sslContext
    })(_ => IO.unit)
  }

  // Create the server
  def createServer(sslContext: SSLContext): Resource[IO, Server] = {
    BlazeServerBuilder[IO]
      .withExecutionContext(scala.concurrent.ExecutionContext.global)
      .bindHttp(8443, "0.0.0.0")
      .withSslContext(sslContext)
      .withHttpApp(htmlRoutes.orNotFound)
      .resource
  }

  def main(args: Array[String]): Unit = {
    // Configuration - in production, get these from config
    val keystorePath = "/Users/abhaykumarlodha/workspace/kotlin/untitled/src/main/resources/keystore.jks"
    val keystorePassword = "password"

    // Create and run the server
    val server = sslContext(keystorePath, keystorePassword)
      .flatMap(createServer)
      .use { server =>
        IO.println(s"Server started at https://localhost:${server.address.getPort}") *>
          IO.never
      }
      .as(ExitCode.Success)
      .unsafeRunSync()
  }
}