package org.twogistest.configuration

import cats.effect.IO
import org.http4s.client.Client
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.twogistest.service.{CompositeService, CompositeServiceImpl, HttpClient, HttpClientService, LoaderService, LoaderServiceImpl, ParserService, ParserServiceImpl}


/**
 * `AppEnvironment` является контейнером для всех объектов и сервисов, необходимых для работы приложения.
 * Он используется для инъекции зависимостей через ReaderT.
 *
 * @param appConfig Конфигурация приложения.
 * @param client HTTP клиент.
 * @param httpClient Сервис для выполнения HTTP запросов.
 * @param loaderService Сервис для загрузки данных.
 * @param parserService Сервис для парсинга данных.
 * @param compositeService Композитный сервис, который объединяет функциональность других сервисов.
 * @param logger Логгер для записи логов.
 */
case class AppEnvironment(
                          appConfig: AppConfig,
                          client: Client[IO],
                          httpClient: HttpClient,
                          loaderService: LoaderService,
                          parserService: ParserService,
                          compositeService: CompositeService,
                          logger: Logger[IO]
                         )

object AppEnvironment {
  /**
   * Инициализирует окружение приложения, создавая все необходимые объекты и сервисы.
   *
   * @param client HTTP клиент.
   * @return Инициализированное окружение приложения.
   */
  def initEnvironment(client: Client[IO]): AppEnvironment = {
    val appConfig: AppConfig = AppConfig.load()
    val httpClient: HttpClient = new HttpClientService
    val loaderService: LoaderService = new LoaderServiceImpl
    val parserService: ParserService = new ParserServiceImpl
    val compositeService: CompositeService = new CompositeServiceImpl

    val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

    AppEnvironment(
      appConfig = appConfig,
      client = client,
      httpClient = httpClient,
      loaderService = loaderService,
      parserService = parserService,
      compositeService = compositeService,
      logger = logger
    )
  }
}
