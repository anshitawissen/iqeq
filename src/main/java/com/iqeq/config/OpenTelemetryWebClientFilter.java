package com.iqeq.config;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
public class OpenTelemetryWebClientFilter implements ExchangeFilterFunction {

    private final Tracer tracer;

    public OpenTelemetryWebClientFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        Span span = tracer.spanBuilder(request.method().name() + " " + request.url())
                .startSpan();
        return next.exchange(ClientRequest.from(request)
                .header("traceparent",
                        "00-" + span.getSpanContext().getTraceId() + "-" + span.getSpanContext().getSpanId() + "-"
                                + span.getSpanContext().getTraceFlags().asHex())
                .build())
                .doFinally(signalType -> span.end());
    }

    @Override
    public ExchangeFilterFunction andThen(ExchangeFilterFunction afterFilter) {
        return ExchangeFilterFunction.super.andThen(afterFilter);
    }

    @Override
    public ExchangeFunction apply(ExchangeFunction exchange) {
        return ExchangeFilterFunction.super.apply(exchange);
    }
}

