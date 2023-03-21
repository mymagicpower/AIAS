
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/lac.zip

### Text-Lexical Analysis SDK [Chinese]
The lexical analysis model can complete Chinese word segmentation, Part-of-Speech tagging, and Named Entity Recognition tasks as a whole.
Part-of-Speech tagging:
-n Common Noun
-f Locative Noun
-s Place Noun
-t Time
-nr Person Name
-ns Place Name
-nt Organization Name
-nw Work Name
-nz Other Proper Nouns
-v Verb
-vd Verb Adverb
-vn Verb Noun
-a Adjective
-ad Adverbial Adjective
-an Nominal Adjective
-d Adverb
-m Quantifier
-q Quantity Word
-r Pronoun
-p Preposition
-c Conjunction
-u Auxiliary
-xc Other Particles
-w Punctuation
-PER Person Name
-LOC Place Name
-ORG Organization Name
-TIME Time


### SDK Algorithm:

Model network structure:   
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/lac_network.png)

- Paper：     
https://arxiv.org/abs/1807.01882

### Run Example-LacExample

After a successful run, you should see the following information on the command line:
```text
...
[INFO ] - input Sentence: 今天是个好日子
[INFO ] - Words : [今天, 是, 个, 好日子]
[INFO ] - Tags : [TIME, v, q, n]
```


### Open source algorithm
#### 1. Open source algorithm used by SDK
- [PaddleNLP](https://github.com/PaddlePaddle/PaddleNLP)
#### 2. How to export the model?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
