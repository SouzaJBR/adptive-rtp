package br.ufjf.dcc082;

public class RTPStreamDescriptor {

    private String streamDescription;
    private String streamURL;

    @Override
    public String toString() {
        return streamDescription + ";" + streamURL;
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
