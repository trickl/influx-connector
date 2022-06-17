package com.trickl.influxdb.persistence;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import java.time.Instant;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Measurement(name = "broker_order")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BrokerOrderEntity {
  @NotNull
  @Column(name = "time", timestamp = true)
  private Instant time;

  @NotNull
  @Column(name = "instrumentId", tag = true)
  private String instrumentId;

  @NotNull
  @Column(name = "exchangeId", tag = true)
  private String exchangeId;

  @Column(name = "simulationId", tag = true)
  private String simulationId;

  @Column(name = "bidOrAsk", tag = true)
  protected String bidOrAsk;

  @NotNull
  @Min(0)
  @Column(name = "price")
  protected Double price;

  /** The amount of liquidity. */
  @Min(0)
  @Column(name = "volume")
  protected Long volume;

  @NotNull
  @Column(name = "createdAtTime", timestamp = true)
  private Instant createdAtTime;

  @Min(0)
  @Column(name = "quantityUnfilled")
  protected Long quantityUnfilled;
     
  @Min(0)
  @Column(name = "quantityFilled")
  protected Long quantityFilled;

  @NotNull
  @Column(name = "brokerId")
  private String brokerId;

  @Column(name = "clientReference")
  private String clientReference;

  @NotNull
  @Column(name = "timeInForce")
  private String timeInForce;

  @NotNull
  @Column(name = "type")
  private String type;

  @Column(name = "reason")  
  private String reason;

  @NotNull
  @Column(name = "state")
  private String state;
}