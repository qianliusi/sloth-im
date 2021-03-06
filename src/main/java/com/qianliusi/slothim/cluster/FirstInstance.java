package com.qianliusi.slothim.cluster;

import com.qianliusi.slothim.HttpService;
import com.qianliusi.slothim.MatchUserVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstInstance {

  private static final Logger logger = LoggerFactory.getLogger(FirstInstance.class);

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), ar -> {
      if (ar.succeeded()) {
        logger.info("First instance has been started");
        Vertx vertx = ar.result();
        JsonObject conf = new JsonObject().put("port", 38888);
        vertx.deployVerticle(HttpService.class.getName(),new DeploymentOptions().setConfig(conf));
        DeploymentOptions opts = new DeploymentOptions().setInstances(2).setWorker(true);
        vertx.deployVerticle(MatchUserVerticle.class.getName(), opts);
      } else {
        logger.error("Could not start", ar.cause());
      }
    });
  }
}
