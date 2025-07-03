package me.calvin.modules.search.common.milvus;

import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DataType;
import io.milvus.param.*;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.index.CreateIndexParam;
import io.milvus.param.partition.CreatePartitionParam;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

/**
 * Milvus 引擎配置
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class MilvusEngine {
    private String host;
    private String port;
    private String collectionName;
    private String partitionName;
    private String indexType;
    private String metricType;
    private String dimension;
    private String nlist;

    private static final String ID_FIELD = "imageId";
    private static final String VECTOR_FIELD = "imageFeature";

    public void init(String host, String port, String collectionName, String partitionName, String indexType, String metricType, String dimension, String nlist) {
        this.host = host;
        this.port = port;
        this.collectionName = collectionName;
        this.partitionName = partitionName;
        this.indexType = indexType;
        this.metricType = metricType;
        this.dimension = dimension;
        this.nlist = nlist;

        // 创建一个 Milvus 客户端
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost(host)
                .withPort(Integer.parseInt(port))
                .build();

        // 创建Milvus连接
        MilvusServiceClient client = new MilvusServiceClient(connectParam);
        R<Boolean> response = client.hasCollection(HasCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .build());
        if (!response.getData()) {
            this.createCollection(client, 3000);
            this.createPartition(client, partitionName);
            this.createIndex(client);
            this.loadCollection(client);
        }
    }

    // 创建 collection
    public R<RpcStatus> createCollection(MilvusClient milvusClient, long timeoutMiliseconds) {
        System.out.println("========== createCollection() ==========");
        FieldType fieldType1 = FieldType.newBuilder()
                .withName(ID_FIELD)
                .withDescription("image id")
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(false) // 使用数据库生成的id
                .build();

        FieldType fieldType2 = FieldType.newBuilder()
                .withName(VECTOR_FIELD)
                .withDescription("image embedding")
                .withDataType(DataType.FloatVector)
                .withDimension(Integer.parseInt(dimension))
                .build();


        CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .withDescription("image info")
                .withShardsNum(2)
                .addFieldType(fieldType1)
                .addFieldType(fieldType2)
                .build();
        R<RpcStatus> response = milvusClient.withTimeout(timeoutMiliseconds, TimeUnit.MILLISECONDS)
                .createCollection(createCollectionReq);
        return response;
    }

    // 加载 collection
    public R<RpcStatus> loadCollection(MilvusClient milvusClient) {
        System.out.println("========== loadCollection() ==========");
        R<RpcStatus> response = milvusClient.loadCollection(LoadCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .build());
        return response;
    }

    // 创建 分区
    public R<RpcStatus> createPartition(MilvusClient milvusClient, String partitionName) {
        System.out.println("========== createPartition() ==========");
        R<RpcStatus> response = milvusClient.createPartition(CreatePartitionParam.newBuilder()
                .withCollectionName(collectionName)
                .withPartitionName(partitionName)
                .build());
        return response;
    }

    // 创建 index
    public R<RpcStatus> createIndex(MilvusClient milvusClient) {
        System.out.println("========== createIndex() ==========");
        String INDEX_PARAM = "{\"nlist\":" + nlist + "}";

        IndexType INDEX_TYPE;
        switch (indexType.toUpperCase()) {
            case "IVF_FLAT":
                INDEX_TYPE = IndexType.IVF_FLAT;
                break;
            case "IVF_SQ8":
                INDEX_TYPE = IndexType.IVF_SQ8;
                break;
            case "IVF_PQ":
                INDEX_TYPE = IndexType.IVF_PQ;
                break;
            case "HNSW":
                INDEX_TYPE = IndexType.HNSW;
                break;
            case "ANNOY":
                INDEX_TYPE = IndexType.ANNOY;
                break;
            case "RHNSW_FLAT":
                INDEX_TYPE = IndexType.RHNSW_FLAT;
                break;
            case "RHNSW_PQ":
                INDEX_TYPE = IndexType.RHNSW_PQ;
                break;
            case "RHNSW_SQ":
                INDEX_TYPE = IndexType.RHNSW_SQ;
                break;
            case "BIN_IVF_FLAT":
                INDEX_TYPE = IndexType.BIN_IVF_FLAT;
                break;
            default:
                INDEX_TYPE = IndexType.IVF_FLAT;
                break;
        }

        MetricType METRIC_TYPE;
        MetricType metricTypeEnum = MetricType.valueOf(metricType.toUpperCase());
        switch (metricTypeEnum) {
            case L2:
                METRIC_TYPE = MetricType.L2;
                break;
            case IP:
                METRIC_TYPE = MetricType.IP;
                break;
            case HAMMING:
                METRIC_TYPE = MetricType.HAMMING;
                break;
            case JACCARD:
                METRIC_TYPE = MetricType.JACCARD;
                break;
            case TANIMOTO:
                METRIC_TYPE = MetricType.TANIMOTO;
                break;
            case SUBSTRUCTURE:
                METRIC_TYPE = MetricType.SUBSTRUCTURE;
                break;
            case SUPERSTRUCTURE:
                METRIC_TYPE = MetricType.SUPERSTRUCTURE;
                break;
            default:
                METRIC_TYPE = MetricType.L2;
                break;
        }
        R<RpcStatus> response = milvusClient.createIndex(CreateIndexParam.newBuilder()
                .withCollectionName(collectionName)
                .withFieldName(VECTOR_FIELD)
                .withIndexType(INDEX_TYPE)
                .withMetricType(METRIC_TYPE)
                .withExtraParam(INDEX_PARAM)
                .withSyncMode(Boolean.TRUE)
                .build());
        return response;
    }
}
