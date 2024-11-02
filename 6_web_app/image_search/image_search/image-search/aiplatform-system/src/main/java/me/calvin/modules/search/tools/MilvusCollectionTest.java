package me.calvin.modules.search.tools;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DescribeCollectionResponse;
import io.milvus.grpc.GetCollectionStatisticsResponse;
import io.milvus.grpc.GetLoadingProgressResponse;
import io.milvus.param.ConnectParam;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.*;
import io.milvus.response.DescCollResponseWrapper;
import io.milvus.response.GetCollStatResponseWrapper;

/**
 * 搜索引擎 -  Collection 测试工具
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class MilvusCollectionTest {
    private static final MilvusServiceClient milvusClient;

    static {
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost("127.0.0.1")
                .withPort(19530)
                .build();
        milvusClient = new MilvusServiceClient(connectParam);
    }

    private static final String COLLECTION_NAME = "arts";// collection name

    public static void main(String[] args) {
        try {

            hasCollection();
//            loadCollection();
            getCollectionState();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 关闭 Milvus 连接
        // Close Milvus connection
        milvusClient.close();
    }

    // 检查是否存在 collection
    // Check if the collection exists
    private static R<Boolean> hasCollection() {
        System.out.println("========== hasCollection() ==========");
        R<Boolean> response = milvusClient.hasCollection(HasCollectionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .build());
        handleResponseStatus(response);
        System.out.println(response);
        return response;
    }

    private static R<RpcStatus> loadCollection() {
        System.out.println("========== loadCollection() ==========");
        R<RpcStatus> response = milvusClient.loadCollection(LoadCollectionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .build());
        handleResponseStatus(response);
        System.out.println(response);
        return response;
    }

    private static R<DescribeCollectionResponse> describeCollection() {
        System.out.println("========== describeCollection() ==========");
        R<DescribeCollectionResponse> response = milvusClient.describeCollection(DescribeCollectionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .build());
        handleResponseStatus(response);
        DescCollResponseWrapper wrapper = new DescCollResponseWrapper(response.getData());

        System.out.println(wrapper.toString());
        return response;
    }


    private static R<GetLoadingProgressResponse> getCollectionState() {
        System.out.println("========== getCollectionState() ==========");
        R<GetLoadingProgressResponse> response = milvusClient.getLoadingProgress(new GetLoadingProgressParam(GetLoadingProgressParam.newBuilder().withCollectionName(COLLECTION_NAME)));
        if (response.getStatus() != R.Status.Success.getCode()) {
            System.out.println(response.getStatus());
        }

        handleResponseStatus(response);

        GetLoadingProgressResponse res = response.getData();
        System.out.println(res.toString());
        return response;
    }

    private static R<GetCollectionStatisticsResponse> getCollectionStatistics() {
        System.out.println("========== getCollectionStatistics() ==========");
        R<GetCollectionStatisticsResponse> response = milvusClient.getCollectionStatistics(
                GetCollectionStatisticsParam.newBuilder()
                        .withCollectionName(COLLECTION_NAME)
                        .build());
        handleResponseStatus(response);

        GetCollStatResponseWrapper wrapper = new GetCollStatResponseWrapper(response.getData());
        System.out.println("Collection row count: " + wrapper.getRowCount());
        System.out.println(wrapper.toString());
        return response;
    }


    private static void handleResponseStatus(R<?> r) {
        if (r.getStatus() != R.Status.Success.getCode()) {
            throw new RuntimeException(r.getMessage());
        }
    }
}
