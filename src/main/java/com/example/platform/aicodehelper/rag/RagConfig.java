package com.example.platform.aicodehelper.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Slf4j
@Configuration
public class RagConfig {
    @Resource
    private EmbeddingModel qwenEmbeddingModel;
    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;
    //TODO 暂时先不每次都加载，等之后使用向量数据库加载一次就搞定，还有就是解决如何增量加载的问题
    //@Bean
    public ContentRetriever contentRetriever() {
        log.info("开始加载文档");
        /*加载文档*/
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/doc");
        /*文档切割为了保证文档切割更完整所以我们没1000个字符切分一次，并且文档重叠字符设置为200*/
        DocumentByParagraphSplitter documentByParagraphSplitter = new DocumentByParagraphSplitter(1000, 200);
        //将文件切割后放入向量数据库
        EmbeddingStoreIngestor embeddingIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentByParagraphSplitter)
                .embeddingModel(qwenEmbeddingModel)
                //将切割后的文档碎片增加文档名称来增加检索的效率
                .textSegmentTransformer(textSegment -> {
                    return TextSegment.from(textSegment.metadata().getString("FILE_NAME") + " " + textSegment.text(), textSegment.metadata());
                })
                .embeddingStore(embeddingStore)
                .build();
        //加载文档
        embeddingIngestor.ingest( documents);
        EmbeddingStoreContentRetriever embeddingStoreContentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(qwenEmbeddingModel)
                .maxResults(5) // 最多返回5个结果
                .minScore(0.7) // 最小分数，如果发现搜索结果不够准确，可以调高这个值，如果发现检索不到那么肯定是需要降低这个分数增加匹配
                .build();
        log.info("加载文档完成");
        return embeddingStoreContentRetriever;
    }


}
