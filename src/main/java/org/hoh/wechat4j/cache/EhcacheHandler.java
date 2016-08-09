package org.hoh.wechat4j.cache;

import org.apache.log4j.Logger;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheHandler
{
  private static final Logger logger = Logger.getLogger(EhcacheHandler.class);
  public static final String FOREVER_CACHE = "foreverCache";
  public static final String EXPIRES_CACHE = "expiresCache";
  private static CacheManager cacheManager = null;
  private static EhcacheHandler ehcacheHandler = new EhcacheHandler();
  
  private EhcacheHandler()
  {
    init();
  }
  
  public static EhcacheHandler getInstance()
  {
    return ehcacheHandler;
  }
  
  public void init()
  {
    if (cacheManager == null)
    {
      logger.info("初始化CacheManager");
      
      String localEhcacheXmlName = "ehcache.xml";
      cacheManager = CacheManager.create(getClass().getClassLoader().getResource(localEhcacheXmlName));
      if (cacheManager == null) {
        throw new RuntimeException("无法创建ehcache对象实例化，检测ehcache配置文件");
      }
    }
  }
  
  public boolean set(String cacheName, String key, Object obj)
  {
    cacheManager.getCache(cacheName).put(new Element(key, obj));
    return true;
  }
  
  @SuppressWarnings("deprecation")
public void set(String cacheName, String key, Object obj, int liveTime)
  {
		cacheManager.getCache(cacheName).put(new Element(key, obj, Boolean.valueOf(false), Integer.valueOf(liveTime), Integer.valueOf(liveTime)));
  }
  
  public Object get(String cacheName, String key)
  {
    Element ele = cacheManager.getCache(cacheName).get(key);
    return ele == null ? null : ele.getObjectValue();
  }
  
  public boolean delete(String cacheName, String key)
  {
    return cacheManager.getCache(cacheName).remove(key);
  }
  
  public void shutdown()
  {
    try
    {
      if (cacheManager.getCache("expiresCache") != null) {
        cacheManager.getCache("expiresCache").dispose();
      }
      if (cacheManager.getCache("foreverCache") != null) {
        cacheManager.getCache("foreverCache").dispose();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if (cacheManager != null) {
      cacheManager.shutdown();
    }
  }
  
  public void destroy()
  {
    shutdown();
  }
}
