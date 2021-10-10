package com.trickl.influxdb.client;

import com.trickl.influxdb.binding.AnalyticDoubleValueReader;
import com.trickl.influxdb.binding.AnalyticDoubleValueWriter;
import com.trickl.influxdb.persistence.AnalyticDoubleValueEntity;
import com.trickl.model.analytics.InstantDouble;
import com.trickl.model.pricing.primitives.TemporalPriceSource;
import com.trickl.model.pricing.statistics.PriceSourceFieldFirstLastDuration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class AnalyticDoubleValueClient {

  private final InfluxDbAdapter influxDbClient;

  /**
   * Stores analytics in the database.
   *
   * @param temporalPriceSource the instrument identifier
   * @param events data to store
   * @return counts of records stored
   */
  public Flux<Integer> store(TemporalPriceSource temporalPriceSource, List<InstantDouble> events) {
    AnalyticDoubleValueWriter transformer = new AnalyticDoubleValueWriter(temporalPriceSource);
    List<AnalyticDoubleValueEntity> measurements =
        events.stream().map(transformer).collect(Collectors.toList());
    return influxDbClient.store(
        measurements, AnalyticDoubleValueEntity.class, AnalyticDoubleValueEntity::getTime);
  }

  /**
   * Find analytic values.
   *
   * @param temporalPriceSource the aanlytic source
   * @param queryBetween Query parameters
   * @return A list of bars
   */
  public Flux<InstantDouble> findBetween(
      TemporalPriceSource temporalPriceSource, QueryBetween queryBetween) {
    AnalyticDoubleValueReader reader = new AnalyticDoubleValueReader();
    return influxDbClient
        .findBetween(
            temporalPriceSource.getPriceSource(),
            queryBetween,
            "analytic_double_value",
            AnalyticDoubleValueEntity.class,
            Optional.empty(),
            Optional.of(temporalPriceSource.getTemporalSource()))
        .map(reader);
  }

  /**
   * Find a summary of changes between a period of time, grouped by instrument.
   *
   * @param queryBetween A time window there series must have a data point within
   * @return A list of series, including the first and last value of a field
   */
  public Flux<PriceSourceFieldFirstLastDuration> findSummary(QueryBetween queryBetween) {
    return influxDbClient.findFieldFirstLastCountByDay(
        queryBetween, "analytic_double_value", "time");
  }
}
