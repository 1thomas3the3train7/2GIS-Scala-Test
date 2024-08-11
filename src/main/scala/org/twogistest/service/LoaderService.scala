package service

import cats.data.ReaderT
import cats.effect.IO
import configuration.AppEnvironment

trait LoaderService {
  def loadHtmlByLink(link: String): ReaderT[IO, AppEnvironment, String]
}

class LoaderServiceImpl extends LoaderService {
  override def loadHtmlByLink(link: String): ReaderT[IO, AppEnvironment, String] =
    for {
      html <- ReaderT[IO, AppEnvironment, String](env => env.httpClient.fetchHtml(link).run(env))
    } yield html
}
