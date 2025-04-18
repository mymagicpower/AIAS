package top.aias.tools;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.SearchResults;
import io.milvus.param.*;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.response.SearchResultsWrapper;
import top.aias.tools.face.FaceFeature;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 搜索引擎 - 搜索测试工具
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class MilvusSearchTest {
    private static final MilvusServiceClient milvusClient;
    private static final String COLLECTION_NAME = "face_search";// collection name
    private static final String VECTOR_FIELD = "feature";
    private static final String ID_FIELD = "featureId";

    static {
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost("127.0.0.1")
                .withPort(19530)
                .build();
        milvusClient = new MilvusServiceClient(connectParam);
    }



    public static void main(String[] args) {

        Path imageFile = Paths.get("src/test/resources/kana1.jpg");

        try (FaceFeature predictor = new FaceFeature(Device.cpu());) {
            Image img = ImageFactory.getInstance().fromFile(imageFile);
            float[] embeddings = predictor.predict(img);
            if (embeddings != null) {
                System.out.println("Face feature: " + Arrays.toString(embeddings));
            }

            List<Float> feature = new ArrayList<>();
            for (int i = 0; i < embeddings.length; i++) {
                feature.add(embeddings[i]);
            }

            List<List<Float>> vectorsToSearch = new ArrayList<>();
            vectorsToSearch.add(feature);

            // 根据图片向量搜索
            // Search for vectors based on image files
            R<SearchResults> searchResponse = search(5, vectorsToSearch);

        } catch (ModelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        }

        // 关闭 Milvus 连接
        // Close Milvus connection
        milvusClient.close();
    }

    // 搜索向量
    //Search vectors
    public static R<SearchResults> search(Integer topK, List<List<Float>> vectorsToSearch) {
        System.out.println("========== searchImage() ==========");
        String SEARCH_PARAM = "{\"nprobe\":" + 10 + "}";

        List<String> outFields = Arrays.asList(ID_FIELD);
        SearchParam searchParam = SearchParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
                .withMetricType(MetricType.L2)
                .withOutFields(outFields)
                .withTopK(topK)
                .withVectors(vectorsToSearch)
                .withVectorFieldName(VECTOR_FIELD)
//                    .withExpr(expr)
                .withParams(SEARCH_PARAM)
                .withGuaranteeTimestamp(Constant.GUARANTEE_EVENTUALLY_TS)
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

    private R<RpcStatus> loadCollection() {
        System.out.println("========== loadCollection() ==========");
        R<RpcStatus> response = milvusClient.loadCollection(LoadCollectionParam.newBuilder()
                .withCollectionName(COLLECTION_NAME)
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
