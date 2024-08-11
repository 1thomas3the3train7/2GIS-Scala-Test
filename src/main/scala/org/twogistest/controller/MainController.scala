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
