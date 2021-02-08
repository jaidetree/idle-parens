FROM clojure:openjdk-8-boot

RUN mkdir /src
VOLUME /src
WORKDIR /src
COPY . .
RUN boot build
CMD ["boot", "develop"]
