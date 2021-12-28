package me.aias.tools;

import io.milvus.client.*;

/**
 * 搜索引擎初始化工具
 *
 * @author Calvin
 * @date 2021-12-12
 **/
public class MilvusInit {

    public static void main(String[] args) throws InterruptedException {

        String host = "127.0.0.1";
        int port = 19530;
        final String collectionName = "mols"; // collection name


        // Connect to Milvus server
        ConnectParam connectParam = new ConnectParam.Builder().withHost(host).withPort(port).build();
        MilvusClient client = new MilvusGrpcClient(connectParam);

        // 检查 collection 是否存在
        HasCollectionResponse hasCollection = hasCollection(client, collectionName);
        if (hasCollection.hasCollection()) {
            dropIndex(client, collectionName);
            dropCollection(client, collectionName);
        }

        // 关闭 Milvus 连接
        client.close();
    }

    // 检查是否存在 collection
    public static HasCollectionResponse hasCollection(MilvusClient client, String collectionName) {
        HasCollectionResponse response = client.hasCollection(collectionName);
        return response;
    }

    // 删除 collection
    public static Response dropCollection(MilvusClient client, String collectionName) {
        // Drop collection
        Response dropCollectionResponse = client.dropCollection(collectionName);
        return dropCollectionResponse;
    }

    // 删除 index
    public static Response dropIndex(MilvusClient client, String collectionName) {
        Response dropIndexResponse = client.dropIndex(collectionName);
        return dropIndexResponse;
    }
}
