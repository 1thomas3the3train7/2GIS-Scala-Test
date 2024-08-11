package org.twogistest.service

import cats.data.ReaderT
import cats.effect.IO
import org.twogistest.configuration.AppEnvironment

/**
 * `LoaderService` - это трейт, который определяет метод для загрузки HTML содержимого по ссылке.
 */
trait LoaderService {
  /**
   * Загружает HTML содержимое по указанной ссылке.
   *
   * @param link Ссылка для загрузки HTML содержимого.
   * @return `ReaderT`.
   */
  def loadHtmlByLink(link: String): ReaderT[IO, AppEnvironment, String]
}

/**
 * `LoaderServiceImpl` - это реализация `LoaderService`, которая использует `HttpClient` для загрузки HTML содержимого.
 */
class LoaderServiceImpl extends LoaderService {
  /**
   * Загружает HTML содержимое по указанной ссылке.
   *
   * @param link Ссылка для загрузки HTML содержимого.
   * @return `ReaderT`.
   */
  override def loadHtmlByLink(link: String): ReaderT[IO, AppEnvironment, String] =
    for {
      html <- ReaderT[IO, AppEnvironment, String](env => env.httpClient.fetchHtml(link).run(env))
    } yield html
}
