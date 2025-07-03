package me.calvin.modules.search.tools;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.DropCollectionParam;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.collection.ReleaseCollectionParam;
import io.milvus.param.index.DropIndexParam;
import io.milvus.param.partition.DropPartitionParam;

/**
 * 搜索引擎初始化工具
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class MilvusInit {
    private static final MilvusServiceClient milvusClient;

    static {
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost("127.0.0.1")
                .withPort(19530)
                .build();
        milvusClient = new MilvusServiceClient(connectParam);
    }

    private static final String COLLECTION_NAME = "arts";// collection name
    private static final String VECTOR_FIELD = "imageFeature";

    public static void main(String[] args) {
        try {
            // 检查 collection 是否存在，不存在会抛异常
            hasCollection();
            releaseCollection();
//            dropPartition("p1");
            dropIndex();
            dropCollection();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 关闭 Milvus 连接
        milvusClient.close();
    }

    // 检查是否存在 collection
    private static R<Boolean> hasCollection() {
        System.out.println("========== hasCollection() ==========");
        R<Boolean> response = milvusClient.hasCollection(HasCollectionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .build());
        handleResponseStatus(response);
        System.out.println(response);
        return response;
    }

    // 删除 collection
    private static R<RpcStatus> dropCollection() {
        System.out.println("========== dropCollection() ==========");
        R<RpcStatus> response = milvusClient.dropCollection(DropCollectionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .build());
        System.out.println(response);
        return response;
    }

    private static R<RpcStatus> releaseCollection() {
        System.out.println("========== releaseCollection() ==========");
        R<RpcStatus> response = milvusClient.releaseCollection(ReleaseCollectionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .build());
        handleResponseStatus(response);
        System.out.println(response);
        return response;
    }

    // 删除 index
    private static R<RpcStatus> dropIndex() {
        System.out.println("========== dropIndex() ==========");
        R<RpcStatus> response = milvusClient.dropIndex(DropIndexParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .withIndexName(VECTOR_FIELD)
                .build());
        handleResponseStatus(response);
        System.out.println(response);
        return response;
    }

    private static R<RpcStatus> dropPartition(String partitionName) {
        System.out.println("========== dropPartition() ==========");
        R<RpcStatus> response = milvusClient.dropPartition(DropPartitionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .withPartitionName(partitionName)
                .build());
        handleResponseStatus(response);
        System.out.println(response);
        return response;
    }

    private static void handleResponseStatus(R<?> r) {
        if (r.getStatus() != R.Status.Success.getCode()) {
            throw new RuntimeException(r.getMessage());
        }
    }
}
