package org.twogistest.service

import cats.data.ReaderT
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.http4s.client.Client
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.Matcher
import org.twogistest.configuration.{AppConfig, AppEnvironment}
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.ExecutionContext

class CompositeServiceImplTest extends AnyFunSuite with Matchers with MockFactory {

  def createTestEnvironment(): AppEnvironment = {
    val loaderService = mock[LoaderService]
    val parserService = mock[ParserService]
    val logger = Slf4jLogger.getLogger[IO]
    val client = mock[Client[IO]]
    val httpClient = mock[HttpClient]
    val appConfig = AppConfig.load()
    val compositeService = new CompositeServiceImpl

    new AppEnvironment(
      loaderService = loaderService,
      parserService = parserService,
      logger = logger,
      client = client,
      httpClient = httpClient,
      appConfig = appConfig,
      compositeService = compositeService
    )
  }

  test("executeUrls should process all URLs and return responses") {

    val env = createTestEnvironment()

    (env.loaderService.loadHtmlByLink _).expects(*).returning(ReaderT[IO, AppEnvironment, String](_ => IO.pure("<html><title>Title</title></html>"))).twice()
    (env.parserService.parseHtml _).expects(*).returning(IO.pure("Title")).twice()

    val service = new CompositeServiceImpl

    val links = Seq("http://example.com", "http://example.org")

    val result = service.executeUrls(links).run(env).unsafeRunSync()

    result should have size 2
    result.foreach { response =>
      response.titleResponse shouldBe Some("Title")
      response.errorMessage shouldBe None
    }
  }

  test("executeUrls should handle errors gracefully") {
    val env = createTestEnvironment()

    (env.loaderService.loadHtmlByLink _).expects(*).returning(ReaderT[IO, AppEnvironment, String](_ => IO.raiseError(new RuntimeException("Load error")))).twice()
    (env.parserService.parseHtml _).expects(*).returning(IO.pure("Title")).never()

    val service = new CompositeServiceImpl

    val links = Seq("http://example.com", "http://example.org")

    val result = service.executeUrls(links).run(env).unsafeRunSync()

    result should have size 2
    result.foreach { response =>
      response.titleResponse shouldBe None
      response.errorMessage shouldBe Some("Load error")
    }
  }
}
