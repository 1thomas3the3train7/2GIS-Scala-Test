package configuration

import cats.effect.IO
import org.http4s.client.Client
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import service.{CompositeService, CompositeServiceImpl, HttpClient, HttpClientService, LoaderService, LoaderServiceImpl, ParserService, ParserServiceImpl}

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
