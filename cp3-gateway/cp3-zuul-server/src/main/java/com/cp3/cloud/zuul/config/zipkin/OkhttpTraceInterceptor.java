package com.cp3.cloud.zuul.config.zipkin;

import brave.Tracer;
import brave.internal.Platform;
import brave.internal.codec.HexCodec;
import brave.propagation.TraceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * okhttp过滤器，添加与trace集成的功能。 实现方式参考see also
 *
 * @author zuihou
 * @date 2019-12-09 17:23
 * @see brave.propagation.B3Propagation
 * @see org.springframework.cloud.netflix.ribbon.okhttp.OkHttpRibbonRequest
 */
@Slf4j
@RequiredArgsConstructor
public class OkhttpTraceInterceptor implements Interceptor {


    /**
     * 128 or 64-bit trace ID lower-hex encoded into 32 or 16 characters (required)
     */
    private static final String TRACE_ID_NAME = "X-B3-TraceId";
    /**
     * 64-bit span ID lower-hex encoded into 16 characters (required)
     */
    private static final String SPAN_ID_NAME = "X-B3-SpanId";
    /**
     * 64-bit parent span ID lower-hex encoded into 16 characters (absent on root span)
     */
    private static final String PARENT_SPAN_ID_NAME = "X-B3-ParentSpanId";

    private final Tracer tracer;

    /**
     * 将X-B3-TraceId | X-B3-SpanId | X-B3-ParentSpanId添加到请求的头部，再将请求发送出去
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        TraceContext traceContext = tracer.currentSpan().context();
        //copy all headers to new headers
        Headers.Builder headers = request.headers().newBuilder();
        //add traceId | spanId | parent span id to headers
        headers.add(TRACE_ID_NAME, traceContext.traceIdString());
        // headers.add(SPAN_ID_NAME, traceContext.spanIdString());
        headers.add(SPAN_ID_NAME, HexCodec.toLowerHex(nextId()));
        String parentId = traceContext.parentIdString();
        if (parentId == null) {
            //set parentId = spanId(root) when null
            parentId = HexCodec.toLowerHex(traceContext.spanId());
        }
        headers.add(PARENT_SPAN_ID_NAME, parentId);
        // headers.add(SAMPLED_NAME,"1");

        //rebuild a new request
        request = request.newBuilder().headers(headers.build()).build();
        Response response = chain.proceed(request);

        //将traceId返回去给调用方，如浏览器发起的请求，可在浏览器看到该信息，方便定位
        Headers.Builder responseHeadersBuilder = response.headers()
                .newBuilder()
                .add(TRACE_ID_NAME, traceContext.traceIdString());
        response = response.newBuilder().headers(responseHeadersBuilder.build()).build();

        return response;
    }

    /**
     * Generates a new 64-bit ID, taking care to dodge zero which can be confused with absent
     */
    long nextId() {
        long nextId = Platform.get().randomLong();
        while (nextId == 0L) {
            nextId = Platform.get().randomLong();
        }
        return nextId;
    }


}
