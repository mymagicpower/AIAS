
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/static_bert_qa.zip

### Bert Q&A SDK

Based on the BERT QA model, input a question and a text paragraph containing the answer (maximum length of 384),
the model can find the best answer from the text paragraph.

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/bertQA.png)

### Running Example - BertQaInferenceExample

- Question:
```text
When did Radio International start broadcasting?
```

- Paragraph containing the answer (maximum length of 384):
```text
Radio International was a general entertainment Channel.
Which operated between December 1983 and April 2001.
```

After a successful run, the command line should display the following information:
```text
...
[INFO ] - Paragraph: Radio International was a general entertainment Channel.
          Which operated between December 1983 and April 2001.
          
[INFO ] - Question: When did Radio International start broadcasting?

[INFO ] - Answer: [december, 1983]

```
