import cats.effect.{ExitCode, IO, IOApp, Resource}
import configuration.AppEnvironment
import controller.MainController
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.client.Client

import scala.concurrent.ExecutionContext.global

object Server extends IOApp{

  override def run(args: List[String]): IO[ExitCode] =
    runServer.use(_ => IO.never).as(ExitCode.Success)

  private def runServer: Resource[IO, Unit] = {
    val clientResource: Resource[IO, Client[IO]] = BlazeClientBuilder[IO].withExecutionContext(global).resource

    clientResource.flatMap { client =>
      val appEnv = AppEnvironment.initEnvironment(client)

      for {
        _ <- BlazeServerBuilder[IO]
          .bindHttp(appEnv.appConfig.http.port, appEnv.appConfig.http.host)
          .withHttpApp(MainController.routes.run(appEnv).orNotFound)
          .resource
      } yield ()
    }
  }

}
