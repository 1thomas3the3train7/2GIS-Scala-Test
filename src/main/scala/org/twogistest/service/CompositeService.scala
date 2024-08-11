package service

import cats.data.ReaderT
import cats.effect.IO
import fs2.Stream
import configuration.AppEnvironment
import models.TitleUrlResponse

trait CompositeService {
  def executeUrls(links: Seq[String]): ReaderT[IO, AppEnvironment, Seq[TitleUrlResponse]]
}

class CompositeServiceImpl extends CompositeService {
  override def executeUrls(links: Seq[String]): ReaderT[IO, AppEnvironment, Seq[TitleUrlResponse]] = ReaderT { env =>
    Stream.emits(links).covary[IO]
      .parEvalMapUnordered(links.size)(processLink(_, env))
      .compile
      .toList
  }

  private def processLink(link: String, env: AppEnvironment): IO[TitleUrlResponse] = {
    (for {
      _ <- env.logger.info(s"Start execute link: $link")
      html <- env.loaderService.loadHtmlByLink(link).run(env)
      parserResult <- env.parserService.parseHtml(html)
    } yield TitleUrlResponse(urlRequest = Some(link), titleResponse = Some(parserResult)))
      .handleErrorWith { error =>
        env.logger.error(s"Error processing link $link: ${error.getMessage}") *>
          IO.pure(TitleUrlResponse(urlRequest = Some(link), errorMessage = Some(error.getMessage)))
      }
  }
}
