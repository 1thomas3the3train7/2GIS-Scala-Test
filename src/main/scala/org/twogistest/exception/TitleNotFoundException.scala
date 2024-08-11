package org.twogistest.exception

/**
 * `TitleNotFoundException` - это исключение, которое возникает при ошибке парсинга заголовка (title) из HTML.
 *
 * @param message Сообщение об ошибке, описывающее причину возникновения исключения.
 */
case class TitleNotFoundException(message: String) extends Exception(message)
