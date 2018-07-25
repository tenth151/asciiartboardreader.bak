package com.github.hyota.asciiartboardreader.data.network.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.hyota.asciiartboardreader.domain.model.ProgressUpdateListener;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressInterceptor implements Interceptor {

    @Inject
    ProgressInterceptor() {
    }

    @Nullable
    private ProgressUpdateListener progressUpdateListener;

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder()
                .body(new ProgressResponseBody(response.body()))
                .build();
    }

    public void setProgressUpdateListener(@Nullable ProgressUpdateListener progressUpdateListener) {
        this.progressUpdateListener = progressUpdateListener;
    }

    private class ProgressResponseBody extends ResponseBody {

        private ResponseBody responseBody;
        private BufferedSource source;

        private ProgressResponseBody(ResponseBody responseBody) {
            this.responseBody = responseBody;
        }

        @Nullable
        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (source == null) {
                source = Okio.buffer(source(responseBody.source()));
            }
            return source;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0;

                @Override
                public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                    int max;
                    int progress;
                    if (responseBody.contentLength() > Integer.MAX_VALUE) {
                        max = Integer.MAX_VALUE;
                        progress = (int) (((double) totalBytesRead / (double) responseBody.contentLength()) * Integer.MAX_VALUE);
                    } else {
                        max = (int) responseBody.contentLength();
                        progress = (int) totalBytesRead;
                    }
                    if (progressUpdateListener != null) {
                        progressUpdateListener.onProgressUpdate(max, progress);
                    }
                    return bytesRead;
                }
            };
        }
    }

}
