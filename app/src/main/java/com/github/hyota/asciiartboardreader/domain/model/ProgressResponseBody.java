package com.github.hyota.asciiartboardreader.domain.model;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private BufferedSource source;

    public ProgressResponseBody(ResponseBody responseBody) {
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
                double percent = bytesRead == -1 ? 100 : ((double) totalBytesRead / (double) responseBody.contentLength()) * 100;
                EventBus.getDefault().post(new Event(percent));
                return bytesRead;
            }
        };
    }

    public static class Event {
        private double progress;

        private Event(double progress) {
            this.progress = progress;
        }

        public double getProgress() {
            return progress;
        }
    }

}
