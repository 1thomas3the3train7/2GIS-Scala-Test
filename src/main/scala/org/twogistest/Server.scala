package org.twogistest

import cats.effect.{ExitCode, IO, IOApp, Resource}
import org.twogistest.configuration.AppEnvironment
import org.twogistest.controller.MainController
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.client.Client

import scala.concurrent.ExecutionContext.global

/**
 * `Server` - это объект, который запускает HTTP сервер с использованием библиотеки Http4s.
 * Он инициализирует клиент для HTTP запросов и конфигурирует сервер.
 */
object Server extends IOApp{

  /**
   * Основной метод, который запускает приложение.
   *
   * @param args Список аргументов командной строки.
   * @return `IO` содержащий код завершения приложения.
   */
  override def run(args: List[String]): IO[ExitCode] =
    runServer.use(_ => IO.never).as(ExitCode.Success)

  /**
   * Метод для инициализации и запуска сервера.
   *
   * @return `Resource` содержащий `IO` инициализацию сервера.
   */
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
