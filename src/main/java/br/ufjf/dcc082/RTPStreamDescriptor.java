package br.ufjf.dcc082;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RTPStreamDescriptor {

    private String streamDescription;
    private String streamURL;

    @Override
    public String toString() {

            return URLEncoder.encode(streamDescription) + ";" + URLEncoder.encode(streamURL) + ";";

    }

    public RTPStreamDescriptor(String streamDescription, String streamURL) {
        this.streamDescription = streamDescription;
        this.streamURL = streamURL;
    }

    public String getStreamDescription() {
        return streamDescription;
    }

    public void setStreamDescription(String streamDescription) {
        this.streamDescription = streamDescription;
    }

    public String getStreamURL() {
        return streamURL;
    }

    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }
}
