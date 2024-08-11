package service

import cats.data.ReaderT
import cats.effect.IO
import configuration.AppEnvironment
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._
import org.http4s._


trait HttpClient {
  def fetchHtml(url: String): ReaderT[IO, AppEnvironment, String]
}

class HttpClientService extends HttpClient {
  override def fetchHtml(url: String): ReaderT[IO, AppEnvironment, String] = ReaderT { env =>
    val request = GET(Uri.unsafeFromString(url))

    env.client.expect[String](request)
  }
}
