package org.hoh.wechat4j.cache;

public class CacheManager
{
  public static enum CacheWhere
  {
    ehcache;
  }
  
  private static CacheManager instance = new CacheManager();
  
  public static CacheManager getInstance()
  {
    return instance;
  }
  
  public static void set(String key, Object value, CacheWhere cacheWhere)
  {
    if (cacheWhere.equals(CacheWhere.ehcache)) {
      EhcacheHandler.getInstance().set("foreverCache", key, value);
    }
  }
}
