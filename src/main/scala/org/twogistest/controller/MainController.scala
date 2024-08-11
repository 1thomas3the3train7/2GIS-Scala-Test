package org.twogistest.controller

import cats.data.Reader
import cats.effect.IO
import org.twogistest.configuration.AppEnvironment
import io.circe.generic.auto.exportEncoder
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.CirceSensitiveDataEntityDecoder.circeEntityDecoder
import org.http4s.dsl.io._

object MainController {


  /**
   * Определяет маршруты для обработки HTTP-запросов.
   *
   * POST /data: Этот маршрут принимает POST-запросы на путь /data.
   * Тело запроса должно содержать массив URL в формате JSON
   *
   * Пример:
     * [
     * "https://www.deepl.com/ru/translator",
     * "https://www.google.com/"
     * ]
   *
   * @return маршруты для обработки HTTP-запросов.
   */
  val routes: Reader[AppEnvironment, HttpRoutes[IO]] = Reader { env =>
    HttpRoutes.of[IO] {
      case req @ POST -> Root / "data" =>
        for {
          _ <- env.logger.info(s"Received POST request to /data ${req}")
          listUrl <- req.as[List[String]]
          response <- env.compositeService.executeUrls(listUrl).run(env)
          json <- Ok(response)
        } yield json
    }
  }
}
