package br.ufjf.dcc082;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RTPStreamDescriptor {

    private RTPStreamDescriptor worseQualitySteam;

    public RTPStreamDescriptor getWorseQualitySteam() {
        return worseQualitySteam;
    }

    public static void chainStreams(RTPStreamDescriptor worseQualitySteam, RTPStreamDescriptor betterQualityStream) {
        worseQualitySteam.setBetterQualityStream(betterQualityStream);
        betterQualityStream.setWorseQualitySteam(worseQualitySteam);
    }

    public void setWorseQualitySteam(RTPStreamDescriptor worseQualitySteam) {
        ///worseQualitySteam.setBetterQualityStream(this);
        this.worseQualitySteam = worseQualitySteam;
    }

    public RTPStreamDescriptor getBetterQualityStream() {
        return betterQualityStream;
    }

    public void setBetterQualityStream(RTPStreamDescriptor betterQualityStream) {
        //betterQualityStream.setWorseQualitySteam(this);
        this.betterQualityStream = betterQualityStream;
    }

    private String streamDescription;
    private String streamURL;
    private RTPStreamDescriptor betterQualityStream;

    @Override
    public String toString() {
        return streamDescription;
    }
    public String toEscapedString() {

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
