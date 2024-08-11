package org.twogistest.service

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.twogistest.exception.TitleNotFoundException

class ParserServiceImplSpec extends AnyFlatSpec with Matchers with EitherValues{
  val parserService: ParserService = new ParserServiceImpl

  "ParserServiceImpl" should "correctly parse HTML with a title" in {
    val htmlWithTitle = "<html><head><title>Test Title</title></head><body></body></html>"

    val result: IO[String] = parserService.parseHtml(htmlWithTitle)

    result.unsafeRunSync() shouldEqual "Test Title"
  }

  it should "return Title not found in the provided HTML" in {
    val htmlWithoutTitle = "<html><head></head><body></body></html>"

    val result = parserService.parseHtml(htmlWithoutTitle).attempt.unsafeRunSync()
    println(result)
    result.left.value shouldBe a [TitleNotFoundException]
    result.left.value.getMessage shouldEqual "Title not found in the provided HTML"
  }
}
