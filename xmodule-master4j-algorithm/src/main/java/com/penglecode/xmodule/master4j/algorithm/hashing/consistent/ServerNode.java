package com.penglecode.xmodule.master4j.algorithm.hashing.consistent;

import java.util.Objects;

public class ServerNode implements ConsistentHashingAlgorithm.Node {

    private final String serverHost;

    public ServerNode(String serverHost) {
        this.serverHost = serverHost;
    }

    @Override
    public String key() {
        return serverHost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerNode)) return false;
        ServerNode that = (ServerNode) o;
        return Objects.equals(serverHost, that.serverHost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverHost);
    }
}