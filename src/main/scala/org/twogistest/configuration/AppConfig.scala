package configuration

import pureconfig.ConfigSource
import pureconfig.generic.auto._

case class HttpConfig(host: String, port: Int)
case class DbConfig(url: String, user: String, password: String, driver: String, poolSize: Int)
case class FlywayConf(url: String, user: String, password: String, baseLineOnMigrate: Boolean)
case class AppConfig(http: HttpConfig, db: DbConfig, flyway: FlywayConf)

object AppConfig {
  def load(): AppConfig = ConfigSource.default.loadOrThrow[AppConfig]
}
