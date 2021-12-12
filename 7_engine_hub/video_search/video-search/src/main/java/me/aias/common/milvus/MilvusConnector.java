package me.aias.common.milvus;

import io.milvus.client.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Calvin
 * @date Oct 20, 2021
 */
@Slf4j
public final class MilvusConnector {

    // 创建一个 Milvus 客户端
    private MilvusClient client = null;

    public void init(String host, int port) {
        client = new MilvusGrpcClient();
        // Connect to Milvus server
        ConnectParam connectParam = new ConnectParam.Builder().withHost(host).withPort(port).build();
        try {
            Response connectResponse = client.connect(connectParam);
        } catch (ConnectFailedException e) {
            log.error("Failed to connect to Milvus server: " + e.toString());
        }

    }

    public void close() throws InterruptedException {
        this.client.disconnect();
    }

    public MilvusClient getClient() {
        return client;
    }
}
