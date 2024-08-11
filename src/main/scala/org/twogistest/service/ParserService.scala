package org.twogistest.service

import cats.effect.IO
import org.twogistest.exception.TitleNotFoundException
import org.jsoup.Jsoup

/**
 * `ParserService` - это трейт, который определяет метод для парсинга HTML содержимого.
 */
trait ParserService {
  /**
   * Парсит HTML содержимое и извлекает заголовок страницы.
   *
   * @param html HTML содержимое для парсинга.
   * @return `IO` содержащий заголовок страницы или ошибку, если заголовок не найден.
   */
  def parseHtml(html: String): IO[String]
}

/**
 * `ParserServiceImpl` - это реализация `ParserService`, которая использует библиотеку Jsoup для парсинга HTML содержимого.
 */
class ParserServiceImpl extends ParserService {
  /**
   * Парсит HTML содержимое и извлекает заголовок страницы.
   *
   * @param html HTML содержимое для парсинга.
   * @return `IO` содержащий заголовок страницы или ошибку, если заголовок не найден.
   */
  override def parseHtml(html: String): IO[String] = IO.blocking {
    val document = Jsoup.parse(html)
    val titleElement = document.select("title").first()
    Option(titleElement).map(_.text)
  }.flatMap {
    case Some(title) => IO.pure(title)
    case None => IO.raiseError[String](TitleNotFoundException("Title not found in the provided HTML"))
  }
}
