package org.twogistest.configuration

import pureconfig.ConfigSource
import pureconfig.generic.auto._


/**
 * `HttpConfig` - это case class, которая содержит конфигурацию для HTTP сервера.
 *
 * @param host Хост, на котором будет запущен HTTP сервер.
 * @param port Порт, на котором будет запущен HTTP сервер.
 */
case class HttpConfig(host: String, port: Int)

/**
 * `AppConfig` - это case class, которая содержит общую конфигурацию приложения.
 *
 * @param http Конфигурация HTTP сервера.
 */
case class AppConfig(http: HttpConfig)

object AppConfig {
  /**
   * Загружает конфигурацию приложения из файла `application.conf`.
   *
   * @return Загруженная конфигурация приложения.
   */
  def load(): AppConfig = ConfigSource.default.loadOrThrow[AppConfig]
}
