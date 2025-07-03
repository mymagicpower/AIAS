package me.calvin.modules.search.tools;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DescribeIndexResponse;
import io.milvus.grpc.GetIndexBuildProgressResponse;
import io.milvus.grpc.GetIndexStateResponse;
import io.milvus.param.ConnectParam;
import io.milvus.param.R;
import io.milvus.param.index.DescribeIndexParam;
import io.milvus.param.index.GetIndexBuildProgressParam;
import io.milvus.param.index.GetIndexStateParam;

/**
 * 搜索引擎 -  索引测试工具
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class MilvusIndexTest {
    private static final MilvusServiceClient milvusClient;

    static {
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost("127.0.0.1")
                .withPort(19530)
                .build();
        milvusClient = new MilvusServiceClient(connectParam);
    }

    private static final String COLLECTION_NAME = "arts";// collection name
    private static final String INDEX_NAME = "imageFeature";

    public static void main(String[] args) {
        try {
            getIndexState();
            describeIndex();
            getIndexBuildProgress();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 关闭 Milvus 连接
        // Close Milvus connection
        milvusClient.close();
    }

    // 检查索引状态
    private static R<GetIndexStateResponse> getIndexState() {
        System.out.println("========== getIndexState() ==========");
        R<GetIndexStateResponse> response = milvusClient.getIndexState(GetIndexStateParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .withIndexName(INDEX_NAME)
                .build());
        handleResponseStatus(response);
        System.out.println(response);
        return response;
    }

    private static R<DescribeIndexResponse> describeIndex() {
        System.out.println("========== describeIndex() ==========");
        R<DescribeIndexResponse> response = milvusClient.describeIndex(DescribeIndexParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .withIndexName(INDEX_NAME)
                .build());
        handleResponseStatus(response);
        System.out.println(response);
        return response;
    }

    private static R<GetIndexBuildProgressResponse> getIndexBuildProgress() {
        System.out.println("========== getIndexBuildProgress() ==========");
        R<GetIndexBuildProgressResponse> response = milvusClient.getIndexBuildProgress(
                GetIndexBuildProgressParam.newBuilder()
                        .withCollectionName(COLLECTION_NAME)
                        .withIndexName(INDEX_NAME)
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
