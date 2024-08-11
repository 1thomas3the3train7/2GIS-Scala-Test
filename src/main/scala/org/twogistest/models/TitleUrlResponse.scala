package org.twogistest.models

/**
 * `TitleUrlResponse` представляет собой ответ на запрос, связанный с парсингом заголовка (title) из HTML.
 *
 * @param urlRequest URL, который был получен в запросе.
 * @param titleResponse Найденный заголовок (title) из HTML.
 * @param errorMessage Сообщение об ошибке, если произошла ошибка при парсинге.
 */
case class TitleUrlResponse(
                           urlRequest: Option[String] = None,
                           titleResponse: Option[String] = None,
                           errorMessage: Option[String] = None
                           )
