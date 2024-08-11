FROM hseeberger/scala-sbt:11.0.11_1.5.5_2.13.6
WORKDIR /app
COPY . .
RUN sbt update
RUN sbt compile
RUN sbt test

CMD ["sbt", "run"]