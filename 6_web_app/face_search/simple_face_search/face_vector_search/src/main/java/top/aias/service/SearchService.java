package top.aias.service;

import io.milvus.client.MilvusClient;
import io.milvus.grpc.MutationResult;
import io.milvus.grpc.QueryResults;
import io.milvus.grpc.SearchResults;
import io.milvus.param.R;
import io.milvus.param.RpcStatus;
import top.aias.common.milvus.ConnectionPool;

import java.util.List;
/**
 * 搜索服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface SearchService {
    // 重置向量引擎
    // Reset vector engine
    void clearSearchEngine();

    // 初始化向量引擎
    // Initialize vector engine
    void initSearchEngine();

    // 获取连接池
    // Get connection pool
    ConnectionPool getConnectionPool(boolean refresh);

    // 获取Milvus Client
    // Get Milvus Client
    MilvusClient getClient(ConnectionPool connPool);

    // 检查是否存在 collection
    // Return connection
    void returnConnection(ConnectionPool connPool, MilvusClient client);

    // 检查是否存在 collection
    // Check if collection exists
    R<Boolean> hasCollection(MilvusClient milvusClient);
    R<Boolean> hasCollection();

    // 检查是否加载了 collection
    boolean getCollectionState();

    // 创建 collection
    // Create collection
    R<RpcStatus> createCollection(MilvusClient milvusClient, long timeoutMiliseconds);

    // 加载 collection
    // Load collection
    R<RpcStatus> loadCollection(MilvusClient milvusClient);
    R<RpcStatus> loadCollection();

    // 释放 collection
    // Release collection
    R<RpcStatus> releaseCollection(MilvusClient milvusClient);

    // 删除 collection
    // Drop collection
    R<RpcStatus> dropCollection(MilvusClient milvusClient);

    // 创建 分区
    // Create partition
    R<RpcStatus> createPartition(MilvusClient milvusClient, String partitionName);

    // 删除 分区
    // Drop partition
    R<RpcStatus> dropPartition(MilvusClient milvusClient, String partitionName);

    // 是否存在分区
    // Check if partition exists
    R<Boolean> hasPartition(MilvusClient milvusClient, String partitionName);

    // 创建 index
    // Create index
    R<RpcStatus> createIndex(MilvusClient client);
    R<RpcStatus> createIndex();

    // 删除 index
    // Drop index
    R<RpcStatus> dropIndex(MilvusClient client);

    // 插入向量
    // Insert vectors
    R<MutationResult> insert(List<Long> vectorIds, List<List<Float>> vectors);

    // 查询向量
    // Query vectors
    R<QueryResults> query(String expr);

    // 搜索向量
    // Search vectors
    R<SearchResults> search(Integer topK, List<List<Float>> vectorsToSearch);

    // 删除向量
    // Delete vectors
    R<MutationResult> delete(String expr);
}
