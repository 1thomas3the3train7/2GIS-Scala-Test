package org.twogistest.service

import cats.data.ReaderT
import cats.effect.IO
import org.http4s.Header.Raw
import org.twogistest.configuration.AppEnvironment
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._
import org.http4s._
import org.typelevel.ci.CIString


/**
 * `HttpClient` - это трейт, который определяет метод для получения HTML содержимого по URL.
 */
trait HttpClient {
  /**
   * Получает HTML содержимое по указанному URL.
   *
   * @param url URL для получения HTML содержимого.
   * @return `ReaderT`.
   */
  def fetchHtml(url: String): ReaderT[IO, AppEnvironment, String]
}

/**
 * `HttpClientService` - это реализация `HttpClient`, которая использует HTTP клиент для получения HTML содержимого.
 */
class HttpClientService extends HttpClient {

  /**
   * Получает HTML содержимое по указанному URL.
   *
   * @param url URL для получения HTML содержимого.
   * @return `ReaderT`.
   */
  override def fetchHtml(url: String): ReaderT[IO, AppEnvironment, String] = ReaderT { env =>
    val request = Request[IO](
      method = GET,
      uri = Uri.unsafeFromString(url),
      headers = Headers(
        Raw(CIString("User-Agent"), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"),
        Raw(CIString("Accept"), "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"),
        Raw(CIString("Accept-Language"), "en-US,en;q=0.5"),
        Raw(CIString("Connection"), "keep-alive")
      )
    )

    env.client.expect[String](request)
  }
}
