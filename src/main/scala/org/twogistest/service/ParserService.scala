package service

import cats.effect.IO
import exception.TitleNotFoundException
import org.jsoup.Jsoup

trait ParserService {
  def parseHtml(html: String): IO[String]
}

class ParserServiceImpl extends ParserService {
  override def parseHtml(html: String): IO[String] = IO.blocking {
    val document = Jsoup.parse(html)
    val titleElement = document.select("title").first()
    Option(titleElement).map(_.text)
  }.flatMap {
    case Some(title) => IO.pure(title)
    case None => IO.raiseError[String](TitleNotFoundException("Title not found in the provided HTML"))
  }
}
