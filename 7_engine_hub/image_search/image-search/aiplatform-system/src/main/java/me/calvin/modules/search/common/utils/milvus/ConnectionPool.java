package me.calvin.modules.search.common.utils.milvus;

import io.milvus.client.*;

import java.util.Enumeration;
import java.util.Vector;

public class ConnectionPool {
  private String host = ""; // Milvus 主机
  private int port = 19530; // Milvus 端口号
  private String username = ""; // 用户名
  private String password = ""; // 用户密码
  private static volatile ConnectionPool uniqueInstance;
  private int initialConnections = 10; // 连接池的初始大小
  private int incrementalConnections = 5; // 连接池自动增加的大小
  private int maxConnections = 50; // 连接池最大的大小
  private Vector connections = null; // 存放连接池中连接的向量, 存放的对象为 PooledConnection 型
  
  private ConnectionPool(String host, int port) {
    this.host = host;
    this.port = port;
    //    this.username = username;
    //    this.password = password;
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

  private void createPool() throws Exception { // synchronized
    if (connections != null) {
      return; // 假如己经创建，则返回
    }
    // 创建保存连接的向量 , 初始时有 0 个元素
    connections = new Vector();
    // 根据 initialConnections 中设置的值，创建连接。

    try {
      createConnections(this.initialConnections);
    } catch (ConnectFailedException e) {
      System.out.println(" 创建Milvus连接失败！ " + e.getMessage());
      throw e;
    }

    System.out.println(" Milvus连接池创建成功！ ");
  }

  private void createConnections(int numConnections) throws ConnectFailedException {
    // 循环创建指定数目的数据库连接
    for (int x = 0; x < numConnections; x++) {
      // 是否连接池中的Milvus连接数量己经达到最大？最大值由类成员 maxConnections
      if (this.maxConnections > 0 && this.connections.size() >= this.maxConnections) {
        break;
      }
      // 增加一个连接到连接池中（Vector connections）
      connections.addElement(new PooledConnection(newConnection()));
      System.out.println(" Milvus连接己创建 ......");
    }
  }

  private MilvusClient newConnection() throws ConnectFailedException {
    // 创建一个 Milvus 客户端
    MilvusClient client = new MilvusGrpcClient();
    // 连接 Milvus 服务器
    ConnectParam connectParam = new ConnectParam.Builder().withHost(host).withPort(port).build();
    Response connectResponse = client.connect(connectParam);

    return client; // 返回创建的新的Milvus连接
  }

  public synchronized MilvusClient getConnection() throws ConnectFailedException {
    // 确保连接池己被创建
    if (connections == null) {
      return null; // 连接池还没创建，则返回 null
    }
    MilvusClient client = getFreeConnection(); // 获得一个可用的数据库连接
    // 假如目前没有可以使用的连接，即所有的连接都在使用中
    while (client == null) {
      // 等一会再试 250 ms
      wait(250);
      client = getFreeConnection(); // 重新再试，直到获得可用的连接，假如
      // getFreeConnection() 返回的为 null
      // 则表明创建一批连接后也不可获得可用连接
    }
    return client; // 返回获得的可用的连接
  }

  private MilvusClient getFreeConnection() throws ConnectFailedException {
    // 从连接池中获得一个可用的Milvus连接
    MilvusClient client = findFreeConnection();
    if (client == null) {
      // 假如目前连接池中没有可用的连接
      // 创建一些连接
      createConnections(incrementalConnections);
      // 重新从池中查找是否有可用连接
      client = findFreeConnection();
      if (client == null) {
        // 假如创建连接后仍获得不到可用的连接，则返回 null
        return null;
      }
    }
    return client;
  }

  private MilvusClient findFreeConnection() throws ConnectFailedException {
    MilvusClient client = null;
    PooledConnection pConn = null;
    // 获得连接池中所有的对象
    Enumeration enumerate = connections.elements();
    // 遍历所有的对象，看是否有可用的连接
    while (enumerate.hasMoreElements()) {
      pConn = (PooledConnection) enumerate.nextElement();
      if (!pConn.isBusy()) {
        // 假如此对象不忙，则获得它的数据库连接并把它设为忙
        client = pConn.getConnection();
        pConn.setBusy(true);
        // 测试此连接是否可用
        if (!testConnection(client)) {
          // 假如此连接不可再用了，则创建一个新的连接，
          // 并替换此不可用的连接对象，假如创建失败，返回 null
          client = newConnection();
          pConn.setConnection(client);
        }
        break; // 己经找到一个可用的连接，退出
      }
    }
    return client; // 返回找到到的可用连接
  }

  private boolean testConnection(MilvusClient client) {
    // 检查是否连接
    boolean connected = client.isConnected();
    return connected;
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
    while (enumerate.hasMoreElements()) {
      pConn = (PooledConnection) enumerate.nextElement();
      // 先找到连接池中的要返回的连接对象
      if (client == pConn.getConnection()) {
        // 找到了 , 设置此连接为空闲状态
        pConn.setBusy(false);
        break;
      }
    }
  }

  public synchronized void refreshConnections() throws ConnectFailedException {
    // 确保连接池己创新存在
    if (connections == null) {
      System.out.println(" 连接池不存在，无法刷新 !");
      return;
    }
    PooledConnection pConn = null;
    Enumeration enumerate = connections.elements();
    while (enumerate.hasMoreElements()) {
      // 获得一个连接对象
      pConn = (PooledConnection) enumerate.nextElement();
      // 假如对象忙则等 5 秒 ,5 秒后直接刷新
      if (pConn.isBusy()) {
        wait(5000); // 等 5 秒
      }
      // 关闭此连接，用一个新的连接代替它。
      closeConnection(pConn.getConnection());
      pConn.setConnection(newConnection());
      pConn.setBusy(false);
    }
  }

  public synchronized void closeConnectionPool() {
    // 确保连接池存在，假如不存在，返回
    if (connections == null) {
      System.out.println(" 连接池不存在，无法关闭 !");
      return;
    }
    PooledConnection pConn = null;
    Enumeration enumerate = connections.elements();
    while (enumerate.hasMoreElements()) {
      pConn = (PooledConnection) enumerate.nextElement();
      // 假如忙，等 5 秒
      if (pConn.isBusy()) {
        wait(5000); // 等 5 秒
      }
      // 5 秒后直接关闭它
      closeConnection(pConn.getConnection());
      // 从连接池向量中删除它
      connections.removeElement(pConn);
    }
    // 置连接池为空
    connections = null;
  }

  private void closeConnection(MilvusClient client) {
    try {
      client.disconnect();
    } catch (InterruptedException e) {
      System.out.println(" 关闭数据库连接出错： " + e.getMessage());
    }
  }

  private void wait(int mSeconds) {
    try {
      Thread.sleep(mSeconds);
    } catch (InterruptedException e) {
    }
  }

  class PooledConnection {
    MilvusClient client = null; // Milvus连接
    boolean busy = false; // 此连接是否正在使用的标志，默认没有正在使用
    // 构造函数，根据一个 Connection 构告一个 PooledConnection 对象
    public PooledConnection(MilvusClient client) {
      this.client = client;
    }

    // 返回此对象中的连接
    public MilvusClient getConnection() {
      return client;
    }

    // 设置此对象的连接
    public void setConnection(MilvusClient client) {
      this.client = client;
    }

    // 获得对象连接是否忙
    public boolean isBusy() {
      return busy;
    }

    // 设置对象的连接正在忙
    public void setBusy(boolean busy) {
      this.busy = busy;
    }
  }

//  public static void main(String[] args) {
//    String host = "192.168.43.107";
//    int port = 19530;
//    final String collectionName = "example"; // collection name
//
//    try {
//      ConnectionPool connPool = ConnectionPool.getInstance(host, port, false);
//      MilvusClient client = connPool.getConnection();
//
//      HasCollectionResponse hasCollection = search.service.SearchServiceImpl.hasCollection(client, collectionName);
//      System.out.println("example存在否？ " + hasCollection.hasCollection());
//
//    } catch (ConnectFailedException e) {
//      e.printStackTrace();
//    }
//  }
}
