
#### Common Performance Optimization Issues


1. Does it support multithreading?
```text
Answer: Yes, it supports multithreading, with one predictor for each thread.
```

2. In a multithreaded environment, how many predictors should be set?
```text
Answer: Generally, there are multiple predictors on a single machine, and the number of predictors can be the same as the number of cores, so that requests can be filled up. However, if the concurrency exceeds the number of cores on a single machine and it cannot be batch processed, then multiple machines will be useful for load balancing...

```

3. Is it meaningful to create multiple predictors and make an instance pool for inference?
```text
Answer: It depends on your calling methods. Each machine has its own physical computing power limit. It is recommended to first test the maximum throughput and latency achievable by a single machine in a multithreaded environment. The purpose of the instance pool is to limit the concurrency, and the same model is loaded only once, so there is no loading overhead.
```

4. How is the thread safety of translator and predictor ensured in a multithreaded environment?
```text
Answer: Translator is an interface that users implement themselves, and thread safety needs to be ensured by the users themselves. If multiple threads call predictor, predictor will call the translator's function in multiple threads. The manager in the ctx is generated at the beginning of preprocessing and is automatically destroyed at the end of postprocessing to help users clean up the input and output ndarray.
```

5. How is the parallelism set in a multithreaded environment?
```text
Answer: The default parallelism of deep learning frameworks is the number of cores. For example, if there are two GPUs on a single machine and the model needs to be loaded twice, one copy is loaded on GPU0 and the other on GPU1. Then multiple predictors are started from each model, and each predictor is bound to the GPU device initially read by the model. At this time, since the CPU computing power is reduced, more threads than the number of CPU cores can be opened (depending on the GPU performance). The specific number can be tested by multi-threaded inference to see when the GPU or CPU utilization is maximized. DJL provides a tool specifically for testing multithreaded throughput and latency.
<https://docs.djl.ai/extensions/benchmark/index.html>
Therefore, many users previously wanted a tool that can test the physical limit. DJL provides a tool specifically for testing multithreaded throughput and latency.
```

6. If multiple predictors are used on GPU, will multiple copies of memory be occupied (approximately one model's storage for one instance) (or multiple copies of memory occupied for CPU)?
```text
Answer: Starting from version 0.9.0, DJL can guarantee that tf/mx/pt three frameworks will not copy models when used in multithreading. Even if you open 100 threads, only one model is occupied in memory, and the GPU is the same. **This is a core advantage of multithreaded inference that beats Python's multiprocessing inference.**
Starting from version 0.10.0, multiple predictors created from one model will share one model memory and will not cause excessive occupancy. Therefore, the Model can be reused. If it is used in a multithreaded environment, it is recommended to create multiple predictors, one for each thread. However, the recent versions of the predictor itself are also thread-safe, so you won't have any problems reusing a predictor across multiple threads.
```

7. If there are no other models, will a single GPU and a single model not continue to occupy memory (regardless of how many predictors are created)?
```text
Answer: The model itself will not, but the current deep learning framework configuration is not very good. Even if there is not so much memory in use, as long as it performs calculations on the GPU, it will forcibly occupy many memory resources to fill temporary calculation data or just let it be... When will there be problems:
1) You use two deep learning engines on the same GPU at the same time, such as tf and pt. Then they will compete for GPU memory resources and fight...
2) Multiple processes in the system use the same GPU, and the deep learning engines between processes will also fight...

For example, suppose I have a 600 MB model and run it on a single 8 GB GPU. You may find that this process occupies more than 6 GB of memory...but the actual usage is much less.
```


Supplementary explanation:
```text
1. Performance tuning documentation
   http://docs.djl.ai/docs/development/inference_performance_optimization.html

2. Java itself is multithreaded, and DJL actually calls the relevant engine's C++ API. Multithreading itself is not repeated here. We have made some efforts in calling these APIs to ensure that the use process is thread-safe. After the inference task arrives, each thread simultaneously calls this C++ API. Then each CPU core will operate a request, and the calculation between each core is independent, and then return to the Java side after processing.

3. PyTorch, MXNet, and TensorFlow all have parameters that specify how many cores the operator can call.

4. The graph is read-only during inference, and each core calculates mutually independent results using memory, so there is no problem in the end.
```

