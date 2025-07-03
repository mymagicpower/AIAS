package me.calvin.modules.search.service.impl;

import io.milvus.client.MilvusClient;
import io.milvus.grpc.*;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import io.milvus.param.collection.*;
import io.milvus.param.dml.DeleteParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.QueryParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.index.CreateIndexParam;
import io.milvus.param.index.DropIndexParam;
import io.milvus.param.partition.CreatePartitionParam;
import io.milvus.param.partition.DropPartitionParam;
import io.milvus.param.partition.HasPartitionParam;
import io.milvus.response.QueryResultsWrapper;
import io.milvus.response.SearchResultsWrapper;
import lombok.extern.slf4j.Slf4j;
import me.calvin.modules.search.common.milvus.ConnectionPool;
import me.calvin.modules.search.service.SearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * 搜索服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    @Value("${search.host}")
    String host;

    @Value("${search.port}")
    String port;

    @Value("${search.indexType}")
    String indexType;

    @Value("${search.metricType}")
    String metricType;

    @Value("${search.dimension}")
    String dimension;

    @Value("${search.collectionName}")
    String collectionName;

    @Value("${search.partitionName}")
    String partitionName;

    @Value("${search.nprobe}")
    String nprobe;

    @Value("${search.nlist}")
    String nlist;

    private static final String ID_FIELD = "imageId";
    private static final String VECTOR_FIELD = "imageFeature";

    // 重置向量引擎
    public void clearSearchEngine() {
        ConnectionPool connPool = this.getConnectionPool(false);
        MilvusClient client = connPool.getConnection();
        try {
            this.releaseCollection(client);
            this.dropPartition(client, partitionName);
            this.dropIndex(client);
            this.dropCollection(client);
        } finally {
            returnConnection(connPool, client);
        }
    }

    // 初始化向量引擎
    public void initSearchEngine() {
        ConnectionPool connPool = this.getConnectionPool(false);
        MilvusClient client = connPool.getConnection();
        try {
            this.createCollection(client, 2000);
            this.createPartition(client, partitionName);
            this.createIndex(client);
            this.loadCollection(client);
        } finally {
            returnConnection(connPool, client);
        }
    }

    public ConnectionPool getConnectionPool(boolean refresh) {
        ConnectionPool connPool = ConnectionPool.getInstance(host, port, refresh);
        return connPool;
    }

    public MilvusClient getClient(ConnectionPool connPool) {
        MilvusClient client = connPool.getConnection();
        return client;
    }

    public void returnConnection(ConnectionPool connPool, MilvusClient client) {
        // 释放 Milvus client 回连接池
        connPool.returnConnection(client);
        // 关闭 Milvus 连接池
        // connPool.closeConnectionPool();
    }

    // 检查是否存在 collection
    public R<Boolean> hasCollection(MilvusClient client) {
        R<Boolean> response = client.hasCollection(HasCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .build());
        return response;
    }

    public R<Boolean> hasCollection() {
        ConnectionPool connPool = this.getConnectionPool(false);
        MilvusClient client = connPool.getConnection();
        try {
            R<Boolean> response = hasCollection(client);
            return response;
        } finally {
            returnConnection(connPool, client);
        }
    }

    // 检查collection是否加载
    public boolean getCollectionState() {
        System.out.println("========== getCollectionState() ==========");
        ConnectionPool connPool = this.getConnectionPool(false);
        MilvusClient milvusClient = connPool.getConnection();
        R<GetLoadingProgressResponse> response = milvusClient.getLoadingProgress(
                new GetLoadingProgressParam(GetLoadingProgressParam.newBuilder().withCollectionName(collectionName)));
        if (response.getStatus() != R.Status.Success.getCode()) {
            return false;
        } else {
            return true;
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

    // 加载 collection
    public R<RpcStatus> loadCollection() {
        ConnectionPool connPool = this.getConnectionPool(false);
        MilvusClient client = connPool.getConnection();
        try {
            R<RpcStatus> response = loadCollection(client);
            return response;
        } finally {
            returnConnection(connPool, client);
        }
    }

    // 释放 collection
    public R<RpcStatus> releaseCollection(MilvusClient milvusClient) {
        System.out.println("========== releaseCollection() ==========");
        R<RpcStatus> response = milvusClient.releaseCollection(ReleaseCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .build());
        return response;
    }

    // 删除 collection
    public R<RpcStatus> dropCollection(MilvusClient milvusClient) {
        System.out.println("========== dropCollection() ==========");
        R<RpcStatus> response = milvusClient.dropCollection(DropCollectionParam.newBuilder()
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

    // 删除 分区
    public R<RpcStatus> dropPartition(MilvusClient milvusClient, String partitionName) {
        System.out.println("========== dropPartition() ==========");
        R<RpcStatus> response = milvusClient.dropPartition(DropPartitionParam.newBuilder()
                .withCollectionName(collectionName)
                .withPartitionName(partitionName)
                .build());
        return response;
    }

    // 是否存在分区
    public R<Boolean> hasPartition(MilvusClient milvusClient, String partitionName) {
        System.out.println("========== hasPartition() ==========");
        R<Boolean> response = milvusClient.hasPartition(HasPartitionParam.newBuilder()
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

    // 删除 index
    public R<RpcStatus> dropIndex(MilvusClient milvusClient) {
        System.out.println("========== dropIndex() ==========");
        R<RpcStatus> response = milvusClient.dropIndex(DropIndexParam.newBuilder()
                .withCollectionName(collectionName)
                .withIndexName(VECTOR_FIELD)
                .build());
        return response;
    }

    // 插入向量
    public R<MutationResult> insert(List<Long> vectorIds, List<List<Float>> vectors) {
        System.out.println("========== insert() ==========");
        ConnectionPool connPool = this.getConnectionPool(false);
        MilvusClient milvusClient = connPool.getConnection();
        try {
            List<InsertParam.Field> fields = new ArrayList<>();
            fields.add(new InsertParam.Field(ID_FIELD, vectorIds));
            fields.add(new InsertParam.Field(VECTOR_FIELD, vectors));

            InsertParam insertParam = InsertParam.newBuilder()
                    .withCollectionName(collectionName)
                    .withPartitionName(partitionName)
                    .withFields(fields)
                    .build();
            R<MutationResult> response = milvusClient.insert(insertParam);
            return response;
        } finally {
            returnConnection(connPool, milvusClient);
        }
    }

    // 查询向量
    // queryExpr = ID_FIELD + " == 60";
    public R<QueryResults> query(String expr) {
        System.out.println("========== query() ==========");
        ConnectionPool connPool = this.getConnectionPool(false);
        MilvusClient milvusClient = connPool.getConnection();
        try {
            List<String> fields = Arrays.asList(ID_FIELD);
            QueryParam test = QueryParam.newBuilder()
                    .withCollectionName(collectionName)
                    .withExpr(expr)
                    .withOutFields(fields)
                    .build();
            R<QueryResults> response = milvusClient.query(test);
            QueryResultsWrapper wrapper = new QueryResultsWrapper(response.getData());
            System.out.println(ID_FIELD + ":" + wrapper.getFieldWrapper(ID_FIELD).getFieldData().toString());
            System.out.println("Query row count: " + wrapper.getFieldWrapper(ID_FIELD).getRowCount());
            return response;
        } finally {
            returnConnection(connPool, milvusClient);
        }
    }

    // 搜索向量
    public R<SearchResults> search(Integer topK, List<List<Float>> vectorsToSearch) {
        System.out.println("========== searchImage() ==========");
        ConnectionPool connPool = this.getConnectionPool(false);
        MilvusClient milvusClient = connPool.getConnection();
        try {
            String SEARCH_PARAM = "{\"nprobe\":" + nprobe + "}";

            List<String> outFields = Arrays.asList(ID_FIELD);
            SearchParam searchParam = SearchParam.newBuilder()
                    .withCollectionName(collectionName)
                    .withMetricType(MetricType.L2)
                    .withOutFields(outFields)
                    .withTopK(topK)
                    .withVectors(vectorsToSearch)
                    .withVectorFieldName(VECTOR_FIELD)
//                .withExpr(expr)
                    .withParams(SEARCH_PARAM)
                    .build();

            R<SearchResults> response = milvusClient.search(searchParam);
            SearchResultsWrapper wrapper = new SearchResultsWrapper(response.getData().getResults());
            for (int i = 0; i < vectorsToSearch.size(); ++i) {
                System.out.println("Search result of No." + i);
                List<SearchResultsWrapper.IDScore> scores = wrapper.getIDScore(i);
                System.out.println(scores);
                System.out.println("Output field data for No." + i);
            }
            return response;
        } finally {
            returnConnection(connPool, milvusClient);
        }
    }

    // 删除向量
    // String deleteExpr = ID_FIELD + " in " + deleteIds.toString();
    public R<MutationResult> delete(String expr) {
        System.out.println("========== delete() ==========");
        ConnectionPool connPool = this.getConnectionPool(false);
        MilvusClient milvusClient = connPool.getConnection();
        try {
            DeleteParam build = DeleteParam.newBuilder()
                    .withCollectionName(collectionName)
                    .withPartitionName(partitionName)
                    .withExpr(expr)
                    .build();
            R<MutationResult> response = milvusClient.delete(build);
            return response;
        } finally {
            returnConnection(connPool, milvusClient);
        }
    }
}
