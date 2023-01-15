package me.aias.common.milvus;

import io.milvus.client.ConnectParam;
import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusGrpcClient;
import lombok.extern.slf4j.Slf4j;

/**
 * Milvus Client Connector
 *
 * @author Calvin
 * @date 2021-12-19
 **/
@Slf4j
public final class MilvusConnector {

    // 创建一个 Milvus 客户端
    private MilvusClient client = null;

    public void init(String host, int port) {
        // Connect to Milvus server
        ConnectParam connectParam = new ConnectParam.Builder().withHost(host).withPort(port).build();
        client = new MilvusGrpcClient(connectParam);

    }

    public void close() throws InterruptedException {
        this.client.close();
    }

    public MilvusClient getClient() {
        return client;
    }
}
