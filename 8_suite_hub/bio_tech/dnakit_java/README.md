## DNA Toolkit

Deoxyribonucleic acid (DNA) is one of the four major biomolecules present in biological cells, which carries the genetic information necessary for the synthesis of RNA and proteins, and is an essential biomolecule for the development and normal operation of organisms. DNA sequence refers to the primary structure of a DNA molecule that carries genetic information, represented by a string of letters (A, T, C, G), either real or hypothetical. There are two DNA sequence determination methods: optical sequencing and chip sequencing.

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/biology_sdks/dna.jpeg)

### SDK Function

- Feature Extraction
  Text (DNA sequence) feature extraction: the process of converting text data into feature vectors. The most commonly used text feature representation method is the bag-of-words method.
  Bag-of-words method: regardless of the order in which words appear, each word that appears separately is used as a column feature. The set of these non-repeating feature words is the vocabulary.
- CountVectorizer is a common feature numerical calculation class and a text feature extraction method. For each training text, it only considers the frequency of each vocabulary appearing in the training text.
- CountVectorizer will convert the words in the text into a frequency matrix, and it will calculate the number of times each word appears through the fit function.
- CountVectorizer aims to convert a document into a vector by counting. When there is no prior dictionary, CountVectorizer acts as an estimator to extract vocabulary for training and generates a CountVectorizerModel to store the corresponding vocabulary vector space. This model produces a sparse representation of documents about words. In the training process of CountVectorizerModel, CountVectorizer will select the vocabulary in descending order of word frequency based on the corpus, and the maximum content of the vocabulary table is specified by the hyperparameter vocabsize, while the hyperparameter minDF specifies that the vocabulary table should appear in at least how many different documents.

Todo list:

- Vector normalization
- Vector similarity calculation
- ......

### Running Example - DNASequenceExample

After a successful run, the command line should see the following information:
```text
# Display the first 5 data
+-----+--------------------+
|label|            sequence|
+-----+--------------------+
|    4|[ATGC, TGCC, GCCC...|
|    4|[ATGA, TGAA, GAAC...|
|    3|[ATGT, TGTG, GTGT...|
|    3|[ATGT, TGTG, GTGT...|
|    3|[ATGC, TGCA, GCAA...|
+-----+--------------------+

# Feature vector

+-----+--------------------+--------------------+
|label|            sequence|            features|
+-----+--------------------+--------------------+
|    4|[ATGC, TGCC, GCCC...|(336,[0,8,14,17,1...|
|    4|[ATGA, TGAA, GAAC...|(336,[0,1,2,3,5,7...|
|    3|[ATGT, TGTG, GTGT...|(336,[0,1,2,3,4,5...|
|    3|[ATGT, TGTG, GTGT...|(336,[0,1,2,3,4,5...|
|    3|[ATGC, TGCA, GCAA...|(336,[0,1,2,3,4,5...|
+-----+--------------------+--------------------+



```

### Reference:
http://spark.apache.org/docs/latest/ml-features.html#countvectorizer
