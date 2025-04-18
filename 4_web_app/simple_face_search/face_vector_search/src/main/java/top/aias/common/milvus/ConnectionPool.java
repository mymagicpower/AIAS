package top.aias.common.milvus;

import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;

import java.util.Enumeration;
import java.util.Vector;
/**
 * Milvus 链接池
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public class ConnectionPool {
    private String host = ""; // Milvus 主机 - Milvus host
    private int port; // Milvus 端口号 - Milvus port
    private static volatile ConnectionPool uniqueInstance;
    private int initialConnections = 10; // 连接池的初始大小 - initial size of the connection pool
    private int incrementalConnections = 5; // 连接池自动增加的大小 - automatic increase in connection pool size
    private int maxConnections = 50; // 连接池最大的大小 - maximum size of the connection pool
    private Vector connections = null; // 存放连接池中连接的向量, 存放的对象为 PooledConnection 型
    // vector that stores the connections in the connection pool, objects stored are of type PooledConnection
    private ConnectionPool(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static ConnectionPool getInstance(String host, String port, boolean refresh) {
        if (uniqueInstance == null || refresh) {
            synchronized (ConnectionPool.class) {
                if (uniqueInstance == null || refresh) {
                    uniqueInstance = new ConnectionPool(host, Integer.parseInt(port));
                    try {
                        uniqueInstance.createPool();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return uniqueInstance;
    }

    private void createPool() { // synchronized
        if (connections != null) {
            return; // 假如己经创建，则返回
            // return if the connection pool already exists
        }
        // 创建保存连接的向量 , 初始时有 0 个元素
        // create a vector to store the connections, initially with 0 elements
        connections = new Vector();
        // 根据 initialConnections 中设置的值，创建连接。
        // create connections based on the value set in initialConnections
        createConnections(this.initialConnections);

        System.out.println(" Milvus连接池创建成功！");
        System.out.println("Milvus connection pool created successfully!");
    }

    private void createConnections(int numConnections) {
        // 循环创建指定数目的数据库连接
        // loop to create the specified number of database connections
        for (int x = 0; x < numConnections; x++) {
            // 是否连接池中的Milvus连接数量己经达到最大？最大值由类成员 maxConnections
            // if the number of Milvus connections in the connection pool has reached the maximum, break the loop. The maximum value is set by the class member maxConnections.
            if (this.maxConnections > 0 && this.connections.size() >= this.maxConnections) {
                break;
            }
            // 增加一个连接到连接池中（Vector connections）
            // add a connection to the connection pool (Vector connections)
            connections.addElement(new PooledConnection(newConnection()));
            System.out.println(" Milvus连接己创建 ......");
            System.out.println("Milvus connection created......");
        }
    }

    private MilvusClient newConnection() {
        // 创建一个 Milvus 客户端
        // create a Milvus client
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost(host)
                .withPort(port)
                .build();

        MilvusServiceClient milvusClient = new MilvusServiceClient(connectParam);
        // 返回创建的新的Milvus连接
        // return the newly created Milvus connection
        return milvusClient;
    }

    public synchronized MilvusClient getConnection() {
        // 确保连接池己被创建
        // ensure that the connection pool has been created
        if (connections == null) {
            return null; // 连接池还没创建，则返回 null
            // if the connection pool has not been created, return null
        }
        MilvusClient client = getFreeConnection(); // 获得一个可用的数据库连接
        // obtain a available database connection
        // 假如目前没有可以使用的连接，即所有的连接都在使用中
        // if there are no available connections, i.e., all connections are in use
        while (client == null) {
            // 等一会再试 250 ms
            // wait for a while and try again in 250 ms
            wait(250);
            client = getFreeConnection(); // 重新再试，直到获得可用的连接，假如
            // getFreeConnection() 返回的为 null
            // 则表明创建一批连接后也不可获得可用连接
            // try again until an available connection is obtained, if getFreeConnection() returns null, it means that even after creating a batch of connections, no available connections can be obtained.
        }
        return client; // 返回获得的可用的连接
        // return the available connection obtained
    }

    private MilvusClient getFreeConnection() {
        // 从连接池中获得一个可用的Milvus连接
        // obtain an available Milvus connection from the connection pool
        MilvusClient client = findFreeConnection();
        if (client == null) {
            // 假如目前连接池中没有可用的连接
            // 创建一些连接
            // if there are no available connections in the connection pool, create some connections
            createConnections(incrementalConnections);
            // 重新从池中查找是否有可用连接
            // search again in the pool for available connections
            client = findFreeConnection();
            if (client == null) {
                // 假如创建连接后仍获得不到可用的连接，则返回 null
                // if no available connections are obtained after creating new connections, return null
                return null;
            }
        }
        return client;
    }

    private MilvusClient findFreeConnection() {
        MilvusClient client = null;
        PooledConnection pConn = null;
        // 获得连接池中所有的对象
        // obtain all objects in the connection pool
        Enumeration enumerate = connections.elements();
        // 遍历所有的对象，看是否有可用的连接
        // traverse all the objects to see if there are any available connections
        while (enumerate.hasMoreElements()) {
            pConn = (PooledConnection) enumerate.nextElement();
            if (!pConn.isBusy()) {
                // 假如此对象不忙，则获得它的数据库连接并把它设为忙
                // if this object is not busy, obtain its database connection and set it as busy
                client = pConn.getConnection();
                pConn.setBusy(true);
                break; // 己经找到一个可用的连接，退出
                // an available connection has been found, exit
            }
        }
        return client; // 返回找到到的可用连接
        // return the found available connection
    }

    public void returnConnection(MilvusClient client) {
        // 确保连接池存在，假如连接没有创建（不存在），直接返回
        if (connections == null) {
            System.out.println(" 连接池不存在，无法返回此连接到连接池中 !");
            return;
        }
        PooledConnection pConn = null;
        Enumeration enumerate = connections.elements();
        // 遍历连接池中的所有连接，找到这个要返回的连接对象
        // ensure that the connection pool exists, if the connection has not been created (does not exist), return directly
        while (enumerate.hasMoreElements()) {
            pConn = (PooledConnection) enumerate.nextElement();
            // 先找到连接池中的要返回的连接对象
            // traverse all connections in the connection pool to find the connection object to be returned
            if (client == pConn.getConnection()) {
                // 找到了 , 设置此连接为空闲状态
                // if found, set the connection to idle status
                pConn.setBusy(false);
                break;
            }
        }
    }

    public synchronized void refreshConnections() {
        // 确保连接池己创新存在
        // ensure that the connection pool has been created
        if (connections == null) {
            System.out.println(" 连接池不存在，无法刷新 !");
            System.out.println("The connection pool does not exist, cannot refresh!");
            return;
        }
        PooledConnection pConn = null;
        Enumeration enumerate = connections.elements();
        while (enumerate.hasMoreElements()) {
            // 获得一个连接对象
            // obtain a connection object
            pConn = (PooledConnection) enumerate.nextElement();
            // 假如对象忙则等 5 秒 ,5 秒后直接刷新
            // if the object is busy, wait for 5 seconds and then refresh it directly
            if (pConn.isBusy()) {
                wait(5000); // 等 5 秒
            }
            // 关闭此连接，用一个新的连接代替它。
            // close this connection and replace it with a new one.
            closeConnection(pConn.getConnection());
            pConn.setConnection(newConnection());
            pConn.setBusy(false);
        }
    }

    public synchronized void closeConnectionPool() {
        // 确保连接池存在，假如不存在，返回
        // ensure that the connection pool exists, if it does not exist, return
        if (connections == null) {
            System.out.println(" 连接池不存在，无法关闭 !");
            System.out.println("The connection pool does not exist, cannot close!");
            return;
        }
        PooledConnection pConn = null;
        Enumeration enumerate = connections.elements();
        while (enumerate.hasMoreElements()) {
            pConn = (PooledConnection) enumerate.nextElement();
            // 假如忙，等 5 秒
            // if busy, wait for 5 seconds
            if (pConn.isBusy()) {
                wait(5000); // 等 5 秒
            }
            // 5 秒后直接关闭它
            // close it directly after 5 seconds
            closeConnection(pConn.getConnection());
            // 从连接池向量中删除它
            // remove it from the connection pool vector
            connections.removeElement(pConn);
        }
        // 置连接池为空
        // set the connection pool to null
        connections = null;
    }

    private void closeConnection(MilvusClient client) {
        client.close();
    }

    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
        }
    }

    class PooledConnection {
        MilvusClient client = null; // Milvus连接
        // Milvus connection
        boolean busy = false; // 此连接是否正在使用的标志，默认没有正在使用
        // flag indicating whether this connection is currently in use, default is not in use
        // 构造函数，根据一个 Connection 构告一个 PooledConnection 对象
        // constructor that constructs a PooledConnection object based on a Connection
        public PooledConnection(MilvusClient client) {
            this.client = client;
        }

        // 返回此对象中的连接
        // return the connection in this object
        public MilvusClient getConnection() {
            return client;
        }

        // 设置此对象的连接
        // set the connection in this object
        public void setConnection(MilvusClient client) {
            this.client = client;
        }

        // 获得对象连接是否忙
        // obtain the status of if the connection in the object is busy
        public boolean isBusy() {
            return busy;
        }

        // 设置对象的连接正在忙
        // set the connection in the object as busy
        public void setBusy(boolean busy) {
            this.busy = busy;
        }
    }
}
