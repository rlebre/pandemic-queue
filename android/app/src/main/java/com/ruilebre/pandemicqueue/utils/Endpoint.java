package com.ruilebre.pandemicqueue.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class Endpoint {
    private String host;
    private int port;
    private String basepath;
    private URL url;

    public Endpoint(URL url) {
        this.host = url.getHost();
        this.port = url.getPort();
        this.basepath = url.getPath();

        this.url = url;
    }

    public Endpoint(String host, String port, String basepath) throws MalformedURLException {
        this(new URL(host + ":" + port + basepath));
    }

    public Endpoint(String host, String port) throws MalformedURLException {
        this(host, port, "");
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    @Override
    public String toString() {
        return url.toString();
    }

    public void concatenateEndpoint(String subEndpoint) throws MalformedURLException {
        url = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + subEndpoint);
    }

    public URL getUrl() {
        return url;
    }
}
