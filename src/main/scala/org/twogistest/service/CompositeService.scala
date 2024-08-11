package org.twogistest.service

import cats.data.ReaderT
import cats.effect.IO
import fs2.Stream
import org.twogistest.configuration.AppEnvironment
import org.twogistest.models.TitleUrlResponse

/**
 * `CompositeService` - это трейт, который определяет метод для выполнения запросов по нескольким URL.
 */
trait CompositeService {
  /**
   * Выполняет запросы по списку URL и возвращает результаты в виде последовательности `TitleUrlResponse`.
   *
   * @param links Список URL для обработки.
   * @return `ReaderT`.
   */
  def executeUrls(links: Seq[String]): ReaderT[IO, AppEnvironment, Seq[TitleUrlResponse]]
}

/**
 * `CompositeServiceImpl` - это реализация `CompositeService`, которая использует асинхронные операции для обработки списка URL.
 */
class CompositeServiceImpl extends CompositeService {

  /**
   * Выполняет запросы по списку URL и возвращает результаты в виде последовательности `TitleUrlResponse`.
   * Обработка выполняется параллельно с использованием стримов fs2.
   *
   * @param links Список URL для обработки.
   * @return `ReaderT`.
   */
  override def executeUrls(links: Seq[String]): ReaderT[IO, AppEnvironment, Seq[TitleUrlResponse]] = ReaderT { env =>
    Stream.emits(links).covary[IO]
      .parEvalMapUnordered(links.size)(processLink(_, env))
      .compile
      .toList
  }

  /**
   * Обрабатывает отдельный URL и возвращает результат в виде `TitleUrlResponse`.
   *
   * @param link URL для обработки.
   * @param env Контекст выполнения, содержащий необходимые сервисы.
   * @return `IO` с результатом `TitleUrlResponse`.
   */
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
