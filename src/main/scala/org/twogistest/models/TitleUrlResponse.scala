package models

case class TitleUrlResponse(
                           urlRequest: Option[String] = None,
                           titleResponse: Option[String] = None,
                           errorMessage: Option[String] = None
                           )
